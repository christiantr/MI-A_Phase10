package com.mia.phase10.gameLogic;

import android.widget.Toast;

import com.google.gson.Gson;
import com.mia.phase10.GameActivity;
import com.mia.phase10.classes.Card;
import com.mia.phase10.classes.CardStack;
import com.mia.phase10.classes.GameData;
import com.mia.phase10.classes.Player;
import com.mia.phase10.classes.SimpleCard;
import com.mia.phase10.exceptionClasses.CardNotFoundException;
import com.mia.phase10.exceptionClasses.EmptyCardStackException;
import com.mia.phase10.exceptionClasses.EmptyHandException;
import com.mia.phase10.exceptionClasses.PlayerNotFoundException;
import com.mia.phase10.gameFlow.GamePhase;
import com.mia.phase10.gameFlow.LayOffCardsPhase;

import java.util.HashMap;
import java.util.List;
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
        this.gameData.getDrawStack().getCardList().clear();
        this.gameData.getDrawStack().generateCardStack();
        this.getGameData().getDrawStack().mixStack();
        this.gameActivity.startShufflingActivity();
        this.gameData.setRoundClosed(false);
        for (Player p : this.gameData.getPlayers().values()) {
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
            if (!this.gameData.getPlayers().get(this.gameData.getActivePlayerId()).isPhaseAchieved() && (
                    !this.gameData.getPlayers().get(this.gameData.getActivePlayerId()).getPhaseCards().isEmpty() ||
                            !this.gameData.getPlayers().get(this.gameData.getActivePlayerId()).getPhaseCards2().isEmpty())) {
                movePhaseCardsBackToHand();
            }

            String currentP = this.gameData.getActivePlayerId();
            moveCardsBackToHand(LayOffCardsPhase.ACTIVE_PHASE);
            moveCardsBackToHand(LayOffCardsPhase.NEXTPLAYER_PHASE);

            Card c = this.gameData.getPlayers().get(playerId).getHand().removeCard(cardId);
            this.gameData.getLayOffStack().addCard(c);

            if (this.gameData.getPlayers().get(playerId).getHand().getCardList().isEmpty()) {
                this.gameData.setRoundClosed(true);
                this.gameData.setPhase(GamePhase.END_TURN_PHASE);
                this.countCards();
                this.setNewPhaseForPlayer(playerId);
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

    public int getFirstNumberOfPhaseCards(List<Card> list) {
        int number = 0;
        boolean firstNumber = false;
        for (Card c : list) {
            if (c instanceof SimpleCard) {
                return ((SimpleCard) c).getNumber();
            }
        }
        return -1;
    }

    public void layoffPhase(PlaystationType t, String playerId, int cardId) throws EmptyHandException, CardNotFoundException, PlayerNotFoundException {
        String currentP = GameLogicHandler.getInstance().getGameData().getActivePlayerId();
        String next;
        if(currentP.equals(this.gameActivity.getPlayer1Name())){
            next=this.gameActivity.getPlayer2Name();
        }
        else {
            next=this.gameActivity.getPlayer1Name(); }
        try {
            if (gameData.getPlayers().get(currentP).getHand().getCardList().size() == 1) {
                this.gameActivity.setVisibilityOfButtons1();
                this.gameActivity.setVisibilityOfButtons2();
                moveCardsBackToHand(LayOffCardsPhase.ACTIVE_PHASE);
                moveCardsBackToHand(LayOffCardsPhase.NEXTPLAYER_PHASE);

                Toast.makeText(this.getGameActivity(), "You must drop your last card onto the layoff stack in order to close the current round!", Toast.LENGTH_SHORT).show();
            } else {
                Card c = gameData.getPlayers().get(playerId).getHand().removeCard(cardId);
                if (t == PlaystationType.PLAYSTATION) {
                    if (((SimpleCard) c).getNumber() <= getFirstNumberOfPhaseCards(this.gameData.getPlayers().get(this.gameData.getActivePlayerId()).getPhaseCards())) {
                        this.gameData.getPlayers().get(this.gameData.getActivePlayerId()).getPhaseCards().add(0, c);
                    } else {
                        this.gameData.getPlayers().get(this.gameData.getActivePlayerId()).getPhaseCards().add(c);
                    }
                    if (this.gameData.getPlayers().get(this.gameData.getActivePlayerId()).isPhaseAchieved()) {
                        this.gameData.getPlayers().get(this.gameData.getActivePlayerId()).getPhaseCardsTemp().add(c);
                    }
                } else if (t == PlaystationType.PLAYSTATION_RIGHT) {
                    if (((SimpleCard) c).getNumber() <= getFirstNumberOfPhaseCards(this.gameData.getPlayers().get(this.gameData.getActivePlayerId()).getPhaseCards2())) {
                        this.gameData.getPlayers().get(this.gameData.getActivePlayerId()).getPhaseCards2().add(0, c);
                    } else {
                        this.gameData.getPlayers().get(this.gameData.getActivePlayerId()).getPhaseCards2().add(c);
                    }
                    if (this.gameData.getPlayers().get(this.gameData.getActivePlayerId()).isPhaseAchieved()) {
                        this.gameData.getPlayers().get(this.gameData.getActivePlayerId()).getPhaseCards2Temp().add(c);
                    }
                } else if (t == PlaystationType.PLAYSTATION_TWO) {
                    if (((SimpleCard) c).getNumber() <= getFirstNumberOfPhaseCards(this.gameData.getPlayers().get(next).getPhaseCards())) {
                        this.gameData.getPlayers().get(next).getPhaseCards().add(0, c);
                    } else {
                        this.gameData.getPlayers().get(next).getPhaseCards().add(c);
                    }
                    if (this.gameData.getPlayers().get(next).isPhaseAchieved()) {
                        this.gameData.getPlayers().get(next).getPhaseCardsTemp().add(c);
                    }
                } else if (t == PlaystationType.PLAYSTATION_TWO_RIGHT) {
                    if (((SimpleCard) c).getNumber() <= getFirstNumberOfPhaseCards(this.gameData.getPlayers().get(next).getPhaseCards2())) {
                        this.gameData.getPlayers().get(next).getPhaseCards2().add(0, c);
                    } else {
                        this.gameData.getPlayers().get(next).getPhaseCards2().add(c);
                    }
                    if (this.gameData.getPlayers().get(next).isPhaseAchieved()) {
                        this.gameData.getPlayers().get(next).getPhaseCards2Temp().add(c);
                    }
                }
                this.gameActivity.visualize();
            }

        } catch (Exception c) {
            throw new PlayerNotFoundException("Player not found!");
        }
    }

    public void movePhaseCardsBackToHand() {
        this.gameActivity.setVisibilityOfButtons1();
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

    public void moveCardsBackToHand(LayOffCardsPhase next) {
        String playerID = this.gameData.getActivePlayerId();

        if (next == LayOffCardsPhase.ACTIVE_PHASE) {
            this.gameActivity.setVisibilityOfButtons1();
        } else if (next == LayOffCardsPhase.NEXTPLAYER_PHASE) {
            this.gameActivity.setVisibilityOfButtons2();
            if(playerID.equals(this.gameActivity.getPlayer1Name())){
                playerID=this.gameActivity.getPlayer2Name();
            }
            else {
                playerID=this.gameActivity.getPlayer1Name();
            }
        }
        for (Card c : this.getGameData().getPlayers().get(playerID).getPhaseCardsTemp()) {
            this.getGameData().getPlayers().get(playerID).getPhaseCards().remove(c);
            this.getGameData().getPlayers().get(this.gameData.getActivePlayerId()).getHand().addCard(c);
        }
        if (!this.getGameData().getPlayers().get(playerID).getPhaseCards2Temp().isEmpty()) {
            for (Card c : this.getGameData().getPlayers().get(playerID).getPhaseCards2Temp()) {
                this.getGameData().getPlayers().get(playerID).getPhaseCards2().remove(c);
                this.getGameData().getPlayers().get(this.gameData.getActivePlayerId()).getHand().addCard(c);
            }
        }
        this.getGameData().getPlayers().get(playerID).getPhaseCardsTemp().clear();
        this.getGameData().getPlayers().get(playerID).getPhaseCards2Temp().clear();
        this.gameActivity.visualize();
    }

    public void drawCard(String playerId, StackType stackType) throws EmptyCardStackException {
        Card card = null;
        switch (stackType) {

            case DRAW_STACK:
                if (gameData.getDrawStack().getCardList().size() == 1) {
                    for (int i = 0; i < gameData.getLayOffStack().getCardList().size() - 2; i++) {
                        Card c = gameData.getLayOffStack().getCardList().remove(i);
                        gameData.getDrawStack().getCardList().add(c);
                    }
                    gameData.getDrawStack().mixStack();
                }
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

    public void setNewPhaseForPlayer(String playerId) {
        if (this.gameData.getPlayers().get(playerId).isPhaseAchieved()) {
            this.gameData.getPlayers().get(playerId).setCurrentPhase((this.gameData.getPlayers().get(playerId).getCurrentPhase()).ordinal() < Phase.values().length - 1 ? Phase.values()[(this.gameData.getPlayers().get(playerId).getCurrentPhase()).ordinal() + 1] : null);
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
        Phase p = this.gameData.getPlayers().get(this.gameData.getActivePlayerId()).getCurrentPhase();
        this.gameActivity.setVisibilityOfButtons1();
        if (p == Phase.PHASE_4 || p == Phase.PHASE_5 || p == Phase.PHASE_6 || p == Phase.PHASE_8) {
            if (CardEvaluator.getInstance().checkPhase(this.gameData.getPlayers().get(gameData.getActivePlayerId()).getCurrentPhase(), this.gameData.getPlayers().get(gameData.getActivePlayerId()).getPhaseCards())) {
                this.gameData.getPlayers().get(this.gameData.getActivePlayerId()).setPhaseAchieved(true);
                this.gameActivity.visualize();
                Toast.makeText(this.getGameActivity(), "The phase is correct!", Toast.LENGTH_SHORT).show();
            } else {
                movePhaseCardsBackToHand();
                Toast.makeText(this.gameActivity, "The phase is not correct!", Toast.LENGTH_SHORT).show();
            }
        } else {
            if (CardEvaluator.getInstance().checkPhase(this.gameData.getPlayers().get(gameData.getActivePlayerId()).getCurrentPhase(), this.gameData.getPlayers().get(this.gameData.getActivePlayerId()).getPhaseCards(),
                    this.gameData.getPlayers().get(this.gameData.getActivePlayerId()).getPhaseCards2())) {

                this.gameData.getPlayers().get(this.gameData.getActivePlayerId()).setPhaseAchieved(true);
                this.gameActivity.visualize();
                Toast.makeText(this.getGameActivity(), "The phase is correct!", Toast.LENGTH_SHORT).show();

            } else {
                movePhaseCardsBackToHand();
                Toast.makeText(this.gameActivity, "The phase is not correct!", Toast.LENGTH_SHORT).show();
            }

        }
    }

    public void checkNewCardList(LayOffCardsPhase next) {
        String playerID = this.gameData.getActivePlayerId();

        if (next == LayOffCardsPhase.ACTIVE_PHASE) {
            this.gameActivity.setVisibilityOfButtons1();
        } else if (next == LayOffCardsPhase.NEXTPLAYER_PHASE) {
            this.gameActivity.setVisibilityOfButtons2();
            if(playerID.equals(this.gameActivity.getPlayer1Name())){
                playerID=this.gameActivity.getPlayer2Name();
            }
            else {
                playerID=this.gameActivity.getPlayer1Name();
            }
        }

        Phase phase = this.gameData.getPlayers().get(playerID).getCurrentPhase();
        boolean result = false;
        if (phase == Phase.PHASE_4 || phase == Phase.PHASE_5 || phase == Phase.PHASE_6) {
            result = isARow(playerID);
        } else if (phase == Phase.PHASE_8) {
            result = CardEvaluator.getInstance().checkSameColors(this.gameData.getPlayers().get(playerID).getPhaseCards());
        } else if (phase == Phase.PHASE_1 || phase == Phase.PHASE_7 || phase == Phase.PHASE_9 || phase == Phase.PHASE_10) {
            result = checkEqualNumbers(playerID);
        } else {
            boolean left1 = false;
            boolean right2 = false;
            boolean left2 = false;
            boolean right1 = false;
            left1 = CardEvaluator.getInstance().checkForEqualNumbers(this.gameData.getPlayers().get(playerID).getPhaseCards());
            left2 = CardEvaluator.getInstance().checkForEqualNumbers(this.gameData.getPlayers().get(playerID).getPhaseCards2());
            right1 = isARow(playerID);
            right2 = CardEvaluator.getInstance().checkIfInARow(this.gameData.getPlayers().get(playerID).getPhaseCards2());
            result = (left1 && right2) || (right1 && left2);
        }

        if (result) {
            this.getGameData().getPlayers().get(playerID).getPhaseCardsTemp().clear();
            this.getGameData().getPlayers().get(playerID).getPhaseCards2Temp().clear();
            Toast.makeText(this.gameActivity, "The list is correct!", Toast.LENGTH_SHORT).show();
        } else {
            if (next == LayOffCardsPhase.NEXTPLAYER_PHASE) {
            }
            moveCardsBackToHand(next);
            Toast.makeText(this.gameActivity, "The list is not correct!", Toast.LENGTH_SHORT).show();
        }

        if (next == LayOffCardsPhase.NEXTPLAYER_PHASE) {
        }
    }

    private boolean checkEqualNumbers(String playerID) {
        boolean left = false;
        boolean right = false;
        left = CardEvaluator.getInstance().checkForEqualNumbers(GameLogicHandler.getInstance().getGameData().getPlayers().get(playerID).getPhaseCards());
        right = CardEvaluator.getInstance().checkForEqualNumbers(GameLogicHandler.getInstance().getGameData().getPlayers().get(playerID).getPhaseCards2());
        return left && right;
    }

    private boolean isARow(String playerID) {
        return CardEvaluator.getInstance().checkIfInARow(GameLogicHandler.getInstance().getGameData().getPlayers().get(playerID).getPhaseCards());
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
