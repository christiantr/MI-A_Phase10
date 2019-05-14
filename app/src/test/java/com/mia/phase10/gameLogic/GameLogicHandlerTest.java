package com.mia.phase10.gameLogic;

import com.mia.phase10.classes.CardStack;
import com.mia.phase10.classes.Player;
import com.mia.phase10.exceptionClasses.CardNotFoundException;
import com.mia.phase10.exceptionClasses.EmptyCardStackException;
import com.mia.phase10.exceptionClasses.EmptyHandException;
import com.mia.phase10.exceptionClasses.PlayerNotFoundException;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class GameLogicHandlerTest {

    @Before
    public void before(){
        GameLogicHandler.getInstance().initializeGame();
    }

    @Test
    public void checkInitializeGame(){
        assertTrue(GameLogicHandler.getInstance().getGameData().getLayOffStack().getCardList().isEmpty());
        assertFalse(GameLogicHandler.getInstance().getGameData().getDrawStack().getCardList().isEmpty());
    }

    @Test
    public void checkAddPlayer(){
        GameLogicHandler.getInstance().addPlayer(new Player("Player1"));
        GameLogicHandler.getInstance().addPlayer(new Player("Player2"));
        assertEquals(2, GameLogicHandler.getInstance().getGameData().getPlayers().size());
    }

    @Test
    public void checkStartRound(){
        GameLogicHandler.getInstance().addPlayer(new Player("Player1"));
        GameLogicHandler.getInstance().addPlayer(new Player("Player2"));
        try {
            GameLogicHandler.getInstance().startRound();
            assertEquals(10, GameLogicHandler.getInstance().getGameData().getPlayers().get("Player1").getHand().getCardList().size());
        } catch (EmptyCardStackException e) {
            fail();
        }
    }

    @Test
    public void checkDrawCard(){
        GameLogicHandler.getInstance().addPlayer(new Player("Player1"));
        GameLogicHandler.getInstance().addPlayer(new Player("Player2"));
        try {
            GameLogicHandler.getInstance().drawCard("Player1", StackType.DRAW_STACK);
            assertEquals(1,GameLogicHandler.getInstance().getGameData().getPlayers().get("Player1").getHand().getCardList().size());
        } catch (EmptyCardStackException e) {
            fail();
        }

    }

    @Test
    public void checkLayoffCard(){
        GameLogicHandler.getInstance().addPlayer(new Player("Player1"));
        GameLogicHandler.getInstance().addPlayer(new Player("Player2"));

        try {
            GameLogicHandler.getInstance().startRound();
            GameLogicHandler.getInstance().layoffCard("Player1",11);
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

}
