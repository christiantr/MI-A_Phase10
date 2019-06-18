package com.mia.phase10.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mia.phase10.R;
import com.mia.phase10.classes.Player;
import com.mia.phase10.gameLogic.GameLogicHandler;
import com.mia.phase10.gameLogic.enums.Phase;
import com.mia.phase10.network.Client;
import com.mia.phase10.network.ConnectionDetails;
import com.mia.phase10.network.transport.ControlObject;
import com.mia.phase10.network.transport.TransportObject;

import java.util.Map;

public class GameEndActivity extends AppCompatActivity {
    private TextView winnerName;
    private TextView winnerPoints;
    private TextView loserName;
    private TextView loserPoints;
    private Button newGame;
    private final String TAG = "GameActivity";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameend);
        winnerName = findViewById(R.id.ID_winner_name);
        winnerPoints = findViewById(R.id.ID_winner_points);
        loserName = findViewById(R.id.ID_loser_name);
        loserPoints = findViewById(R.id.ID_loser_points);
        newGame = findViewById(R.id.ID_back_to_start);

        Map<String, Player> players = GameLogicHandler.getInstance().getGameData().getPlayers();
        for (Map.Entry<String, Player> entry : players.entrySet()) {
            if (entry.getValue().getCurrentPhase() == Phase.WIN) {
                winnerName.setText(entry.getValue().getCurrentName());
                String points = String.valueOf(entry.getValue().getPoints());
                winnerPoints.setText(points);
            } else {
                loserName.setText(entry.getValue().getCurrentName());
                String points = String.valueOf(entry.getValue().getPoints());
                loserPoints.setText(points);
            }
        }

        newGame.setOnClickListener(new View.OnClickListener() {
            //@Override
            public void onClick(View v) {
                exitApp();
            }
        });
    }

    protected void showAlert(){
        Log.i(TAG, "ReturnButton GameStartActivity.");
        AlertDialog.Builder alertShuttingDown = new AlertDialog.Builder(this);
        alertShuttingDown.setCancelable(false);
        alertShuttingDown.setTitle("Ein Spieler hat das Spiel verlassen!\nSpiel wird beendet!");
        alertShuttingDown.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                exitApp2();
            }
        });
        alertShuttingDown.setIcon(android.R.drawable.ic_dialog_info);
        alertShuttingDown.show();
    }


    protected void exitApp2(){
        GameLogicHandler.getInstance().closeConnections();
        overridePendingTransition(0, 0);
        finish();
    }

   protected void exitApp() {
        //GameLogicHandler.getInstance().getGameActivity().onBackPressed();
       showAlert();
    }

}
