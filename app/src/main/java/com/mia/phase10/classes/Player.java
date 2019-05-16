package com.mia.phase10.classes;

import java.util.ArrayList;

public class Player {
    private String id;
    private int points;
    private Hand hand;
    private ArrayList<Card> phaseCards; 

    public Player(String id) {
        this.id = id;
        this.hand = new Hand();
        this.points = 0;
        this.phaseCards=new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public Hand getHand() {
        return hand;
    }

    public void setHand(Hand hand) {
        this.hand = hand;
    }

    public ArrayList<Card> getPhaseCards() {
        return phaseCards;
    }

}
