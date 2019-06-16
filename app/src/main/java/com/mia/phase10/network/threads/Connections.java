package com.mia.phase10.network.threads;

import android.app.Activity;
import android.util.Log;

import com.mia.phase10.activities.GameStartActivity;
import com.mia.phase10.network.Host;
import com.mia.phase10.network.UserID;
import com.mia.phase10.network.transport.TransportObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Connections {
    private List<Connection> connections;
    private final static String TAG = "Connections";
    private final Host host;
    private static final String HOST_NOT_NULL = "Host must not be NULL";
    private static final String LIST_NOT_NULL = "List must not be NULL";
    private Activity activity;

    private Connections(Host host, List<Connection> connections, Activity activity) {
        Objects.requireNonNull(host, HOST_NOT_NULL);
        Objects.requireNonNull(connections, LIST_NOT_NULL);
        this.host = host;
        this.connections = connections;
        this.activity = activity;
    }

    public void addConnection(final Connection connection) {
        this.connections.add(connection);
        connection.sendObject(TransportObject.tellUserName(connection.getConnectionDetails()));
        Log.i(TAG, "Connection Added");
        GameStartActivity.runOnUI(new Runnable() {
            public void run() {
                ((GameStartActivity) activity).changeConnectionDetails(connection.getConnectionDetails());
            }
        });

    }

    public static Connections emptyList(Host host, Activity activity) {

        return new Connections(host, new ArrayList<Connection>(), activity);
    }

    public void sendObjectToAll(Serializable obj) {
        for (Connection connection : connections) {
            connection.sendObject(obj);
        }
    }

    public void sendObjectToAllAndCloseAll(Serializable obj) {
        for (Connection connection : connections) {
            connection.sendObjectAndCloseConnection(obj);
        }
        Log.i(TAG, "All connections closed.\n");
        host.closeServer();
    }

    public Connection getConnectionById(UserID userID) {
        Log.i(TAG, String.format("%d,\n", userID.getUserId()));

        for (Connection connection : connections) {
            Log.i(TAG, String.format("%s\n", connection.getConnectionDetails().getUserID().toString()));
            if (connection.getConnectionDetails().getUserID().equals(userID)) {
                Log.i(TAG, "Connection found");

                return connection;
            }
        }
        return null;

    }
}
