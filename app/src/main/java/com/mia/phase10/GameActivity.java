package com.mia.phase10;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mia.phase10.classes.Card;
import com.mia.phase10.classes.GameData;
import com.mia.phase10.classes.Player;
import com.mia.phase10.exceptionClasses.EmptyCardStackException;
import com.mia.phase10.gameLogic.CardEvaluator;
import com.mia.phase10.gameLogic.GameLogicHandler;
import com.mia.phase10.gameLogic.Phase;

import java.util.ArrayList;
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
    private ArrayList <Card> cardlist;


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
        setContentView(R.layout.activity_game);

        GameLogicHandler gameLogicHandler;
        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        player1 = findViewById(R.id.ID_player_1);
        player2 = findViewById(R.id.ID_player_2);
        score = findViewById(R.id.ID_score);
        // Capture the layout's TextView and set the string as its text
        player1Name = intent.getStringExtra(MainActivity.FIRST_PLAYER);
        player2Name = intent.getStringExtra(MainActivity.SECOND_PLAYER);
        player1.setText(player1Name);
        player2.setText(player2Name);
        gameLogicHandler = GameLogicHandler.getInstance();
        gameLogicHandler.initializeGame();
        gameLogicHandler.getGameData().getDrawStack().mixStack();
        gameLogicHandler.addPlayer(new Player("player_1"));
        gameLogicHandler.addPlayer(new Player("player_2"));
        try {
            gameLogicHandler.startRound();
        } catch (EmptyCardStackException e) {
            e.printStackTrace();
        }
        gameData = gameLogicHandler.getGameData();
        gameData.setActivePlayerId("player_1");

        ImageView stack = findViewById(R.id.ID_stack);
        deck = findViewById(R.id.ID_deck);
        discardPileLayout = findViewById(R.id.ID_discard_layout);
        playstationP1Layout = findViewById(R.id.ID_p1_playstation_layout);
        playstationP2Layout = findViewById(R.id.ID_p2_playstation_layout);

        Button shuffle = findViewById(R.id.openShuffling);
        shuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startShufflingActivity();
            }
        });

        // shows cards form player1
        showHandCards();

        MyDragEventListener myDragEventListener = new MyDragEventListener(this);
        discardPileLayout.setOnDragListener(myDragEventListener);


        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        lp.setMargins(0, 0, 0, 0);
        ImageView cardImage = new ImageView(this);
        cardImage.setLayoutParams(lp);
        Drawable c = getResources().getDrawable(getResources().getIdentifier(gameData.getDrawStack().getFirstCard().getImagePath(), DRAWABLE, getPackageName()));
        cardImage.setImageDrawable(c);
        cardImage.setTag(DISCARD_PILE);
        cardImage.setOnLongClickListener(this);
        discardPileLayout.addView(cardImage);

        MyDragEventListenerTwo myDrag= new MyDragEventListenerTwo(this);
        playstationP1Layout.setOnDragListener(myDrag);
        cardlist = gameData.getPlayers().get(gameData.getActivePlayerId()).getPhaseCards();

        check = findViewById(R.id.checkPhase);
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPhase();
            }
        });
        cancel = findViewById(R.id.Cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeCardsFromPlaystation();
            }
        });


        stack.setOnClickListener(new View.OnClickListener() {
            //@Override
            public void onClick(View v) {
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
                lp.setMargins(0, 0, 0, 0);
                ImageView cardImage = new ImageView(GameActivity.this);
                Card drawStackCard = null;
                String imagePath = "";
                try {
                    drawStackCard = gameData.getDrawStack().drawCard();
                    imagePath = drawStackCard.getImagePath();
                } catch (EmptyCardStackException e) {
                    e.printStackTrace();
                }
                cardImage.setLayoutParams(lp);
                Drawable c = getResources().getDrawable(getResources().getIdentifier(imagePath, DRAWABLE, getPackageName()));
                cardImage.setImageDrawable(c);
                cardImage.setTag(DISCARD_PILE);
                cardImage.setOnLongClickListener(GameActivity.this);
                cardImage.setId(drawStackCard.getId());
                gameData.getPlayers().get(gameData.getActivePlayerId()).getHand().addCard(drawStackCard);
                deck.addView(cardImage);
            }
        });
    }

    private void checkPhase() {
        Phase phase=Phase.PHASE_4; //later get from Player
        CardEvaluator evaluator=CardEvaluator.getInstance();

       if(evaluator.checkPhase(phase,cardlist)){
            check.setVisibility(View.INVISIBLE);
            cancel.setVisibility(View.INVISIBLE);

           if (gameData.getActivePlayerId().equals("player_1")) {
                    getDeck().removeAllViews();
                    getPlaystationP1Layout().removeAllViews();
                    gameData.setActivePlayerId("player_2");
                    switchPlayerName(getPlayer2(), getPlayer1());

                } else {
                    gameData.setActivePlayerId("player_1");
                    getDeck().removeAllViews();
                    switchPlayerName(getPlayer1(), getPlayer2());
                    getPlaystationP1Layout().removeAllViews();

                }
           showHandCards();
           showPlaystationCards();
        }
        else removeCardsFromPlaystation();
    }
    public void showPlaystationCards() {
        Map<String, Player> players = gameData.getPlayers();
        ArrayList <Card> cards = players.get(gameData.getActivePlayerId()).getPhaseCards();
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        lp.setMargins(0, 0, 0, 0);
        for (Card card : cards) {
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

    public void removeCardsFromPlaystation(){
        check.setVisibility(View.INVISIBLE);
        cancel.setVisibility(View.INVISIBLE);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        lp.setMargins(0, 0, 0, 0);
        for (Card card : cardlist) {
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
        cardlist.clear();

    }

    public void startShufflingActivity() {
        Intent shufflingActivity = new Intent(this, ShufflingActivity.class);
        startActivity(shufflingActivity);
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
        Map<String, Player> players = gameData.getPlayers();
        Map<Integer, Card> cards = players.get(gameData.getActivePlayerId()).getHand().getCardList();
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

    public ArrayList<Card> getPhaseCards() {
        return cardlist;
    }
}
