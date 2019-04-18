package com.mia.phase10.classes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Hand {
    private Map<Integer,Card> cardList;

    public Hand() {
        cardList = new HashMap<Integer,Card>();
    }

    public void addCard(Card c){
        cardList.put(c.getId(),c);
    }

    public Card removeCard(int cardid){
        //this is currently just for testing purposes
        //TODO implement this method
       return cardList.remove(cardid);
    }

    public Map<Integer, Card> getCardList() {
        return cardList;
    }

    public void setCardList(Map<Integer, Card> cardList) {
        this.cardList = cardList;
    }
}
