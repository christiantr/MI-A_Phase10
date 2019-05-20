package com.mia.phase10.network;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
            e.printStackTrace();
        }
        return null;
    }


    public void run() throws IOException {
        Log.i(TAG, String.format("Connecting to %s at %d", serverIp.toString(), serverPort));
        socket = new Socket(serverIp, serverPort);

        out = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        Log.i(TAG, String.format("I/O created"));
        active = true;

        out.writeObject(new TextTransportObject("Hello there."));
        out.flush();
        Log.i(TAG, "message sent");




    }


}
