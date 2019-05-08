package com.mia.phase10;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public static final String FIRST_PLAYER = "player_1";
    public static final String SECOND_PLAYER = "player_2";
    private EditText ip;
    private EditText port;
    private TextView hostPortIp;
    private Button joinGame;
    private Button hostGame;
    private Button connecToHost;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gamestart);
        hostGame = (Button) findViewById(R.id.button_hostGame);
        joinGame = (Button) findViewById(R.id.button_joinGame);
        connecToHost = (Button) findViewById(R.id.button_connectToHost);
        connecToHost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ipAdress = ip.getText().toString();
                int portNumber = Integer.parseInt(port.getText().toString());
                Log.i("TAG", String.format("Connect to: %s  at %d", ipAdress, portNumber));
            }
        });


        ip = (EditText) findViewById(R.id.input_Ip);
        port = (EditText) findViewById(R.id.input_port);
        hostPortIp = (TextView) findViewById(R.id.textView_IpAndPort);
        ip.setVisibility(View.GONE);
        port.setVisibility(View.GONE);
        connecToHost.setVisibility(View.GONE);
        hostPortIp.setVisibility(View.GONE);

//        setContentView(R.layout.activity_main);
    }

    /**
     * Called when the user uses the "Host Game" button
     */
    public void startHost(View view) {
        Log.i("TAG", "Starting game as host.");
        joinGame.setVisibility(View.GONE);
        hostGame.setVisibility(View.GONE);
        String dummyIp = "1.1.1.1";
        int dummyPort = 22;
        hostPortIp.setText(String.format("Hosting at: %s : %d", dummyIp, dummyPort));
        hostPortIp.setVisibility(View.VISIBLE);


    }

    /**
     * Called when the user uses the "Host Game" button
     */

    public void startClient(View view) {
        Log.i("TAG", "Starting game as client.");
//
        ip.setVisibility(View.VISIBLE);
        port.setVisibility(View.VISIBLE);
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
}
