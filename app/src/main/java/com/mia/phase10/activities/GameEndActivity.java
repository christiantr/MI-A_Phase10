package com.mia.phase10.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mia.phase10.R;
import com.mia.phase10.classes.Player;
import com.mia.phase10.gameLogic.GameLogicHandler;
import com.mia.phase10.gameLogic.enums.Phase;

import java.util.Map;

public class GameEndActivity extends AppCompatActivity {
    private TextView winnerName;
    private TextView winnerPoints;
    private TextView loserName;
    private TextView loserPoints;
    private Button newGame;

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

    protected void exitApp() {
        GameLogicHandler.getInstance().closeConnections();
        overridePendingTransition(0, 0);
        finish();
    }

}
