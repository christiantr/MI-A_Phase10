package com.mia.phase10.gameLogic;

import com.mia.phase10.activities.GameActivity;
import com.mia.phase10.classes.Card;
import com.mia.phase10.classes.CardStack;
import com.mia.phase10.classes.Player;
import com.mia.phase10.classes.SpecialCard;
import com.mia.phase10.classes.enums.SpecialCardValue;
import com.mia.phase10.classes.enums.SpecialCardValue;
import com.mia.phase10.exceptionClasses.CardNotFoundException;
import com.mia.phase10.exceptionClasses.EmptyCardStackException;
import com.mia.phase10.exceptionClasses.EmptyHandException;
import com.mia.phase10.exceptionClasses.PlayerNotFoundException;
import com.mia.phase10.gameLogic.enums.StackType;
import com.mia.phase10.network.Client;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class GameLogicHandlerTest {
    private GameActivity gameActivity;
    private Client client;

    @Before
    public void before(){
        GameLogicHandler.getInstance().initializeGame();
        gameActivity= Mockito.mock(GameActivity.class);
        client=Mockito.mock(Client.class);
        GameLogicHandler.getInstance().setClient(client);
        GameLogicHandler.getInstance().setGameActivity(gameActivity);
        doNothing().when(GameLogicHandler.getInstance().getGameActivity()).visualize();
        GameLogicHandler.getInstance().addPlayer(new Player("Player1"));
        GameLogicHandler.getInstance().addPlayer(new Player("Player2"));
        when(GameLogicHandler.getInstance().getGameActivity().getPlayer1ID()).thenReturn("Player1");
        when(GameLogicHandler.getInstance().getGameActivity().getPlayer2ID()).thenReturn("Player2");
    }

    @Test
    public void checkInitializeGame(){
        assertTrue(GameLogicHandler.getInstance().getGameData().getLayOffStack().getCardList().isEmpty());
        assertFalse(GameLogicHandler.getInstance().getGameData().getDrawStack().getCardList().isEmpty());
    }

    @Test
    public void checkAddPlayer(){
        assertEquals(2, GameLogicHandler.getInstance().getGameData().getPlayers().size());
    }

    @Test
    public void checkStartRound(){
        try {
            GameLogicHandler.getInstance().startRound();
            assertEquals(10, GameLogicHandler.getInstance().getGameData().getPlayers().get("Player1").getHand().getCardList().size());
        } catch (EmptyCardStackException e) {
            fail();
        }
    }



   @Test
    public void checkDrawCard(){
        try {
            GameLogicHandler.getInstance().drawCard("Player1", StackType.DRAW_STACK);
            assertEquals(1,GameLogicHandler.getInstance().getGameData().getPlayers().get("Player1").getHand().getCardList().size());
        } catch (EmptyCardStackException e) {
            fail();
        }

    }

    @Test
    public void checkLayoffCard(){
        try {
            GameLogicHandler.getInstance().startRound();
            System.out.println("83");
            SpecialCard c=new SpecialCard(120, SpecialCardValue.JOKER);
            GameLogicHandler.getInstance().getGameData().getPlayers().get("Player1").getHand().addCard(c);
            GameLogicHandler.getInstance().layoffCard("Player1",c.getId());
            System.out.println("85");
            assertEquals(c.getId(),GameLogicHandler.getInstance().getGameData().getLayOffStack().getLastCard().getId());


        } catch (EmptyCardStackException e) {
            fail();
        } catch (CardNotFoundException e) {
            fail();
        } catch (PlayerNotFoundException e) {
            fail();
        } catch (EmptyHandException e) {
            fail();
        }
    }



    @Test
    public void addPlayer() {
        Player p=new Player("Player3");
        GameLogicHandler.getInstance().addPlayer(new Player("Player3"));
        assertTrue(GameLogicHandler.getInstance().getGameData().getPlayers().containsKey("Player3"));
    }


    @Test
    public void cheat() {
    }

    @Test
    public void getFirstNumberOfPhaseCards() {
    }

    @Test
    public void sendGameState() {
    }

    @Test
    public void exposeCheat() {
    }

    @Test
    public void layoffPhase() {
    }

    @Test
    public void movePhaseCardsBackToHand() {
    }

    @Test
    public void moveCardsBackToHand() {
    }

    @Test
    public void drawCard() {
    }

    @Test
    public void countCards() {
    }

    @Test
    public void setNewPhaseForPlayer() {
    }


    @Test
    public void checkPhase() {
    }

    @Test
    public void checkNewCardList() {
    }

    @Test
    public void exposePlayer() {
    }

}
