package com.mia.phase10.classes;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GameDataTest {

    GameData gameData;

    @Before
    public void before() {
        gameData = new GameData();
    }

    /*@Test
    public void checkAddPlayer() {
        int size = gameData.getPlayers().size();
        gameData.addPlayer(new Player("Player1"));
        assertEquals(size + 1, gameData.getPlayers().size());
    }*/

    @Test
    public void checkActivePlayer() {
        String active = "Player1";
        gameData.setActivePlayerId(active);
        assertEquals(active, gameData.getActivePlayerId());
    }

    @Test
    public void checkDrawStack() {
        CardStack stack = new CardStack();
        stack.generateCardStack();
        gameData.setDrawStack(stack);
        assertEquals(stack.getCardList(), gameData.getDrawStack().getCardList());
    }


    @Test
    public void checkLayoffStack() {
        CardStack stack = new CardStack();
        stack.generateCardStack();
        gameData.setLayOffStack(stack);
        assertEquals(stack.getCardList(), gameData.getLayOffStack().getCardList());
    }

   /* @Test
    public void checkNextPlayer(){
        gameData.addPlayer(new Player("Player1"));
        gameData.addPlayer(new Player("Player2"));

        gameData.nextPlayer();
        gameData.nextPlayer();
        assertEquals("Player1",gameData.getActivePlayerId());
        gameData.nextPlayer();
        assertEquals("Player2",gameData.getActivePlayerId());
        gameData.nextPlayer();
        assertEquals("Player1",gameData.getActivePlayerId());
        gameData.nextPlayer();
        assertEquals("Player2",gameData.getActivePlayerId());

    }*/

}