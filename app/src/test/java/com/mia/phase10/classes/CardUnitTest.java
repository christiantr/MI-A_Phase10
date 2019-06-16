package com.mia.phase10.classes;

import com.mia.phase10.classes.enums.Colour;
import com.mia.phase10.classes.enums.SpecialCardValue;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CardUnitTest {
    SimpleCard card;
    SpecialCard specialCard;
    @Before
    public void before(){
        card = new SimpleCard(1,Colour.BLUE,1, 5);
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
    public void specialCardSetValue(){
        specialCard.setValue(SpecialCardValue.EXPOSE);
        assertEquals(SpecialCardValue.EXPOSE, specialCard.getValue());
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

    @Test
    public void checkImagePath(){
        card.setImagePath("Path");
        assertEquals("Path",card.getImagePath());
    }


}
