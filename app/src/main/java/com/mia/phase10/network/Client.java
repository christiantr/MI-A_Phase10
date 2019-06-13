package com.mia.phase10.network;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.mia.phase10.GameActivity;
import com.mia.phase10.GameStartActivity;
import com.mia.phase10.classes.GameData;
import com.mia.phase10.gameFlow.GamePhase;
import com.mia.phase10.gameLogic.GameLogicHandler;
import com.mia.phase10.network.threads.SentObjectThread;
import com.mia.phase10.network.transport.ControlCommand;
import com.mia.phase10.network.transport.ControlObject;
import com.mia.phase10.network.transport.ObjectContentType;
import com.mia.phase10.network.transport.TransportObject;

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
    private final Activity activity;



    private Client(InetAddress serverIp, int serverPort, boolean local, Activity activity) {
//        this.mHandler = mHandler;
        this.serverPort = serverPort;
        this.serverIp = serverIp;
        this.local = local;
        this.activity = activity;

    }

    public static Client atAddress(InetAddress serverIp, int serverPort, Activity activity) {
        return new Client(serverIp, serverPort, false, activity);
    }

    public static Client atLocal(int serverPort, GameStartActivity activity) {
        Log.i(TAG, "local");
        return new Client(null, serverPort, true, activity);
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
                TransportObject received = (TransportObject) in.readObject();
                ObjectContentType objectContentType = received.getObjectContentType();

                if (objectContentType.equals(ObjectContentType.TEXT)) {
                    Log.i(TAG, received.getPayload().toString());
                }

                if (objectContentType.equals(ObjectContentType.CONTROLINFO)) {
                    handleControlObject(received);
                }

                if (objectContentType.equals(ObjectContentType.USERNAME)) {
                    handleUsernameObject(received);
                }

                if(objectContentType.equals(ObjectContentType.GAMEDATA)){
                    handleGamedata(received);



                }


            } catch (ClassNotFoundException e) {
                Log.e(TAG, e.toString());
            }
        }


    }

    private void handleGamedata(TransportObject received) {
        GameLogicHandler.getInstance().setGameData((GameData) received.getPayload());
        GameActivity gameActivity = GameLogicHandler.getInstance().getGameActivity();
        if (gameActivity!=null) {
            gameActivity.runOnUiThread(new Runnable() {
                public void run() {
                    GameLogicHandler.getInstance().getGameActivity().visualize();            }
            } );
        }
    }

    public void sendObject(Serializable obj) {
        Thread sent = new Thread(new SentObjectThread(out, obj));
        sent.start();

    }

    private void handleControlObject(TransportObject obj) {
        ControlObject controlObject = (ControlObject) obj.getPayload();
        if (controlObject.getControlCommand().equals(ControlCommand.CLOSECONNECTIONS)) {
            closeConnection();
        }
        if(controlObject.getControlCommand().equals(ControlCommand.STARTGAME)){
            GameStartActivity.runOnUI(new Runnable() {
                public void run() {
                    ((GameStartActivity) activity).startGame();            }
            });
        }
        if(controlObject.getControlCommand().equals(ControlCommand.ALERTUSERS)){
            GameStartActivity.runOnUI(new Runnable() {
                public void run() {
                    ((GameStartActivity) activity).showAlert();            }
            });
        }


    }

    private void handleUsernameObject(TransportObject obj) {
        final ConnectionDetails connectionDetails = (ConnectionDetails) obj.getPayload();
        Log.i(TAG, String.format("Username %s \n", connectionDetails.getUserDisplayName().getName()));
        GameStartActivity.runOnUI(new Runnable() {
            public void run() {
                ((GameStartActivity) activity).setUsername(connectionDetails);            }
        });

    }

    private void closeConnection() {
        Log.i(TAG, "Closing connection to Host.\n");
        active = false;
        try {
            out.close();
            in.close();
            socket.close();

        } catch (IOException e) {
            Log.e(TAG, e.toString());
        }
        Log.i(TAG, "Connection to Host closed!\n");
        System.exit(0);
    }
}