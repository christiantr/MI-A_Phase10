package com.mia.phase10.network;

import android.os.AsyncTask;
import android.util.Log;

import com.mia.phase10.network.threads.SentObjectThread;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.Socket;

public class Client extends AsyncTask {

    private static final String TAG = "CLIENT";
    private final int serverPort;
    private final InetAddress serverIp;
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private boolean active;


    public Client(InetAddress serverIp, int serverPort) {
//        this.mHandler = mHandler;
        this.serverPort = serverPort;
        this.serverIp = serverIp;
        Log.i(TAG, String.format("Client connecting to: %s  %d", serverIp.toString(), serverPort));
    }

    @Override
    protected Object doInBackground(Object[] objects) {

        try {
            run();
        } catch (IOException e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }


    public void run() throws IOException {
        Log.i(TAG, String.format("Connecting to %s at %d", serverIp.toString(), serverPort));
        socket = new Socket(serverIp, serverPort);


        out = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        out.flush();
        Log.i(TAG, String.format("I/O created"));
        active = true;


        Serializable obj = new TextTransportObject("Hello there.");
        sendObject(obj);

        Log.i(TAG, "message sent");
        in = new ObjectInputStream(
                new BufferedInputStream(socket.getInputStream()));
        Log.i(TAG, "in created");
        try {
            Object received = in.readObject();
            Log.i(TAG, ((TextTransportObject) received).toString());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


//


    }

    private void sendObject(Serializable obj) {
        Thread sent = new Thread(new SentObjectThread(out, obj));
        sent.start();

    }
}