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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mia.phase10.classes.Card;
import com.mia.phase10.classes.Player;
import com.mia.phase10.exceptionClasses.EmptyCardStackException;
import com.mia.phase10.gameLogic.GameLogicHandler;
import com.mia.phase10.gameLogic.Phase;
import com.mia.phase10.gameLogic.StackType;

public class GameActivity extends AppCompatActivity implements View.OnLongClickListener {

    private LinearLayout deck;
    private ImageView stack;

    private LinearLayout discardPileLayout;
    private TextView player1;
    private TextView player2;
    private TextView score;
    private TextView phase;
    private ImageView playstationP1Image;
    private ImageView playstationP1ImageSeperated;
    private ImageView playstationP2Image;
    private ImageView playstationP2ImageSeperated;
    private LinearLayout playstationP1Layout;
    private LinearLayout playstationP1LayoutL;
    private LinearLayout playstationP1LayoutR;
    private LinearLayout playstationP2Layout;
    private LinearLayout playstationP2LayoutL;
    private LinearLayout playstationP2LayoutR;
    private Button check;
    private Button cancel;
    private ImageButton checkTwo;
    private ImageButton cancelTwo;
    // private ConstraintLayout phaseClosed;
    private String player1Name;
    private String player2Name;

    static final String DISCARD_PILE = "DISCARD PILE";
    static final String DRAWABLE = "drawable";

