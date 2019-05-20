package com.mia.phase10.network.threads;

import android.util.Log;

import java.io.Serializable;

public class ConnectionListener {
    private Connections connections;

    private String TAG = "CONNECTION_LISTENER";

    public ConnectionListener(Connections connections) {
        this.connections = connections;
    }

    public void onReceivedObject(Serializable obj) {
        Log.i(TAG, "Object received.\n");
        sendToAllClients(obj);
    }

    private void sendToAllClients(Serializable obj) {
        Log.i(TAG, "Send Object to all Clients\n");

        this.connections.sendObjectToAll(obj);

    }


}
