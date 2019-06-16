package com.mia.phase10.classes;

import com.mia.phase10.classes.enums.Colour;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class CardStackTest {
    CardStack   stack;
        @Before
    public void before(){
            stack = new CardStack();
            stack.generateCardStack();
        }
    @Test
    public void checkGenerate(){
        Card c = new SimpleCard(1, Colour.BLUE, 1, 5);

            int size = stack.getCardList().size();

            try{
                assertEquals(c.toString(), stack.drawCard().toString());
                assertEquals(size-1, stack.getCardList().size());
            }
            catch (Exception e){
                fail();            }
            }

           /* @Test
            public void checkMix(){
                Card c = stack.getFirstCard();
                stack.mixStack();
                assertNotEquals(c,stack.getFirstCard());
            }*/
}
