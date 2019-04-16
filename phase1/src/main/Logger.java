package main;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.File;
import java.util.ArrayList;

public class Logger {
    private static String day;
    private static boolean active = false;
    private static ArrayList<String> logs;
    private static ArrayList<String> stats;
    private static int stopsVisited;
    private static int revenue;
    private static int trueValue;

    public static void log(String logString) {
        if (!active) throw new RuntimeException("Attempted logging when non-active day");
        logs.add(logString);
        System.out.println(logString);
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

    static void startDay(String day) {
        if (active) throw new RuntimeException("New day started before previous day ended.");
        Logger.day = day;
        active = true;
        logs = new ArrayList<>();
        stats = new ArrayList<>();
        stopsVisited = 0;
        revenue = 0;
        trueValue = 0;
    }

    static void endDay() {
        if (!active) throw new RuntimeException("Day ended before day started.");
        new File("logs/").mkdirs();
        try {
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

        active = false;
    }
}
