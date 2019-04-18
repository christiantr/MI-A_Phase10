package com.mia.phase10.classes;

import android.graphics.Color;

import com.mia.phase10.exceptionClasses.EmptyCardStackException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EmptyStackException;
import java.util.List;

public class CardStack {

    private List<Card> cardList;

    public CardStack() {
        cardList = new ArrayList<Card>();
    }

    public CardStack(List<Card> cardList) {
        this.cardList = cardList;
    }

    public void generateCardStack(){
        int count = 1;
        Colour colour= null;
        for (int i=0; i<4; i++){
            for (int j=1; j<=12; j++){
                switch (i) {
                    case (1):
                        colour = Colour.BLUE;
                        break;
                    case (2):
                        colour = Colour.GREEN;
                        break;
                    case (3):
                        colour = Colour.RED;
                        break;
                    case (4):
                        colour = Colour.YELLOW;
                        break;
                }
                        cardList.add(new SimpleCard(count, colour, j));
                        count+=1;
                }
            }
        }




    public Card getFirstCard(){
        return cardList.get(0);
    }

    public void mixStack(){
        Collections.shuffle(cardList);
    }

    public void addCard(Card c){
        cardList.add(c);
    }

    public Card drawCard() throws EmptyCardStackException {
        if(this.cardList.isEmpty()) {
            throw new EmptyCardStackException("tried to draw from empty CardStack");
        }
        return cardList.remove(0);
    }

}
