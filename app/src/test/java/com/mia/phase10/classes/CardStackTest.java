package com.mia.phase10.classes;

import com.mia.phase10.exceptionClasses.EmptyCardStackException;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;

public class CardStackTest {
    CardStack stack;
    Card c;

    @Before
    public void before() {
        stack = new CardStack();
        stack.generateCardStack();
        c = new SimpleCard(1, Colour.BLUE, 1, 5);
    }

    @Test
    public void checkGenerate() {
        int size = stack.getCardList().size();

        try {
            assertEquals(c.toString(), stack.drawCard().toString());
            assertEquals(size - 1, stack.getCardList().size());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testAddCard(){
        Card c = new SimpleCard(1, Colour.BLUE, 1, 5);
        stack.addCard(c);
        try {
            assertEquals(c.toString(), stack.drawCard().toString());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testDrawCardIfEmpty(){
        stack.getCardList().clear();
        try {
            stack.drawCard();
            fail();
        }
        catch (EmptyCardStackException em){
        }
    }

    @Test
    public void testGetLastCardReturn(){
        stack.addCard(c);
        assertEquals(c.toString(),stack.getLastCard().toString());
    }
    @Test
    public void testGetLastCard(){
        CardStack temp=new CardStack();
        temp.generateCardStack();

        stack.addCard(c);

        stack.getLastCard();

        assertTrue(stack.getCardList().size()==temp.getCardList().size()+1);
    }

    @Test
    public void testDrwaLastCardReturn(){
        stack.addCard(c);
        assertEquals(c.toString(),stack.drawLastCard().toString());
    }

    @Test
    public void testDrawLastCard(){
        CardStack temp=new CardStack();
        temp.generateCardStack();
        stack.addCard(c);
        stack.drawLastCard();
        assertTrue(stack.getCardList().size()==temp.getCardList().size());
    }

           /*@Test
            public void checkMix(){
                Card c = stack.getFirstCard();
                stack.mixStack();
                assertNotEquals(c,stack.getFirstCard());
            }*/
}
