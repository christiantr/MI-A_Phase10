package com.mia.phase10.network;

import java.util.ArrayList;
import java.util.List;

public class ConnectionDetailsList {
    private List<ConnectionDetails> connections;


    public static ConnectionDetailsList empty() {
        return new ConnectionDetailsList(new ArrayList<ConnectionDetails>());
    }

    private ConnectionDetailsList(List<ConnectionDetails> connections) {
        this.connections = connections;
    }

    public void update(ConnectionDetails newDetails) {
        int indexReplace = -1;
        for (int i = 0; i < connections.size(); i++) {
            if (connections.get(i).getUserID().equals(newDetails.getUserID())) {
                indexReplace = i;
            }

        }

        if (indexReplace != -1) {
            connections.set(indexReplace, newDetails);
        } else {
            connections.add(newDetails);
        }

    }

    public List<ConnectionDetails> getList() {
        return connections;
    }
}

