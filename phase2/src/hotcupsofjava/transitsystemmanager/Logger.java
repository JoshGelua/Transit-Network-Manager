package hotcupsofjava.transitsystemmanager;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Logger {
    private static String day;
    private static boolean active = false;
    private static ArrayList<String> logs;
    private static ArrayList<String> stats;
    private static int stopsVisited;
    private static long revenue;
    private static long trueValue;
    private static String startTime;

    public static String getStartTime() {
        return startTime;
    }

    public static void log(String logString) {
        if (!active) throw new RuntimeException("Attempted logging when non-active day");
        logs.add(logString);
    }

    public static void logStats(String statString) {
        if (!active) throw new RuntimeException("Attempted logging when non-active day");
        stats.add(statString);
    }

    public static void logStops(int numStops) {
        stopsVisited += numStops;
    }

    public static void logRevenue(double revenue, double trueValue) {
        Logger.revenue += revenue;
        Logger.trueValue += trueValue;
    }

    public static void logLoadingCard(String statString){
        logs.add(statString);
    }

    public static void startDay() {
        if (active) throw new RuntimeException("New day started before previous day ended.");

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        Logger.day = formatter.format(date);
        active = true;
        logs = new ArrayList<>();
        stats = new ArrayList<>();
        stopsVisited = 0;
        revenue = 0;
        trueValue = 0;
        Calendar cal = Calendar.getInstance();
        startTime = cal.get(Calendar.HOUR) + ":" + cal.get(Calendar.MINUTE);
    }

    public static void generateLog() {
        new File("logs/").mkdirs();
        if (!active) return;
        try {
            day = day.replaceAll("/", "-");
            PrintWriter writer = new PrintWriter("logs/Day_" + day + ".txt");
            writer.write("Day: " + day + "\n");
            writer.write("==================\n");

            for (String line : logs) {
                writer.write(line + "\n");
            }

            writer.write("==================\n\n\n");
            writer.write("Day " + day + "'s aggregated statistics:\n");
            writer.write("Stops visited:       " + stopsVisited + "\n");
            writer.write("Revenue collected:   " + revenue + "\n");
            writer.write("True Value of Trips: " + trueValue + "\n");

            for (String line : stats) {
                writer.write(line + "\n");
            }

            writer.close();
        } catch (FileNotFoundException e) {
            System.out.println("Failed to write log file: " + e.getMessage());
        }
    }

    public static void endDay() {
        generateLog();
        active = false;
    }

    public static boolean isActive() {
        return active;
    }
}
