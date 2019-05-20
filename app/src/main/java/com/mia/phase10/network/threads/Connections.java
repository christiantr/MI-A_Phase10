package com.mia.phase10.network.threads;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Connections {
    private List<Connection> connections;

    private Connections(List<Connection> connections) {
        this.connections = connections;
    }

    public void addConnection(Connection connection){
        this.connections.add(connection);
    }

    public static Connections emptyList() {

        return new Connections(new ArrayList<Connection>());
    }

    public void sendObjectToAll(Serializable obj) {
        for (Connection connection : connections) {
            connection.sendObject(obj);
        }
    }
}
