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
    private boolean active = false;
    private ConnectionListener connectionListener;

    public Connection(Socket socket, ConnectionListener connectionListener) {
        this.connectionListener = connectionListener;
        this.socket = socket;
        this.out = null;
        this.in = null;
        try {
            this.out = new ObjectOutputStream(
                    new BufferedOutputStream(socket.getOutputStream()));
            out.flush();

        } catch (IOException e) {
            Log.e(TAG, e.toString());
        }

        try {
            this.in = new ObjectInputStream(
                    new BufferedInputStream(socket.getInputStream()));
        } catch (IOException e) {
            Log.e(TAG, e.toString());
        }
        Log.i(TAG, String.format("Connection created for: %s", socket.getInetAddress().toString()));
        active = true;
    }

    @Override
    public void run() {
        while (active) {
            while (true) {
                try {
                    Object inObject = in.readObject();
                    Log.i(TAG, String.format("Received: %s", inObject.toString()));
                    connectionListener.onReceivedObject((Serializable) inObject);

                } catch (IOException i) {
                    Log.e(TAG, i.toString());

                } catch (ClassNotFoundException e) {
                    Log.e(TAG, e.toString());
                }
//            }
            }

        }
    }

    public void sendObject(Serializable obj) {
        Thread sent = new Thread(new SentObjectThread(out, obj));
        sent.start();

    }
}
