package com.mia.phase10.classes;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class PlayerTest {

    Player player;
    Hand hand;

    @Before
    public void before(){
        player = new Player("Tester");
        hand = new Hand();
        Map map = new HashMap<Integer, Card>();
        map.put(1,new SimpleCard(1,Colour.BLUE,1));
        map.put(2,new SimpleCard(2,Colour.BLUE,2));
        map.put(3,new SimpleCard(3,Colour.BLUE,3));
        map.put(4,new SimpleCard(4,Colour.BLUE,4));
        map.put(5,new SimpleCard(5,Colour.BLUE,5));
        hand.setCardList(map);
        player.setHand(hand);

    }

    @Test
    public void checkGetId(){
        assertEquals("Tester",player.getId());
    }

    @Test
    public void checkGetHand(){
        assertEquals(hand.getCardList(), player.getHand().getCardList());
    }

    @Test
    public void checkGetSetPoints(){
        player.setPoints(100);
        assertEquals(100, player.getPoints());
    }
}
