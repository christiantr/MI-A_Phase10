package com.mia.phase10.gameLogic;

import android.widget.Toast;

import com.mia.phase10.activities.GameActivity;
import com.mia.phase10.classes.Card;
import com.mia.phase10.classes.CardStack;
import com.mia.phase10.classes.Player;
import com.mia.phase10.classes.SimpleCard;
import com.mia.phase10.classes.SpecialCard;
import com.mia.phase10.classes.enums.Colour;
import com.mia.phase10.classes.enums.SpecialCardValue;
import com.mia.phase10.classes.enums.SpecialCardValue;
import com.mia.phase10.exceptionClasses.CardNotFoundException;
import com.mia.phase10.exceptionClasses.EmptyCardStackException;
import com.mia.phase10.exceptionClasses.EmptyHandException;
import com.mia.phase10.exceptionClasses.PlayerNotFoundException;
import com.mia.phase10.gameLogic.enums.Phase;
import com.mia.phase10.gameLogic.enums.StackType;
import com.mia.phase10.network.Client;
import com.mia.phase10.network.transport.TransportObject;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;


import java.util.ArrayList;
import java.util.LinkedList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GameLogicHandlerTest {
    private GameActivity gameActivity;
    private Client client;
    private Toast toast;

    ArrayList list1, list2;
    Card b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, b12, j1, j2, j3, e1;

    //Franziska
    @Before
    public void before() {
        GameLogicHandler.getInstance().initializeGame();
        gameActivity = Mockito.mock(GameActivity.class);
        client = Mockito.mock(Client.class);
        toast=Mockito.mock(Toast.class);
        GameLogicHandler.getInstance().setClient(client);
        GameLogicHandler.getInstance().setGameActivity(gameActivity);
        doNothing().when(GameLogicHandler.getInstance().getGameActivity()).visualize();
        GameLogicHandler.getInstance().addPlayer(new Player("Player1"));
        GameLogicHandler.getInstance().addPlayer(new Player("Player2"));
        when(GameLogicHandler.getInstance().getGameActivity().getPlayer1ID()).thenReturn("Player1");
        when(GameLogicHandler.getInstance().getGameActivity().getPlayer2ID()).thenReturn("Player2");
        list1 = new ArrayList();
        list2 = new ArrayList();

        b1 = new SimpleCard(1, Colour.BLUE, 1, 5);
        b2 = new SimpleCard(2, Colour.BLUE, 2, 5);
        b3 = new SimpleCard(3, Colour.BLUE, 3, 5);
        b4 = new SimpleCard(4, Colour.BLUE, 4, 5);
        b5 = new SimpleCard(5, Colour.BLUE, 5, 5);
        b6 = new SimpleCard(6, Colour.BLUE, 6, 10);
        b7 = new SimpleCard(7, Colour.BLUE, 7, 10);
        b8 = new SimpleCard(8, Colour.BLUE, 8, 10);
        b9 = new SimpleCard(9, Colour.BLUE, 9, 10);
        b10 = new SimpleCard(10, Colour.BLUE, 10, 10);
        b11 = new SimpleCard(11, Colour.BLUE, 11, 10);
        b12 = new SimpleCard(12, Colour.BLUE, 12, 10);

        j1 = new SpecialCard(13, SpecialCardValue.JOKER);
        j2 = new SpecialCard(14, SpecialCardValue.JOKER);
        j3 = new SpecialCard(15, SpecialCardValue.JOKER);

        e1 = new SpecialCard(16, SpecialCardValue.EXPOSE);
    }

    //Alexander
    @Test
    public void checkInitializeGame() {
        assertTrue(GameLogicHandler.getInstance().getGameData().getLayOffStack().getCardList().isEmpty());
        assertFalse(GameLogicHandler.getInstance().getGameData().getDrawStack().getCardList().isEmpty());
    }

    @Test
    public void checkStartRound() {
        try {
            GameLogicHandler.getInstance().startRound();
            assertEquals(10, GameLogicHandler.getInstance().getGameData().getPlayers().get("Player1").getHand().getCardList().size());
        } catch (EmptyCardStackException e) {
            fail();
        }
    }

    @Test
    public void checkDrawCard() {
        try {
            GameLogicHandler.getInstance().drawCard("Player1", StackType.DRAW_STACK);
            assertEquals(1, GameLogicHandler.getInstance().getGameData().getPlayers().get("Player1").getHand().getCardList().size());
        } catch (EmptyCardStackException e) {
            fail();
        }

    }

    @Test
    public void cheat() {
        try {
            GameLogicHandler.getInstance().startRound();
        } catch (EmptyCardStackException e) {
            fail();
        }

        GameLogicHandler.getInstance().cheat();
        assertEquals(true,GameLogicHandler.getInstance().getGameData().getPlayers().get(GameLogicHandler.getInstance().getGameData().getActivePlayerId()).hasCheated());

    }

    @Test
    public void exposeCheatTrue() {
        try {
            GameLogicHandler.getInstance().startRound();
        } catch (EmptyCardStackException e) {
            fail();
        }
        GameLogicHandler.getInstance().cheat();
        int points = GameLogicHandler.getInstance().getGameData().getPlayers().get(GameLogicHandler.getInstance().getGameData().getActivePlayerId()).getPoints();
        GameLogicHandler.getInstance().getGameData().nextPlayer();
        GameLogicHandler.getInstance().exposeCheat();
        assertEquals(true,GameLogicHandler.getInstance().getGameData().getPlayers().get(GameLogicHandler.getInstance().getGameData().getPreviousPlayer()).isCheatUncovered());
        assertEquals(points +5,GameLogicHandler.getInstance().getGameData().getPlayers().get(GameLogicHandler.getInstance().getGameData().getPreviousPlayer()).getPoints() );
        }



    //Franziska
    @Test
    public void checkAddPlayer() {
        assertEquals(2, GameLogicHandler.getInstance().getGameData().getPlayers().size());
    }

    @Test
    public void checkLayoffCard() {
        try {
            GameLogicHandler.getInstance().startRound();
            System.out.println("83");
            SpecialCard c = new SpecialCard(120, SpecialCardValue.JOKER);
            GameLogicHandler.getInstance().getGameData().getPlayers().get("Player1").getHand().addCard(c);
            GameLogicHandler.getInstance().layoffCard("Player1", c.getId());
            System.out.println("85");
            assertEquals(c.getId(), GameLogicHandler.getInstance().getGameData().getLayOffStack().getLastCard().getId());

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
    public void testAddPlayer() {
        Player p = new Player("Player3");
        GameLogicHandler.getInstance().addPlayer(new Player("Player3"));
        assertTrue(GameLogicHandler.getInstance().getGameData().getPlayers().containsKey("Player3"));
    }

    @Test
    public void testGetFirstNumberOfPhaseCards() {
        list1.add(j1);
        list1.add(b2);
        list1.add(b3);
        int cID = GameLogicHandler.getInstance().getFirstNumberOfPhaseCards(list1);
        assertEquals(((SimpleCard) b2).getNumber(), cID);
    }

    @Test
    public void testGetFirstNumberOfPhaseCardsFalse() {
        list1.add(j1);
        list1.add(j2);
        int cID = GameLogicHandler.getInstance().getFirstNumberOfPhaseCards(list1);
        assertEquals(-1, cID);
    }

    @Test
    public void layoffPhase() {
    }

    @Test
    public void testMovePhaseCardsBackToHand() {
    }

    @Test
    public void testMoveCardsBackToHand() {
    }

    @Test
    public void testDrawCard() {
    }

    @Test
    public void testCheckPhaseForPhasesTrue() {
        list1.add(b1);
        list1.add(b2);
        list1.add(b3);
        list1.add(b4);
        list1.add(b5);
        list1.add(b6);
        list1.add(b7);

        try {
            GameLogicHandler.getInstance().startRound();
        } catch (EmptyCardStackException e) {
            fail();
        }
        GameLogicHandler.getInstance().getGameData().getPlayers().get(GameLogicHandler.getInstance().getGameData().getActivePlayerId()).setCurrentPhase(Phase.PHASE_4);
        GameLogicHandler.getInstance().getGameData().getPlayers().get(GameLogicHandler.getInstance().getGameData().getActivePlayerId()).setPhaseCards(list1);
        GameLogicHandler.getInstance().checkPhase();
        verify(gameActivity, times(1)).setVisibilityOfButtons1();
        verify(gameActivity, times(1)).visualize();
        assertTrue(GameLogicHandler.getInstance().getGameData().getPlayers().get(GameLogicHandler.getInstance().getGameData().getActivePlayerId()).isPhaseAchieved());
    }

    @Test
    public void testCheckPhaseForPhasesFalse() {
        list1.add(b1);
        list1.add(b2);
        list1.add(b3);
        list1.add(b4);
        list1.add(b5);
        list1.add(b6);

        try {
            GameLogicHandler.getInstance().startRound();
        } catch (EmptyCardStackException e) {
            fail();
        }
        GameLogicHandler.getInstance().getGameData().getPlayers().get(GameLogicHandler.getInstance().getGameData().getActivePlayerId()).setCurrentPhase(Phase.PHASE_4);
        GameLogicHandler.getInstance().getGameData().getPlayers().get(GameLogicHandler.getInstance().getGameData().getActivePlayerId()).setPhaseCards(list1);
        GameLogicHandler.getInstance().checkPhase();
        verify(gameActivity, times(2)).setVisibilityOfButtons1();
        verify(gameActivity, times(1)).visualize();
        assertFalse(GameLogicHandler.getInstance().getGameData().getPlayers().get(GameLogicHandler.getInstance().getGameData().getActivePlayerId()).isPhaseAchieved());
        assertTrue(GameLogicHandler.getInstance().getGameData().getPlayers().get(GameLogicHandler.getInstance().getGameData().getActivePlayerId()).getPhaseCards().isEmpty());
    }

    @Test
    public void testCheckPhaseForPhasesWithTwoArgumentsTrue() {
        list1.add(b1);
        list1.add(b1);
        list1.add(b1);

        list2.add(b2);
        list2.add(b2);
        list2.add(b2);

        try {
            GameLogicHandler.getInstance().startRound();
        } catch (EmptyCardStackException e) {
            fail();
        }

        GameLogicHandler.getInstance().getGameData().getPlayers().get(GameLogicHandler.getInstance().getGameData().getActivePlayerId()).setPhaseCards(list1);
        GameLogicHandler.getInstance().getGameData().getPlayers().get(GameLogicHandler.getInstance().getGameData().getActivePlayerId()).setPhaseCards2(list2);
        GameLogicHandler.getInstance().checkPhase();
        verify(gameActivity, times(1)).setVisibilityOfButtons1();
        verify(gameActivity, times(1)).visualize();
        assertTrue(GameLogicHandler.getInstance().getGameData().getPlayers().get(GameLogicHandler.getInstance().getGameData().getActivePlayerId()).isPhaseAchieved());
    }

    @Test
    public void testCheckPhaseForPhasesWithTwoArgumentsFalse() {
        list1.add(b1);
        list1.add(b1);
        try {
            GameLogicHandler.getInstance().startRound();
        } catch (EmptyCardStackException e) {
            fail();
        }
        GameLogicHandler.getInstance().getGameData().getPlayers().get(GameLogicHandler.getInstance().getGameData().getActivePlayerId()).setPhaseCards(list1);
        GameLogicHandler.getInstance().checkPhase();
        verify(gameActivity, times(2)).setVisibilityOfButtons1();
        verify(gameActivity, times(1)).visualize();
        assertFalse(GameLogicHandler.getInstance().getGameData().getPlayers().get(GameLogicHandler.getInstance().getGameData().getActivePlayerId()).isPhaseAchieved());
        assertTrue(GameLogicHandler.getInstance().getGameData().getPlayers().get(GameLogicHandler.getInstance().getGameData().getActivePlayerId()).getPhaseCards().isEmpty());
    }

    @Test
    public void testCheckNewCardList() {
    }

    @Test
    public void exposePlayer() {

        try {
            GameLogicHandler.getInstance().startRound();
        } catch (EmptyCardStackException e) {
            fail();
        }
        Player p= GameLogicHandler.getInstance().getGameData().getPlayers().get(GameLogicHandler.getInstance().getGameData().getActivePlayerId());
        GameLogicHandler.getInstance().getGameData().getPlayers().get(GameLogicHandler.getInstance().getGameData().getActivePlayerId()).getHand().addCard(e1);
        try {
            GameLogicHandler.getInstance().exposePlayer(e1.getId());
        } catch (EmptyHandException e) {
            fail();
        }catch (CardNotFoundException e) {
            fail();
        }
        verify(gameActivity,times(3)).visualize();
        verify(gameActivity).visualizeExposingPlayer();
        verify(gameActivity).setListenerForExposingPlayer(e1.getId());
        assertEquals(e1,GameLogicHandler.getInstance().getGameData().getLayOffStack().getLastCard());
    }

    @Test
    public void testChoosePlayerToExpose() {
         }


    //Albin
    @Test
    public void countCards() {
    }

    @Test
    public void setNewPhaseForPlayer() {
    }

}
