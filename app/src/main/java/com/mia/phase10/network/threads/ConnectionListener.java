package com.mia.phase10.network.threads;

import android.util.Log;

import com.mia.phase10.network.transport.BroadcastType;
import com.mia.phase10.network.transport.ControlObject;
import com.mia.phase10.network.transport.ObjectContentType;
import com.mia.phase10.network.transport.TransportObject;

import java.io.Serializable;

public class ConnectionListener {
    private Connections connections;

    private String TAG = "CONNECTION_LISTENER";

    public ConnectionListener(Connections connections) {
        this.connections = connections;
    }

    public void onReceivedObject(TransportObject obj) {
        Log.i(TAG, "Object received.\n");
        BroadcastType broadcastType = obj.getBroadcastType();
        ObjectContentType objectContentType = obj.getObjectContentType();

        if (broadcastType.equals(BroadcastType.BROADCAST_ALL) && objectContentType.equals(ObjectContentType.TEXT)) {
            sendToAllClients(obj);
        }

        if (objectContentType.equals(ObjectContentType.CONTROLINFO)) {
            handleControlObject(obj);
        }


    }

    private void sendToAllClients(Serializable obj) {
        Log.i(TAG, "Send Object to all Clients\n");

        this.connections.sendObjectToAll(obj);

    }

    private void handleControlObject(TransportObject obj) {
        ControlObject controlObject = (ControlObject) obj.getPayload();
        switch (controlObject.getControlCommand()) {
            case CLOSECONNECTIONS:
                connections.sendObjectToAllAndCloseAll(obj);
        }

    }


}
