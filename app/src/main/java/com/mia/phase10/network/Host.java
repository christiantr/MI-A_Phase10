package com.mia.phase10.network;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Host extends AsyncTask {

    private static final int SERVER_PORT = 4001;
    private ServerSocket serverSocket;
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    private final static String TAG = "HOST";
    private List<Socket> connections;

    private void startServer(int port) {
        Log.i(TAG, "Host start");
        connections = new ArrayList<>();
        try {
            serverSocket = new ServerSocket(port);


            connections.add(serverSocket.accept());
            InetAddress remoteIp = connections.get(0).getInetAddress();


            Log.i(TAG, String.format("Client connected. Remote Ip is: %s", remoteIp.toString()));

            // takes input from the client socket
            in = new ObjectInputStream(
                    new BufferedInputStream(connections.get(0).getInputStream()));

            while (true) {
                try {
                    Object inObject = in.readObject();
                  Log.i(TAG, String.format("Received: %s",inObject.toString()));

                } catch (IOException i) {
                    Log.e(TAG, i.toString());

                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void closeServer() {
        try {
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




