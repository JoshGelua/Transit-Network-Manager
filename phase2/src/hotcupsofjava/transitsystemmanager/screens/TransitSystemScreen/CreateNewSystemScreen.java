package hotcupsofjava.transitsystemmanager.screens.TransitSystemScreen;

import hotcupsofjava.transitsystemmanager.MainSystem;
import hotcupsofjava.transitsystemmanager.managers.IDManager;
import hotcupsofjava.transitsystemmanager.managers.RouteManager;
import hotcupsofjava.transitsystemmanager.managers.UserManager;
import hotcupsofjava.transitsystemmanager.objects.TransitSystemObject;
import hotcupsofjava.transitsystemmanager.objects.transitobjects.BusStop;
import hotcupsofjava.transitsystemmanager.objects.userobjects.User;
import hotcupsofjava.transitsystemmanager.screens.AdminScreen.AdminStartScreen;
import hotcupsofjava.transitsystemmanager.screens.UserScreen.UserLoginScreen;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateNewSystemScreen extends AnchorPane{

    public TextField nameSystemLbl;
    public TextField busRideLbl;
    public Button createSystemBtn;
    public TextField subwayRideLbl;

    public CreateNewSystemScreen(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CreateNewSystemScreen.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        }
        catch (IOException e){
            System.out.println("");
        }
    }

    public void createNewSystem(ActionEvent actionEvent) {
        boolean checkDetails = !nameSystemLbl.getText().isEmpty() && !busRideLbl.getText().isEmpty() &&
                !subwayRideLbl.getText().isEmpty();
        try{
            IDManager idManager;
            UserManager userManager;
            RouteManager routeManager;
            idManager = new IDManager();
            TransitSystemObject.setIdManager(idManager);
            userManager = new UserManager(Double.parseDouble(busRideLbl.getText()),
                    Double.parseDouble(subwayRideLbl.getText()));
            routeManager = new RouteManager();
            readBusStops(routeManager);
            readStations(routeManager);
            readRoutes(routeManager);
            readUsers(userManager);
            readInitialCards(userManager);
            MainSystem.setInstanceName(nameSystemLbl.getText());
            openInitialScreens(userManager, routeManager);
            Stage stage = (Stage) createSystemBtn.getScene().getWindow();
            stage.close();
        }
        catch(Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Incomplete information");
            alert.setContentText("Warning! Please enter proper information");
            alert.showAndWait();
        }
    }

    private void openInitialScreens(UserManager userManager, RouteManager routeManager){

        Stage loginWindow = new Stage();
        UserLoginScreen loginController = new UserLoginScreen(userManager,routeManager);
        loginWindow.initModality(Modality.WINDOW_MODAL);
        loginWindow.setTitle("Login Screen");
        loginWindow.setScene(new Scene(loginController));
        loginWindow.show();

        Stage adminWindow = new Stage();
        AdminStartScreen adminController = new AdminStartScreen(userManager,routeManager);
        adminWindow.initModality(Modality.WINDOW_MODAL);
        adminWindow.setTitle("Admin Screen");
        adminWindow.setScene(new Scene(adminController));
        adminWindow.show();

    }

    private void readInitialCards(UserManager userManager){
        try{
            // Cards file processing
            BufferedReader br = new BufferedReader(new FileReader("config/cards.txt"));
            String fileRead = br.readLine();

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

        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    private void readStations(RouteManager routeManager){
        try{
            BufferedReader br = new BufferedReader(new FileReader("config/stations.txt"));
            String fileRead = br.readLine();

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
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    private void readBusStops(RouteManager routeManager){
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
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    private void readUsers(UserManager userManager){
        try{
            // Users file processing
            BufferedReader br = new BufferedReader(new FileReader("config/users.txt"));
            String fileRead = br.readLine();

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
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
    private void readRoutes(RouteManager routeManager){
        try{
            // Routes file processing
            BufferedReader br = new BufferedReader(new FileReader("config/routes.txt"));
            String fileRead = br.readLine();

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
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

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
}
