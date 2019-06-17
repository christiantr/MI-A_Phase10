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
import com.mia.phase10.exceptionClasses.CardNotFoundException;
import com.mia.phase10.exceptionClasses.EmptyCardStackException;
import com.mia.phase10.exceptionClasses.EmptyHandException;
import com.mia.phase10.exceptionClasses.PlayerNotFoundException;
import com.mia.phase10.gameLogic.enums.GamePhase;
import com.mia.phase10.gameLogic.enums.LayOffCardsPhase;
import com.mia.phase10.gameLogic.enums.Phase;
import com.mia.phase10.gameLogic.enums.PlaystationType;
import com.mia.phase10.gameLogic.enums.StackType;
import com.mia.phase10.network.Client;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;

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
    Card b1, b2, b3, b4, b5, b6, b7,b8, j1, j2, j3, e1, e2;

    //Franziska
    @Before
    public void before() {
        GameLogicHandler.getInstance().initializeGame();
        gameActivity = Mockito.mock(GameActivity.class);
        client = Mockito.mock(Client.class);
        toast=Mockito.mock(Toast.class);
        GameLogicHandler.getInstance().setClient(client);
        gameActivity.setPlayer2ID("Player2");
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
        b7 = new SimpleCard(8, Colour.BLUE, 7, 10);

        j1 = new SpecialCard(13, SpecialCardValue.JOKER);
        j2 = new SpecialCard(14, SpecialCardValue.JOKER);
        j3 = new SpecialCard(15, SpecialCardValue.JOKER);

        e1 = new SpecialCard(16, SpecialCardValue.EXPOSE);
        e2 = new SpecialCard(17, SpecialCardValue.EXPOSE);
        e1.setImagePath("card_expose");
        e2.setImagePath("card_expose");
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

    @Test
    public void exposeCheatFalse(){
        try {
            GameLogicHandler.getInstance().startRound();
        } catch (EmptyCardStackException e) {
            fail();
        }

        GameLogicHandler.getInstance().getGameData().nextPlayer();
        int points = GameLogicHandler.getInstance().getGameData().getPlayers().get(GameLogicHandler.getInstance().getGameData().getActivePlayerId()).getPoints();

        GameLogicHandler.getInstance().exposeCheat();
        assertEquals(false,GameLogicHandler.getInstance().getGameData().getPlayers().get(GameLogicHandler.getInstance().getGameData().getPreviousPlayer()).isCheatUncovered());
        assertEquals(points +5,GameLogicHandler.getInstance().getGameData().getPlayers().get(GameLogicHandler.getInstance().getGameData().getActivePlayerId()).getPoints() );

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
            SpecialCard c = new SpecialCard(120, SpecialCardValue.JOKER);
            GameLogicHandler.getInstance().getGameData().getPlayers().get("Player1").getHand().addCard(c);
            GameLogicHandler.getInstance().layoffCard("Player1", c.getId());
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
    public void checkLayoffCardFinishOneRound() {
        try {
            GameLogicHandler.getInstance().startRound();
            GameLogicHandler.getInstance().getGameData().getPlayers().get("Player1").getHand().getCardList().clear();
            GameLogicHandler.getInstance().getGameData().getPlayers().get("Player1").getHand().addCard(b1);
            GameLogicHandler.getInstance().layoffCard("Player1", b1.getId());
            assertEquals(GamePhase.START_PHASE,GameLogicHandler.getInstance().getGameData().getPhase());

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
    public void checkLayoffCardLastRound() {
        try {
            GameLogicHandler.getInstance().startRound();
            GameLogicHandler.getInstance().getGameData().getPlayers().get("Player1").getHand().getCardList().clear();
            GameLogicHandler.getInstance().getGameData().getPlayers().get("Player1").getHand().addCard(b1);
            GameLogicHandler.getInstance().getGameData().getPlayers().get("Player1").setCurrentPhase(Phase.PHASE_10);
            GameLogicHandler.getInstance().layoffCard("Player1", b1.getId());
            assertEquals(b1.getId(), GameLogicHandler.getInstance().getGameData().getLayOffStack().getLastCard().getId());
            assertTrue(GameLogicHandler.getInstance().getGameData().isGameClosed());

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
    public void testMovePhaseCardsBackToHand() {
        list1.add(b1);
        list1.add(b2);
        list1.add(b1);

        list2.add(b2);
        list2.add(b3);
        list2.add(b2);

        try {
            GameLogicHandler.getInstance().startRound();
        } catch (EmptyCardStackException e) {
            fail();
        }

        GameLogicHandler.getInstance().getGameData().getPlayers().get(GameLogicHandler.getInstance().getGameData().getActivePlayerId()).setPhaseCards(list1);
        GameLogicHandler.getInstance().getGameData().getPlayers().get(GameLogicHandler.getInstance().getGameData().getActivePlayerId()).setPhaseCards2(list2);

        GameLogicHandler.getInstance().movePhaseCardsBackToHand();

        verify(gameActivity, times(1)).setVisibilityOfButtons1();
        assertTrue(GameLogicHandler.getInstance().getGameData().getPlayers().get(GameLogicHandler.getInstance().getGameData().getActivePlayerId()).getPhaseCards().isEmpty());
        assertTrue(GameLogicHandler.getInstance().getGameData().getPlayers().get(GameLogicHandler.getInstance().getGameData().getActivePlayerId()).getPhaseCards2().isEmpty());


    }

    @Test
    public void testMoveCardsBackToHand() {
        list1.add(b1);
        list1.add(b2);
        list1.add(b1);

        list2.add(b2);
        list2.add(b3);
        list2.add(b2);

        try {
            GameLogicHandler.getInstance().startRound();
        } catch (EmptyCardStackException e) {
            fail();
        }

        GameLogicHandler.getInstance().getGameData().getPlayers().get(GameLogicHandler.getInstance().getGameData().getActivePlayerId()).setPhaseCardsTemp(list1);
        GameLogicHandler.getInstance().getGameData().getPlayers().get(GameLogicHandler.getInstance().getGameData().getActivePlayerId()).setPhaseCards2Temp(list2);

        GameLogicHandler.getInstance().moveCardsBackToHand(LayOffCardsPhase.ACTIVE_PHASE);

        verify(gameActivity, times(1)).setVisibilityOfButtons1();
        assertTrue(GameLogicHandler.getInstance().getGameData().getPlayers().get(GameLogicHandler.getInstance().getGameData().getActivePlayerId()).getPhaseCardsTemp().isEmpty());
        assertTrue(GameLogicHandler.getInstance().getGameData().getPlayers().get(GameLogicHandler.getInstance().getGameData().getActivePlayerId()).getPhaseCards2Temp().isEmpty());


    }

    @Test
    public void testDrawCardDrawStack() {

        try {
            GameLogicHandler.getInstance().startRound();
        } catch (EmptyCardStackException e) {
            fail();
        }

        list1.add(e1);
        list1.add(e2);
        CardStack stack=new CardStack(list1);
        GameLogicHandler.getInstance().getGameData().setDrawStack(stack);


        try {
            GameLogicHandler.getInstance().drawCard(GameLogicHandler.getInstance().getGameData().getActivePlayerId(),StackType.DRAW_STACK);
        } catch (EmptyCardStackException e) {
            fail();
        }
        verify(gameActivity, times(1)).visualize();
        assertTrue(GameLogicHandler.getInstance().getGameData().getPlayers().get(GameLogicHandler.getInstance().getGameData().getActivePlayerId()).getHand().getCardList().containsKey(e1.getId()));
        assertEquals(GamePhase.LAYOFF_PHASE,GameLogicHandler.getInstance().getGameData().getPhase());

    }

    @Test
    public void testDrawCardLayOffStack() {
        try {
            GameLogicHandler.getInstance().startRound();
        } catch (EmptyCardStackException e) {
            fail();
        }

        list1.add(b1);
        list1.add(b2);
        CardStack stack=new CardStack(list1);
        GameLogicHandler.getInstance().getGameData().setLayOffStack(stack);

        try {
            GameLogicHandler.getInstance().drawCard(GameLogicHandler.getInstance().getGameData().getActivePlayerId(),StackType.LAYOFF_STACK);
        } catch (EmptyCardStackException e) {
            fail();
        }
        verify(gameActivity, times(1)).visualize();
        assertTrue(GameLogicHandler.getInstance().getGameData().getPlayers().get(GameLogicHandler.getInstance().getGameData().getActivePlayerId()).getHand().getCardList().containsKey(b2.getId()));
        assertEquals(GamePhase.LAYOFF_PHASE,GameLogicHandler.getInstance().getGameData().getPhase());

    }

    @Test
    public void testDrawCardLayOffStackExpose() {

        try {
            GameLogicHandler.getInstance().startRound();
        } catch (EmptyCardStackException e) {
            fail();
        }

        list1.add(e1);
        list1.add(e2);
        CardStack stack=new CardStack(list1);
        GameLogicHandler.getInstance().getGameData().setLayOffStack(stack);

        try {
            GameLogicHandler.getInstance().drawCard(GameLogicHandler.getInstance().getGameData().getActivePlayerId(),StackType.LAYOFF_STACK);
        } catch (EmptyCardStackException e) {
            fail();
        }
        verify(gameActivity, times(1)).visualize();
        assertTrue(GameLogicHandler.getInstance().getGameData().getLayOffStack().getCardList().contains(e2));
        assertEquals(GamePhase.DRAW_PHASE,GameLogicHandler.getInstance().getGameData().getPhase());

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
    public void testLayOffPhaseSimpleCardPlaystation(){
        list1.add(b1);

        try {
            GameLogicHandler.getInstance().startRound();
        } catch (EmptyCardStackException e) {
            fail();
        }
        GameLogicHandler.getInstance().getGameData().getPlayers().get(GameLogicHandler.getInstance().getGameData().getActivePlayerId()).getHand().addCard(b1);
        GameLogicHandler.getInstance().getGameData().getPlayers().get(GameLogicHandler.getInstance().getGameData().getActivePlayerId()).setPhaseAchieved(true);
        try {
            GameLogicHandler.getInstance().layoffPhase(PlaystationType.PLAYSTATION,b1.getId());
        } catch (EmptyHandException e) {
            e.printStackTrace();
        } catch (CardNotFoundException e) {
            e.printStackTrace();
        } catch (PlayerNotFoundException e) {
            e.printStackTrace();
        }
        assertEquals(b1,GameLogicHandler.getInstance().getGameData().getPlayers().get(GameLogicHandler.getInstance().getGameData().getActivePlayerId()).getPhaseCards().get(0));
        assertEquals(b1,GameLogicHandler.getInstance().getGameData().getPlayers().get(GameLogicHandler.getInstance().getGameData().getActivePlayerId()).getPhaseCardsTemp().get(0));
    }

    @Test
    public void testLayOffPhaseSimpleCardPlaystationRight(){
        list1.add(b1);

        try {
            GameLogicHandler.getInstance().startRound();
        } catch (EmptyCardStackException e) {
            fail();
        }
        GameLogicHandler.getInstance().getGameData().getPlayers().get(GameLogicHandler.getInstance().getGameData().getActivePlayerId()).getHand().addCard(b1);
        GameLogicHandler.getInstance().getGameData().getPlayers().get(GameLogicHandler.getInstance().getGameData().getActivePlayerId()).setPhaseAchieved(true);

        try {
            GameLogicHandler.getInstance().layoffPhase(PlaystationType.PLAYSTATION_RIGHT,b1.getId());
        } catch (EmptyHandException e) {
            e.printStackTrace();
        } catch (CardNotFoundException e) {
            e.printStackTrace();
        } catch (PlayerNotFoundException e) {
            e.printStackTrace();
        }
        assertEquals(b1,GameLogicHandler.getInstance().getGameData().getPlayers().get(GameLogicHandler.getInstance().getGameData().getActivePlayerId()).getPhaseCards2().get(0));
        assertEquals(b1,GameLogicHandler.getInstance().getGameData().getPlayers().get(GameLogicHandler.getInstance().getGameData().getActivePlayerId()).getPhaseCards2Temp().get(0));
    }

    @Test
    public void testLayOffPhaseSimpleCardPlaystationTwo(){
        list1.add(b1);

        String next;
        if (GameLogicHandler.getInstance().getGameData().getActivePlayerId().equals(this.gameActivity.getPlayer1ID())) {
            next = this.gameActivity.getPlayer2ID();
        } else {
            next = this.gameActivity.getPlayer1ID();
        }

        try {
            GameLogicHandler.getInstance().startRound();
        } catch (EmptyCardStackException e) {
            fail();
        }
        GameLogicHandler.getInstance().getGameData().getPlayers().get(GameLogicHandler.getInstance().getGameData().getActivePlayerId()).getHand().addCard(b1);
        GameLogicHandler.getInstance().getGameData().getPlayers().get(next).setPhaseAchieved(true);

        try {
            GameLogicHandler.getInstance().layoffPhase(PlaystationType.PLAYSTATION_TWO,b1.getId());
        } catch (EmptyHandException e) {
            e.printStackTrace();
        } catch (CardNotFoundException e) {
            e.printStackTrace();
        } catch (PlayerNotFoundException e) {
            e.printStackTrace();
        }

        assertEquals(b1,GameLogicHandler.getInstance().getGameData().getPlayers().get(next).getPhaseCards().get(0));
        assertEquals(b1,GameLogicHandler.getInstance().getGameData().getPlayers().get(next).getPhaseCardsTemp().get(0));

    }

    @Test
    public void testLayOffPhaseSimpleCardPlaystationTwoRight(){
        list1.add(b1);

        String next;
        if (GameLogicHandler.getInstance().getGameData().getActivePlayerId().equals(this.gameActivity.getPlayer1ID())) {
            next = this.gameActivity.getPlayer2ID();
        } else {
            next = this.gameActivity.getPlayer1ID();
        }

        try {
            GameLogicHandler.getInstance().startRound();
        } catch (EmptyCardStackException e) {
            fail();
        }
        GameLogicHandler.getInstance().getGameData().getPlayers().get(GameLogicHandler.getInstance().getGameData().getActivePlayerId()).getHand().addCard(b1);
        GameLogicHandler.getInstance().getGameData().getPlayers().get(next).setPhaseAchieved(true);

        try {
            GameLogicHandler.getInstance().layoffPhase(PlaystationType.PLAYSTATION_TWO_RIGHT,b1.getId());
        } catch (EmptyHandException e) {
            e.printStackTrace();
        } catch (CardNotFoundException e) {
            e.printStackTrace();
        } catch (PlayerNotFoundException e) {
            e.printStackTrace();
        }

        assertEquals(b1,GameLogicHandler.getInstance().getGameData().getPlayers().get(next).getPhaseCards2().get(0));
        assertEquals(b1,GameLogicHandler.getInstance().getGameData().getPlayers().get(next).getPhaseCards2Temp().get(0));

    }

    @Test
    public void testCheckNewCardListActivePhase() {
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

        list1.add(b1);
        GameLogicHandler.getInstance().getGameData().getPlayers().get(GameLogicHandler.getInstance().getGameData().getActivePlayerId()).setPhaseCardsTemp(list1);

        GameLogicHandler.getInstance().checkNewCardList(LayOffCardsPhase.ACTIVE_PHASE);

        verify(gameActivity, times(1)).setVisibilityOfButtons1();
        assertEquals(list1,GameLogicHandler.getInstance().getGameData().getPlayers().get(GameLogicHandler.getInstance().getGameData().getActivePlayerId()).getPhaseCards());
    }

    @Test
    public void testCheckNewCardListActivePhasePhase2() {
        list1.add(b1);
        list1.add(b1);
        list1.add(b1);

        list2.add(b2);
        list2.add(b3);
        list2.add(b4);
        list2.add(b5);


        try {
            GameLogicHandler.getInstance().startRound();
        } catch (EmptyCardStackException e) {
            fail();
        }

        GameLogicHandler.getInstance().getGameData().getPlayers().get(GameLogicHandler.getInstance().getGameData().getActivePlayerId()).setCurrentPhase(Phase.PHASE_2);
        GameLogicHandler.getInstance().getGameData().getPlayers().get(GameLogicHandler.getInstance().getGameData().getActivePlayerId()).setPhaseCards(list1);
        GameLogicHandler.getInstance().getGameData().getPlayers().get(GameLogicHandler.getInstance().getGameData().getActivePlayerId()).setPhaseCards2(list2);


        list1.add(b1);
        GameLogicHandler.getInstance().getGameData().getPlayers().get(GameLogicHandler.getInstance().getGameData().getActivePlayerId()).setPhaseCardsTemp(list1);

        GameLogicHandler.getInstance().checkNewCardList(LayOffCardsPhase.ACTIVE_PHASE);

        verify(gameActivity, times(1)).setVisibilityOfButtons1();
        assertEquals(list1,GameLogicHandler.getInstance().getGameData().getPlayers().get(GameLogicHandler.getInstance().getGameData().getActivePlayerId()).getPhaseCards());
    }

    @Test
    public void testCheckNewCardListActivePhasePhase4() {
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

        list1.add(b8);
        GameLogicHandler.getInstance().getGameData().getPlayers().get(GameLogicHandler.getInstance().getGameData().getActivePlayerId()).setPhaseCardsTemp(list1);

        GameLogicHandler.getInstance().checkNewCardList(LayOffCardsPhase.ACTIVE_PHASE);

        verify(gameActivity, times(1)).setVisibilityOfButtons1();
        assertEquals(list1,GameLogicHandler.getInstance().getGameData().getPlayers().get(GameLogicHandler.getInstance().getGameData().getActivePlayerId()).getPhaseCards());
    }


    @Test
    public void testCheckNewCardListNextPlayerPhase() {
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

        list1.add(b1);
        GameLogicHandler.getInstance().getGameData().getPlayers().get(gameActivity.getPlayer2ID()).setPhaseCards(list1);

        GameLogicHandler.getInstance().checkNewCardList(LayOffCardsPhase.NEXTPLAYER_PHASE);
        verify(gameActivity, times(1)).setVisibilityOfButtons2();
        assertEquals(list1,GameLogicHandler.getInstance().getGameData().getPlayers().get(gameActivity.getPlayer2ID()).getPhaseCards());

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
        try {
            GameLogicHandler.getInstance().startRound();
        } catch (EmptyCardStackException e) {
            fail();
        }
        GameLogicHandler.getInstance().getGameData().getPlayers().get("Player1").getHand().addCard(e1);
        GameLogicHandler.getInstance().getGameData().getPlayers().get("Player2").getHand().addCard(e1);

        try {
            GameLogicHandler.getInstance().choosePlayerToExpose(e1.getId());
        } catch (CardNotFoundException e) {
            fail();
        } catch (PlayerNotFoundException e) {
            fail();
        } catch (EmptyHandException e) {
            fail();
        }
        verify(gameActivity,times(3)).visualize();
        assertEquals(e1,GameLogicHandler.getInstance().getGameData().getLayOffStack().getLastCard());
}


    //Albin
    @Test
    public void countCards() {
    }

    @Test
    public void setNewPhaseForPlayer() {
    }

}
