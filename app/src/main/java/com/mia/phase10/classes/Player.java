package com.mia.phase10.classes;

import com.mia.phase10.gameLogic.Phase;

import java.util.ArrayList;

public class Player {
    private String id;
    private int points;
    private Hand hand;
    private ArrayList<Card> phaseCards;
    private Phase currentPhase;

    public Player(String id) {
        this.id = id;
        this.hand = new Hand();
        this.points = 0;
        this.phaseCards=new ArrayList<>();
        currentPhase=Phase.PHASE_1;
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

    public void setPhaseCards(ArrayList<Card> phaseCards) {
        this.phaseCards = phaseCards;
    }

    public Phase getCurrentPhase() {
        return currentPhase;
    }

    public void setCurrentPhase(Phase currentPhase) {
        this.currentPhase = currentPhase;
    }
}
