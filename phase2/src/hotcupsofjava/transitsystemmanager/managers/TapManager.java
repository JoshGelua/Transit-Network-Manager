package hotcupsofjava.transitsystemmanager.managers;

import hotcupsofjava.transitsystemmanager.Logger;
import hotcupsofjava.transitsystemmanager.objects.transitobjects.BusStop;
import hotcupsofjava.transitsystemmanager.objects.transitobjects.Route;
import hotcupsofjava.transitsystemmanager.objects.transitobjects.Station;
import hotcupsofjava.transitsystemmanager.objects.transitobjects.Stop;
import hotcupsofjava.transitsystemmanager.objects.userobjects.Card;
import hotcupsofjava.transitsystemmanager.objects.userobjects.Trip;
import hotcupsofjava.transitsystemmanager.objects.userobjects.TripLocation;

import java.io.Serializable;

public class TapManager implements Serializable {

    private static TapManager instance; // The instance of tap manager.
    private double busCost = 0; // The charge for the bus rides.
    private double subwayCost = 0; // The charge for the subway rides.
    private double tripCap = 6; // The tripCap for the trips
    /**
     * Return the tap manager.
     *
     * @return tap manager.
     */
    public static TapManager getInstance() {
        return instance;
    }

    /**
     * Set the tap manager for the system.
     *
     * @param i The tap manager to be set.
     */
    public static void setInstance(TapManager i) {
        instance = i;
    }

    public TapManager() {
        TapManager.setInstance(this);
    }
    /**
     * Set the trip cap for the system.
     *
     * @param tripCap The trip cap to be set.
     */
    public void updateTripCap(double tripCap){
        this.tripCap = tripCap;
    }
    public double getTripCap(double tripCap){
        return this.tripCap;
    }
    /**
     * Updates the cost for bus rides.
     *
     * @param cost The cost to be set.
     */
    public void updateBusCost(double cost) {
        this.busCost = cost;
    }

    /**
     * Updates the cost for subway rides
     *
     * @param cost The cost to be set
     */
    public void updateSubwayCost(double cost) {
        this.subwayCost = cost;
    }

    /**
     * Returns the cost for bus rides.
     *
     * @return busCost
     */
    public double getBusCost() {
        return busCost;
    }

    /**
     * Returns the cost for subway rides.
     *
     * @return subwayCost.
     */
    public double getSubwayCost() {
        return subwayCost;
    }

    /**
     * Handles the way a tap was made at a stop
     *
     * @param card      The card which was used to tap
     * @param timestamp The time at which the tap was made.
     * @param stop      The stop at which the tap was made.
     * @return Whether the tap was legal or no
     */
    private boolean tapOnHandler(Card card, long timestamp, Stop stop) {
        Trip trip = card.getCurrentTrip();
        boolean disconnectedTrip = false;
        if (trip != null) {
            // if the last location was a tap on and a station, charge fine
            // of the tripCap and end the previous trip as it is now invalid.
            TripLocation lastLocation = trip.getLastLocation();
            if (lastLocation.isTapOn() && lastLocation.isStation()) {
                Logger.log("Illegally tried to tap on at with out tapping off");
                card.chargeFine(tripCap);
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
     * @param card The card used for tapping
     * @return true if there was abnormal tapping, false otherwise
     */
    private boolean tapOffHandler(Stop stop, Card card) {
        Trip trip = card.getCurrentTrip();
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
            card.chargeFine(tripCap);
            stop.addFine(tripCap);
        } else if (chargeBus) {
            stop.addFine(busCost);
            card.chargeFine(busCost);
        }

        return chargeBus || chargeStation;
    }

    /**
     * Tap On method at a bus stop
     *
     * @param timestamp The time at which the tap was made
     * @param busStop   The busStop at which the tap was made.
     * @param card      The card used to make the tap.
     * @param route     The route the user was travelling on.
     */
    public void tapOn(long timestamp, Stop busStop, Card card, Route route) {
        boolean continuousTrip = tapOnHandler(card, timestamp, busStop);
        if (card.getBalance() > 0 && !card.isSuspended()) {
            if (!continuousTrip) card.newTrip(new Trip(timestamp, (BusStop) busStop, route));
            else card.getCurrentTrip().addLocation(timestamp, true, (BusStop) busStop, route, false);
            Logger.log(String.format("%s tapped on at bus stop %s on route %s at %d",
                    card.toString(), busStop.getName(), route.getId(), timestamp));
            card.charge(busCost, tripCap);
            busStop.addTap();
            busStop.addRevenue(busCost);
        } else {
            Logger.log(String.format("%s failed to tap on at bus stop %s on route %s at %d",
                    card.toString(), busStop.getName(), route.getId(), timestamp));
        }
    }

    /**
     * Tap Off method at a bus stop
     *
     * @param timestamp The time at which the tap was made.
     * @param busStop   The bus stop at which the tap was made.
     * @param card      The card which tapped at the stop
     * @param route     The route the user was travelling on.
     */
    public void tapOff(long timestamp, Stop busStop, Card card, Route route) {
        boolean abnormalTap = tapOffHandler(busStop, card);
        if (abnormalTap) {
            Logger.log("Illegally tried to tap off at" + busStop.getName() + "on route" + route.getId());
        }
        busStop.addTap();
        Logger.log(String.format("%s tapped off at bus stop %s on route %s at %d",
                card.toString(), busStop.getName(), route.getId(), timestamp));
        card.getCurrentTrip().addLocation(timestamp, false, (BusStop) busStop, route, abnormalTap);
    }

    /**
     * Tap On method at a subway stop
     *
     * @param timestamp The time at which the tap was made.
     * @param station   The station at which the tap was made.
     * @param card      The card with which the tap was made.
     */
    public void tapOn(long timestamp, Stop station, Card card) {
        boolean continuousTrip = tapOnHandler(card, timestamp, station);
        if (card.getBalance() > 0 && !card.isSuspended()) {
            if (!continuousTrip) card.newTrip(new Trip(timestamp, (Station) station));
            else card.getCurrentTrip().addLocation(timestamp, true, (Station) station, false);
            Logger.log(String.format("%s tapped on at subway station %s at %d",
                    card.toString(), station.getName(), timestamp));
            station.addTap();
        } else {
            Logger.log(String.format("%s failed to tap on at subway station %s at %d",
                    card.toString(), station.getName(), timestamp));
        }
    }

    /**
     * Tap Off method at a subway station
     *
     * @param timestamp The time at which the tap was made.
     * @param station   The station at which the tap was made.
     * @param card      The card which tapped at the station.
     */
    public void tapOff(long timestamp, Stop station, Card card) {
        boolean chargedFine = tapOffHandler(station, card);
        if (chargedFine) {
            if (card.getCurrentTrip() != null) {
                card.getCurrentTrip().addLocation(timestamp, false, (Station) station, chargedFine);
                card.getCurrentTrip().endTrip();
            }
            Logger.log(String.format("%s tapped off illegally at subway station %s at %d",
                    card.toString(), station.getName(), timestamp));
        } else {
            double cost = subwayCost * ((Station) station).getDistance(card.getCurrentTrip().getLastSubwayTap());
            card.charge(cost, tripCap);
            station.addRevenue(cost);
            card.getCurrentTrip().addLocation(timestamp, false, (Station) station, chargedFine);
            if (timestamp - card.getCurrentTrip().getInitialTime() > 120) {
                card.getCurrentTrip().endTrip();
            }
            station.addTap();
            Logger.log(String.format("%s tapped off at subway station %s at %d",
                    card.toString(), station.getName(), timestamp));
        }
    }
}
