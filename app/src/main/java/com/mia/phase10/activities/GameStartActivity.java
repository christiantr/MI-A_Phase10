package com.mia.phase10.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mia.phase10.R;
import com.mia.phase10.classes.Player;
import com.mia.phase10.exceptionClasses.EmptyCardStackException;
import com.mia.phase10.gameLogic.GameLogicHandler;
import com.mia.phase10.network.Client;
import com.mia.phase10.network.ConnectionDetails;
import com.mia.phase10.network.ConnectionDetailsList;
import com.mia.phase10.network.Host;
import com.mia.phase10.network.IpAddressGet;
import com.mia.phase10.network.UserDisplayName;
import com.mia.phase10.network.transport.ControlObject;
import com.mia.phase10.network.transport.TransportObject;

import org.apache.commons.validator.routines.InetAddressValidator;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class GameStartActivity extends AppCompatActivity {
    private static final String TAG = "GameStartActivity";

    public static final String USERNAME = "username";

    private EditText ip;
    private TextView hostPortIp;
    private TextView textConnection1;
    private TextView textConnection2;
    private TextView textConnection3;
    private Button joinGame;
    private Button hostGame;
    private Button connecToHost;
    private Button start;
    private EditText username;
    private static final int SERVER_PORT = 9999;
    private static final String DEFAULT_IP = "10.0.0.5";
    AsyncTask client;
    AsyncTask server;
    private int numberOfConnections;
    private ConnectionDetails connectionDetails;
    private ConnectionDetailsList connectionDetailsList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        numberOfConnections = 0;
        setContentView(R.layout.activity_gamestart);
        hostGame = (Button) findViewById(R.id.button_hostGame);
        joinGame = (Button) findViewById(R.id.button_joinGame);
        connecToHost = (Button) findViewById(R.id.button_connectToHost);
        start = (Button) findViewById(R.id.button_start);
        textConnection1 = (TextView) findViewById(R.id.textView_connection1);
        textConnection2 = (TextView) findViewById(R.id.textView_connection2);
        textConnection3 = (TextView) findViewById(R.id.textView_connection3);
        username = (EditText) findViewById(R.id.editText_username);

        connecToHost.setOnClickListener(new View.OnClickListener() {
            //            @Override
            public void onClick(View view) {
                clickJoinGameButton();
            }
        });


        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Start Game.");
                boolean allowed = true;
                ConnectionDetails u1 = connectionDetailsList.getList().get(0);
                ConnectionDetails u2 = connectionDetailsList.getList().get(1);
                if (u1.getUserDisplayName().getName().equals(u2.getUserDisplayName().getName())) {
                    Log.i(TAG, "Same username");
                    String usrName = username.getText().toString();
                    username.setText(usrName + "(1)");
                    changeUserName();
                    allowed = false;

                }

                if (allowed) {
                    for (ConnectionDetails details : connectionDetailsList.getList()) {
                        GameLogicHandler.getInstance().addPlayer(new Player(details.getUserDisplayName().getName()));
                        Log.i(TAG, String.format("Player %s added.\n", details.getUserDisplayName().getName()));
                    }
                    // setContentView(R.layout.activity_main);
                    try {
                        GameLogicHandler.getInstance().startRound();
                    } catch (EmptyCardStackException e) {
                        Log.e(TAG, e.toString());
                    }
                    ((Client) client).sendObject(TransportObject.makeGameDataTransportObject());
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        Log.e(TAG, e.toString());
                        Thread.currentThread().interrupt();
                    }
                    TransportObject obj = TransportObject.ofControlObjectToAll(ControlObject.startGame());
                    ((Client) client).sendObject(obj);
                }
            }
        });


        username.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    changeUserName();
                    return true;
                }
                return false;
            }
        });


        ip = (EditText) findViewById(R.id.input_Ip);


        ip.setText(DEFAULT_IP);

        ip.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    enterNewIp();
                    return true;
                }
                return false;
            }
        });

        hostPortIp = (TextView) findViewById(R.id.textView_IpAndPort);
        ip.setVisibility(View.GONE);
        connecToHost.setVisibility(View.GONE);
        hostPortIp.setVisibility(View.GONE);
        start.setVisibility(View.GONE);
