package com.mia.phase10.gameLogic;

import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mia.phase10.GameActivity;
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
        this.gameActivity.startShufflingActivity();
        for (Player p : gameData.getPlayers().values()) {
            for (int i = 0; i < 10; i++) {
                Card c = this.gameData.getDrawStack().drawCard();
                p.getHand().addCard(c);
            }
            p.getPhaseCards().clear();
            p.getPhaseCards2().clear();
        }
        this.gameData.setPhase(GamePhase.DRAW_PHASE);
        this.gameData.nextPlayer();

        this.gameActivity.makePlaystationLayoutVisible(GameLogicHandler.getInstance().getGameData().getPlayers().get(GameLogicHandler.getInstance().getGameData().getActivePlayerId()).getCurrentPhase());
        this.gameActivity.visualize();
    }

    public void layoffCard(String playerId, int cardId) throws EmptyHandException, CardNotFoundException, PlayerNotFoundException {

        try {

            Card c = gameData.getPlayers().get(playerId).getHand().removeCard(cardId);
            this.gameData.getLayOffStack().addCard(c);

            this.gameData.nextPlayer();
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
                card = gameData.getLayOffStack().getFirstCard();
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

    public void checkPhase() {
        Phase p = GameLogicHandler.getInstance().getGameData().getPlayers().get(GameLogicHandler.getInstance().getGameData().getActivePlayerId()).getCurrentPhase();
        if (p == Phase.PHASE_4 || p == Phase.PHASE_5 || p == Phase.PHASE_6 || p == Phase.PHASE_8) {
            if (CardEvaluator.getInstance().checkPhase(this.gameData.getPlayers().get(gameData.getActivePlayerId()).getCurrentPhase(), this.gameData.getPlayers().get(gameData.getActivePlayerId()).getPhaseCards())) {
                this.gameActivity.setVisibilityOfButtons();
                for (Card card : GameLogicHandler.getInstance().getGameData().getPlayers().get(GameLogicHandler.getInstance().getGameData().getActivePlayerId()).getPhaseCards()) {
                    try {
                        this.layoffCard(GameLogicHandler.getInstance().getGameData().getActivePlayerId(), card.getId()); //delete card of hand
                    } catch (EmptyHandException | CardNotFoundException | PlayerNotFoundException e) {
                        e.printStackTrace();
                    }
                }

                Toast.makeText(this.getGameActivity(), "The phase is correct!", Toast.LENGTH_SHORT).show();

                if (this.gameData.getActivePlayerId().equals("player_1")) {
                    this.gameActivity.getDeck().removeAllViews();
                    this.gameActivity.getPlaystationP1Layout().removeAllViews();
                    this.gameActivity.getPlaystationP2Layout().removeAllViews();
                    this.gameActivity.showPlaystation2Cards();
                    gameData.setActivePlayerId("player_2");
                    this.gameActivity.switchPlayerName(this.gameActivity.getPlayer2(), this.gameActivity.getPlayer1());

                } else {
                    this.gameActivity.getPlaystationP2Layout().removeAllViews();
                    this.gameActivity.showPlaystation2Cards();
                    gameData.setActivePlayerId("player_1");
                    this.gameActivity.getDeck().removeAllViews();
                    this.gameActivity.switchPlayerName(this.gameActivity.getPlayer1(), this.gameActivity.getPlayer2());
                    this.gameActivity.getPlaystationP1Layout().removeAllViews();

                }
                this.gameActivity.showHandCards();
                this.gameActivity.showPlaystation1Cards();

            } else {
                this.gameActivity.removeCardsFromPlaystationBackToHand();
                this.gameData.getPlayers().get(this.gameData.getActivePlayerId()).getPhaseCards().clear();
                Toast.makeText(this.gameActivity, "The phase is not correct!", Toast.LENGTH_SHORT).show();
            }
        } else {
            if (CardEvaluator.getInstance().checkPhase(this.gameData.getPlayers().get(gameData.getActivePlayerId()).getCurrentPhase(), this.gameData.getPlayers().get(gameData.getActivePlayerId()).getPhaseCards(),
                    this.gameData.getPlayers().get(gameData.getActivePlayerId()).getPhaseCards2())) {
                this.gameActivity.setVisibilityOfButtons();
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

                if (this.gameData.getActivePlayerId().equals("player_1")) {
                    this.gameActivity.getDeck().removeAllViews();
                    this.gameActivity.getPlaystationP1LayoutL().removeAllViews();
                    this.gameActivity.getPlaystationP1LayoutR().removeAllViews();
                    this.gameActivity.getPlaystationP2LayoutL().removeAllViews();
                    this.gameActivity.getPlaystationP2LayoutR().removeAllViews();
                    this.gameActivity.showPlaystation2Cards();
                    gameData.setActivePlayerId("player_2");
                    this.gameActivity.switchPlayerName(this.gameActivity.getPlayer2(), this.gameActivity.getPlayer1());

                } else {
                    this.gameActivity.getPlaystationP2LayoutL().removeAllViews();
                    this.gameActivity.getPlaystationP2LayoutR().removeAllViews();
                    this.gameActivity.showPlaystation2Cards();
                    gameData.setActivePlayerId("player_1");
                    this.gameActivity.getDeck().removeAllViews();
                    this.gameActivity.switchPlayerName(this.gameActivity.getPlayer1(), this.gameActivity.getPlayer2());
                    this.gameActivity.getPlaystationP1LayoutL().removeAllViews();
                    this.gameActivity.getPlaystationP1LayoutR().removeAllViews();

                }
                this.gameActivity.showHandCards();
                this.gameActivity.showPlaystation1Cards();

            } else {
                this.gameActivity.removeCardsFromPlaystationBackToHand();
                this.gameData.getPlayers().get(this.gameData.getActivePlayerId()).getPhaseCards().clear();
                this.gameData.getPlayers().get(this.gameData.getActivePlayerId()).getPhaseCards2().clear();
                Toast.makeText(this.gameActivity, "The phase is not correct!", Toast.LENGTH_SHORT).show();
            }

        }
    }
}
