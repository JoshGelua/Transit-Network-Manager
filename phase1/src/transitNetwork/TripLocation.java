package transitNetwork;

public class TripLocation {
    // true: tap on, false: tap off
    private boolean tappingOn;
    private Stop stop;
    private Route route;
    private long timestamp;

    /**
     * The constructor for TripLocation that tracks the Location of the tap within a Trip.
     * This constructor handles TripLocation for BusStops.
     *
     * @param timestamp the time of tap
     * @param tappingOn true if tap on, false otherwise
     * @param stop      the stop where the tap is located
     * @param route     the route taken when tapped
     * @see Trip
     */
    TripLocation(long timestamp, boolean tappingOn, BusStop stop, Route route) {
        this.tappingOn = tappingOn;
        this.stop = stop;
        this.route = route;
        this.timestamp = timestamp;
    }

    /**
     * The constructor for TripLocation that tracks location of the tap within a Trip.
     * This constructor handles TripLocation for Stations.
     *
     * @param timestamp the time at which the tap was
     * @param tappingOn true if tap on, false otherwise
     * @param station   the station that the tap was at
     * @see Trip
     */
    TripLocation(long timestamp, boolean tappingOn, Station station) {
        this.tappingOn = tappingOn;
        this.stop = station;
        this.route = null;
        this.timestamp = timestamp;
    }

    /**
     * Return true if Tap is on for the TripLocation
     *
     * @return true if tap is on
     */
    public boolean isTapOn() {
        return this.tappingOn;
    }

    /**
     * Return Stop for this TripLocation
     * @return Stop
     */
    public Stop getStop() {
        return stop;
    }

    /**
     * Return true if the TripLocation has a Route
     * @return true if has Route, false otherwise.
     */
    public boolean hasRoute() {
        return route != null;
    }

    /**
     * Return Route that is attributed with this TripLocation
     * @return Route of this TripLocation
     */
    public Route getRoute() {
        return route;
    }

    /**
     * Return long of the timestamp of this TripLocation
     * @return long of timestamp of this TripLocation
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * Return true if the TripLocation is at BusStop, false otherwise
     * @return true if BusStop, false otherwise
     */
    public boolean isBusStop() {
        return stop instanceof BusStop;
    }

    /**
     * Return true if the TripLocation is at Station, false otherwise
     * @return true if Station, false otherwise
     */
    public boolean isStation() {
        return stop instanceof Station;
    }
}
