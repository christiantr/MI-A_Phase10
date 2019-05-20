package com.mia.phase10;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mia.phase10.network.Client;
import com.mia.phase10.network.IpAddressGet;
import com.mia.phase10.network.Host;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "MAIN";
    public static final String FIRST_PLAYER = "player_1";
    public static final String SECOND_PLAYER = "player_2";
    private EditText ip;
    private EditText port;
    private TextView hostPortIp;
    private Button joinGame;
    private Button hostGame;
    private Button connecToHost;
    private static final int SERVER_PORT = 4001;
    private static final String DEFAULT_IP = "192.168.1.5";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gamestart);
        hostGame = (Button) findViewById(R.id.button_hostGame);
        joinGame = (Button) findViewById(R.id.button_joinGame);
        connecToHost = (Button) findViewById(R.id.button_connectToHost);
        connecToHost.setOnClickListener(new View.OnClickListener() {
            //            @Override
            public void onClick(View view) {
                Log.i("TAG", "Connect button clicked");
                InetAddress hostIpAddress = null;
                try {
                    hostIpAddress = InetAddress.getByName(ip.getText().toString());
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }

                AsyncTask client = new Client(hostIpAddress, SERVER_PORT);
                client.execute();
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

//        setContentView(R.layout.activity_main);
    }

    /**
     * Called when the user uses the "Client Game" button
     */
    public void startHost(View view) {
        Log.i("TAG", "Starting game as host.");
        joinGame.setVisibility(View.GONE);
        hostGame.setVisibility(View.GONE);
        AsyncTask server = new Host();
        server.execute();
        showIpAddress();


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
    public void startGame(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        EditText firstPlayer = (EditText) findViewById(R.id.ID_first_player);
        EditText secondPlayer = (EditText) findViewById(R.id.ID_second_player);
        intent.putExtra(FIRST_PLAYER, firstPlayer.getText().toString());
        intent.putExtra(SECOND_PLAYER, secondPlayer.getText().toString());
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
}
