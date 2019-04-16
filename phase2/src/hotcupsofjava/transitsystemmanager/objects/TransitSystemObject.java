package hotcupsofjava.transitsystemmanager.objects;


import hotcupsofjava.transitsystemmanager.managers.IDManager;

import java.io.Serializable;

public abstract class TransitSystemObject implements Serializable {
    private static IDManager idManager = null;

    public static void setIdManager(IDManager m) {
        TransitSystemObject.idManager = m;
    }

    private final String id;
    
    public TransitSystemObject(String id) {
        if (idManager == null) throw new RuntimeException("System was not initialized properly!");
        this.id = idManager.addId(id);
    }

    public String getId() {
        return this.id;
    }

}

