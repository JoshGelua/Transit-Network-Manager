package hotcupsofjava.transitsystemmanager.objects.transitobjects;

import hotcupsofjava.transitsystemmanager.managers.RouteManager;

import java.util.Queue;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * @version 1.0
 */
public class Station extends Stop {

    private ArrayList<Station> connectingStations;

    /**
     * The constructor of the Station that initializes the unique id and the name of the Station.
     * The id must be unique to all Stops, including BusStops and Stations, but the name may be
     * shared.
     *
     * @param id   the identification of the stop that is unique that cannot be changed
     * @param name the name of the station
     * @see Stop
     */
    public Station(String id, String name) {
        super(id, name);
        this.connectingStations = new ArrayList<>();
    }

    /**
     * The constructor of the Station that initializes the unique id and the name of the Station.
     * The id must be unique to all Stops, including BusStops and Stations, but the name may be
     * shared. Takes in a BusStop that connects to this Station.
     *
     * @param id                the identification of the stop that is unique that cannot be changed
     * @param name              the name of the station
     * @param connectingBusStop the BusStop that this Station is connected to
     * @see Stop
     */
    public Station(String id, String name, BusStop connectingBusStop) {
        super(id, name);
        this.connectingStations = new ArrayList<>();
        this.connectStop(connectingBusStop);
        connectingBusStop.connectStation(this);
    }

    /**
     * Connects an immediate station to this Station.
     *
     * @param station connects a station to this Station
     */
    public void connectStation(Station station) {
        connectingStations.add(station);
        if (!station.getConnectedStations().contains(this)) {
            station.connectStation(this);
        }
    }

    /**
     * Connects a list of stations to this station.
     *
     * @param stations an arraylist of stations to connect this one to
     */
    /* Connect an arrayList of stations to this station */
    public void connectStations(ArrayList<Station> stations) {
        connectingStations.addAll(stations);
        for (Station station : stations) {
            if (!station.getConnectedStations().contains(this)) {
                station.connectStation(this);
            }
        }
    }

    /**
     * Connects a BusStop to this Station so that they can be used for jointed trips, throws exception
     * otherwise.
     *
     * @param stop a stop that connects to this Station
     */
    @Override
    void connectStop(Stop stop) {
        if (!(stop instanceof BusStop))
            throw new RuntimeException("Station connected with Station when supposed to be BusStop");
        BusStop busStop = (BusStop) stop;
        super.connectStop(busStop);
        busStop.connectStop(this);
    }

    /**
     * Connects a BusStop to this Station so that they can be used for jointed trips.
     *
     * @param busStop a BusStop that connects to this Station
     */
    void connectBusStop(BusStop busStop) {
        connectStop(busStop);
    }

    /**
     * Returns a BusStop that this Station is connected to.
     *
     * @return a BusStop
     */
    BusStop getConnectedBusStop() {
        return (BusStop) this.getConnectedStop();
    }

    /**
     * Returns a list of Stations that are connected to this station.
     *
     * @return a list of Stations
     */
    public ArrayList<Station> getConnectedStations() {
        return connectingStations;
    }

    /**
     * Returns an integer that is the shortest distance between the initial Station and the last
     * Station at tap off.
     *
     * @param lastStop the Station that was initially tapped onto
     * @return an integer of the distance between initial Station and this Station
     */
    /* Breadth-first search
     * null values in the queue separate depth values
     * max depth is the number of existing stations
     * ArrayList prevents re-visiting nodes */
    public int getDistance(Station lastStop) {
        ArrayList<Station> visitedNodes = new ArrayList<>();
        Queue<Station> queue = new LinkedList<>();
        Station currentNode = null;
        int depth = 0;
        queue.add(this);
        queue.add(null);

        while (currentNode != lastStop && depth < RouteManager.getInstance().getStationCount()) {
            currentNode = queue.remove();
            if (currentNode == null) {
                depth++;
                queue.add(null);
            } else if (!visitedNodes.contains(currentNode)) {
                visitedNodes.add(currentNode);
                queue.addAll(currentNode.getConnectedStations());
            }
        }
        if (currentNode != lastStop) {
            //Illegal exit!
            return -1;
        }
        return depth;
    }
}
