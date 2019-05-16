package com.mia.phase10;

import com.mia.phase10.classes.Card;
import com.mia.phase10.classes.Colour;
import com.mia.phase10.classes.SimpleCard;
import com.mia.phase10.classes.SpecialCard;
import com.mia.phase10.classes.SpecialCardValue;
import com.mia.phase10.gameLogic.CardEvaluator;
import com.mia.phase10.gameLogic.Phase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

public class CardEvaluatorTest {
    CardEvaluator evaluator;
    List list1,list2;
    Card b1,b2,b3,b4,b5,b6,b7,b8,b9,b10,b11,b12,j1,j2,j3;


    @Before
    public void setUp() {
        evaluator = evaluator.getInstance();
        list1= new LinkedList();
        list2= new LinkedList();

        b1=new SimpleCard(1, Colour.BLUE,1);
        b2=new SimpleCard(2, Colour.BLUE,2);
        b3=new SimpleCard(3, Colour.BLUE,3);
        b4=new SimpleCard(4, Colour.BLUE,4);
        b5=new SimpleCard(5, Colour.BLUE,5);
        b6=new SimpleCard(6, Colour.BLUE,6);
        b7=new SimpleCard(7, Colour.BLUE,7);
        b8=new SimpleCard(8, Colour.BLUE,8);
        b9=new SimpleCard(9, Colour.BLUE,9);
        b10=new SimpleCard(10, Colour.BLUE,10);
        b11=new SimpleCard(11, Colour.BLUE,11);
        b12=new SimpleCard(12, Colour.BLUE,12);

        j1=new SpecialCard(1, SpecialCardValue.JOKER);
        j2=new SpecialCard(2, SpecialCardValue.JOKER);
        j3=new SpecialCard(3, SpecialCardValue.JOKER);

    }

    @After
    public void tearDown() {
        evaluator = null;
    }
    @Test
    public void checkPhaseMissingArgumentFalse() {
        list1.add(b1);
        list1.add(b1);
        list1.add(b1);

        list2.add(b2);
        list2.add(b2);
        list2.add(b2);
        assertFalse(evaluator.checkPhase(Phase.PHASE_1,list1));
    }

    @Test
    public void checkPhaseTooMuchArgumentsFalse() {
        list1.add(b1);
        list1.add(b2);
        list1.add(b3);
        list1.add(b4);
        list1.add(b5);
        list1.add(b6);
        list1.add(b7);
        assertFalse(evaluator.checkPhase(Phase.PHASE_4,list1,list2));
    }

    @Test
    public void checkPhase1True() {
        list1.add(b1);
        list1.add(b1);
        list1.add(b1);

        list2.add(b2);
        list2.add(b2);
        list2.add(b2);
        assertTrue(evaluator.checkPhase(Phase.PHASE_1,list1,list2));
    }

    @Test
    public void checkPhase1False() {
        assertFalse(evaluator.checkPhase(Phase.PHASE_1,list1,list2));
    }

    @Test
    public void checkPhase2TrueLeftExpression() {
        list1.add(b1);
        list1.add(b1);
        list1.add(b1);

        list2.add(b2);
        list2.add(b3);
        list2.add(b4);
        list2.add(b5);
        assertTrue(evaluator.checkPhase(Phase.PHASE_2,list1,list2));
    }

    @Test
    public void checkPhase2TrueRightExpression() {
        list1.add(b1);
        list1.add(b1);
        list1.add(b1);

        list2.add(b2);
        list2.add(b3);
        list2.add(b4);
        list2.add(b5);

        assertTrue(evaluator.checkPhase(Phase.PHASE_2,list2,list1));
    }

    @Test
    public void checkPhase2False() {
        assertFalse(evaluator.checkPhase(Phase.PHASE_2,list2,list1));
    }

    @Test
    public void checkPhase3TrueLeftExpression() {
        list1.add(b1);
        list1.add(b1);
        list1.add(b1);
        list1.add(b1);

        list2.add(b2);
        list2.add(b3);
        list2.add(b4);
        list2.add(b5);

        assertTrue(evaluator.checkPhase(Phase.PHASE_3,list1,list2));
    }

