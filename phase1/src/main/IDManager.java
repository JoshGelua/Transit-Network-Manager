package main;

import java.util.HashMap;

public class IDManager {
    private HashMap<String, Boolean> ids;

    IDManager() {
        ids = new HashMap<>();
    }

    public void addId(String id) throws RuntimeException {
        if (hasId(id)) {
            throw new RuntimeException("An item already exists with this id: " + id);
        }

        ids.put(id, true);
    }

    public boolean hasId(String id) {
        return ids.containsKey(id);
    }
}
