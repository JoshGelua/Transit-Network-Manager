package user;

import transitNetwork.*;
import main.Logger;

public class Card {
    private double balance;
    private boolean suspended;
    private double totalSpending;
    private double totalFines;

    private String id;
    private User user;

    private Trip[] trips;

    public Card(String id, User user) {
        balance = 19;
        suspended = false;
        trips = new Trip[3];
        this.user = user;
        this.id = id;
    }

    public Trip[] getTrips() {
        return trips;
    }

    protected void suspend() {
        suspended = true;
    }

    public boolean isSuspended() {
        return suspended;
    }

    protected void addBalance(int amount) {
        balance += amount;
    }

    /* Returns the available balance on the card. */
    public double getBalance() {
        if (trips[0] != null) return balance - trips[0].getValue();
        else return balance;
    }

    /* Ensures that when a card is charged, the value is added to the current trip's total. */
    public boolean charge(double amount) {
        if (!suspended && getBalance() > 0) {
            trips[0].charge(amount);
            Logger.log(toString() + " charged $" + amount + ".");
            return true;
        }
        return false;
    }

    public void chargeFine(double amount) {
        balance -= amount;
        totalFines += amount;
        Logger.log(String.format("%s was charged a fine of $%.2f.", toString(), amount));
    }

    private void newTrip(Trip trip) {
        if (!suspended) {
            Trip previousTrip = getCurrentTrip();
            if (previousTrip != null) {
                balance -= previousTrip.getValue();
                totalSpending += previousTrip.getValue();
                Logger.logRevenue(previousTrip.getValue(), previousTrip.getTrueValue());
                previousTrip.endTrip();
            }
            trips[2] = trips[1];
            trips[1] = trips[0];
            trips[0] = trip;
            Logger.log(toString() + " started a new trip.");
        }
    }

    public Trip getCurrentTrip() {
        return trips[0];
    }

    private boolean tapOnHandler(long timestamp, Stop stop) {
        Trip trip = getCurrentTrip();
        boolean disconnectedTrip = false;
        if (trip != null) {
            // if the last location was a tap on and a station, charge fine
            // of $6 and end the previous trip as it is now invalid.
            TripLocation lastLocation = trip.getLastLocation();
            if (lastLocation.isTapOn() && lastLocation.isStation()) {
                chargeFine(6);
                trip.endTrip();
            }
            disconnectedTrip = trip.getLastStop() != stop && trip.getLastStop().getConnectedStop() != stop;
        }
        boolean activeTrip = trip != null && !(trip.isEnded() || timestamp - trip.getInitialTime() > 120);
        return activeTrip && !disconnectedTrip;
    }

    /**
     * General things that need to be done for tap off.
     *
     * @param stop transit system stop
     * @return true if there was abnormal tapping, false otherwise
     */
    private boolean tapOffHandler(Stop stop) {
        Trip trip = getCurrentTrip();
        boolean isStation = stop instanceof Station;
        boolean chargeStation = false;
        boolean chargeBus = false;
        if (trip == null || trip.isEnded()) {
            if (isStation) chargeStation = true;
            else chargeBus = true;
        } else if (trip.getLastStop() instanceof BusStop &&
                !trip.getLastRoute().containsBoth((BusStop) trip.getLastStop(), (BusStop) stop)) {
            chargeBus = true;
        }
        if (chargeStation) {
            chargeFine(6);
        } else if (chargeBus) {
            charge(2);
        }

        return chargeBus || chargeStation;
    }

    public void tapOn(long timestamp, Station station) {
        boolean continuousTrip = tapOnHandler(timestamp, station);
        if (balance > 0 && !suspended) {
            if (!continuousTrip) newTrip(new Trip(timestamp, station));
            else getCurrentTrip().addLocation(timestamp, true, station, false);
            Logger.log(String.format("%s tapped on at subway station %s at %d",
                    toString(), station.getName(), timestamp));
        } else {
            Logger.log(String.format("%s failed to tap on at subway station %s at %d",
                    toString(), station.getName(), timestamp));
        }
    }

    public void tapOff(long timestamp, Station station) {
        boolean chargedFine = tapOffHandler(station);
        if (chargedFine) {
            if (getCurrentTrip() != null) {
                getCurrentTrip().addLocation(timestamp, false, station, chargedFine);
                getCurrentTrip().endTrip();
            }
            Logger.log(String.format("%s tapped off illegally at subway station %s at %d",
                    toString(), station.getName(), timestamp));
        } else {
            getCurrentTrip().addLocation(timestamp, false, station, chargedFine);
            if (timestamp - getCurrentTrip().getInitialTime() > 120) {
                getCurrentTrip().endTrip();
            }
            Logger.log(String.format("%s tapped off at subway station %s at %d",
                    toString(), station.getName(), timestamp));
        }
    }

    public void tapOn(long timestamp, BusStop busStop, Route route) {
        boolean continousTrip = tapOnHandler(timestamp, busStop);
        if (balance > 0 && !suspended) {
            if (!continousTrip) newTrip(new Trip(timestamp, busStop, route));
            else getCurrentTrip().addLocation(timestamp, true, busStop, route, false);
            Logger.log(String.format("%s tapped on at bus stop %s on route %s at %d",
                    toString(), busStop.getName(), route.getId(), timestamp));
        } else {
            Logger.log(String.format("%s failed to tap on at bus stop %s on route %s at %d",
                    toString(), busStop.getName(), route.getId(), timestamp));
        }
    }

    public void tapOff(long timestamp, BusStop busStop, Route route) {
        boolean abnormalTap = tapOffHandler(busStop);
        Logger.log(String.format("%s tapped off at bus stop %s on route %s at %d",
                toString(), busStop.getName(), route.getId(), timestamp));
        getCurrentTrip().addLocation(timestamp, false, busStop, route, abnormalTap);
    }

    protected double getTotalSpending() {
        return totalSpending;
    }

    protected double getTotalFines() {
        return totalFines;
    }

    public String getId() {
        return id;
    }

    public String toString() {
        return "Card " + id + " owned by user " + user.getName();
    }
}