    @Test
    public void checkPhase3RightExpression() {
        list1.add(b1);
        list1.add(b1);
        list1.add(b1);
        list1.add(b1);

        list2.add(b2);
        list2.add(b3);
        list2.add(b4);
        list2.add(b5);

        assertTrue(evaluator.checkPhase(Phase.PHASE_3,list2,list1));
    }
    @Test
    public void checkPhase3False() {
        assertFalse(evaluator.checkPhase(Phase.PHASE_3,list2,list1));
    }

    @Test
    public void checkPhase4True() {
        list1.add(b1);
        list1.add(b2);
        list1.add(b3);
        list1.add(b4);
        list1.add(b5);
        list1.add(b6);
        list1.add(b7);
        assertTrue(evaluator.checkPhase(Phase.PHASE_4,list1));
    }

    @Test
    public void checkPhase4False() {
        assertFalse(evaluator.checkPhase(Phase.PHASE_4,list1));
    }

    @Test
    public void checkPhase5True() {
        list1.add(b1);
        list1.add(b2);
        list1.add(b3);
        list1.add(b4);
        list1.add(b5);
        list1.add(b6);
        list1.add(b7);
        list1.add(b8);
        assertTrue(evaluator.checkPhase(Phase.PHASE_5,list1));
    }

    @Test
    public void checkPhase5False() {
        assertFalse(evaluator.checkPhase(Phase.PHASE_5,list1));
    }

    @Test
    public void checkPhase6True() {
        list1.add(b1);
        list1.add(b2);
        list1.add(b3);
        list1.add(b4);
        list1.add(b5);
        list1.add(b6);
        list1.add(b7);
        list1.add(b8);
        list1.add(b9);
        assertTrue(evaluator.checkPhase(Phase.PHASE_6,list1));
    }

    @Test
    public void checkPhase6False() {
        assertFalse(evaluator.checkPhase(Phase.PHASE_6,list1));
    }

    @Test
    public void checkPhase7True() {
        list1.add(b1);
        list1.add(b1);
        list1.add(b1);
        list1.add(b1);

        list2.add(b2);
        list2.add(b2);
        list2.add(b2);
        list2.add(b2);
        assertTrue(evaluator.checkPhase(Phase.PHASE_7,list1,list2));

    }

    @Test
    public void checkPhase7False() {
        assertFalse(evaluator.checkPhase(Phase.PHASE_7,list1));
    }

    @Test
    public void checkPhase8OnlySimpleCardsTrue() {
        list1.add(b1);
        list1.add(b12);
        list1.add(b11);
        list1.add(b5);
        list1.add(b4);
        list1.add(b6);
        list1.add(b3);

        assertTrue(evaluator.checkPhase(Phase.PHASE_8,list1));
    }

    @Test
    public void checkPhase8WithJokerTrue() {
        list1.add(b1);
        list1.add(b12);
        list1.add(j1);
        list1.add(b5);
        list1.add(b4);
        list1.add(j2);
        list1.add(b3);

        assertTrue(evaluator.checkPhase(Phase.PHASE_8,list1));
    }

    @Test
    public void checkPhase8ColorFalse() {
        list1.add(b1);
        list1.add(b12);
        list1.add(j1);
        list1.add(b5);
        list1.add(b4);
        list1.add(new SimpleCard(13,Colour.RED,3));
        list1.add(b3);

        assertFalse(evaluator.checkPhase(Phase.PHASE_8,list1));
    }

    @Test
    public void checkPhase8CardAmountFalse() {
        list1.add(b1);
        list1.add(b12);
        list1.add(j1);
        list1.add(b5);
        list1.add(b4);

        assertFalse(evaluator.checkPhase(Phase.PHASE_8,list1));
    }

