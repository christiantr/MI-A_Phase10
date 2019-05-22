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
    private final boolean local;


    private Client(InetAddress serverIp, int serverPort, boolean local) {
//        this.mHandler = mHandler;
        this.serverPort = serverPort;
        this.serverIp = serverIp;
        this.local = local;

    }

    public static Client atAddress(InetAddress serverIp, int serverPort) {
        return new Client(serverIp, serverPort, false);
    }

    public static Client atLocal(int serverPort) {
        Log.i(TAG, "local");
        return new Client(null, serverPort, true);
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        Log.i(TAG, "execute");

        try {
            run();
        } catch (IOException e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }


    public void run() throws IOException {
        Log.i(TAG, "run");
        if (!local) {
            Log.i(TAG, String.format("Client connecting to %s at %d", serverIp.toString(), serverPort));
            socket = new Socket(serverIp, serverPort);
        }
        if (local) {
            Log.i(TAG, "Try locahost");

//            InetAddress localhost = InetAddress.getLoopbackAddress();
            InetAddress localhost = InetAddress.getByName(null);

            if (localhost != null) {
                Log.i(TAG, localhost.toString());
            } else {
                Log.i(TAG, "Localhost not found");
            }
            socket = new Socket(localhost, serverPort);
        }


        out = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        out.flush();
        Log.i(TAG, String.format("I/O created"));
        active = true;
        in = new ObjectInputStream(
                new BufferedInputStream(socket.getInputStream()));


        while (active) {
            try {
                Object received = in.readObject();
                Log.i(TAG, ((TextTransportObject) received).toString());
            } catch (ClassNotFoundException e) {
                Log.e(TAG, e.toString());
            }
        }


//


    }

    public void sendObject(Serializable obj) {
        Thread sent = new Thread(new SentObjectThread(out, obj));
        sent.start();

    }
}