package hotcupsofjava.transitsystemmanager.managers;

import hotcupsofjava.transitsystemmanager.objects.transitobjects.BusStop;
import hotcupsofjava.transitsystemmanager.objects.transitobjects.Route;
import hotcupsofjava.transitsystemmanager.objects.transitobjects.Station;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @version 1.0
 */
public class RouteManager implements Serializable {
    private static RouteManager instance;

    public static RouteManager getInstance() {
        return instance;
    }

    public static void setInstance(RouteManager m) {
        instance = m;
    }

    private HashMap<String, BusStop> stops;
    private HashMap<String, Route> routes;
    private HashMap<String, Station> stations;
    private Station lastStation;
    private int stationCount = 1;

    /**
     * The constructor for RouteManager, which manages all the stops, stations, and routes of the
     * transitNetwork.
     * There can be many RouteManagers for different transitNetworks existing on the same system,
     * one for each network
     */
    public RouteManager() {
        stops = new HashMap<>();
        routes = new HashMap<>();
        stations = new HashMap<>();
        RouteManager.setInstance(this);
    }

    /**
     * Creates a BusStop and adds it in the transitNetwork, where id is unique of both
     * BusStops and Stations and name is initialized.
     *
     * @param id    the unique id of the created BusStop
     * @param name  the name of the created BusStop
     */
    public void addStop(String id, String name) {
        BusStop stop = new BusStop(id, name);
        stops.put(stop.getId(), stop);
    }

    /**
     * Creates a Station and adds it in the transitNetwork, where id is unique of both
     * BusStops and Stations and name is initialized.
     *
     * @param id    the unique id of the created Station
     * @param name  the name of the created Station
     */
    public void addStation(String id, String name) {
        Station station = new Station(id, name);
        stationCount++;
        if (lastStation == null) {
            lastStation = station;
        } else {
            lastStation.connectStation(station);
            lastStation = station;
        }
        stations.put(station.getId(), station);
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
        Station station = new Station(id, name, connectedBusStop);
        stationCount++;
        if (lastStation == null) {
            lastStation = station;
        } else {
            lastStation.connectStation(station);
            lastStation = station;
        }
        stations.put(station.getId(), station);
    }

    /**
     * Creates a route without BusStops and adds it to the transitNetwork.
     *
     * @param id the unique id of this Route
     */
    public void addRoute(String id) {
        Route route = new Route(id);
        routes.put(route.getId(), route);
    }

    /**
     * Creates a route with a list of existing Stops and adds it to the transitNetwork.
     *
     * @param id    the unique id of this Route
     * @param stops the list of existing Stops that this Route will be connected to
     */
    public void addRoute(String id, ArrayList<BusStop> stops) {
        Route route = new Route(id, stops);
        routes.put(route.getId(), route);
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

    public HashMap<String, Station> getStations() {
        return stations;
    }

    public HashMap<String, BusStop> getStops() {
        return stops;
    }

    public int getStationCount(){
        return stationCount;
    }
}
