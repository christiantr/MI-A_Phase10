package com.mia.phase10.classes;
//this class contains all game data needed for displaying the current status to clients.

import android.os.Build;
import android.util.Log;

import com.mia.phase10.gameFlow.GamePhase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class GameData implements Serializable {

    private CardStack layOffStack;
    private CardStack drawStack;
    private Map<String, Player> players;
    private String activePlayerId;
    private String previousPlayer;
    private GamePhase phase;
    private boolean roundClosed;
    private final String TAG = "GAMEDATA";
    private boolean gameClosed;

    public GameData() {
        this.layOffStack = new CardStack();
        this.drawStack = new CardStack();
        this.players = new HashMap<String, Player>();
        this.activePlayerId = "";
        this.previousPlayer = "";
    }

    ;

    public GameData(CardStack layOffStack, CardStack drawStack, Map<String, Player> players, String activePlayerId) {
        this.layOffStack = layOffStack;
        this.drawStack = drawStack;
        this.players = players;
        this.activePlayerId = activePlayerId;
    }

    public CardStack getLayOffStack() {
        return layOffStack;
    }

    public String getPreviousPlayer() {
        return previousPlayer;
    }

    public void setPreviousPlayer(String previousPlayer) {
        this.previousPlayer = previousPlayer;
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

    public Map<String, Player> getPlayers() {
        return players;
    }

    public void setPlayers(Map<String, Player> players) {
        this.players = players;
    }

    public String getActivePlayerId() {
        return activePlayerId;
    }

    public void setActivePlayerId(String activePlayerId) {
        this.activePlayerId = activePlayerId;
    }

    public void addPlayer(Player player) {
        this.players.put(player.getId(), player);
    }

    public GamePhase getPhase() {
        return phase;
    }

    public void setPhase(GamePhase phase) {
        this.phase = phase;
    }

    public boolean isRoundClosed() {
        return roundClosed;
    }

    public void setRoundClosed(boolean roundClosed) {
        this.roundClosed = roundClosed;
    }

    public boolean isGameClosed() {
        return gameClosed;
    }

    public void setGameClosed(boolean gameClosed) {
        this.gameClosed = gameClosed;
    }

    public void nextPlayer() {
        Set<String> playerSet = this.players.keySet();
        ArrayList<String> playerList = new ArrayList<String>(playerSet);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            playerList.sort(new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    return o1.compareTo(o2);
                }
            });
        }
        this.previousPlayer = this.activePlayerId;
        int found = -1;
        int index = 0;
        if (activePlayerId.equals("")) {
            this.activePlayerId = playerList.get(0);
        } else {
            do {
                this.getPlayers().get(activePlayerId).setExposed(false);
                found = -1;
                index = 0;
                for (String name : playerList) {

                    if (name.equals(activePlayerId)) {
                        found = index;
                    }
                    index++;
                }
                if (found + 1 <= playerList.size() - 1) {
                    activePlayerId = playerList.get(found + 1);
                } else {
                    activePlayerId = playerList.get(0);
                }
            } while (this.getPlayers().get(activePlayerId).isExposed());
        }
        this.getPlayers().get(activePlayerId).setCheated(false);
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
