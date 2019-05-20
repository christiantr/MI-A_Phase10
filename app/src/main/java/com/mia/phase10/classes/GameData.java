package com.mia.phase10.classes;
//this class contains all game data needed for displaying the current status to clients.

import com.mia.phase10.gameFlow.GamePhase;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class GameData {

    private CardStack layOffStack;
    private CardStack drawStack;
    private Map<String,Player> players;
    private String activePlayerId;
    private GamePhase phase;


    public GameData(){
        this.layOffStack = new CardStack();
        this.drawStack = new CardStack();
        this.players = new HashMap<String, Player>();
        this.activePlayerId = "";
    };
    public GameData(CardStack layOffStack, CardStack drawStack, Map<String, Player> players, String activePlayerId) {
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

    public Map<String,Player> getPlayers() {
        return players;
    }

    public void setPlayers(Map<String,Player> players) {
        this.players = players;
    }

    public String getActivePlayerId() {
        return activePlayerId;
    }

    public void setActivePlayerId(String activePlayerId) {
        this.activePlayerId = activePlayerId;
    }

    public void addPlayer(Player player) {
        this.players.put(player.getId(),player);
    }

    public GamePhase getPhase() {
        return phase;
    }

    public void setPhase(GamePhase phase) {
        this.phase = phase;
    }

    public void nextPlayer(){
        Set<String> playerSet = this.players.keySet();
        ArrayList<String> playerList = new ArrayList<String>(playerSet);
        playerList.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });

        int found=-1;
        int index = 0;
        if(activePlayerId.equals("")){
            this.activePlayerId = playerList.get(0);
        }
        else {
            for (String name : playerList) {

                if (name.equals(activePlayerId)) {
                    found = index;
                }
                index++;
            }
            if(found+1<=playerList.size()-1){
                activePlayerId=playerList.get(found+1);
            }
            else{
                activePlayerId = playerList.get(0);
            }
        }
       /* Iterator<Player> iter = this.players.values().iterator();
        int index =0;

        if (activePlayerId ==""){
            this.activePlayerId = this.players.values().iterator().next().getId();
        }else {
            while (iter.hasNext()) {
                if (iter.next().getId() == activePlayerId) {
                    if (this.players.size() - 2 > index) {
                        this.activePlayerId = iter.next().getId();
                    } else {
                        this.activePlayerId = this.players.values().iterator().next().getId();
                    }
                }
            }
        }*/
    }
}
