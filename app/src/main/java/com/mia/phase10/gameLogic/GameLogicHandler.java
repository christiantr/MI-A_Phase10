package com.mia.phase10.gameLogic;

import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mia.phase10.GameActivity;
import com.mia.phase10.MainActivity;
import com.mia.phase10.classes.Card;
import com.mia.phase10.classes.CardStack;
import com.mia.phase10.classes.GameData;
import com.mia.phase10.classes.Player;
import com.mia.phase10.exceptionClasses.CardNotFoundException;
import com.mia.phase10.exceptionClasses.EmptyCardStackException;
import com.mia.phase10.exceptionClasses.EmptyHandException;
import com.mia.phase10.exceptionClasses.PlayerNotFoundException;
import com.mia.phase10.gameFlow.GamePhase;

import java.util.HashMap;

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
            for (int i = 0; i < 10; i++) {
                Card c = this.gameData.getDrawStack().drawCard();
                p.getHand().addCard(c);
            }
            p.getPhaseCards().clear();
            p.getPhaseCards2().clear();
            p.setPhaseAchieved(false);
        }
        this.gameData.setPhase(GamePhase.DRAW_PHASE);
        this.gameData.nextPlayer();
        this.setPlayerNames();

        //JUST FOR TESTING PURPOSE
        //TODO DELETE!
        for(Player p : this.gameData.getPlayers().values()){
            p.getPhaseCards().add(this.gameData.getDrawStack().drawCard());
        }
        //END OF TESTING
        this.gameData.getLayOffStack().addCard(this.gameData.getDrawStack().drawCard());
        this.gameActivity.visualize();
    }

    public void layoffCard(String playerId, int cardId) throws EmptyHandException, CardNotFoundException, PlayerNotFoundException {

        try {

            Card c = gameData.getPlayers().get(playerId).getHand().removeCard(cardId);
            this.gameData.getLayOffStack().addCard(c);

            this.gameData.nextPlayer();
            this.setPlayerNames();
            this.gameData.setPhase(GamePhase.DRAW_PHASE);
            this.gameActivity.visualize();

        } catch (Exception c) {
            throw new PlayerNotFoundException("Player not found!");
        }
    }

    public void drawCard(String playerId, StackType stackType) throws EmptyCardStackException {
        Card card = null;
        switch (stackType) {

            case DRAW_STACK:
                card = gameData.getDrawStack().drawCard();
                gameData.getPlayers().get(playerId).getHand().addCard(card);
                break;
            case LAYOFF_STACK:
                card = gameData.getLayOffStack().drawLastCard();
                gameData.getPlayers().get(playerId).getHand().addCard(card);

        }
        this.gameData.setPhase(GamePhase.LAYOFF_PHASE);
        this.gameActivity.visualize();
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

    public void checkPhase() { //needs to be updated if there are more than 2 players
        Phase p = GameLogicHandler.getInstance().getGameData().getPlayers().get(GameLogicHandler.getInstance().getGameData().getActivePlayerId()).getCurrentPhase();
        if (p == Phase.PHASE_4 || p == Phase.PHASE_5 || p == Phase.PHASE_6 || p == Phase.PHASE_8) {
            if (CardEvaluator.getInstance().checkPhase(this.gameData.getPlayers().get(gameData.getActivePlayerId()).getCurrentPhase(), this.gameData.getPlayers().get(gameData.getActivePlayerId()).getPhaseCards())) {
                this.gameActivity.setVisibilityOfButtons();
                this.gameData.getPlayers().get(this.gameData.getActivePlayerId()).setPhaseAchieved(true);
                for (Card card : GameLogicHandler.getInstance().getGameData().getPlayers().get(GameLogicHandler.getInstance().getGameData().getActivePlayerId()).getPhaseCards()) {
                    try {
                        this.layoffCard(GameLogicHandler.getInstance().getGameData().getActivePlayerId(), card.getId()); //delete card of hand
                    } catch (EmptyHandException | CardNotFoundException | PlayerNotFoundException e) {
                        e.printStackTrace();
                    }
                }

                Toast.makeText(this.getGameActivity(), "The phase is correct!", Toast.LENGTH_SHORT).show();

            } else {
                this.gameActivity.removeCardsFromPlaystationBackToHand();
                Toast.makeText(this.gameActivity, "The phase is not correct!", Toast.LENGTH_SHORT).show();
            }
        } else {
            if (CardEvaluator.getInstance().checkPhase(this.gameData.getPlayers().get(gameData.getActivePlayerId()).getCurrentPhase(), this.gameData.getPlayers().get(this.gameData.getActivePlayerId()).getPhaseCards(),
                    this.gameData.getPlayers().get(this.gameData.getActivePlayerId()).getPhaseCards2())) {

                this.gameActivity.setVisibilityOfButtons();
                this.gameData.getPlayers().get(this.gameData.getActivePlayerId()).setPhaseAchieved(true);
                for (Card card : GameLogicHandler.getInstance().getGameData().getPlayers().get(GameLogicHandler.getInstance().getGameData().getActivePlayerId()).getPhaseCards()) {
                    try {
                        this.layoffCard(GameLogicHandler.getInstance().getGameData().getActivePlayerId(), card.getId()); //delete card of hand
                    } catch (EmptyHandException | CardNotFoundException | PlayerNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                for (Card card : GameLogicHandler.getInstance().getGameData().getPlayers().get(GameLogicHandler.getInstance().getGameData().getActivePlayerId()).getPhaseCards2()) {
                    try {
                        this.layoffCard(GameLogicHandler.getInstance().getGameData().getActivePlayerId(), card.getId()); //delete card of hand
                    } catch (EmptyHandException | CardNotFoundException | PlayerNotFoundException e) {
                        e.printStackTrace();
                    }
                }

                Toast.makeText(this.getGameActivity(), "The phase is correct!", Toast.LENGTH_SHORT).show();

            } else {
                this.gameActivity.removeCardsFromPlaystationBackToHand();
                Toast.makeText(this.gameActivity, "The phase is not correct!", Toast.LENGTH_SHORT).show();
            }

        }
    }

    public void setPlayerNames() {
        this.gameActivity.setPlayer1Name(this.gameData.getActivePlayerId());
        String player2="";
        for(Player p : gameData.getPlayers().values()){
            if(!p.getId().equals(gameData.getActivePlayerId())){
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
