package hotcupsofjava.transitsystemmanager.objects.transitobjects;

import java.util.ArrayList;

/**
 * @version 1.0
 */
public class BusStop extends Stop {

    private ArrayList<Route> routes; // All the routes that this busStop is connected to.

    /**
     * The constructor of the BusStop that initializes the unique id and the name of the BusStop.
     * The id must be unique to all Stops, including BusStops and Stations, but the name may be
     * shared.
     *
     * @param id   the identification of the stop that is unique that cannot be changed
     * @param name the name of the bus stop
     * @see Stop
     */
    public BusStop(String id, String name) {
        super(id, name);
        this.routes = new ArrayList<>();
    }

    /**
     * Connects the stop to this BusStop so that tap on/tap off functions will consider trips between
     * these two stops legal. Override function.
     *
     * @param stop connects the stop to this BusStop if station, throws exception otherwise
     */
    @Override
    void connectStop(Stop stop) {
        if (!(stop instanceof Station)) throw new RuntimeException("BusStop connected with BusStop instead of Station");
        Station station = (Station) stop;
        super.connectStop(station);
    }

    /**
     * Connects the station to this BusStop so that tap on/tap off functions will consider trips between
     * the station and the BusStop connected.
     *
     * @param station connects station to this BusStop
     */
    void connectStation(Station station) {
        connectStop(station);
    }

    /**
     * Returns the Station that this BusStop is connected to.
     *
     * @return the station that this BusStop is connected to
     */
    Station getConnectedStation() {
        return (Station) this.getConnectedStop();
    }

    /**
     * Adds the given route to this stops routes.
     *
     * @param route the route to be added.
     */
    public void addToRoutes(Route route) {
        routes.add(route);
    }

    /**
     * Returns whether this stop has the given route.
     *
     * @param routeId The id of the given route.
     * @return boolean whether this stop has the given route.
     */
    public boolean hasRoute(String routeId) {
        for (Route route : routes) {
            if (route.getId().equals(routeId)) {
                return true;
            }
        }
        return false;
    }
}
