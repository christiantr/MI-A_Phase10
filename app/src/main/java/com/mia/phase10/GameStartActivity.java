package com.mia.phase10;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mia.phase10.classes.Player;
import com.mia.phase10.gameLogic.GameLogicHandler;
import com.mia.phase10.network.Client;
import com.mia.phase10.network.ConnectionDetails;
import com.mia.phase10.network.Host;
import com.mia.phase10.network.IpAddressGet;
import com.mia.phase10.network.UserDisplayName;
import com.mia.phase10.network.transport.ControlCommand;
import com.mia.phase10.network.transport.ControlObject;
import com.mia.phase10.network.transport.TransportObject;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class GameStartActivity extends AppCompatActivity {
    private final String TAG = "GameStartActivity";
    public static final String USERNAME  = "username";
    public static final String FIRST_PLAYER = "player_1";
    public static final String SECOND_PLAYER = "player_2";
    private EditText ip;
    private EditText port;
    private TextView hostPortIp;
    private TextView textConnection1;
    private TextView textConnection2;
    private TextView textConnection3;
    private Button joinGame;
    private Button hostGame;
    private Button connecToHost;
    private Button testMessage;
    private Button start;
    private EditText username;
    private static final int SERVER_PORT = 9999;
    private static final String DEFAULT_IP = "192.168.43.124";
    AsyncTask client;
    private int numberOfConnections;
    private ConnectionDetails connectionDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//        StrictMode.setThreadPolicy(policy);

        super.onCreate(savedInstanceState);
        numberOfConnections = 0;
        setContentView(R.layout.activity_gamestart);
        hostGame = (Button) findViewById(R.id.button_hostGame);
        joinGame = (Button) findViewById(R.id.button_joinGame);
        connecToHost = (Button) findViewById(R.id.button_connectToHost);
        testMessage = (Button) findViewById(R.id.button_sendTestMessage);
        start = (Button) findViewById(R.id.button_start);
        textConnection1 = (TextView) findViewById(R.id.textView_connection1);
        textConnection2 = (TextView) findViewById(R.id.textView_connection2);
        textConnection3 = (TextView) findViewById(R.id.textView_connection3);
        username = (EditText) findViewById(R.id.editText_username);

        connecToHost.setOnClickListener(new View.OnClickListener() {
            //            @Override
            public void onClick(View view) {
                clickJoinGameButton(view);


            }
        });

        testMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Serializable obj = new TextTransportObject("Test message");
//                TransportObject obj = TransportObject.makeTextTransportObject("Test message");
                TransportObject obj = TransportObject.makeTextTransportObject("Hello");
                ((Client) client).sendObject(obj);

            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // setContentView(R.layout.activity_main);
                ((Client) client).sendObject(TransportObject.makeGameDataTransportObject());
                TransportObject obj = TransportObject.ofControlObjectToAll(ControlObject.StartGame());
             //   ((Client) client).sendObject(obj);
            }
        });


        username.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    changeUserName(v);
                    return true;
                }
                return false;
            }
        });


        ip = (EditText) findViewById(R.id.input_Ip);
        ip.setText(DEFAULT_IP);
        port = (EditText) findViewById(R.id.input_port);
        hostPortIp = (TextView) findViewById(R.id.textView_IpAndPort);
        ip.setVisibility(View.GONE);
        port.setVisibility(View.GONE);
        connecToHost.setVisibility(View.GONE);
        hostPortIp.setVisibility(View.GONE);
        testMessage.setVisibility(View.VISIBLE);
