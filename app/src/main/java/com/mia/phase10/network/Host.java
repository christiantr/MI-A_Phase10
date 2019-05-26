package com.mia.phase10.network;

import android.os.AsyncTask;
import android.util.Log;

import com.mia.phase10.network.threads.Connection;
import com.mia.phase10.network.threads.ConnectionListener;
import com.mia.phase10.network.threads.Connections;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Host extends AsyncTask {

    private static final int SERVER_PORT = 9999;
    private ServerSocket serverSocket;
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    private final static String TAG = "HOST";
    private Connections connections;
    private ConnectionListener connectionListener;
    private boolean active;

    private void startServer(int port) {
        Log.i(TAG, "Host start");
        active = true;
        connections = Connections.emptyList();
        connectionListener = new ConnectionListener(connections);

        try {
            serverSocket = new ServerSocket(port);

            while (active) {
                Connection connection = Connection.establishConnection(serverSocket.accept(), connectionListener);
                connections.addConnection(connection);
                Thread conn = new Thread(connection);
                conn.start();
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void closeServer() {
        try {
            active = false;
            serverSocket.close();
            in.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        startServer(SERVER_PORT);
        return null;

    }


}




