package com.mia.phase10;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mia.phase10.classes.Card;
import com.mia.phase10.classes.Player;
import com.mia.phase10.exceptionClasses.EmptyCardStackException;
import com.mia.phase10.gameFlow.GamePhase;
import com.mia.phase10.gameFlow.LayOffCardsPhase;
import com.mia.phase10.gameLogic.GameLogicHandler;
import com.mia.phase10.gameLogic.Phase;
import com.mia.phase10.gameLogic.StackType;


public class GameActivity extends AppCompatActivity implements View.OnLongClickListener {

    private LinearLayout deck;
    ProgressDialog progressDialog;
    private ImageView stack;
    private static final String USERNAME = "username";
    private LinearLayout discardPileLayout;
    private LinearLayout discardPileLayoutButton;
    private TextView player1;
    private TextView player2;
    private TextView score;
    private LinearLayout phases;
    private Button more;
    private ImageView playerImage;
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
    private Button cheat;
    private Button cheatExpose;
    private ImageButton checkTwo;
    private ImageButton cancelTwo;
    private String player1ID;
    private String player2ID;

    static final String DISCARD_PILE = "DISCARD PILE";
    static final String DRAWABLE = "drawable";
    private final String TAG = "GameActivity";

    MyDragEventListener myDragEventListener;
    MyDragEventListenerTwo myDrag;
    protected static AsyncTask client;

    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize View and set Listeners
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        findViewByIDObjects();
        initializeListeners();
        setPlayers();
        //preparing gameData
        GameLogicHandler.getInstance().setGameActivity(this);
        visualize();
    }


    @Override
    public void onBackPressed() {
        GameLogicHandler.getInstance().getGameData().setExit(true);
        GameLogicHandler.getInstance().sendGameState();
    }

    protected void showAlert(){
        Log.i(TAG, "ReturnButton GameStartActivity.");
        AlertDialog.Builder alertShuttingDown = new AlertDialog.Builder(this);
        alertShuttingDown.setCancelable(false);
        alertShuttingDown.setTitle("Ein Spieler hat das Spiel verlassen!\nSpiel wird beendet!");
        alertShuttingDown.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                exitApp();
            }
        });
        alertShuttingDown.setIcon(android.R.drawable.ic_dialog_info);
        alertShuttingDown.show();
    }


    protected void exitApp(){
        GameLogicHandler.getInstance().closeConnections();
        overridePendingTransition(0, 0);
        finish();
    }


    private void setPlayers() {
        Intent intent = getIntent();
        player1ID = intent.getStringExtra(USERNAME);
        //currently only two players!
        for (Player p : GameLogicHandler.getInstance().getGameData().getPlayers().values()) {
            if (!p.getId().equals(player1ID)) {
                player2ID = p.getId();
            }
        }
    }

    private void initializeListeners() {
        myDragEventListener = new MyDragEventListener();
        myDrag = new MyDragEventListenerTwo();

        cheat.setOnClickListener(new View.OnClickListener() {
            //@Override
            public void onClick(View v) {
                setCheatButtonListener();
            }
        });
        cheatExpose.setOnClickListener(new View.OnClickListener() {
            //@Override
            public void onClick(View v) {
                setCheatExposeButtonListener();
            }
        });

        more.setOnClickListener(new View.OnClickListener() {
            //@Override
            public void onClick(View v) {
                startGamerulesActivity();
            }
        });

        stack.setOnClickListener(new View.OnClickListener() {
            //@Override
            public void onClick(View v) {
                setStackListener(StackType.DRAW_STACK);
            }
        });

        discardPileLayoutButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setStackListener(StackType.LAYOFF_STACK);
            }
        });
    }

    public void setCheatExposeButtonListener() {
        GameLogicHandler.getInstance().exposeCheat();
    }

    public void setCheatButtonListener() {
        Card card = GameLogicHandler.getInstance().cheat();
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        lp.setMargins(0, 0, 0, 0);
        ImageView cardImage = new ImageView(GameLogicHandler.getInstance().getGameActivity());
        cardImage.setLayoutParams(lp);
        Drawable c = getResources().getDrawable(getResources().getIdentifier(card.getImagePath(), DRAWABLE, getPackageName()));
        cardImage.setImageDrawable(c);
        cardImage.setTag(DISCARD_PILE);
        cardImage.setOnLongClickListener(GameLogicHandler.getInstance().getGameActivity());
        cardImage.setId(card.getId());
        stack.setImageDrawable(c);
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                resetStackView();
            }
        }, 2000);
    }

    public void resetStackView() {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        lp.setMargins(0, 0, 0, 0);
        ImageView cardImage = new ImageView(GameLogicHandler.getInstance().getGameActivity());
        cardImage.setLayoutParams(lp);
        Drawable c = getResources().getDrawable(getResources().getIdentifier("stack", DRAWABLE, getPackageName()));
        cardImage.setImageDrawable(c);
        cardImage.setTag(DISCARD_PILE);
        cardImage.setOnLongClickListener(GameLogicHandler.getInstance().getGameActivity());
        stack.setImageDrawable(c);
    }


    public void setStackListener(StackType stackType) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        lp.setMargins(0, 0, 0, 0);
        try {
            GameLogicHandler.getInstance().drawCard(GameLogicHandler.getInstance().getGameData().getActivePlayerId(), stackType);
        } catch (EmptyCardStackException e) {
            e.printStackTrace();
        }
    }


    public void findViewByIDObjects() {
        setContentView(R.layout.activity_game);
        stack = findViewById(R.id.ID_stack);
        deck = findViewById(R.id.ID_deck);
        discardPileLayout = findViewById(R.id.ID_discard_layout);
        discardPileLayoutButton = findViewById(R.id.ID_discard_layout_button);
        player1 = findViewById(R.id.ID_player_1);
        player2 = findViewById(R.id.ID_player_2);
        score = findViewById(R.id.ID_score);
        phases = findViewById(R.id.ID_phases);
        more = findViewById(R.id.ID_more);
        playerImage = findViewById(R.id.ID_p2);
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
        cheat = findViewById(R.id.btnCheat);
        cheatExpose = findViewById(R.id.btnCheatExpose);
    }

    //Visualizing Data from GameData (GUI drawing ONLY here)
    public void visualize() {
      if( GameLogicHandler.getInstance().getGameData().isExit()){
            showAlert();
        }
      
      if (GameLogicHandler.getInstance().getGameData().isGameClosed()) {
            startGameEndActivity();

        } else {
            moveBackgroundToTheBack();
            makePlaystationLayoutVisible();
            player1.setText(player1ID);
            player2.setText(player2ID);
            player1.invalidate();
            player2.invalidate();
            player1.requestLayout();
            player2.requestLayout();
            View mainView = findViewById(R.id.drawerLayout);
            mainView.invalidate();
            LinearLayout phaseLinearLayout = (LinearLayout) this.phases.getChildAt(GameLogicHandler.getInstance().getGameData().getPlayers().get(GameLogicHandler.getInstance().getGameData().getActivePlayerId()).getCurrentPhase().ordinal());
            TextView phaseTextView = (TextView) phaseLinearLayout.getChildAt(1);
            phaseTextView.setTextColor(Color.parseColor("#CDDC39"));
            this.score.setText(GameLogicHandler.getInstance().getGameData().getPlayers().get(GameLogicHandler.getInstance().getGameData().getActivePlayerId()).getPoints() + "");
            visualizePhase();

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

            seperateActiveInactivPlayer();

        }
    }

    private void seperateActiveInactivPlayer(){
        if (GameLogicHandler.getInstance().getGameData().getActivePlayerId().equals(player1ID)) {
            try {
                progressDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (GameLogicHandler.getInstance().getGameData().getPhase() == GamePhase.START_PHASE) {
                startShufflingActivity();
            }
        } else {
            moveBackgroundToFront();
            if (progressDialog == null) {
                progressDialog = ProgressDialog.show(this, "Bitte warten",
                        "Spieler " + GameLogicHandler.getInstance().getGameData().getActivePlayerId() + " ist am Zug!", true);

            } else if (!progressDialog.isShowing()) {
                progressDialog = ProgressDialog.show(this, "Bitte warten",
                        "Spieler " + GameLogicHandler.getInstance().getGameData().getActivePlayerId() + " ist am Zug!", true);
            }
        }
    }

    private void moveBackgroundToTheBack(){
        stack.setVisibility(View.VISIBLE);
        deck.setVisibility(View.VISIBLE);
        discardPileLayout.setVisibility(View.VISIBLE);
        discardPileLayoutButton.setVisibility(View.VISIBLE);

        this.findViewById(R.id.ID_avatar).setVisibility(View.VISIBLE);
        playerImage.setVisibility(View.VISIBLE);
        this.findViewById(R.id.ID_player_1).setVisibility(View.VISIBLE);
        this.findViewById(R.id.ID_player_2).setVisibility(View.VISIBLE);

        playstationP1Image.setVisibility(View.VISIBLE);
        playstationP2Image.setVisibility(View.VISIBLE);
        playstationP1ImageSeperated.setVisibility(View.VISIBLE);
        playstationP2ImageSeperated.setVisibility(View.VISIBLE);

        this.getPlaystationP1Layout().removeAllViews();
        this.getPlaystationP1LayoutL().removeAllViews();
        this.getPlaystationP1LayoutR().removeAllViews();
        this.getPlaystationP2Layout().removeAllViews();
        this.getPlaystationP2LayoutL().removeAllViews();
        this.getPlaystationP2LayoutR().removeAllViews();
        this.getDeck().removeAllViews();
        this.getDiscardPileLayout().removeAllViews();

        cheat.setVisibility(View.VISIBLE);
        cheatExpose.setVisibility(View.VISIBLE);
    }

    private void moveBackgroundToFront(){
        stack.setVisibility(View.INVISIBLE);
        deck.setVisibility(View.INVISIBLE);
        discardPileLayout.setVisibility(View.INVISIBLE);
        discardPileLayoutButton.setVisibility(View.INVISIBLE);

        this.findViewById(R.id.ID_avatar).setVisibility(View.INVISIBLE);
        playerImage.setVisibility(View.INVISIBLE);
        this.findViewById(R.id.ID_player_1).setVisibility(View.INVISIBLE);
        this.findViewById(R.id.ID_player_2).setVisibility(View.INVISIBLE);

        playstationP1Image.setVisibility(View.INVISIBLE);
        playstationP2Image.setVisibility(View.INVISIBLE);
        playstationP1ImageSeperated.setVisibility(View.INVISIBLE);
        playstationP2ImageSeperated.setVisibility(View.INVISIBLE);

        this.getPlaystationP1Layout().removeAllViews();
        this.getPlaystationP1LayoutL().removeAllViews();
        this.getPlaystationP1LayoutR().removeAllViews();
        this.getPlaystationP2Layout().removeAllViews();
        this.getPlaystationP2LayoutL().removeAllViews();
        this.getPlaystationP2LayoutR().removeAllViews();
        this.getDeck().removeAllViews();
        this.getDiscardPileLayout().removeAllViews();

        cheat.setVisibility(View.INVISIBLE);
        cheatExpose.setVisibility(View.INVISIBLE);
    }

    private void showLayOffStack() {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1);

        Card card = GameLogicHandler.getInstance().getGameData().getLayOffStack().getLastCard();
        ImageView cardImage = new ImageView(GameLogicHandler.getInstance().getGameActivity());
        cardImage.setLayoutParams(lp);
        if (card != null) {
            Drawable c = getResources().getDrawable(getResources().getIdentifier(card.getImagePath(), DRAWABLE, getPackageName()));
            cardImage.setImageDrawable(c);
            cardImage.setTag(DISCARD_PILE);
            cardImage.setOnLongClickListener(GameLogicHandler.getInstance().getGameActivity());
            cardImage.setId(card.getId());
            discardPileLayout.addView(cardImage);
        }
    }

    public void makePlaystationLayoutVisible() {
        String activeID = GameLogicHandler.getInstance().getGameData().getActivePlayerId();
        Phase p = GameLogicHandler.getInstance().getGameData().getPlayers().get(activeID).getCurrentPhase();
        if (p == Phase.PHASE_4 || p == Phase.PHASE_5 || p == Phase.PHASE_6 || p == Phase.PHASE_8) {
            playstationP1ImageSeperated.setVisibility(View.INVISIBLE);
            playstationP1LayoutL.setVisibility(View.INVISIBLE);
            playstationP1LayoutR.setVisibility(View.INVISIBLE);
            playstationP1Image.setVisibility(View.VISIBLE);
            playstationP1Layout.setVisibility(View.VISIBLE);
        } else {
            playstationP1Image.setVisibility(View.INVISIBLE);
            playstationP1Layout.setVisibility(View.INVISIBLE);
            playstationP1ImageSeperated.setVisibility(View.VISIBLE);
            playstationP1LayoutL.setVisibility(View.VISIBLE);
            playstationP1LayoutR.setVisibility(View.VISIBLE);
        }

        String next;
        if (activeID.equals(getPlayer1ID())) {
            next = getPlayer2ID();
        } else {
            next = getPlayer1ID();
        }

        p = GameLogicHandler.getInstance().getGameData().getPlayers().get(next).getCurrentPhase();


        if (p == Phase.PHASE_4 || p == Phase.PHASE_5 || p == Phase.PHASE_6 || p == Phase.PHASE_8) {
            playstationP2ImageSeperated.setVisibility(View.INVISIBLE);
            playstationP2LayoutL.setVisibility(View.INVISIBLE);
            playstationP2LayoutR.setVisibility(View.INVISIBLE);
            playstationP2Image.setVisibility(View.VISIBLE);
            playstationP2Layout.setVisibility(View.VISIBLE);

        } else {
            playstationP2Image.setVisibility(View.INVISIBLE);
            playstationP2Layout.setVisibility(View.INVISIBLE);
            playstationP2ImageSeperated.setVisibility(View.VISIBLE);
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
        for (Card card : GameLogicHandler.getInstance().getGameData().getPlayers().get(player2ID).getPhaseCards()) {
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
        for (Card card : GameLogicHandler.getInstance().getGameData().getPlayers().get(player2ID).getPhaseCards2()) {
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

    public void setVisibilityOfButtons1() {
        check.setVisibility(View.INVISIBLE);
        cancel.setVisibility(View.INVISIBLE);
    }

    public void setVisibilityOfButtons2() {
        checkTwo.setVisibility(View.INVISIBLE);
        cancelTwo.setVisibility(View.INVISIBLE);
    }

    public void startShufflingActivity() {
        Intent shufflingActivity = new Intent(this, ShufflingActivity.class);
        startActivity(shufflingActivity);
    }

    public void startGamerulesActivity() {
        Intent gameRulesActivity = new Intent(this, GameRulesActivity.class);
        startActivity(gameRulesActivity);
    }

    public void startGameEndActivity() {
        Intent gameEndActivity = new Intent(this, GameEndActivity.class);
        startActivity(gameEndActivity);
    }

    public void startStartActivity() {
        Intent startActivity = new Intent(this, MainActivity.class);
        startActivity(startActivity);
    }

    public void visualizePhase() {
        ImageView drawStack = findViewById(R.id.ID_stack);
        LinearLayout layoffStack = findViewById(R.id.ID_discard_layout);

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
                        setStackListener(StackType.DRAW_STACK);
                    }
                });

                discardPileLayoutButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setStackListener(StackType.LAYOFF_STACK);
                    }
                });

                if (!GameLogicHandler.getInstance().getGameData().getLayOffStack().getLastCard().getImagePath().equals("card_expose")) {
                    layoffStack.setBackgroundColor(0x83462BA0);
                } else layoffStack.setBackgroundColor(Color.TRANSPARENT);
                drawStack.setBackgroundColor(0x83462BA0);
                playerImage.setBackgroundColor(Color.TRANSPARENT);
                break;

            case LAYOFF_PHASE:
                stack.setOnClickListener(null);
                discardPileLayout.setOnDragListener(myDragEventListener);
                discardPileLayoutButton.setOnClickListener(null);
                playstationP1Layout.setOnDragListener(myDrag);
                playstationP1LayoutL.setOnDragListener(myDrag);
                playstationP1LayoutR.setOnDragListener(myDrag);
                final String currentP = GameLogicHandler.getInstance().getGameData().getActivePlayerId();


                if (GameLogicHandler.getInstance().getGameData().getPlayers().get(GameLogicHandler.getInstance().getGameData().getActivePlayerId()).isPhaseAchieved()) {
                    check.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            GameLogicHandler.getInstance().checkNewCardList(LayOffCardsPhase.ACTIVE_PHASE);

                        }
                    });
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            GameLogicHandler.getInstance().moveCardsBackToHand(LayOffCardsPhase.ACTIVE_PHASE);
                        }
                    });

                    String next;
                    if (currentP.equals(getPlayer1ID())) {
                        next = getPlayer2ID();
                    } else {
                        next = getPlayer1ID();
                    }

                    if (GameLogicHandler.getInstance().getGameData().getPlayers().get(next).isPhaseAchieved()) {
                        playstationP2Layout.setOnDragListener(myDrag);
                        playstationP2LayoutL.setOnDragListener(myDrag);
                        playstationP2LayoutR.setOnDragListener(myDrag);

                        checkTwo.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                GameLogicHandler.getInstance().checkNewCardList(LayOffCardsPhase.NEXTPLAYER_PHASE);
                            }
                        });
                        cancelTwo.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                GameLogicHandler.getInstance().moveCardsBackToHand(LayOffCardsPhase.NEXTPLAYER_PHASE);

                            }
                        });
                    }

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
                layoffStack.setBackgroundColor(0x83462BA0);
                playerImage.setBackgroundColor(Color.TRANSPARENT);
                break;
            case END_TURN_PHASE:
                break;
            case START_PHASE:
                break;
        }
    }

    public void visualizeExposingPlayer() {
        getDiscardPileLayout().setOnDragListener(null);
        getPlaystationP1Layout().setOnDragListener(null);
        getPlaystationP1LayoutL().setOnDragListener(null);
        getPlaystationP1LayoutR().setOnDragListener(null);
        getPlaystationP2Layout().setOnDragListener(null);
        getPlaystationP2LayoutL().setOnDragListener(null);
        getPlaystationP2LayoutR().setOnDragListener(null);
        getStack().setOnClickListener(null);
        getDiscardPileLayoutButton().setOnClickListener(null);
        playerImage.setBackgroundColor(Color.rgb(157, 71, 188));
        findViewById(R.id.ID_discard_layout).setBackgroundColor(Color.TRANSPARENT);


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
        p.setText(player1ID);
        q.setText(player2ID);
    }

    public String getPlayer1ID() {
        return player1ID;
    }

    public void setPlayer1ID(String player1ID) {
        this.player1ID = player1ID;
    }

    public String getPlayer2ID() {
        return player2ID;
    }

    public void setPlayer2ID(String player2ID) {
        this.player2ID = player2ID;
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
        this.player1ID = name;
    }

    public void setPlayer2(String name) {
        this.player2ID = name;
    }

    public ImageButton getCheckTwo() {
        return checkTwo;
    }

    public ImageButton getCancelTwo() {
        return cancelTwo;
    }

    public ImageView getStack() {
        return stack;
    }

    public LinearLayout getDiscardPileLayoutButton() {
        return discardPileLayoutButton;
    }

    public void setDiscardPileLayoutButton(LinearLayout discardPileLayoutButton) {
        this.discardPileLayoutButton = discardPileLayoutButton;
    }

    public void showMessage(String text) {
        Toast.makeText(this.getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }

    public static Handler UIHandler;

    static {
        UIHandler = new Handler(Looper.getMainLooper());
    }

    public static void runOnUI(Runnable runnable) {
        UIHandler.post(runnable);
    }
}