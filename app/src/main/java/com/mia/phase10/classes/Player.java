package com.mia.phase10.classes;

import com.mia.phase10.gameLogic.Phase;

import java.io.Serializable;
import java.util.ArrayList;

public class Player implements Serializable {
    private String id;
    private int points;
    private Hand hand;
    private ArrayList<Card> phaseCards;
    private ArrayList<Card> phaseCards2;
    private ArrayList<Card> phaseCardsTemp;
    private ArrayList<Card> phaseCards2Temp;
    private Phase currentPhase;
    private String currentName;
    private boolean phaseAchieved;
    private boolean isExposed;
    private boolean cheated;
    private  boolean cheatUncovered;

    public Player(String id) {
        this.id = id;
        this.hand = new Hand();
        this.points = 0;
        this.phaseCards=new ArrayList<>();
        this.phaseCards2=new ArrayList<>();
        this.phaseCardsTemp=new ArrayList<>();
        this.phaseCards2Temp=new ArrayList<>();
        this.currentPhase=Phase.PHASE_1;
        this.currentName=id;
        this.phaseAchieved=false;
        this.isExposed=false;
        this.cheated = false;
        this.cheatUncovered = false;
    }

    public boolean hasCheated() {
        return cheated;
    }

    public void setCheated(boolean cheated) {
        this.cheated = cheated;
    }

    public boolean isCheatUncovered() {
        return cheatUncovered;
    }

    public void setCheatUncovered(boolean cheatUncovered) {
        this.cheatUncovered = cheatUncovered;
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
        this.points += points;
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

    public ArrayList<Card> getPhaseCards2() {
        return phaseCards2;
    }

    public void setPhaseCards2(ArrayList<Card> phaseCards2) {
        this.phaseCards2 = phaseCards2;
    }

    public Phase getCurrentPhase() {
        return currentPhase;
    }

    public void setCurrentPhase(Phase currentPhase) {
        this.currentPhase = currentPhase;
    }

    public void setCurrentName(String currentName) {
        this.currentName = currentName;
    }

    public String getCurrentName() {
        return currentName;
    }

    public boolean isPhaseAchieved() {
        return phaseAchieved;
    }

    public void setPhaseAchieved(boolean phaseAchieved) {
        this.phaseAchieved = phaseAchieved;
    }

    public ArrayList<Card> getPhaseCardsTemp() {
        return phaseCardsTemp;
    }

    public void setPhaseCardsTemp(ArrayList<Card> phaseCardsTemp) {
        this.phaseCardsTemp = phaseCardsTemp;
    }

    public ArrayList<Card> getPhaseCards2Temp() {
        return phaseCards2Temp;
    }

    public void setPhaseCards2Temp(ArrayList<Card> phaseCards2Temp) {
        this.phaseCards2Temp = phaseCards2Temp;
    }

    public boolean isExposed() {
        return isExposed;
    }

    public void setExposed(boolean exposed) {
        isExposed = exposed;
    }
}
