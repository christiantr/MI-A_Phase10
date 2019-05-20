package com.mia.phase10.network;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Host extends AsyncTask {

    private static final int SERVER_PORT = 4001;
    private ServerSocket serverSocket;
    private Socket socket;
    private ObjectInputStream in;
    private final static String TAG = "HOST";


    private void startServer(int port) {
        Log.i(TAG, "Host start");
        try {
            serverSocket = new ServerSocket(port);


            socket = serverSocket.accept();
            InetAddress remoteIp = socket.getInetAddress();


            Log.i(TAG, String.format("Client connected. Remote Ip is: %s", remoteIp.toString()));

            // takes input from the client socket
//            in = new ObjectInputStream(
//                    new BufferedInputStream(socket.getInputStream()));
//            while (true) {
//                try {
//                    Object inObject = in.readObject();
//                    System.out.format(inObject.toString());
//
//                } catch (IOException i) {
//                    System.out.println(i);
//                } catch (ClassNotFoundException e) {
//                    e.printStackTrace();
//                }
//            }


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