//
        textConnection1.setVisibility(View.GONE);
        textConnection2.setVisibility(View.GONE);
        textConnection3.setVisibility(View.GONE);

        username.setVisibility(View.GONE);


    }


    @Override
    public void onBackPressed() {
        if (client != null) {
            TransportObject object = TransportObject.ofControlObjectToAll(ControlObject.alertUsers());
            ((Client) client).sendObject(object);
        } else if (this.findViewById(R.id.button_connectToHost).getVisibility() == View.VISIBLE) {
            joinGame.setVisibility(View.VISIBLE);
            hostGame.setVisibility(View.VISIBLE);
            connecToHost.setVisibility(View.GONE);
            ip.setVisibility(View.GONE);
        } else {
            super.onBackPressed();
        }

    }

    public void showAlert() {
        Log.i(TAG, "ReturnButton GameStartActivity.");
        AlertDialog.Builder alertShuttingDown = new AlertDialog.Builder(this);
        alertShuttingDown.setCancelable(false);
        alertShuttingDown.setTitle("Ein Spieler hat die Verbindung unterbrochen!\nSpiel wird beendet!");
        alertShuttingDown.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                exitApp();
            }
        });
        alertShuttingDown.setIcon(android.R.drawable.ic_dialog_info);
        alertShuttingDown.show();
    }


    protected void exitApp() {
        Log.i(TAG, "Close GameStartActivity.");
        if (client != null) {
            TransportObject object = TransportObject.ofControlObjectToAll(ControlObject.closeConnections());
            ((Client) client).sendObject(object);
        }
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Log.e(TAG, e.toString());
            Thread.currentThread().interrupt();
        }
        overridePendingTransition(0, 0);
        finish();
    }


    private boolean enterNewIp() {
        Log.i(TAG, "New Ip entered!");
        String enteredIp = ip.getText().toString();
        InetAddressValidator validator = InetAddressValidator.getInstance();
        if (!validator.isValid(enteredIp)) {
            ip.setText("Invalid Ip Entered");
            return false;

        }

        return true;


    }

    private void changeUserName() {
        Log.i(TAG, client.toString());
        String newusername = username.getText().toString();
        Log.i(TAG, String.format("username manually set to %s %n", newusername));
        ConnectionDetails newConnectionDetails = this.connectionDetails.changeDisplayName(new UserDisplayName(newusername));
        TransportObject obj = TransportObject.setUserName(newConnectionDetails);
        ((Client) client).sendObject(obj);

    }

    private void clickJoinGameButton() {
        Log.i("TAG", "Connect button clicked");

        boolean validIp = enterNewIp();

        boolean hostKnown = true;
        if (validIp) {
            Log.i(TAG, "connecting");
            InetAddress hostIpAddress = null;
            try {
                hostIpAddress = InetAddress.getByName(ip.getText().toString());
            } catch (UnknownHostException e) {
                Log.e(TAG, "Unknown Host");
                ip.setText("Unknown Host");
                hostKnown = false;
            }
            if (hostKnown) {
                client = Client.atAddress(hostIpAddress, SERVER_PORT, this);
                Log.i(TAG, client.toString());
                try {
                    ip.setEnabled(false);
                    client.execute();

                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                    Log.i(TAG, "Cant connect to host");
                    this.cantConnectClient();
                }


//
            }
        }
    }

    /**
     * Called when the user uses the "Client Game" button
     */
    public void startHost(View view) {
        connectionDetailsList = connectionDetailsList.empty();
        GameLogicHandler.getInstance().initializeGame();
        Log.i("TAG", "Starting game as host.");
        joinGame.setVisibility(View.GONE);
        hostGame.setVisibility(View.GONE);
        server = new Host(this);
        server.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        showIpAddress();

        Log.i(TAG, "Starting Client at Host.\n");

        client = Client.atLocal(SERVER_PORT, this);

        Log.i(TAG, "local");
        client.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        GameLogicHandler.getInstance().setClient((Client) client);

    }

    /**
     * Called when the user uses the "Client Game" button
     */

    public void startClient(View view) {
        Log.i("TAG", "Starting game as client.");
//
        ip.setVisibility(View.VISIBLE);
        joinGame.setVisibility(View.GONE);
        hostGame.setVisibility(View.GONE);
        connecToHost.setVisibility(View.VISIBLE);
    }


    /**
     * Called when the user taps the Send button
     */
    public void startGame() {


        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(USERNAME, connectionDetails.getUserDisplayName().getName());
        GameActivity.client = client;
        finish();
        startActivity(intent);

    }

    private void showIpAddress() {
        IpAddressGet finder = new IpAddressGet();
        String ipAddress = finder.wifiIpAddress(getApplicationContext());
        if (ipAddress != null) {
            Log.i(TAG, String.format("IP: %s%n", ipAddress));
            hostPortIp.setText(String.format("Hosting at: %s", ipAddress));
            hostPortIp.setVisibility(View.VISIBLE);
        } else {
            hostPortIp.setText("WiFi not available!");
            hostPortIp.setTextColor(Color.RED);
        }


        hostPortIp.setVisibility(View.VISIBLE);


    }

    public void setUsername(ConnectionDetails connectionDetails) {
        Log.i(TAG, String.format("Username set to: %s\n", connectionDetails.getUserDisplayName().getName()));
        this.connectionDetails = connectionDetails;
        this.username.setText(connectionDetails.getUserDisplayName().getName());
        this.username.setVisibility(View.VISIBLE);
    }

    public void cantConnectClient() {
        Log.i(TAG, "Notfied that Client could not be started");
        ip.setText("Could not connect to Host!");
        ip.setVisibility(View.VISIBLE);
        connecToHost.setVisibility(View.VISIBLE);
        ip.setEnabled(true);


    }

    public void clientConnected() {
        Log.i(TAG, "Notified that client succesfull connected");
        connecToHost.setVisibility(View.GONE);
        ip.setVisibility(View.GONE);
        GameLogicHandler.getInstance().setClient((Client) client);

    }

    public void changeConnectionDetails(ConnectionDetails connectionDetails) {
        Log.i(TAG, String.format("new connection, currently %d%n", numberOfConnections));
        connectionDetailsList.update(connectionDetails);
        if (connectionDetails.getUserID().getIdentification() == 1) {
            Log.i(TAG, "First connection");

            this.textConnection1.setText(connectionDetails.getUserDisplayName().getName());
            this.textConnection1.setVisibility(View.VISIBLE);

        }
        if (connectionDetails.getUserID().getIdentification() == 2) {
            Log.i(TAG, "Second connection");

            this.textConnection2.setText(connectionDetails.getUserDisplayName().getName());
            this.textConnection2.setVisibility(View.VISIBLE);

        }

        if (connectionDetails.getUserID().getIdentification() == 3) {
            Log.i(TAG, "Second connection");

            this.textConnection3.setText(connectionDetails.getUserDisplayName().getName());
            this.textConnection3.setVisibility(View.VISIBLE);

        }


        numberOfConnections++;
        Log.i(TAG, String.format("Number of connections: %d %n", numberOfConnections));
        if (numberOfConnections >= 2) {
            start.setVisibility(View.VISIBLE);
        }

    }

    public static Handler UIHandler;

    static {
        UIHandler = new Handler(Looper.getMainLooper());
    }

    public static void runOnUI(Runnable runnable) {
        UIHandler.post(runnable);
    }


}
