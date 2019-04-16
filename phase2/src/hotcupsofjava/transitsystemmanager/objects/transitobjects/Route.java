package hotcupsofjava.transitsystemmanager.objects.transitobjects;

import hotcupsofjava.transitsystemmanager.objects.TransitSystemObject;

import java.util.ArrayList;

/**
 * @version 1.0
 */
public class Route extends TransitSystemObject {
    private ArrayList<BusStop> route; // The list of BusStops in this route.

    /**
     * Creates a Route with an id that can be connected to BusStops.
     *
     * @param id an id that specifies which route this is.
     */
    public Route(String id) {
        super(id);
    }

    /**
     * Creates a Route with an id that is connected to a list of BusStops.
     *
     * @param id    an id that specifies which route this is
     * @param stops a list of BusStops that this route is connected to
     */
    public Route(String id, ArrayList<BusStop> stops) {
        super(id);
        this.route = stops;
        addRouteToBusStops();
    }

    /**
     * Adds a list of stops to connect to this route.
     *
     * @param stops a list of stops
     */
    public void addStops(ArrayList<BusStop> stops) {
        this.route.addAll(stops);
    }

    /**
     * Adds a list of stops to connect to this route in after stop located at the index.
     *
     * @param stops a list of stops
     * @param index the indexed stop at which a list of stops will be added after.
     */
    public void addStops(ArrayList<BusStop> stops, int index) {
        this.route.addAll(index, stops);
    }

    public boolean containsBoth(BusStop stop1, BusStop stop2) {
        return route.contains(stop1) && route.contains(stop2);
    }

    /**
     * Returns the distance between the first stop and the last stop of this Route.
     * Or, the amount of stops that this Route has.
     *
     * @param initialStop the first stop of this Route
     * @param finalStop   the last stop of this Route
     * @return an integer of the distance between the first stop and last stop
     */
    public int getDistance(BusStop initialStop, BusStop finalStop) {
        int start = route.indexOf(initialStop);
        int end = route.indexOf(finalStop);
        return Math.abs(end - start);
    }

    /**
     * Add this route to the busStop.
     */
    private void addRouteToBusStops() {
        for (BusStop busStop : route) {
            busStop.addToRoutes(this);
        }
    }

}