    @Test
    public void checkPhase9TrueLeftExpression() {
        list1.add(b1);
        list1.add(b1);
        list1.add(b1);
        list1.add(b1);
        list1.add(b1);

        list2.add(b2);
        list2.add(b2);

        assertTrue(evaluator.checkPhase(Phase.PHASE_9,list1,list2));
    }

    @Test
    public void checkPhase9TrueRightExpression() {
        list1.add(b1);
        list1.add(b1);
        list1.add(b1);
        list1.add(b1);
        list1.add(b1);

        list2.add(b2);
        list2.add(b2);

        assertTrue(evaluator.checkPhase(Phase.PHASE_9,list2,list1));
    }

    @Test
    public void checkPhase9False() {
        assertFalse(evaluator.checkPhase(Phase.PHASE_9,list1,list2));
    }

    @Test
    public void checkPhase10TrueLeftExpression() {
        list1.add(b1);
        list1.add(b1);
        list1.add(b1);
        list1.add(b1);
        list1.add(b1);

        list2.add(b2);
        list2.add(b2);
        list2.add(b2);

        assertTrue(evaluator.checkPhase(Phase.PHASE_10,list1,list2));
    }

    @Test
    public void checkPhase10TrueRightExpression() {
        list1.add(b1);
        list1.add(b1);
        list1.add(b1);
        list1.add(b1);
        list1.add(b1);

        list2.add(b2);
        list2.add(b2);
        list2.add(b2);

        assertTrue(evaluator.checkPhase(Phase.PHASE_10,list2,list1));
    }

    @Test
    public void checkPhase10False() {
        assertFalse(evaluator.checkPhase(Phase.PHASE_10,list1,list2));
    }

    @Test
    public void testCheckIfInARowOnlySimpleCardsTrue(){
        list1.add(b4);
        list1.add(b5);
        list1.add(b6);
        list1.add(b7);
        list1.add(b8);
        list1.add(b9);
        list1.add(b10);
        assertTrue(evaluator.checkIfInARow(list1));
    }

    @Test
    public void testCheckIfInARowOnlySimpleCardsFalse(){
        list1.add(b1);
        list1.add(b2);
        list1.add(b2);
        assertFalse(evaluator.checkIfInARow(list1));
    }

    @Test
    public void testCheckIfInARowWithJokerInBetween(){
        list1.add(b1);
        list1.add(j1);
        list1.add(b3);
        assertTrue(evaluator.checkIfInARow(list1));
    }

    @Test
    public void testCheckIfInARowWithJokerAtBeginning(){
        list1.add(j1);
        list1.add(j2);
        list1.add(b3);
        list1.add(b4);
        assertTrue(evaluator.checkIfInARow(list1));
    }

    @Test
    public void testCheckIfInARowWithJokers(){
        list1.add(j1);
        list1.add(j2);
        list1.add(b3);
        list1.add(b4);
        list1.add(j3);
        list1.add(b6);

        assertTrue(evaluator.checkIfInARow(list1));
    }

    @Test
    public void testCheckIfInARowWithJokersFalse(){
        list1.add(j1);
        list1.add(j2);
        list1.add(b3);
        list1.add(b4);
        list1.add(j3);
        list1.add(b5);

        assertFalse(evaluator.checkIfInARow(list1));
    }
    @Test
    public void testCheckForEqualNumbersOnlySimpleCardsTrue(){
        list1.add(b1);
        list1.add(b1);
        list1.add(b1);

        assertTrue(evaluator.checkForEqualNumbers(list1));
    }


    @Test
    public void testCheckForEqualNumbersOnlySimpleCardsFalse(){
        list1.add(b1);
        list1.add(b1);
        list1.add(b2);

        assertFalse(evaluator.checkForEqualNumbers(list1));
    }


    @Test
    public void testCheckForEqualNumbersWithJoker(){
        list1.add(j1);
        list1.add(b3);
        list1.add(b3);
        list1.add(j3);

        assertTrue(evaluator.checkForEqualNumbers(list1));
    }
}