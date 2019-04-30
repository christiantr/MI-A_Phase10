package com.mia.phase10;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public static final String FIRST_PLAYER = "player_1";
    public static final String SECOND_PLAYER = "player_2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /** Called when the user taps the Send button */
    public void startGame(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        EditText firstPlayer = (EditText) findViewById(R.id.ID_first_player);
        EditText secondPlayer = (EditText) findViewById(R.id.ID_second_player);
        intent.putExtra(FIRST_PLAYER, firstPlayer.getText().toString());
        intent.putExtra(SECOND_PLAYER, secondPlayer.getText().toString());
        startActivity(intent);
    }
}
