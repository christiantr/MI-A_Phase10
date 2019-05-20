package com.mia.phase10;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mia.phase10.classes.Card;
import com.mia.phase10.classes.GameData;
import com.mia.phase10.classes.Player;
import com.mia.phase10.exceptionClasses.CardNotFoundException;
import com.mia.phase10.exceptionClasses.EmptyCardStackException;
import com.mia.phase10.exceptionClasses.EmptyHandException;
import com.mia.phase10.exceptionClasses.PlayerNotFoundException;
import com.mia.phase10.gameLogic.CardEvaluator;
import com.mia.phase10.gameLogic.GameLogicHandler;

import com.mia.phase10.gameLogic.StackType;

import com.mia.phase10.gameLogic.Phase;


import java.util.Map;

public class GameActivity extends AppCompatActivity implements View.OnLongClickListener {

    private LinearLayout deck;
    private LinearLayout discardPileLayout;
    private TextView player1;
    private TextView player2;

    private TextView score;
    private GameData gameData;

    private String player1Name;
    private String player2Name;
    static final String DISCARD_PILE = "DISCARD PILE";
    static final String DRAWABLE = "drawable";
    private LinearLayout playstationP1Layout;
    private LinearLayout playstationP2Layout;
    private Button check;
    private Button cancel;
    GameLogicHandler gameLogicHandler;



    public Button getCheck() {
        return check;
    }

    public Button getCancel() {
        return cancel;
    }

    public LinearLayout getPlaystationP2Layout() {
        return playstationP2Layout;
    }

    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // Initialize View and set Listeners
        setContentView(R.layout.activity_game);
        ImageView stack = findViewById(R.id.ID_stack);
        deck = findViewById(R.id.ID_deck);

        stack.setOnClickListener(new View.OnClickListener() {
            //@Override
            public void onClick(View v) {
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
                lp.setMargins(0, 0, 0, 0);
                ImageView cardImage = new ImageView(GameActivity.this);
                Card drawStackCard = null;
                String imagePath = "";
                try {
                     GameLogicHandler.getInstance().drawCard(GameLogicHandler.getInstance().getGameData().getActivePlayerId(), StackType.DRAW_STACK);
                } catch (EmptyCardStackException e) {
                    e.printStackTrace();
                }

            }
        });

