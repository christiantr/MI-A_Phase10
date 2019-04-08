package com.mia.phase10.classes;

import java.util.ArrayList;
import java.util.List;

public class Hand {
    private List<Card> cardList;

    public Hand() {
        cardList = new ArrayList<Card>();
    }

    public void addCard(Card c){
        cardList.add(c);
    }

    public Card removeCard(){
        //this is currently just for testing purposes
        //TODO implement this method
       return cardList.remove(0);
    }

    public List<Card> getHand(){
        return cardList;
    }

}