    MyDragEventListener myDragEventListener;
    MyDragEventListenerTwo myDrag;

    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize View and set Listeners
        findViewByIDObjects();
        initializeListeners();

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
        Intent intent = getIntent();

    }

    private void initializeListeners() {

        myDragEventListener = new MyDragEventListener();
        myDrag = new MyDragEventListenerTwo();

        stack.setOnClickListener(new View.OnClickListener() {
            //@Override
            public void onClick(View v) {
                setStackListener();
            }
        });
    }

    public void setStackListener() {
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

    public void findViewByIDObjects() {
        setContentView(R.layout.activity_game);
        stack = findViewById(R.id.ID_stack);
        deck = findViewById(R.id.ID_deck);
        discardPileLayout = findViewById(R.id.ID_discard_layout);
        player1 = findViewById(R.id.ID_player_1);
        player2 = findViewById(R.id.ID_player_2);
        score = findViewById(R.id.ID_score);
        phase = findViewById(R.id.ID_phase);
        playstationP1Image = findViewById(R.id.ID_p1_playstation);
        playstationP2Image = findViewById(R.id.ID_p2_playstation);
        playstationP1ImageSeperated = findViewById(R.id.ID_p1_playstation_two);
        playstationP2ImageSeperated = findViewById(R.id.ID_p2_playstation_two);
        playstationP1Layout = findViewById(R.id.ID_p1_playstation_layout);
        playstationP2Layout = findViewById(R.id.ID_p2_playstation_layout);
        playstationP1LayoutL = findViewById(R.id.ID_p1_playstation_two_layout_left);
        playstationP1LayoutR = findViewById(R.id.ID_p1_playstation_two_layout_right);
        playstationP2LayoutL = findViewById(R.id.ID_p2_playstation_layout_left);
        playstationP2LayoutR = findViewById(R.id.ID_p2_playstation_layout_right);
        check = findViewById(R.id.checkPhase);
        cancel = findViewById(R.id.Cancel);
        checkTwo = findViewById(R.id.check);
        cancelTwo = findViewById(R.id.cross);
        // phaseClosed = findViewById(R.id.ID_phase_closed);
    }

    public void visualize() {
        GameLogicHandler.getInstance().getGameActivity().makePlaystationLayoutVisible(GameLogicHandler.getInstance().getGameData().getPlayers().get(GameLogicHandler.getInstance().getGameData().getActivePlayerId()).getCurrentPhase());
        //Visualizing Data from GameData (GUI drawing ONLY here)
        player1.setText(player1Name);
        player2.setText(player2Name);
        player1.invalidate();
        player2.invalidate();
        player1.requestLayout();
        player2.requestLayout();
        View mainView = findViewById(R.id.drawerLayout);
        mainView.invalidate();
        this.phase.setText(GameLogicHandler.getInstance().getGameData().getPlayers().get(GameLogicHandler.getInstance().getGameData().getActivePlayerId()).getCurrentPhase().toString());
        this.score.setText(GameLogicHandler.getInstance().getGameData().getPlayers().get(GameLogicHandler.getInstance().getGameData().getActivePlayerId()).getPoints() + "");
        visualizePhase();

        //Visualizing playstation of other player

        this.getPlaystationP1Layout().removeAllViews();
        this.getPlaystationP1LayoutL().removeAllViews();
        this.getPlaystationP1LayoutR().removeAllViews();
        this.getPlaystationP2Layout().removeAllViews();
        this.getPlaystationP2LayoutL().removeAllViews();
        this.getPlaystationP2LayoutR().removeAllViews();
        this.getDeck().removeAllViews();
        this.getDiscardPileLayout().removeAllViews();

        //Visualizing cards of active player
        showHandCards();
        showPlaystation1Cards();
        showPlaystation1RCards();
        showPlaystation2Cards();
        showPlaystation2RCards();
        showLayOffStack();
    }

    private void showLayOffStack() {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1);

        Card card = GameLogicHandler.getInstance().getGameData().getLayOffStack().getLastCard();
        ImageView cardImage = new ImageView(GameLogicHandler.getInstance().getGameActivity());
        cardImage.setLayoutParams(lp);
        Drawable c = getResources().getDrawable(getResources().getIdentifier(card.getImagePath(), DRAWABLE, getPackageName()));
        cardImage.setImageDrawable(c);
        cardImage.setTag(DISCARD_PILE);
        cardImage.setOnLongClickListener(GameLogicHandler.getInstance().getGameActivity());
        cardImage.setId(card.getId());
        discardPileLayout.addView(cardImage);
    }

    public void makePlaystationLayoutVisible(Phase p) {
        if (p == Phase.PHASE_4 || p == Phase.PHASE_5 || p == Phase.PHASE_6 || p == Phase.PHASE_8) {
            playstationP1ImageSeperated.setVisibility(View.INVISIBLE);
            playstationP2ImageSeperated.setVisibility(View.INVISIBLE);
            playstationP1LayoutL.setVisibility(View.INVISIBLE);
            playstationP1LayoutR.setVisibility(View.INVISIBLE);
            playstationP2LayoutL.setVisibility(View.INVISIBLE);
            playstationP2LayoutR.setVisibility(View.INVISIBLE);
            playstationP1Image.setVisibility(View.VISIBLE);
            playstationP2Image.setVisibility(View.VISIBLE);
            playstationP1Layout.setVisibility(View.VISIBLE);
            playstationP2Layout.setVisibility(View.VISIBLE);

        } else {
            playstationP1Image.setVisibility(View.INVISIBLE);
            playstationP2Image.setVisibility(View.INVISIBLE);
            playstationP1Layout.setVisibility(View.INVISIBLE);
            playstationP2Layout.setVisibility(View.INVISIBLE);
            playstationP1ImageSeperated.setVisibility(View.VISIBLE);
            playstationP2ImageSeperated.setVisibility(View.VISIBLE);
            playstationP1LayoutL.setVisibility(View.VISIBLE);
            playstationP1LayoutR.setVisibility(View.VISIBLE);
            playstationP2LayoutL.setVisibility(View.VISIBLE);
            playstationP2LayoutR.setVisibility(View.VISIBLE);
        }
    }

    public void showHandCards() {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        lp.setMargins(0, 0, 0, 0);
        deck.removeAllViews();
        for (Card card : GameLogicHandler.getInstance().getGameData().getPlayers().get(GameLogicHandler.getInstance().getGameData().getActivePlayerId()).getHand().getCardList().values()) {
            ImageView cardImage = new ImageView(GameLogicHandler.getInstance().getGameActivity());
            cardImage.setLayoutParams(lp);
            Drawable c = getResources().getDrawable(getResources().getIdentifier(card.getImagePath(), DRAWABLE, getPackageName()));
            cardImage.setImageDrawable(c);
            cardImage.setTag(DISCARD_PILE);
            cardImage.setOnLongClickListener(GameLogicHandler.getInstance().getGameActivity());
            cardImage.setId(card.getId());
            deck.addView(cardImage);
        }
    }

    public void showPlaystation1Cards() {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        lp.setMargins(0, 0, 0, 0);
        for (Card card : GameLogicHandler.getInstance().getGameData().getPlayers().get(GameLogicHandler.getInstance().getGameData().getActivePlayerId()).getPhaseCards()) {
            ImageView cardImage = new ImageView(GameLogicHandler.getInstance().getGameActivity());
            cardImage.setLayoutParams(lp);
            Drawable c = getResources().getDrawable(getResources().getIdentifier(card.getImagePath(), DRAWABLE, getPackageName()));
            cardImage.setImageDrawable(c);
            cardImage.setTag(DISCARD_PILE);
            cardImage.setOnLongClickListener(GameLogicHandler.getInstance().getGameActivity());
            cardImage.setId(card.getId());
            if (playstationP1Image.getVisibility() == View.VISIBLE) {
                playstationP1Layout.addView(cardImage);
            } else {
                playstationP1LayoutL.addView(cardImage);
            }
            cardImage.setVisibility(View.VISIBLE);
        }

    }

    public void showPlaystation1RCards() {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        lp.setMargins(0, 0, 0, 0);
        for (Card card : GameLogicHandler.getInstance().getGameData().getPlayers().get(GameLogicHandler.getInstance().getGameData().getActivePlayerId()).getPhaseCards2()) {
            ImageView cardImage = new ImageView(GameLogicHandler.getInstance().getGameActivity());
            cardImage.setLayoutParams(lp);
            Drawable c = getResources().getDrawable(getResources().getIdentifier(card.getImagePath(), DRAWABLE, getPackageName()));
            cardImage.setImageDrawable(c);
            cardImage.setTag(DISCARD_PILE);
            cardImage.setOnLongClickListener(GameLogicHandler.getInstance().getGameActivity());
            cardImage.setId(card.getId());
            playstationP1LayoutR.addView(cardImage);
            cardImage.setVisibility(View.VISIBLE);
        }
    }

    public void showPlaystation2Cards() {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        lp.setMargins(0, 0, 0, 0);
        for (Card card : GameLogicHandler.getInstance().getGameData().getPlayers().get(player2Name).getPhaseCards()) {
            ImageView cardImage = new ImageView(GameLogicHandler.getInstance().getGameActivity());
            cardImage.setLayoutParams(lp);
            Drawable c = getResources().getDrawable(getResources().getIdentifier(card.getImagePath(), DRAWABLE, getPackageName()));
            cardImage.setImageDrawable(c);
            cardImage.setTag(DISCARD_PILE);
            cardImage.setOnLongClickListener(GameLogicHandler.getInstance().getGameActivity());
            cardImage.setId(card.getId());
            if (playstationP2Image.getVisibility() == View.VISIBLE) {
                playstationP2Layout.addView(cardImage);
            } else {
                playstationP2LayoutL.addView(cardImage);
            }
            cardImage.setVisibility(View.VISIBLE);
        }
    }

    public void showPlaystation2RCards() {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        lp.setMargins(0, 0, 0, 0);
        for (Card card : GameLogicHandler.getInstance().getGameData().getPlayers().get(player2Name).getPhaseCards2()) {
            ImageView cardImage = new ImageView(GameLogicHandler.getInstance().getGameActivity());
            cardImage.setLayoutParams(lp);
            Drawable c = getResources().getDrawable(getResources().getIdentifier(card.getImagePath(), DRAWABLE, getPackageName()));
            cardImage.setImageDrawable(c);
            cardImage.setTag(DISCARD_PILE);
            cardImage.setOnLongClickListener(GameLogicHandler.getInstance().getGameActivity());
            cardImage.setId(card.getId());
            playstationP2LayoutR.addView(cardImage);
            cardImage.setVisibility(View.VISIBLE);
        }
    }

    public void setVisibilityOfButtons() {
        check.setVisibility(View.INVISIBLE);
        cancel.setVisibility(View.INVISIBLE);
        checkTwo.setVisibility(View.INVISIBLE);
        cancelTwo.setVisibility(View.INVISIBLE);
    }

    public void startShufflingActivity() {
        Intent shufflingActivity = new Intent(this, ShufflingActivity.class);
        startActivity(shufflingActivity);
    }

    public void visualizePhase() {
        ImageView drawStack = findViewById(R.id.ID_stack);
        LinearLayout layoffStack = findViewById(R.id.ID_discard_layout);
        final String currentP = GameLogicHandler.getInstance().getGameData().getActivePlayerId();

        switch (GameLogicHandler.getInstance().getGameData().getPhase()) {

            case DRAW_PHASE:
                discardPileLayout.setOnDragListener(null);
                playstationP1Layout.setOnDragListener(null);
                playstationP1LayoutL.setOnDragListener(null);
                playstationP1LayoutR.setOnDragListener(null);
                playstationP2Layout.setOnDragListener(null);
                playstationP2LayoutL.setOnDragListener(null);
                playstationP2LayoutR.setOnDragListener(null);

                stack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setStackListener();
                    }
                });

                layoffStack.setBackgroundColor(Color.TRANSPARENT);
                drawStack.setBackgroundColor(Color.rgb(0, 255, 224));
                break;
            case LAYOFF_PHASE:
                stack.setOnClickListener(null);
                discardPileLayout.setOnDragListener(myDragEventListener);
                playstationP1Layout.setOnDragListener(myDrag);
                playstationP1LayoutL.setOnDragListener(myDrag);
                playstationP1LayoutR.setOnDragListener(myDrag);

                if (GameLogicHandler.getInstance().getGameData().getPlayers().get(GameLogicHandler.getInstance().getGameData().getActivePlayerId()).isPhaseAchieved()) {
                    check.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            GameLogicHandler.getInstance().checkNewCardList(currentP);
                        }
                    });
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            GameLogicHandler.getInstance().moveCardsBackToHand(currentP);
                        }
                    });


                    GameLogicHandler.getInstance().getGameData().nextPlayer();
                    if (GameLogicHandler.getInstance().getGameData().getPlayers().get(GameLogicHandler.getInstance().getGameData().getActivePlayerId()).isPhaseAchieved()) {

                        playstationP2Layout.setOnDragListener(myDrag);
                        playstationP2LayoutL.setOnDragListener(myDrag);
                        playstationP2LayoutR.setOnDragListener(myDrag);

                        checkTwo.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                GameLogicHandler.getInstance().checkNewCardList(currentP);
                            }
                        });
                        cancelTwo.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                GameLogicHandler.getInstance().moveCardsBackToHand(currentP);
                            }
                        });
                    }
                    GameLogicHandler.getInstance().getGameData().setActivePlayerId(currentP);

                } else {
                    check.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            GameLogicHandler.getInstance().checkPhase();
                        }
                    });
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            GameLogicHandler.getInstance().movePhaseCardsBackToHand();
                        }
                    });
                }

                drawStack.setBackgroundColor(Color.TRANSPARENT);
                layoffStack.setBackgroundColor(Color.rgb(0, 255, 224));

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


    public void switchPlayerName(TextView p, TextView q) {
        p.setText(player1Name);
        q.setText(player2Name);
    }

    public String getPlayer1Name() {
        return player1Name;
    }

    public void setPlayer1Name(String player1Name) {
        this.player1Name = player1Name;
    }

    public String getPlayer2Name() {
        return player2Name;
    }

    public void setPlayer2Name(String player2Name) {
        this.player2Name = player2Name;
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

    public Button getCheck() {
        return check;
    }

    public Button getCancel() {
        return cancel;
    }

    public LinearLayout getPlaystationP1Layout() {
        return playstationP1Layout;
    }

    public LinearLayout getPlaystationP2Layout() {
        return playstationP2Layout;
    }

    public LinearLayout getPlaystationP1LayoutL() {
        return playstationP1LayoutL;
    }

    public LinearLayout getPlaystationP1LayoutR() {
        return playstationP1LayoutR;
    }

    public LinearLayout getPlaystationP2LayoutL() {
        return playstationP2LayoutL;
    }

    public LinearLayout getPlaystationP2LayoutR() {
        return playstationP2LayoutR;
    }

    public void setPlayer1(String name) {
        this.player1Name = name;
    }

    public void setPlayer2(String name) {
        this.player2Name = name;
    }

    public ImageButton getCheckTwo() {
        return checkTwo;
    }

    public ImageButton getCancelTwo() {
        return cancelTwo;
    }
}
