package com.mia.phase10.network.threads;

import android.app.Activity;
import android.util.Log;

import com.mia.phase10.GameStartActivity;
import com.mia.phase10.network.ConnectionDetails;
import com.mia.phase10.network.transport.BroadcastType;
import com.mia.phase10.network.transport.ControlObject;
import com.mia.phase10.network.transport.ObjectContentType;
import com.mia.phase10.network.transport.TransportObject;

import java.io.Serializable;

public class ConnectionListener {
    private Activity activity;
    private Connections connections;

    private static final String TAG = "CONNECTION_LISTENER";

    public ConnectionListener(Activity activity, Connections connections) {
        this.activity = activity;
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

        if (objectContentType.equals(ObjectContentType.USERNAME) && broadcastType.equals(BroadcastType.HOSTONLY)) {
            handleUsernameChange(obj);
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

    private void handleUsernameChange(TransportObject obj) {
        final ConnectionDetails connectionDetails = (ConnectionDetails) obj.getPayload();
        Log.i(TAG, String.format("new details: id %d  name %s \n", connectionDetails.getUserID().getUserId(),
                connectionDetails.getUserDisplayName().getName()));


        Connection connection = connections.getConnectionById(connectionDetails.getUserID());
        if (connection != null) {
            Log.i(TAG, connection.toString());
            connection.setConnectionDetails(connectionDetails);

            connection.sendObject(TransportObject.tellUserName(connectionDetails));
            Log.i(TAG, String.format("User &d informed about new name", connection.getConnectionDetails().getUserID().getUserId()));
        }

        GameStartActivity.runOnUI(new Runnable() {
            public void run() {
                ((GameStartActivity) activity).changeConnectionDetails(connectionDetails);
            }
        });


    }


}