//
        textConnection1.setVisibility(View.GONE);
        textConnection2.setVisibility(View.GONE);
        textConnection3.setVisibility(View.GONE);

        username.setVisibility(View.GONE);

    }


    private void changeUserName(View view) {
        Log.i(TAG, client.toString());
        String newusername = username.getText().toString();
        Log.i(TAG, String.format("username manually set to %s \n", newusername));
        ConnectionDetails newConnectionDetails = this.connectionDetails.changeDisplayName(new UserDisplayName(newusername));
        TransportObject obj = TransportObject.setUserName(newConnectionDetails);
        ((Client) client).sendObject(obj);

    }

    private void clickJoinGameButton(View view) {
        Log.i("TAG", "Connect button clicked");
        InetAddress hostIpAddress = null;
        try {
            hostIpAddress = InetAddress.getByName(ip.getText().toString());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        client = Client.atAddress(hostIpAddress, SERVER_PORT, this);
        Log.i(TAG, client.toString());
        client.execute();
//                testMessage.setVisibility(View.VISIBLE);
        connecToHost.setVisibility(View.GONE);
        ip.setVisibility(View.GONE);
        testMessage.setVisibility(View.VISIBLE);
        GameLogicHandler.getInstance().setClient((Client) client);
    }

    /**
     * Called when the user uses the "Client Game" button
     */
    public void startHost(View view) {
        GameLogicHandler.getInstance().initializeGame();
        Log.i("TAG", "Starting game as host.");
        joinGame.setVisibility(View.GONE);
        hostGame.setVisibility(View.GONE);
        AsyncTask server = new Host(this);
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
//        port.setVisibility(View.VISIBLE);
        joinGame.setVisibility(View.GONE);
        hostGame.setVisibility(View.GONE);
        connecToHost.setVisibility(View.VISIBLE);
    }


    /**
     * Called when the user taps the Send button
     */
    public void startGame() {

        Intent intent = new Intent(this, GameActivity.class);
        EditText firstPlayer = (EditText) findViewById(R.id.ID_first_player);
        EditText secondPlayer = (EditText) findViewById(R.id.ID_second_player);
        intent.putExtra(USERNAME, username.getText().toString());
       // intent.putExtra("Client",client);
        startActivity(intent);

    }

    private void showIpAddress() {
        IpAddressGet finder = new IpAddressGet();
        String ipAddress = finder.wifiIpAddress(getApplicationContext());
        if (ipAddress != null) {
            Log.i(TAG, String.format("IP: %s\n", ipAddress));
            hostPortIp.setText(String.format("Hosting at: %s", ipAddress));
            hostPortIp.setVisibility(View.VISIBLE);
        } else {
            hostPortIp.setText(String.format("WiFi not available!"));
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


    public void changeConnectionDetails(ConnectionDetails connectionDetails) {
        Log.i(TAG, String.format("new connection, currently %d\n", numberOfConnections));

        if (connectionDetails.getUserID().getUserId() == 1) {
            Log.i(TAG, "First connection");

            this.textConnection1.setText(connectionDetails.getUserDisplayName().getName());
            this.textConnection1.setVisibility(View.VISIBLE);
            GameLogicHandler.getInstance().addPlayer(new Player(connectionDetails.getUserDisplayName().getName()));

        }
        if (connectionDetails.getUserID().getUserId() == 2) {
            Log.i(TAG, "Second connection");

            this.textConnection2.setText(connectionDetails.getUserDisplayName().getName());
            this.textConnection2.setVisibility(View.VISIBLE);
            GameLogicHandler.getInstance().addPlayer(new Player(connectionDetails.getUserDisplayName().getName()));

        }

        if (connectionDetails.getUserID().getUserId() == 3) {
            Log.i(TAG, "Second connection");

            this.textConnection3.setText(connectionDetails.getUserDisplayName().getName());
            this.textConnection3.setVisibility(View.VISIBLE);
            GameLogicHandler.getInstance().addPlayer(new Player(connectionDetails.getUserDisplayName().getName()));

        }

//
//
//        if (numberOfConnections == 2) {
//            Log.i(TAG, "Third connection");
//
//            this.textConnection3.setText("User 3");
//            this.textConnection3.setVisibility(View.VISIBLE);
//
//        }

        numberOfConnections++;
        Log.i(TAG, String.format("Number of connections: %d \n", numberOfConnections));

    }

    public static Handler UIHandler;

    static {
        UIHandler = new Handler(Looper.getMainLooper());
    }

    public static void runOnUI(Runnable runnable) {
        UIHandler.post(runnable);
    }

}
