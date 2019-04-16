package transitNetwork;

import user.Card;
/**
 * @author
 * @version 1.0
 */
public abstract class Stop {

    private final String id;
    private String name;
    private Stop connectedStop;

    /**
     * Constructor for abstract Stop.
     * Child classes for Stop are BusStop and Station that have unique ids in between each other.
     *
     * @param id    the unique id of the Stop
     * @param name  the name of the Stop
     */
    Stop(String id, String name) {
        this.id = id;
        this.name = name;
        this.connectedStop = null;
    }

    /**
     * Overloaded constructor for an abstract Stop that is connected to another Stop
     * @param id    the unique id for the Stop
     * @param name  the name of the Stop
     * @param stop  the connecting Stop to this Stop
     */
    Stop(String id, String name, Stop stop) {
        this.id = id;
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
     * @param name  the new name for this Stop
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Return String unique id of this Stop
     *
     * @return id of this Stop
     */
    public String getID() {
        return id;
    }

    /**
     * Connects a Stop to this Stop
     * @param stop  a Stop to connect to
     */
    void connectStop(Stop stop) {
        connectedStop = stop;
    }

    /**
     * Return Stop that is connected to this Stop
     * @return  Stop that is connected to this Stop
     */
    public Stop getConnectedStop() {
        return connectedStop;
    }
}
