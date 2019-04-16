package hotcupsofjava.transitsystemmanager.objects.userobjects;

import hotcupsofjava.transitsystemmanager.objects.transitobjects.BusStop;
import hotcupsofjava.transitsystemmanager.objects.transitobjects.Route;
import hotcupsofjava.transitsystemmanager.objects.transitobjects.Station;
import hotcupsofjava.transitsystemmanager.objects.transitobjects.Stop;
import hotcupsofjava.transitsystemmanager.Logger;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @version 1.0
 */
public class Trip implements Serializable {
    private ArrayList<TripLocation> locations; // The locations this trip has been on.
    private Station lastSubwayTap = null; // The lastSubwayTap

    private int distanceTravelled; // The distance travelled in this trip.
    private boolean tripEnded; // The trip has ended.

    private long initialTime; // The initial time of this trip.

    private double tripValue; // The cost of this trip.
    private double trueValue = 0; // The original cost of this trip.

    /**
     * The constructor for Trip to track Card Trips
     *
     * @param timestamp the time of the initial timeStamp
     * @param busStop   the busStop that this Trip is being initialized
     * @param route     the Route that the Card is on
     */
    public Trip(long timestamp, BusStop busStop, Route route) {
        locations = new ArrayList<>();
        addLocation(timestamp, true, busStop, route, false);
        initialTime = timestamp;
        tripEnded = false;
        tripValue = 0;
        distanceTravelled = 0;
    }

    /**
     * @param timestamp      The time when this trip started.
     * @param initialStation The initial station this trip began at.
     */
    public Trip(long timestamp, Station initialStation) {
        locations = new ArrayList<>();
        addLocation(timestamp, true, initialStation, false);
        initialTime = timestamp;
        tripEnded = false;
        tripValue = 0;
        distanceTravelled = 0;
    }

    /**
     * Tracks the total fare for this Trip. Once it reaches $6.00, the trip no longer charges
     * additionally.
     *
     * @param amount The amount charge added onto this Trip.
     */
    public void charge(double amount, Card card, double tripCap) {
        tripValue += amount;
        trueValue += amount;
        if (tripValue > tripCap) {
            tripValue = tripCap;
        }
        if (tripValue < tripCap) {
            card.setBalance(card.getBalance() - amount);
            card.setTotalSpending(card.getTotalSpending() + amount);
        }
    }

    /**
     * Ends this trip.
     */
    public void endTrip() {
        tripEnded = true;
        Logger.logStops(distanceTravelled);
    }

    /**
     * Returns true if the Trip has ended, otherwise false.
     *
     * @return If trip is ended, otherwise false.
     */
    public boolean isEnded() {
        return tripEnded;
    }

    /**
     * Returns long of the time of the initial tap on of the first stop.
     *
     * @return initial time of the first tap on of the first stop of this Trip
     */
    public long getInitialTime() {
        return initialTime;
    }

    /**
     * Returns the initial Stop that the card user first tapped onto in this Trip.
     *
     * @return the first Stop that was tapped onto this Trip
     */
    public Stop getInitialStop() {
        return locations.get(0).getStop();
    }

    /**
     * Returns the last Stop of this Trip that the card user last tapped off in this Trip.
     *
     * @return the last Stop tapped off in this Trip
     */
    public Stop getLastStop() {
        return getLastLocation().getStop();
    }

    /**
     * Returns last Route that the Trip was on
     *
     * @return last Route that Trip was on
     */
    public Route getLastRoute() {
        return getLastLocation().getRoute();
    }

    /**
     * Returns true if the last Tap was a TapOn function
     *
     * @return true if last Tap was tapOn, false otherwise
     */
    public boolean lastWasTapOn() {
        return getLastLocation().isTapOn();
    }

    /**
     * Returns last TripLocation of this Trip
     *
     * @return The last location of this trip before it ended.
     */
    public TripLocation getLastLocation() {
        return locations.get(locations.size() - 1);
    }

    /**
     * Returns the amount in double that this Trip is being charged for.
     *
     * @return the charge value of this Trip
     */
    public double getValue() {
        return tripValue;
    }

    /**
     * Return double of the true value of the Trip, the amount the Trip costs total
     *
     * @return Return double of the true value of the Trip
     */
    public double getTrueValue() {
        return trueValue;
    }

    /**
     * Return last Station that was Tapped
     *
     * @return last Station that was Tapped On/Off
     */
    public Station getLastSubwayTap() {
        return lastSubwayTap;
    }

    /**
     * Adds additional TripLocation on this Trip
     *
     * @param timestamp the time that the tap was
     * @param tappingOn whether it was tap on or tap off
     * @param stop      the stop location that the tap was located
     * @param route     the route of the tap
     * @param fined     whether the Trip is going to have fines
     */
    public void addLocation(long timestamp, boolean tappingOn, BusStop stop, Route route, boolean fined) {
        if (!tappingOn && !fined) {
            distanceTravelled += route.getDistance((BusStop) getLastStop(), stop);
        }
        locations.add(new TripLocation(timestamp, tappingOn, stop, route));
    }

    /**
     * Adds additional TripLocation on this Trip
     *
     * @param timestamp the time that the tap was
     * @param tappingOn true if tap on, false otherwise
     * @param station   the station that the tap was located
     * @param fined     true if Trip has fines, false otherwise
     */
    public void addLocation(long timestamp, boolean tappingOn, Station station, boolean fined) {
        if (!tappingOn && !fined) {
            distanceTravelled += station.getDistance((Station) getLastStop());
        }
        locations.add(new TripLocation(timestamp, tappingOn, station));
        lastSubwayTap = station;
    }

    /**
     * Returns the toString representation of this Trip. This shows the Taps, Locations of tap
     * , routes taken, the value of the Trip, the timeStamps.
     *
     * @return toString representation
     */
    public String toString() {
        String time = "[" + initialTime / 60 + ":" + initialTime % 60 + "]";
        StringBuilder details = new StringBuilder();
        details.append("Trip Details\n");
        details.append("==================\n");
        details.append("Started at:  ").append(time);
        details.append("\nActive:      ").append(!tripEnded);
        details.append(String.format("\nCost:        $%.2f", tripValue));
        details.append("\n\nLocation History:\n==================\n");
        for (TripLocation location : locations) {
            String locationTime = location.getTimestamp() / 60 + ":" + location.getTimestamp() % 60;
            details.append(String.format("[%s] %s ",
                    locationTime, (location.isTapOn()) ? "tap on " : "tap off "));
            if (location.isBusStop()) {
                details.append(String.format("on bus at stop %s on route %s",
                        location.getStop().getName(), location.getRoute().getId()));
            } else if (location.isStation()) {
                details.append("at station ").append(location.getStop().getName());
            }
        }
        return details.toString();
    }

    /**
     * Return the total distance travelled in this trip.
     *
     * @return distanceTravelled in this trip.
     */
    public int getDistanceTravelled() {
        return distanceTravelled;
    }
}
