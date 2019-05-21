package com.mia.phase10.gameLogic;

import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mia.phase10.GameActivity;
import com.mia.phase10.classes.Card;
import com.mia.phase10.classes.CardStack;
import com.mia.phase10.classes.GameData;
import com.mia.phase10.classes.Hand;
import com.mia.phase10.classes.Player;
import com.mia.phase10.exceptionClasses.CardNotFoundException;
import com.mia.phase10.exceptionClasses.EmptyCardStackException;
import com.mia.phase10.exceptionClasses.EmptyHandException;
import com.mia.phase10.exceptionClasses.PlayerNotFoundException;
import com.mia.phase10.gameFlow.GamePhase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GameLogicHandler {
    private static volatile GameLogicHandler glhInstance = new GameLogicHandler();
    private GameData gameData;
    private GameActivity gameActivity;

    //private constructor.
    private GameLogicHandler() {
    }

    public static GameLogicHandler getInstance() {
        return glhInstance;
    }

    public void setGameActivity(GameActivity gameActivity) {
        this.gameActivity = gameActivity;
    }

    public GameActivity getGameActivity() {
        return gameActivity;
    }

    public void initializeGame() {
        CardStack drawStack = new CardStack();
        drawStack.generateCardStack();
        CardStack layOffStack = new CardStack();

        this.gameData = new GameData(layOffStack, drawStack, new HashMap<String, Player>(), "");

    }

    public void addPlayer(Player player) {
        this.gameData.addPlayer(player);
    }

    public void startRound() throws EmptyCardStackException {
        this.getGameData().getDrawStack().mixStack();
        //this.gameActivity.startShufflingActivity();
        for (Player p : gameData.getPlayers().values()) {
            p.getHand().getCardList().clear();
            for (int i = 0; i < 10; i++) {
                Card c = this.gameData.getDrawStack().drawCard();
                p.getHand().addCard(c);
            }
            p.getPhaseCards().clear();
            p.getPhaseCardsTemp().clear();
            p.getPhaseCards2().clear();
            p.getPhaseCards2Temp().clear();
            p.setPhaseAchieved(false);
        }

        this.gameData.setPhase(GamePhase.DRAW_PHASE);
        this.gameData.nextPlayer();
        this.setPlayerNames();
        this.gameData.getLayOffStack().addCard(this.gameData.getDrawStack().drawCard());
        this.gameActivity.visualize();
    }

    public void layoffCard(String playerId, int cardId) throws EmptyHandException, CardNotFoundException, PlayerNotFoundException {
        try {
            Card c = this.gameData.getPlayers().get(playerId).getHand().removeCard(cardId);
            this.gameData.getLayOffStack().addCard(c);

            if (gameData.getPlayers().get(playerId).getHand().getCardList().isEmpty()) {
                this.gameData.setRoundClosed(true);
                this.gameData.setPhase(GamePhase.END_TURN_PHASE);
                this.countCards();
                this.checkPhasePlayer();
                startRound();
            } else {
                this.gameData.nextPlayer();
                this.setPlayerNames();
                this.gameData.setPhase(GamePhase.DRAW_PHASE);
                this.gameActivity.visualize();
            }
        } catch (Exception c) {
            throw new PlayerNotFoundException("Player not found!");
        }
    }

    public void layoffPhase(PlaystationType t, String playerId, int cardId) throws EmptyHandException, CardNotFoundException, PlayerNotFoundException {

        try {
            Card c = gameData.getPlayers().get(playerId).getHand().removeCard(cardId);
            if (t==PlaystationType.PLAYSTATION) {
                this.gameData.getPlayers().get(this.gameData.getActivePlayerId()).getPhaseCards().add(c);
                if (this.gameData.getPlayers().get(this.gameData.getActivePlayerId()).isPhaseAchieved()) {
                    this.gameData.getPlayers().get(this.gameData.getActivePlayerId()).getPhaseCardsTemp().add(c);
                }
            } else {
                this.gameData.getPlayers().get(this.gameData.getActivePlayerId()).getPhaseCards2().add(c);
                if (this.gameData.getPlayers().get(this.gameData.getActivePlayerId()).isPhaseAchieved()) {
                    this.gameData.getPlayers().get(this.gameData.getActivePlayerId()).getPhaseCards2Temp().add(c);
                }
            }
            this.gameActivity.visualize();

        } catch (Exception c) {
            throw new PlayerNotFoundException("Player not found!");
        }
    }

    public void movePhaseCardsBackToHand() {
        this.gameActivity.setVisibilityOfButtons();
        for (Card c : this.getGameData().getPlayers().get(GameLogicHandler.getInstance().getGameData().getActivePlayerId()).getPhaseCards()) {
            this.getGameData().getPlayers().get(GameLogicHandler.getInstance().getGameData().getActivePlayerId()).getHand().addCard(c);
        }
        if (this.getGameData().getPlayers().get(GameLogicHandler.getInstance().getGameData().getActivePlayerId()).getPhaseCards2() != null) {
            for (Card c : this.getGameData().getPlayers().get(GameLogicHandler.getInstance().getGameData().getActivePlayerId()).getPhaseCards2()) {
                this.getGameData().getPlayers().get(GameLogicHandler.getInstance().getGameData().getActivePlayerId()).getHand().addCard(c);
            }

        }
        this.getGameData().getPlayers().get(GameLogicHandler.getInstance().getGameData().getActivePlayerId()).getPhaseCards().clear();
        this.getGameData().getPlayers().get(GameLogicHandler.getInstance().getGameData().getActivePlayerId()).getPhaseCards2().clear();
        this.gameActivity.visualize();
    }

    public void moveCardsBackToHand() {
        this.gameActivity.setVisibilityOfButtons();
        for (Card c : this.getGameData().getPlayers().get(GameLogicHandler.getInstance().getGameData().getActivePlayerId()).getPhaseCardsTemp()) {
            this.getGameData().getPlayers().get(GameLogicHandler.getInstance().getGameData().getActivePlayerId()).getPhaseCards().remove(c);
            this.getGameData().getPlayers().get(GameLogicHandler.getInstance().getGameData().getActivePlayerId()).getHand().addCard(c);
        }
        if (this.getGameData().getPlayers().get(GameLogicHandler.getInstance().getGameData().getActivePlayerId()).getPhaseCards2Temp() != null) {
            for (Card c : this.getGameData().getPlayers().get(GameLogicHandler.getInstance().getGameData().getActivePlayerId()).getPhaseCards2Temp()) {
                this.getGameData().getPlayers().get(GameLogicHandler.getInstance().getGameData().getActivePlayerId()).getPhaseCards2().remove(c);
                this.getGameData().getPlayers().get(GameLogicHandler.getInstance().getGameData().getActivePlayerId()).getHand().addCard(c);
            }

        }
        this.getGameData().getPlayers().get(GameLogicHandler.getInstance().getGameData().getActivePlayerId()).getPhaseCardsTemp().clear();
        this.getGameData().getPlayers().get(GameLogicHandler.getInstance().getGameData().getActivePlayerId()).getPhaseCards2Temp().clear();
        this.gameActivity.visualize();
    }

    public void drawCard(String playerId, StackType stackType) throws EmptyCardStackException {
        Card card = null;
        switch (stackType) {

            case DRAW_STACK:
                card = gameData.getDrawStack().drawCard();
                this.gameData.getPlayers().get(playerId).getHand().addCard(card);
                break;
            case LAYOFF_STACK:
                card = gameData.getLayOffStack().drawLastCard();
                this.gameData.getPlayers().get(playerId).getHand().addCard(card);

        }
        this.gameData.setPhase(GamePhase.LAYOFF_PHASE);
        this.gameActivity.visualize();
    }


    public void countCards() {
        for (Player p : gameData.getPlayers().values()) {
            Map<Integer, Card> cards = p.getHand().getCardList();
            int points = p.getPoints();
            for (Map.Entry<Integer, Card> item : cards.entrySet()) {
                points = points + item.getValue().getcountCard();
            }
            p.setPoints(points);
        }
    }

    public void checkPhasePlayer() {
        for (Player p : gameData.getPlayers().values()) {
            if (p.isPhaseAchieved()) {
                p.setCurrentPhase((p.getCurrentPhase()).ordinal() < Phase.values().length - 1 ? Phase.values()[(p.getCurrentPhase()).ordinal() + 1] : null);
            }
        }
    }

    public GameData getGameData() {
        return this.gameData;
    }

    public String getGameState() {
        Gson gson = new Gson();
        return gson.toJson(this.gameData);
    }

    public void setGameState(String json) {
        Gson gson = new Gson();
        this.gameData = gson.fromJson(json, GameData.class);
    }

    public void checkPhase() {
        Phase p = GameLogicHandler.getInstance().getGameData().getPlayers().get(GameLogicHandler.getInstance().getGameData().getActivePlayerId()).getCurrentPhase();
        this.gameActivity.setVisibilityOfButtons();
        if (p == Phase.PHASE_4 || p == Phase.PHASE_5 || p == Phase.PHASE_6 || p == Phase.PHASE_8) {
            if (CardEvaluator.getInstance().checkPhase(this.gameData.getPlayers().get(gameData.getActivePlayerId()).getCurrentPhase(), this.gameData.getPlayers().get(gameData.getActivePlayerId()).getPhaseCards())) {
                this.gameData.getPlayers().get(this.gameData.getActivePlayerId()).setPhaseAchieved(true);
                Toast.makeText(this.getGameActivity(), "The phase is correct!", Toast.LENGTH_SHORT).show();

            } else {
                movePhaseCardsBackToHand();
                Toast.makeText(this.gameActivity, "The phase is not correct!", Toast.LENGTH_SHORT).show();
            }
        } else {
            if (CardEvaluator.getInstance().checkPhase(this.gameData.getPlayers().get(gameData.getActivePlayerId()).getCurrentPhase(), this.gameData.getPlayers().get(this.gameData.getActivePlayerId()).getPhaseCards(),
                    this.gameData.getPlayers().get(this.gameData.getActivePlayerId()).getPhaseCards2())) {

                this.gameData.getPlayers().get(this.gameData.getActivePlayerId()).setPhaseAchieved(true);
                Toast.makeText(this.getGameActivity(), "The phase is correct!", Toast.LENGTH_SHORT).show();

            } else {
                movePhaseCardsBackToHand();
                Toast.makeText(this.gameActivity, "The phase is not correct!", Toast.LENGTH_SHORT).show();
            }

        }
    }

    public void checkNewCardList() {
        this.gameActivity.setVisibilityOfButtons();
        Phase phase = GameLogicHandler.getInstance().getGameData().getPlayers().get(GameLogicHandler.getInstance().getGameData().getActivePlayerId()).getCurrentPhase();
        boolean result=false;
        if (phase == Phase.PHASE_4 || phase == Phase.PHASE_5 || phase == Phase.PHASE_6) {
            result=isARow();
        }
        else if (phase == Phase.PHASE_8) {
            result=CardEvaluator.getInstance().checkSameColors(GameLogicHandler.getInstance().getGameData().getPlayers().get(GameLogicHandler.getInstance().getGameData().getActivePlayerId()).getPhaseCards());
        }
        else if(phase == Phase.PHASE_1 || phase == Phase.PHASE_7 || phase == Phase.PHASE_9 || phase == Phase.PHASE_10){result=checkEqualNumbers();}
        else {
            boolean left1=false;
            boolean right2=false;
            boolean left2=false;
            boolean right1=false;
            left1=CardEvaluator.getInstance().checkForEqualNumbers(GameLogicHandler.getInstance().getGameData().getPlayers().get(GameLogicHandler.getInstance().getGameData().getActivePlayerId()).getPhaseCards());
            left2=CardEvaluator.getInstance().checkForEqualNumbers(GameLogicHandler.getInstance().getGameData().getPlayers().get(GameLogicHandler.getInstance().getGameData().getActivePlayerId()).getPhaseCards2());
            right1=isARow();
            right2=CardEvaluator.getInstance().checkIfInARow(GameLogicHandler.getInstance().getGameData().getPlayers().get(GameLogicHandler.getInstance().getGameData().getActivePlayerId()).getPhaseCards2());
            result =(left1&&right2)||(right1&&left2);
        }

        if (result){
            this.getGameData().getPlayers().get(GameLogicHandler.getInstance().getGameData().getActivePlayerId()).getPhaseCardsTemp().clear();
            this.getGameData().getPlayers().get(GameLogicHandler.getInstance().getGameData().getActivePlayerId()).getPhaseCards2Temp().clear();
            Toast.makeText(this.gameActivity, "The list is not correct!", Toast.LENGTH_SHORT).show();
        }
        else {
            moveCardsBackToHand();
            Toast.makeText(this.gameActivity, "The list is not correct!", Toast.LENGTH_SHORT).show();
        }

    }

    private boolean checkEqualNumbers(){
        boolean left=false;
        boolean right=false;
        left=CardEvaluator.getInstance().checkForEqualNumbers(GameLogicHandler.getInstance().getGameData().getPlayers().get(GameLogicHandler.getInstance().getGameData().getActivePlayerId()).getPhaseCards());
        right=CardEvaluator.getInstance().checkForEqualNumbers(GameLogicHandler.getInstance().getGameData().getPlayers().get(GameLogicHandler.getInstance().getGameData().getActivePlayerId()).getPhaseCards2());
        return left && right;
    }

    private boolean isARow(){
        return CardEvaluator.getInstance().checkIfInARow(GameLogicHandler.getInstance().getGameData().getPlayers().get(GameLogicHandler.getInstance().getGameData().getActivePlayerId()).getPhaseCards());
    }

    public void setPlayerNames() {
        this.gameActivity.setPlayer1Name(this.gameData.getActivePlayerId());
        String player2 = "";
        for (Player p : gameData.getPlayers().values()) {
            if (!p.getId().equals(gameData.getActivePlayerId())) {
                player2 = p.getId();
            }
        }
        this.gameActivity.setPlayer2Name(player2);


        /*this.gameData.getPlayers().get(this.gameData.getActivePlayerId()).setCurrentName(intent.getStringExtra(MainActivity.FIRST_PLAYER));
        this.getGameActivity().setPlayer1Name(this.gameData.getPlayers().get(this.gameData.getActivePlayerId()).getCurrentName());
        this.gameData.nextPlayer();
        this.gameData.getPlayers().get(this.gameData.getActivePlayerId()).setCurrentName(intent.getStringExtra(MainActivity.SECOND_PLAYER));
        this.getGameActivity().setPlayer2Name(this.gameData.getPlayers().get(this.gameData.getActivePlayerId()).getCurrentName());
        this.gameData.nextPlayer();*/


    }
}
