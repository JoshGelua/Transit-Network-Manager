package hotcupsofjava.transitsystemmanager.objects.transitobjects;

import hotcupsofjava.transitsystemmanager.objects.TransitSystemObject;

/**
 * @version 1.0
 */
public class Stop extends TransitSystemObject {

    private String name; // Name of this stop.
    private Stop connectedStop; // The connected stop.
    private int taps; // Number of taps at this stop.
    private double revenueAtStop; // Revenue collected at this stop.
    private int fineValue; // The fine collected at this stop.

    /**
     * Constructor for abstract Stop.
     * Child classes for Stop are BusStop and Station that have unique ids in between each other.
     *
     * @param id   the unique id of the Stop
     * @param name the name of the Stop
     */
    Stop(String id, String name) {
        super(id);
        this.name = name;
        this.connectedStop = null;
        this.taps = 0;
        this.revenueAtStop = 0;
        this.fineValue = 0;
    }

    /**
     * Overloaded constructor for an abstract Stop that is connected to another Stop
     *
     * @param id   the unique id for the Stop
     * @param name the name of the Stop
     * @param stop the connecting Stop to this Stop
     */
    Stop(String id, String name, Stop stop) {
        super(id);
        this.name = name;
        this.connectedStop = stop;
    }

    /**
     * Returns name of this Stop
     *
     * @return name of Stop
     */
    public String getName() {
        return name;
    }

    /**
     * Set name of this Stop
     *
     * @param name the new name for this Stop
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Connects a Stop to this Stop
     *
     * @param stop a Stop to connect to
     */
    void connectStop(Stop stop) {
        connectedStop = stop;
    }

    /**
     * Return Stop that is connected to this Stop
     *
     * @return Stop that is connected to this Stop
     */
    public Stop getConnectedStop() {
        return connectedStop;
    }

    /**
     * Increases the number of taps at this stop
     */
    public void addTap() {
        taps += 1;
    }

    /**
     * Returns the taps at this stop.
     *
     * @return the taps at this stop.
     */
    public int getTaps() {
        return taps;
    }

    /**
     * Increases the revenue at this stop
     */
    public void addRevenue(double value) {
        revenueAtStop += value;
    }

    /**
     * Returns the total revenue at this stop.
     *
     * @return the total revenue at this stop.
     */
    public double getRevenueAtStop() {
        return revenueAtStop;
    }

    /**
     * Increases the fine at this stop
     */
    public void addFine(double value) {
        fineValue += value;
    }

    /**
     * Returns the total fines at this stop.
     *
     * @return the total fines at this stop.
     */
    public int getFineValue() {
        return fineValue;
    }
}
