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

import com.mia.phase10.classes.Card;
import com.mia.phase10.classes.GameData;
import com.mia.phase10.classes.Player;
import com.mia.phase10.exceptionClasses.EmptyCardStackException;
import com.mia.phase10.gameLogic.GameLogicHandler;

import java.util.Map;

public class GameActivity extends AppCompatActivity implements View.OnLongClickListener {

    private LinearLayout deck;
    private LinearLayout discardPileLayout;
    private TextView player1;
    private TextView player2;
    private GameData gameData;
    private String player1Name;
    private String player2Name;
    private Intent intent;


    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        GameLogicHandler gameLogicHandler;
        // Get the Intent that started this activity and extract the string
        intent = getIntent();
        player1 = findViewById(R.id.ID_player_1);
        player2 = findViewById(R.id.ID_player_2);
        // Capture the layout's TextView and set the string as its text
        player1Name = intent.getStringExtra(MainActivity.FIRST_PLAYER);
        player2Name = intent.getStringExtra(MainActivity.SECOND_PLAYER);
        player1.setText(player1Name);
        player2.setText(player1Name);
        gameLogicHandler = GameLogicHandler.getInstance();
        gameLogicHandler.initializeGame();
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
        Drawable c = getResources().getDrawable(getResources().getIdentifier(gameData.getDrawStack().getFirstCard().getImagePath(), "drawable", getPackageName()));
        cardImage.setImageDrawable(c);
        cardImage.setTag("DISCARD PILE");
        cardImage.setOnLongClickListener(this);
        discardPileLayout.addView(cardImage);

        stack.setOnClickListener(new View.OnClickListener() {
            //@Override
            public void onClick(View v) {
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
                lp.setMargins(-70, 0, 0, 0);
                ImageView cardImage = new ImageView(GameActivity.this);
                Card drawStackCard = null;
                String imagePath = "";
                try {
                    drawStackCard = gameData.getDrawStack().drawCard();
                    imagePath= drawStackCard.getImagePath();
                } catch (EmptyCardStackException e) {
                    e.printStackTrace();
                }
                cardImage.setLayoutParams(lp);
                Drawable c = getResources().getDrawable(getResources().getIdentifier(imagePath, "drawable", getPackageName()));
                cardImage.setImageDrawable(c);
                cardImage.setTag("DISCARD PILE");
                cardImage.setOnLongClickListener(GameActivity.this);
                gameData.getPlayers().get(gameData.getActivePlayerId()).getHand().addCard(drawStackCard);
                deck.addView(cardImage);
            }
        });
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
        lp.setMargins(-70, 0, 0, 0);
        for (Card card : cards.values()) {
            ImageView cardImage = new ImageView(GameActivity.this);
            cardImage.setLayoutParams(lp);
            Drawable c = getResources().getDrawable(getResources().getIdentifier(card.getImagePath(), "drawable", getPackageName()));
            cardImage.setImageDrawable(c);
            cardImage.setTag("DISCARD PILE");
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

    public LinearLayout getDiscardPileLayout() {
        return discardPileLayout;
    }
}
