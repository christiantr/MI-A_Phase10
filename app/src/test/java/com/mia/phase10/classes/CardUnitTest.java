package com.mia.phase10.classes;

import com.mia.phase10.classes.Colour;
import com.mia.phase10.classes.SimpleCard;
import com.mia.phase10.classes.SpecialCard;
import com.mia.phase10.classes.SpecialCardValue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.junit.Assert.assertEquals;

public class CardUnitTest {
    SimpleCard card;
    SpecialCard specialCard;
    @Before
    public void before(){
        card = new SimpleCard(1,Colour.BLUE,1);
        specialCard = new SpecialCard(90, SpecialCardValue.JOKER);
    }
    @Test
    public void simpleCardGetIdTest() {
            assertEquals(1,card.getId());
    }
    @Test
    public void simpleCardGetColorTest() {
        assertEquals(Colour.BLUE   ,card.getColor());
    }
    @Test
    public void simpleCardGetNumberTest() {
        assertEquals(1,card.getNumber());
    }
    @Test
    public  void specialCardGetValue() {
        assertEquals(SpecialCardValue.JOKER,specialCard.getValue());
    }
    @Test
    public void specialCardGetIdTest() {
        assertEquals(90,specialCard.getId());
    }
    @Test
    public void simpleCardSetIdTest(){
        card.setId(100);
        assertEquals(card.getId(), 100);
    }
    @Test
    public void simpleCardSetColourTest(){
        card.setColor(Colour.RED);
        assertEquals(card.getColor(), Colour.RED);
    }
    @Test
    public void simpleCardSetNumberTest(){
        card.setNumber(10);
        assertEquals(card.getNumber(), 10);
    }


}
