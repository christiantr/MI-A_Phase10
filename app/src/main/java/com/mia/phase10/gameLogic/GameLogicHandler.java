package com.mia.phase10.gameLogic;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;

import com.google.gson.Gson;
import com.mia.phase10.activities.GameActivity;
import com.mia.phase10.classes.Card;
import com.mia.phase10.classes.CardStack;
import com.mia.phase10.classes.GameData;
import com.mia.phase10.classes.Player;
import com.mia.phase10.classes.SimpleCard;
import com.mia.phase10.classes.SpecialCard;
import com.mia.phase10.classes.enums.SpecialCardValue;
import com.mia.phase10.exceptionClasses.CardNotFoundException;
import com.mia.phase10.exceptionClasses.EmptyCardStackException;
import com.mia.phase10.exceptionClasses.EmptyHandException;
import com.mia.phase10.exceptionClasses.PlayerNotFoundException;
import com.mia.phase10.gameLogic.enums.GamePhase;
import com.mia.phase10.gameLogic.enums.LayOffCardsPhase;
import com.mia.phase10.gameLogic.enums.Phase;
import com.mia.phase10.gameLogic.enums.PlaystationType;
import com.mia.phase10.gameLogic.enums.StackType;
import com.mia.phase10.gameLogic.enums.ToastMessages;
import com.mia.phase10.network.Client;
import com.mia.phase10.network.transport.ControlObject;
import com.mia.phase10.network.transport.TransportObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class GameLogicHandler {
    private static volatile GameLogicHandler glhInstance = new GameLogicHandler();
    private GameData gameData;
    private GameActivity gameActivity;
    private Client client;
    private final static String TAG = "GAMELOGICHANDLDER";

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
        this.gameData.setRoundClosed(false);
        for (Player p : this.gameData.getPlayers().values()) {
            p.setCheated(false);
            p.setCheatUncovered(false);
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
            p.setExposed(false);
        }

        this.gameData.setPhase(GamePhase.START_PHASE);
        this.gameData.nextPlayer();
        this.gameData.getLayOffStack().addCard(this.gameData.getDrawStack().drawCard());
    }

    public void layoffCard(String playerId, int cardId) throws EmptyHandException, CardNotFoundException, PlayerNotFoundException {
        try {
            if (!this.gameData.getPlayers().get(this.gameData.getActivePlayerId()).isPhaseAchieved() && (
                    !this.gameData.getPlayers().get(this.gameData.getActivePlayerId()).getPhaseCards().isEmpty() ||
                            !this.gameData.getPlayers().get(this.gameData.getActivePlayerId()).getPhaseCards2().isEmpty())) {
                movePhaseCardsBackToHand();
            }

            moveCardsBackToHand(LayOffCardsPhase.ACTIVE_PHASE);
            moveCardsBackToHand(LayOffCardsPhase.NEXTPLAYER_PHASE);

            Card c = this.gameData.getPlayers().get(playerId).getHand().removeCard(cardId);
            this.gameData.getLayOffStack().addCard(c);


            if (this.gameData.getPlayers().get(playerId).getHand().getCardList().isEmpty() && this.gameData.getPlayers().get(playerId).getCurrentPhase() == Phase.PHASE_10) {
                this.gameData.setGameClosed(true);
                this.setNewPhaseForPlayer(playerId);
            } else if (this.gameData.getPlayers().get(playerId).getHand().getCardList().isEmpty()) {
                this.gameData.setRoundClosed(true);
                this.gameData.setPhase(GamePhase.START_PHASE);
                this.countCards();
                this.setNewPhaseForPlayer(playerId);
                startRound();
            } else {
                this.gameData.nextPlayer();
                this.gameData.setPhase(GamePhase.DRAW_PHASE);
                this.gameActivity.visualize();
            }
        } catch (Exception c) {
            throw new PlayerNotFoundException("Player not found!");
        }

        sendGameState();
    }

    public Card cheat() {
        this.gameData.getPlayers().get(this.gameData.getActivePlayerId()).setCheated(true);
        return this.gameData.getDrawStack().getFirstCard();
    }

    public int getFirstNumberOfPhaseCards(List<Card> list) {
        for (Card c : list) {
            if (c instanceof SimpleCard) {
                return ((SimpleCard) c).getNumber();
            }
        }
        return -1;
    }

    public void sendGameState() {
        ((Client) client).sendObject(TransportObject.makeGameDataTransportObject());
    }

    public void exposeCheat() {
        if (!this.gameData.getPreviousPlayer().isEmpty()) {
            if (this.gameData.getPlayers().get(gameData.getPreviousPlayer()).hasCheated()) {
                //player cheated!
                this.gameData.getPlayers().get(gameData.getPreviousPlayer()).setCheatUncovered(true);
                this.gameData.getPlayers().get(gameData.getPreviousPlayer()).setPoints(5);
                this.gameActivity.showMessage("Congrats: you exposed " + this.gameData.getPreviousPlayer());
            } else {
                //player did not cheat
                this.gameData.getPlayers().get(gameData.getActivePlayerId()).setPoints(5);
                this.gameActivity.showMessage("OOOHH: player didn't cheat --> 5 points for you");
                this.gameActivity.visualize();
            }
        }
    }

    public void layoffPhase(PlaystationType t, int cardId) throws EmptyHandException, CardNotFoundException, PlayerNotFoundException {
        String currentP = GameLogicHandler.getInstance().getGameData().getActivePlayerId();
        String next;
        if (currentP.equals(this.gameActivity.getPlayer1ID())) {
            next = this.gameActivity.getPlayer2ID();
        } else {
            next = this.gameActivity.getPlayer1ID();
        }
        try {
            if (gameData.getPlayers().get(currentP).getHand().getCardList().size() == 1) {
                this.gameActivity.setVisibilityOfButtons1();
                this.gameActivity.setVisibilityOfButtons2();
                moveCardsBackToHand(LayOffCardsPhase.ACTIVE_PHASE);
                moveCardsBackToHand(LayOffCardsPhase.NEXTPLAYER_PHASE);
                this.gameActivity.showToasts(ToastMessages.LAST_CARD);
            } else {
                Card c = gameData.getPlayers().get(currentP).getHand().removeCard(cardId);
                if (c instanceof SimpleCard) {
                    layOffSimpleCard(t, next, (SimpleCard) c);
                } else if (((SpecialCard) c).getValue() == SpecialCardValue.JOKER) {
                    layOffJoker(t, next, (SpecialCard) c);
                }
            }

        } catch (Exception c) {
            throw new PlayerNotFoundException("Player not found!");
        }
    }

    private void layOffJoker(final PlaystationType t, final String next, final SpecialCard c) {
        AlertDialog.Builder chooseSide = new AlertDialog.Builder(this.gameActivity);
        chooseSide.setCancelable(false);
        chooseSide.setTitle("Wollen sie den Joker links oder rechts von der bereits abgelegten Karte ablegen?");
        chooseSide.setNegativeButton("Links", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                layOffJokerLeftOrRight(LayOffCardsPhase.LEFT, t, next, c);
            }
        });
        chooseSide.setPositiveButton("Rechts", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                layOffJokerLeftOrRight(LayOffCardsPhase.RIGHT, t, next, c);
            }
        });
        chooseSide.setIcon(android.R.drawable.ic_dialog_info);
        chooseSide.show();

    }

    public void layOffJokerLeftOrRight(LayOffCardsPhase side, PlaystationType t, String next, SpecialCard c) {
        if (t == PlaystationType.PLAYSTATION) {
            if (side == LayOffCardsPhase.LEFT) {
                this.gameData.getPlayers().get(this.gameData.getActivePlayerId()).getPhaseCards().add(0, c);
            } else {
                this.gameData.getPlayers().get(this.gameData.getActivePlayerId()).getPhaseCards().add(c);
            }
            if (this.gameData.getPlayers().get(this.gameData.getActivePlayerId()).isPhaseAchieved()) {
                this.gameData.getPlayers().get(this.gameData.getActivePlayerId()).getPhaseCardsTemp().add(c);
            }
        } else if (t == PlaystationType.PLAYSTATION_RIGHT) {
            if (side == LayOffCardsPhase.LEFT) {
                this.gameData.getPlayers().get(this.gameData.getActivePlayerId()).getPhaseCards2().add(0, c);
            } else {
                this.gameData.getPlayers().get(this.gameData.getActivePlayerId()).getPhaseCards2().add(c);
            }
            if (this.gameData.getPlayers().get(this.gameData.getActivePlayerId()).isPhaseAchieved()) {
                this.gameData.getPlayers().get(this.gameData.getActivePlayerId()).getPhaseCards2Temp().add(c);
            }
        } else if (t == PlaystationType.PLAYSTATION_TWO) {
            if (side == LayOffCardsPhase.LEFT) {
                this.gameData.getPlayers().get(next).getPhaseCards().add(0, c);
            } else {
                this.gameData.getPlayers().get(next).getPhaseCards().add(c);
            }
            if (this.gameData.getPlayers().get(next).isPhaseAchieved()) {
                this.gameData.getPlayers().get(next).getPhaseCardsTemp().add(c);
            }
        } else if (t == PlaystationType.PLAYSTATION_TWO_RIGHT) {
            if (side == LayOffCardsPhase.LEFT) {
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

    private void layOffSimpleCard(PlaystationType t, String next, SimpleCard c) {
        if (t == PlaystationType.PLAYSTATION) {
            if ((c.getNumber() <= getFirstNumberOfPhaseCards(this.gameData.getPlayers().get(this.gameData.getActivePlayerId()).getPhaseCards()))) {
                this.gameData.getPlayers().get(this.gameData.getActivePlayerId()).getPhaseCards().add(0, c);
            } else {
                this.gameData.getPlayers().get(this.gameData.getActivePlayerId()).getPhaseCards().add(c);
            }
            if (this.gameData.getPlayers().get(this.gameData.getActivePlayerId()).isPhaseAchieved()) {
                this.gameData.getPlayers().get(this.gameData.getActivePlayerId()).getPhaseCardsTemp().add(c);
            }
        } else if (t == PlaystationType.PLAYSTATION_RIGHT) {
            if (c.getNumber() <= getFirstNumberOfPhaseCards(this.gameData.getPlayers().get(this.gameData.getActivePlayerId()).getPhaseCards2())) {
                this.gameData.getPlayers().get(this.gameData.getActivePlayerId()).getPhaseCards2().add(0, c);
            } else {
                this.gameData.getPlayers().get(this.gameData.getActivePlayerId()).getPhaseCards2().add(c);
            }
            if (this.gameData.getPlayers().get(this.gameData.getActivePlayerId()).isPhaseAchieved()) {
                this.gameData.getPlayers().get(this.gameData.getActivePlayerId()).getPhaseCards2Temp().add(c);
            }
        } else if (t == PlaystationType.PLAYSTATION_TWO) {
            if (c.getNumber() <= getFirstNumberOfPhaseCards(this.gameData.getPlayers().get(next).getPhaseCards())) {
                this.gameData.getPlayers().get(next).getPhaseCards().add(0, c);
            } else {
                this.gameData.getPlayers().get(next).getPhaseCards().add(c);
            }
            if (this.gameData.getPlayers().get(next).isPhaseAchieved()) {
                this.gameData.getPlayers().get(next).getPhaseCardsTemp().add(c);
            }
        } else if (t == PlaystationType.PLAYSTATION_TWO_RIGHT) {
            if (c.getNumber() <= getFirstNumberOfPhaseCards(this.gameData.getPlayers().get(next).getPhaseCards2())) {
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
            if (playerID.equals(this.gameActivity.getPlayer1ID())) {
                playerID = this.gameActivity.getPlayer2ID();
            } else {
                playerID = this.gameActivity.getPlayer1ID();
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
                if (this.gameData.getDrawStack().getCardList().size() == 1) {
                    for (int i = 0; i < this.gameData.getLayOffStack().getCardList().size() - 2; i++) {
                        Card c = this.gameData.getLayOffStack().getCardList().remove(i);
                        this.gameData.getDrawStack().getCardList().add(c);
                    }
                    this.gameData.getDrawStack().mixStack();
                }
                card = this.gameData.getDrawStack().drawCard();
                this.gameData.getPlayers().get(playerId).getHand().addCard(card);
                this.gameData.setPhase(GamePhase.LAYOFF_PHASE);
                break;
            case LAYOFF_STACK:
                boolean isExposeCard = false;
                card = this.gameData.getLayOffStack().drawLastCard();
                if (card.getImagePath().equals("card_expose")) {
                    isExposeCard = true;
                }
                if (!isExposeCard) {
                    this.gameData.getPlayers().get(playerId).getHand().addCard(card);
                    this.gameData.setPhase(GamePhase.LAYOFF_PHASE);
                } else {
                    this.gameData.getLayOffStack().addCard(card);
                    this.gameData.setPhase(GamePhase.DRAW_PHASE);
                }
                break;
        }

        this.gameActivity.visualize();
    }


    public void countCards() {
        for (Player p : gameData.getPlayers().values()) {
            Map<Integer, Card> cards = p.getHand().getCardList();
            int points = 0;
            for (Card item : cards.values()) {
                points = points + item.getcountCard();
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
                this.gameActivity.showToasts(ToastMessages.PHASE_CORRECT);
            } else {
                movePhaseCardsBackToHand();
                this.gameActivity.showToasts(ToastMessages.PHASE_INCORRECT);
            }
        } else {
            if (CardEvaluator.getInstance().checkPhase(this.gameData.getPlayers().get(gameData.getActivePlayerId()).getCurrentPhase(), this.gameData.getPlayers().get(this.gameData.getActivePlayerId()).getPhaseCards(),
                    this.gameData.getPlayers().get(this.gameData.getActivePlayerId()).getPhaseCards2())) {

                this.gameData.getPlayers().get(this.gameData.getActivePlayerId()).setPhaseAchieved(true);
                this.gameActivity.visualize();
                this.gameActivity.showToasts(ToastMessages.PHASE_CORRECT);

            } else {
                movePhaseCardsBackToHand();
                this.gameActivity.showToasts(ToastMessages.PHASE_INCORRECT);

            }

        }
    }

    public void checkNewCardList(LayOffCardsPhase next) {
        String playerID = this.gameData.getActivePlayerId();

        if (next == LayOffCardsPhase.ACTIVE_PHASE) {
            this.gameActivity.setVisibilityOfButtons1();
        } else if (next == LayOffCardsPhase.NEXTPLAYER_PHASE) {
            this.gameActivity.setVisibilityOfButtons2();
            if (playerID.equals(this.gameActivity.getPlayer1ID())) {
                playerID = this.gameActivity.getPlayer2ID();
            } else {
                playerID = this.gameActivity.getPlayer1ID();
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
            this.gameActivity.showToasts(ToastMessages.LIST_CORRECT);

        } else {
            moveCardsBackToHand(next);
            this.gameActivity.showToasts(ToastMessages.LIST_INCORRECT);
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

    public void exposePlayer(int id) throws CardNotFoundException, EmptyHandException {
        final int cardID = id;
        if (!this.gameData.getPlayers().get(this.gameData.getActivePlayerId()).isPhaseAchieved() && (
                !this.gameData.getPlayers().get(this.gameData.getActivePlayerId()).getPhaseCards().isEmpty() ||
                        !this.gameData.getPlayers().get(this.gameData.getActivePlayerId()).getPhaseCards2().isEmpty())) {
            movePhaseCardsBackToHand();
        }

        String currentP = this.gameData.getActivePlayerId();
        moveCardsBackToHand(LayOffCardsPhase.ACTIVE_PHASE);
        moveCardsBackToHand(LayOffCardsPhase.NEXTPLAYER_PHASE);

        Card c = this.gameData.getPlayers().get(currentP).getHand().removeCard(id);
        this.gameData.getLayOffStack().addCard(c);
        this.gameActivity.visualize();
        this.gameActivity.visualizeExposingPlayer();
        this.gameActivity.setListenerForExposingPlayer(cardID);

    }

    public void choosePlayerToExpose(int id) throws CardNotFoundException, PlayerNotFoundException, EmptyHandException {
        Card c = this.gameData.getLayOffStack().drawLastCard();
        this.gameData.getPlayers().get(this.gameData.getActivePlayerId()).getHand().addCard(c);
        if (!this.gameData.getPlayers().get(this.gameActivity.getPlayer2ID()).isExposed()) {
            this.gameData.getPlayers().get(this.gameActivity.getPlayer2ID()).setExposed(true);
            layoffCard(this.gameData.getActivePlayerId(), id);
        } else {
            this.gameActivity.visualize();
        }
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setGameData(GameData gameData) {
        this.gameData = gameData;
    }

    public void closeConnections() {
        Log.i(TAG, "Close GameStartActivity.");
        if (client != null) {
            TransportObject object = TransportObject.ofControlObjectToAll(ControlObject.closeConnections());
            ((Client) client).sendObject(object);
        }
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Log.e(TAG, e.toString());
            //Restore interrupted state...
            Thread.currentThread().interrupt();
        }
    }
}