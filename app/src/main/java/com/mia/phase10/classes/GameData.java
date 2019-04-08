package com.mia.phase10.classes;
//this class contains all game data needed for displaying the current status to clients.

import java.util.List;

public class GameData {

    private CardStack layOffStack;
    private CardStack drawStack;
    private List<Player> players;
    private String activePlayerId;

    public GameData(CardStack layOffStack, CardStack drawStack, List<Player> players, String activePlayerId) {
        this.layOffStack = layOffStack;
        this.drawStack = drawStack;
        this.players = players;
        this.activePlayerId = activePlayerId;
    }

    public CardStack getLayOffStack() {
        return layOffStack;
    }

    public void setLayOffStack(CardStack layOffStack) {
        this.layOffStack = layOffStack;
    }

    public CardStack getDrawStack() {
        return drawStack;
    }

    public void setDrawStack(CardStack drawStack) {
        this.drawStack = drawStack;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public String getActivePlayerId() {
        return activePlayerId;
    }

    public void setActivePlayerId(String activePlayerId) {
        this.activePlayerId = activePlayerId;
    }
}
