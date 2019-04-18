package com.mia.phase10.classes;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.Collections;
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
        return cardList.remove(cardList.size()-1);
    }

    public void mixStack(){
        Collections.shuffle(cardList);
    }

    public void addCard(Card c){
        cardList.add(c);
    }

    public Card drawCard(){

        return cardList.remove(0);
    }

}
