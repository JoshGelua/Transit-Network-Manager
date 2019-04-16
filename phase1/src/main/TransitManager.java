package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import transitNetwork.Trip;
import user.Card;
import transitNetwork.BusStop;
import transitNetwork.RouteManager;
import user.User;
import user.UserManager;

/* This class will perform initial setup upon launch. */
public class TransitManager {

    private static final IDManager idManager = new IDManager();
    private static final UserManager userManager = new UserManager(idManager);
    private static final RouteManager routeManager = new RouteManager(idManager);

    private static String[] extractArgs(String str) {
        // id content pattern
        Pattern itemSyntax = Pattern.compile("(\\S+)\\s+(.+)");
        Matcher tokenize = itemSyntax.matcher(str);

        if (!tokenize.find()) return null;
        else {
            String[] result = {tokenize.group(1), tokenize.group(2)};
            return result;
        }
    }

    public static void main(String[] args) {

        try {

            // Stops file processing
            BufferedReader br = new BufferedReader(new FileReader("config/stops.txt"));
            String fileRead = br.readLine();

            // stopId name
            while (fileRead != null) {
                String[] tokenize = extractArgs(fileRead);

                // only given one argument
                if (tokenize == null) throw new RuntimeException("Invalid format for stop: " + fileRead);
                else routeManager.addStop(tokenize[0], tokenize[1]);

                fileRead = br.readLine();
            }

            br.close();

            // Stations file processing
            br = new BufferedReader(new FileReader("config/stations.txt"));
            fileRead = br.readLine();

            // stationId name
            // stationId name connect stopId
            while (fileRead != null) {
                String stationString = fileRead;
                BusStop connection = null;
                if (stationString.contains(" connects ")) {
                    String[] connectionString = stationString.split(" connects ");
                    if (!routeManager.hasStop(connectionString[1])) {
                        throw new RuntimeException("Invalid BusStop id provided for station connection: " + fileRead);
                    }
                    connection = routeManager.getStop(connectionString[1]);
                    stationString = connectionString[0];
                }
                String[] tokenize = extractArgs(stationString);

                if (tokenize == null) throw new RuntimeException("Invalid format for station: " + fileRead);
                else if (connection != null) routeManager.addStation(tokenize[0], tokenize[1], connection);
                else routeManager.addStation(tokenize[0], tokenize[1]);

                fileRead = br.readLine();
            }

            br.close();

            // Routes file processing
            br = new BufferedReader(new FileReader("config/routes.txt"));
            fileRead = br.readLine();

            // routeId stopIds...
            while (fileRead != null) {
                String[] tokenize = extractArgs(fileRead);

                // only given one argument
                if (tokenize == null) {
                    routeManager.addRoute(fileRead.trim());
                } else {
                    ArrayList<BusStop> stops = new ArrayList<>();
                    for (String stopId : tokenize[1].split("\\s+")) {
                        if (!routeManager.hasStop(stopId)) {
                            throw new RuntimeException(String.format("Invalid BusStop id (%s) provided for route creation: %s", stopId, fileRead));
                        }
                        stops.add(routeManager.getStop(stopId));
                    }

                    routeManager.addRoute(tokenize[0], stops);
                }

                fileRead = br.readLine();
            }

            br.close();

            // Users file processing
            br = new BufferedReader(new FileReader("config/users.txt"));
            fileRead = br.readLine();

            // userId email@example.com name
            while (fileRead != null) {
                Pattern userSyntax = Pattern.compile("(\\S+)\\s+(\\S+@\\S+\\.\\S+)\\s+(.+)");
                Matcher tokenize = userSyntax.matcher(fileRead);

                if (!tokenize.find()) {
                    throw new RuntimeException("Invalid user syntax; was the email formatted properly?: " + fileRead);
                }

                userManager.addUser(tokenize.group(1), tokenize.group(3), tokenize.group(2));

                fileRead = br.readLine();
            }

            br.close();

            // Cards file processing
            br = new BufferedReader(new FileReader("config/cards.txt"));
            fileRead = br.readLine();

            // userId cardIds...
            while (fileRead != null) {
                String[] tokenize = extractArgs(fileRead);

                if (tokenize != null) {
                    String userId = tokenize[0];
                    if (!userManager.hasUser(userId))
                        throw new RuntimeException("Invalid User id provided when adding cards: " + fileRead);
                    User user = userManager.getUser(userId);

                    for (String cardId : tokenize[1].split("\\s+")) {
                        userManager.addCard(user, cardId);
                    }
                }

                fileRead = br.readLine();
            }

            br.close();

            // Events file processing
            br = new BufferedReader(new FileReader("config/events.txt"));
            fileRead = br.readLine();

            // system start day
            if (!fileRead.contains("system start")) throw new RuntimeException("No date provided on startup");

            // system start day
            // system stop
            // card id tapOn station stationId
            // card id tapOff station stationId
            // card id tapOn busStop stopId routeId
            // card id tapOff busStop stopId routeid
            // user userId load cardId amount
            // user userId suspend cardId
            // user userId newCard cardId
            // user userId viewRecentTrips cardId
            // user userId changeName newName
            while (fileRead != null) {
                String[] extractTimestamp = extractArgs(fileRead);
                long timestamp = Long.parseLong(extractTimestamp[0]);
                String[] tokenize = extractArgs(extractTimestamp[1]);
                String[] eventArgs = tokenize[1].split("\\s+");

                switch (tokenize[0]) {
                    case "system":
                        if (eventArgs[0].equals("start")) {
                            Logger.startDay(eventArgs[1]);
                        } else if (eventArgs[0].equals("stop")) {
                            Logger.endDay();
                        } else {
                            throw new RuntimeException("Invalid argument passed to system.");
                        }
                        break;
                    case "card":
                        if (userManager.hasCard(eventArgs[0])) {
                            Card card = userManager.getCard(eventArgs[0]);
                            if (eventArgs[2].equals("station") && eventArgs.length != 4) throw new RuntimeException("Not enough args given to station tap: " + fileRead);
                            if (eventArgs[2].equals("busStop") && eventArgs.length != 5) throw new RuntimeException("Not enough args given to station tap: " + fileRead);
                            if (eventArgs[1].equals("tapOn")) {
                                if (eventArgs[2].equals("station")) {
                                    card.tapOn(timestamp, routeManager.getStation(eventArgs[3]));
                                } else if (eventArgs[2].equals("busStop")) {
                                    card.tapOn(timestamp, routeManager.getStop(eventArgs[3]), routeManager.getRoute(eventArgs[4]));
                                }
                            } else if (eventArgs[1].equals("tapOff")) {
                                if (eventArgs[2].equals("station")) {
                                    card.tapOff(timestamp, routeManager.getStation(eventArgs[3]));
                                } else if (eventArgs[2].equals("busStop")) {
                                    card.tapOff(timestamp, routeManager.getStop(eventArgs[3]), routeManager.getRoute(eventArgs[4]));
                                }
                            } else {
                                throw new RuntimeException("Unrecognized card");
                            }
                            break;
                        }
                    case "user":
                        if (userManager.hasUser(eventArgs[0])) {
                            User user = userManager.getUser(eventArgs[0]);
                            if (eventArgs[1].equals("load")) {
                                user.loadCard(userManager.getCard(eventArgs[2]),
                                        Integer.valueOf(eventArgs[3]));
                            } else if (eventArgs[1].equals("suspend")) {
                                user.suspendCard(userManager.getCard(eventArgs[2]));
                            } else if (eventArgs[1].equals("newCard")) {
                                userManager.addCard(user, eventArgs[2]);
                            } else if (eventArgs[1].equals("viewRecentTrips")) {
                                Card card = userManager.getCard(eventArgs[2]);
                                System.out.println(card + "'s 3 recent trips:");
                                for (Trip trip : user.viewTrips(card)) {
                                    System.out.println(trip);
                                }
                            } else if (eventArgs[1].equals("changeName")) {
                                String newName = fileRead.split("\\s+changeName\\s+")[1];
                                user.setName(newName);
                            }
                        } else {
                            throw new RuntimeException("User does not exist in the system: " + fileRead);
                        }
                        break;
                    default:
                        throw new RuntimeException("Unrecognized event in events.");
                }

                fileRead = br.readLine();
            }

            br.close();

        } catch (Throwable e) {
            e.printStackTrace();
            System.exit(1);
        }

    }

}
