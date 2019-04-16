package transitNetwork;

import java.util.ArrayList;

/**
 * @author
 * @version 1.0
 */
public class Route {
    private String id;
    private ArrayList<BusStop> route;

    /**
     * Creates a Route with an id that can be connected to BusStops.
     *
     * @param id an id that specifies which route this is
     */
    Route(String id) {
        this.id = id;
    }

    /**
     * Creates a Route with an id that is connected to a list of BusStops.
     *
     * @param id    an id that specifies which route this is
     * @param stops a list of BusStops that this route is connected to
     */
    Route(String id, ArrayList<BusStop> stops) {
        this.id = id;
        this.route = stops;
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

    /**
     * Returns a String of the id of this Route.
     *
     * @return this Route's id
     */
    public String getId() {
        return id;
    }

    /**
     * Returns true if the stop is a stop that is connected in this Route.
     *
     * @param stop a BusStop
     * @return true if stop is connected in this Route
     */
    public boolean contains(BusStop stop) {
        return route.contains(stop);
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

}
