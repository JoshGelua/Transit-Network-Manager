package transitNetwork;

import main.IDManager;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author
 * @version 1.0
 */
public class RouteManager {
    private IDManager idManager;
    private HashMap<String, BusStop> stops;
    private HashMap<String, Route> routes;
    private HashMap<String, Station> stations;
    private Station lastStation;
    private int stationCount = 0;

    /**
     * The constructor for RouteManager, which manages all the stops, stations, and routes of the
     * transitNetwork.
     * There can be many RouteManagers for different transitNetworks existing on the same system,
     * one for each network
     *
     * @param idManager the id of this Manager for this transitNetwork.
     */
    public RouteManager(IDManager idManager) {
        this.idManager = idManager;
        stops = new HashMap<>();
        routes = new HashMap<>();
        stations = new HashMap<>();
    }

    /**
     * Creates a BusStop and adds it in the transitNetwork, where id is unique of both
     * BusStops and Stations and name is initialized.
     *
     * @param id    the unique id of the created BusStop
     * @param name  the name of the created BusStop
     */
    public void addStop(String id, String name) {
        idManager.addId(id);
        stops.put(id, new BusStop(id, name));
    }

    /**
     * Creates a Station and adds it in the transitNetwork, where id is unique of both
     * BusStops and Stations and name is initialized.
     *
     * @param id    the unique id of the created Station
     * @param name  the name of the created Station
     */
    public void addStation(String id, String name) {
        idManager.addId(id);
        Station station = new Station(id, name, stationCount++);
        if (lastStation == null) {
            lastStation = station;
        } else {
            lastStation.connectStation(station);
            lastStation = station;
        }
        stations.put(id, station);
    }

    /**
     * Overloaded method. Creates a station that is connected to an existing BusStop,
     * where id is unique of BusStops and Stations and name is initialized.
     * This method is necessary in order to initialize a Station with an existing connected BusStop.
     *
     * @param id                the unique id of this Station
     * @param name              the name of this Station
     * @param connectedBusStop  the connected BusStop that is adjacent to this Station
     */
    public void addStation(String id, String name, BusStop connectedBusStop) {
        idManager.addId(id);
        Station station = new Station(id, name, connectedBusStop);
        if (lastStation == null) {
            lastStation = station;
        } else {
            lastStation.connectStation(station);
            lastStation = station;
        }
        stations.put(id, station);
    }

    /**
     * Creates a route without BusStops and adds it to the transitNetwork.
     *
     * @param id the unique id of this Route
     */
    public void addRoute(String id) {
        idManager.addId(id);
        routes.put(id, new Route(id));
    }

    /**
     * Creates a route with a list of existing Stops and adds it to the transitNetwork.
     *
     * @param id    the unique id of this Route
     * @param stops the list of existing Stops that this Route will be connected to
     */
    public void addRoute(String id, ArrayList<BusStop> stops) {
        idManager.addId(id);
        routes.put(id, new Route(id, stops));
    }

    /**
     * Returns the BusStop with the unique id that was specified.
     *
     * @param id    the unique id of the BusStop
     * @return      the BusStop with the respective unique id specified
     */
    public BusStop getStop(String id) {
        return stops.get(id);
    }

    /**
     * Returns the Route with the unique id that was specified.
     *
     * @param id    the unique id of the Route
     * @return      the Route with the respective unique id specified
     */
    public Route getRoute(String id) {
        return routes.get(id);
    }

    /**
     * Returns the Station with the unique id that was specified.
     *
     * @param id    the unique id of the Station
     * @return      the Station with the respective unique id specified
     */
    public Station getStation(String id) {
        return stations.get(id);
    }

    /**
     * Returns true if the BusStop with the specified unique id exists, otherwise false.
     *
     * @param id    the unique id of the BusStop
     * @return      true if BusStop with id exists, otherwise false
     */
    public boolean hasStop(String id) {
        return stops.containsKey(id);
    }

    /**
     * Returns true if the Route with the specified unique id exists, otherwise false.
     *
     * @param id    the unique id of the Route
     * @return      true if Route with the id exists, otherwise false
     */
    public boolean hasRoute(String id) {
        return routes.containsKey(id);
    }

    /**
     * Returns true if the Station with the specified unique id exists, otherwise false.
     *
     * @param id    the unique id of the Station
     * @return      true if Station with the id exists, otherwise false
     */
    public boolean hasStation(String id) {
        return stations.containsKey(id);
    }
}