        //preparing gameData
        GameLogicHandler.getInstance().initializeGame();
        GameLogicHandler.getInstance().addPlayer(new Player("player_1"));
        GameLogicHandler.getInstance().addPlayer(new Player("player_2"));
        GameLogicHandler.getInstance().setGameActivity(this);
        try {
            GameLogicHandler.getInstance().startRound();
        } catch (EmptyCardStackException e) {
            e.printStackTrace();
        }

    }


    public void visualize(){

        //Visualizing Data from GameData (GUI drawing ONLY here)
        View mainView =findViewById(R.id.id_main_screen);
        mainView.invalidate();
        visualizePhase();

        //Visualizing cards of active player
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        lp.setMargins(-70, 0, 0, 0);
        deck.removeAllViews();
        for (Card card : GameLogicHandler.getInstance().getGameData().getPlayers().get(GameLogicHandler.getInstance().getGameData().getActivePlayerId()).getHand().getCardList().values()) {
           cardImage.setId(card.getId());
            cardImage.setTag(DISCARD_PILE);
            cardImage.setOnLongClickListener(GameActivity.this);
            deck.addView(cardImage);

        }

    }
          
  
    private void checkPhase() {
        Phase phase = Phase.PHASE_4; //later get from Player
        CardEvaluator evaluator = CardEvaluator.getInstance();

        if (evaluator.checkPhase(phase, gameData.getPlayers().get(gameData.getActivePlayerId()).getPhaseCards())) {
            check.setVisibility(View.INVISIBLE);
            cancel.setVisibility(View.INVISIBLE);
            removeCardsFromHand();
            Toast.makeText(this, "The phase is correct!", Toast.LENGTH_SHORT).show();

            if (gameData.getActivePlayerId().equals("player_1")) {
                getDeck().removeAllViews();
                getPlaystationP1Layout().removeAllViews();
                getPlaystationP2Layout().removeAllViews();
                showPlaystation2Cards();
                gameData.setActivePlayerId("player_2");
                switchPlayerName(getPlayer2(), getPlayer1());

            } else {
                getPlaystationP2Layout().removeAllViews();
                showPlaystation2Cards();
                gameData.setActivePlayerId("player_1");
                getDeck().removeAllViews();
                switchPlayerName(getPlayer1(), getPlayer2());
                getPlaystationP1Layout().removeAllViews();

            }
            showHandCards();
            showPlaystation1Cards();
        } else {
            removeCardsFromPlaystationBackToHand();
            Toast.makeText(this, "The phase is not correct!", Toast.LENGTH_SHORT).show();
        }
    }

    public void removeCardsFromHand(){
        for (Card card : gameData.getPlayers().get(gameData.getActivePlayerId()).getPhaseCards()) {
            try {
                gameLogicHandler.layoffCard(gameData.getActivePlayerId(), card.getId()); //delete card of hand
            } catch (EmptyHandException | CardNotFoundException | PlayerNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void showPlaystation1Cards() {
        Map<String, Player> players = gameData.getPlayers();
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        lp.setMargins(0, 0, 0, 0);
        for (Card card : gameData.getPlayers().get(gameData.getActivePlayerId()).getPhaseCards()) {
            ImageView cardImage = new ImageView(GameActivity.this);
            cardImage.setLayoutParams(lp);
            Drawable c = getResources().getDrawable(getResources().getIdentifier(card.getImagePath(), DRAWABLE, getPackageName()));
            cardImage.setImageDrawable(c);
            cardImage.setTag(DISCARD_PILE);
            cardImage.setOnLongClickListener(GameActivity.this);
            cardImage.setId(card.getId());
            playstationP1Layout.addView(cardImage);
            cardImage.setVisibility(View.VISIBLE);
        }
    }
    public void showPlaystation2Cards() {
        Map<String, Player> players = gameData.getPlayers();
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        lp.setMargins(0, 0, 0, 0);
        for (Card card : gameData.getPlayers().get(gameData.getActivePlayerId()).getPhaseCards()) {
            ImageView cardImage = new ImageView(GameActivity.this);
            cardImage.setLayoutParams(lp);
            Drawable c = getResources().getDrawable(getResources().getIdentifier(card.getImagePath(), DRAWABLE, getPackageName()));
            cardImage.setImageDrawable(c);

            cardImage.setTag(DISCARD_PILE);
            cardImage.setOnLongClickListener(GameActivity.this);
            cardImage.setId(card.getId());
            playstationP2Layout.addView(cardImage);
            cardImage.setVisibility(View.VISIBLE);
        }
    }

    public void removeCardsFromPlaystationBackToHand() {
        check.setVisibility(View.INVISIBLE);
        cancel.setVisibility(View.INVISIBLE);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        lp.setMargins(0, 0, 0, 0);
        for (Card card : gameData.getPlayers().get(gameData.getActivePlayerId()).getPhaseCards()) {
            ImageView cardImage = new ImageView(GameActivity.this);
            cardImage.setLayoutParams(lp);
            Drawable c = getResources().getDrawable(getResources().getIdentifier(card.getImagePath(), DRAWABLE, getPackageName()));
            cardImage.setImageDrawable(c);
            cardImage.setTag(DISCARD_PILE);
            cardImage.setOnLongClickListener(GameActivity.this);
            cardImage.setId(card.getId());
            deck.addView(cardImage);
        }
        playstationP1Layout.removeAllViews();
        gameData.getPlayers().get(gameData.getActivePlayerId()).getPhaseCards().clear();
    }


    public void startShufflingActivity() {
        Intent shufflingActivity = new Intent(this, ShufflingActivity.class);
        startActivity(shufflingActivity);
    }

    public void visualizePhase(){
        ImageView drawStack = findViewById(R.id.ID_stack);
        LinearLayout layoffStack = findViewById(R.id.ID_discard_layout);
        switch(GameLogicHandler.getInstance().getGameData().getPhase()){

            case DRAW_PHASE:
                Toast.makeText(this, "DRAWPHASE", Toast.LENGTH_SHORT).show();


                drawStack.setBackgroundColor(Color.rgb(0,255,224));
                break;
            case LAYOFF_PHASE:
                Toast.makeText(this, "LAYOFFPHASE", Toast.LENGTH_SHORT).show();

                drawStack.setBackgroundColor(Color.argb(100,0,0,0));
                layoffStack.setBackgroundColor(Color.rgb(0,255,224));
                break;
            case END_TURN_PHASE:
                break;
        }
    }

    @Override
    public boolean onLongClick(View v) {
        // Create a new ClipData.Item from the ImageView object's tag
        ClipData.Item item = new ClipData.Item((CharSequence) v.getTag());
        // Create a new ClipData using the tag as a label, the plain text MIME type, and
        // the already-created item. This will create a new ClipDescription object within the
        // ClipData, and set its MIME type entry to "text/plain"
        String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};
        ClipData data = new ClipData(v.getTag().toString(), mimeTypes, item);
        // Instantiates the drag shadow builder.
        View.DragShadowBuilder dragshadow = new View.DragShadowBuilder(v);
        // Starts the drag
        v.startDrag(data      // data to be dragged
                , dragshadow  // drag shadow builder
                , v           // local data about the drag and drop operation
                , 0      // flags (not currently used, set to 0)
        );
        return true;
    }

    public void showHandCards() {
        Map<String, Player> players = GameLogicHandler.getInstance().getGameData().getPlayers();
        Map<Integer, Card> cards = GameLogicHandler.getInstance().getGameData().getPlayers().get(GameLogicHandler.getInstance().getGameData().getActivePlayerId()).getHand().getCardList();
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        lp.setMargins(0, 0, 0, 0);
        for (Card card : cards.values()) {
            ImageView cardImage = new ImageView(GameActivity.this);
            cardImage.setLayoutParams(lp);
            Drawable c = getResources().getDrawable(getResources().getIdentifier(card.getImagePath(), DRAWABLE, getPackageName()));
            cardImage.setImageDrawable(c);
            cardImage.setTag(DISCARD_PILE);
            cardImage.setOnLongClickListener(GameActivity.this);
            cardImage.setId(card.getId());
            deck.addView(cardImage);
        }
    }

    public void switchPlayerName(TextView p, TextView q) {
        p.setText(player1Name);
        q.setText(player2Name);
    }

    public void drawLayoffStack(){

    }

    public LinearLayout getDeck() {
        return deck;
    }

    public TextView getPlayer1() {
        return player1;
    }

    public TextView getPlayer2() {
        return player2;
    }

    public TextView getScore() {
        return score;
    }

    public LinearLayout getDiscardPileLayout() {
        return discardPileLayout;
    }

    public LinearLayout getPlaystationP1Layout() {
        return playstationP1Layout;
    }

}
