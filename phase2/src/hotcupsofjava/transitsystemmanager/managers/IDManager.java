package hotcupsofjava.transitsystemmanager.managers;

import java.io.Serializable;
import java.util.HashMap;

public class IDManager implements Serializable {

    private static IDManager instance; // The instance for IDManager.
    private HashMap<String, Boolean> ids; // The ids for all the objects in the system.

    /**
     * Returns the id manager.
     *
     * @return The id manager.
     */
    public static IDManager getInstance() {
        return instance;
    }

    /**
     * Sets the instance for the id manager.
     *
     * @param m The id manager to be set.
     */
    public static void setInstance(IDManager m) {
        instance = m;
    }

    /**
     * Constructs an id manager.
     */
    public IDManager() {
        ids = new HashMap<>();
        IDManager.setInstance(this);
    }

    /**
     * Adds the id when a transit object or user object is created.
     *
     * @param id The id of the object
     * @return The id.
     */
    public String addId(String id) {
        int modifier = -1;
        String resultId = id;
        while (ids.containsKey(resultId)) {
            modifier++;
            resultId = id + modifier;
        }

        ids.put(id, true);
        return id;
    }
}

