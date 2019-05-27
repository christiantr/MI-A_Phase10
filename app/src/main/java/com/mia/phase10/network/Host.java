package com.mia.phase10.network;

import android.os.AsyncTask;
import android.util.Log;

import com.mia.phase10.network.threads.Connection;
import com.mia.phase10.network.threads.ConnectionListener;
import com.mia.phase10.network.threads.Connections;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketException;

public class Host extends AsyncTask {

    private static final int SERVER_PORT = 9999;
    private ServerSocket serverSocket;


    private final static String TAG = "HOST";
    private Connections connections;
    private ConnectionListener connectionListener;
    private boolean active;

    private void startServer(int port) {
        Log.i(TAG, "Host start");
        active = true;
        connections = Connections.emptyList(this);
        connectionListener = new ConnectionListener(connections);

        try {
            serverSocket = new ServerSocket(port);

            while (active) {
                Connection connection = Connection.establishConnection(serverSocket.accept(), connectionListener);
                connections.addConnection(connection);
                Thread conn = new Thread(connection);
                conn.start();
            }


        } catch (SocketException se) {
            Log.i(TAG, "");
        } catch (IOException e) {
            Log.e(TAG, e.toString());
        }

    }

    public void closeServer() {
        active = false;
        try {
            serverSocket.close();


        } catch (IOException e) {
            Log.e(TAG, e.toString());
        }
        Log.i(TAG, "Host closed!\n");
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        startServer(SERVER_PORT);
        return null;

    }


}




