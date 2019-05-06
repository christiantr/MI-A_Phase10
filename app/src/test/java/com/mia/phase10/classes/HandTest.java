package com.mia.phase10.classes;

import com.mia.phase10.exceptionClasses.CardNotFoundException;
import com.mia.phase10.exceptionClasses.EmptyHandException;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

public class HandTest {

    Hand hand ;
    Map<Integer,Card> map;

    @Before
    public void before(){
     hand = new Hand();
     map = new HashMap<Integer, Card>();
        map.put(1,new SimpleCard(1,Colour.BLUE,1));
        map.put(2,new SimpleCard(2,Colour.BLUE,2));
        map.put(3,new SimpleCard(3,Colour.BLUE,3));
        map.put(4,new SimpleCard(4,Colour.BLUE,4));
        map.put(5,new SimpleCard(5,Colour.BLUE,5));
        hand.setCardList(map);
 }

 @Test
    public void checkAddCard(){
        Card c = new SimpleCard(6,Colour.BLUE,6);
        map.put(c.getId(),c);
        hand.addCard(c);
        assertEquals(map, hand.getCardList());
 }

 @Test
    public void checkSetCardList(){
        assertEquals(map,hand.getCardList());
 }
 @Test
    public void checkRemoveCard(){
        Card c = new SimpleCard(1,Colour.BLUE,1);
        int size = hand.getCardList().size();
     try {
         assertEquals(c,hand.removeCard(1));
         assertEquals(size-1,hand.getCardList().size());
     } catch (EmptyHandException e) {
         fail();
     } catch (CardNotFoundException e) {
         fail();
     }
 }
    @Test(expected = CardNotFoundException.class)
    public void checkRemoveCardNotInHand() throws CardNotFoundException, EmptyHandException {
        Card c = new SimpleCard(1,Colour.BLUE,1);
        int size = hand.getCardList().size();


            hand.removeCard(10);



    }
}
