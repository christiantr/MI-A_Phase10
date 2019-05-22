package com.mia.phase10.network.threads;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

public class Connection implements Runnable {

    private final Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private final static String TAG = "CONNECTION";
    private ConnectionListener connectionListener;
    private boolean active;

    public static Connection establishConnection(Socket socket, ConnectionListener connectionListener) {

        ObjectOutputStream out = null;
        ObjectInputStream in = null;
        try {
            out = new ObjectOutputStream(
                    new BufferedOutputStream(socket.getOutputStream()));
            out.flush();

        } catch (IOException e) {
            Log.e(TAG, e.toString());
        }

        try {
            in = new ObjectInputStream(
                    new BufferedInputStream(socket.getInputStream()));
        } catch (IOException e) {
            Log.e(TAG, e.toString());
        }
        Log.i(TAG, String.format("Connection created for: %s", socket.getInetAddress().toString()));
        return new Connection(socket, out, in, connectionListener, true);
    }


    public Connection(Socket socket,
                      ObjectOutputStream out,
                      ObjectInputStream in,
                      ConnectionListener connectionListener,
                      boolean active) {
        this.socket = socket;
        this.out = out;
        this.in = in;
        this.connectionListener = connectionListener;
        this.active = active;
    }

    @Override
    public void run() {
        while (active) {

            try {
                Object inObject = in.readObject();
                Log.i(TAG, String.format("Received: %s", inObject.toString()));
                connectionListener.onReceivedObject((Serializable) inObject);

            } catch (IOException i) {
                Log.e(TAG, i.toString());

            } catch (ClassNotFoundException e) {
                Log.e(TAG, e.toString());
            }


        }
    }

    public void sendObject(Serializable obj) {
        Thread sent = new Thread(new SentObjectThread(out, obj));
        sent.start();

    }
}