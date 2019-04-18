package com.mia.phase10.gameLogic;

import android.content.Context;

import com.mia.phase10.classes.Card;
import com.mia.phase10.classes.CardStack;
import com.mia.phase10.classes.GameData;
import com.mia.phase10.classes.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class GameLogicHandler {
    private static volatile GameLogicHandler glhInstance = new GameLogicHandler();
    private GameData gameData;
    private Context context;
    //private constructor.
    private GameLogicHandler(){

    }

    public static GameLogicHandler getInstance() {
        return glhInstance;
    }
    public void setContext(Context context){

    }
    public void initializeGame(Context context){
        CardStack drawStack = new CardStack();
        drawStack.generateCardStack();

        CardStack layOffStack = new CardStack();

        this.gameData = new GameData(layOffStack,drawStack,new HashMap<String,Player>(),"");

    }

    public void addPlayer(Player player){
        this.gameData.addPlayer(player);
    }

    public void startRound(){

        for(Player p : gameData.getPlayers().values()){
            for(int i=0; i<10;i++){
                Card c = this.gameData.getDrawStack().drawCard();
                p.getHand().addCard(c);
            }
        }

    }
    public void layoffCard(String playerId, int cardId){

        Card c = gameData.getPlayers().get(playerId).getHand().removeCard(cardId);
        this.gameData.getLayOffStack().addCard(c);

    }
    public void drawCard(String playerId, StackType stackType){
        switch(stackType){

            case DRAW_STACK:
                Card drawCard = gameData.getDrawStack().drawCard();
                gameData.getPlayers().get(playerId).getHand().addCard(drawCard);
                break;
            case LAYOFF_STACK:
                 Card firstCard = gameData.getLayOffStack().getFirstCard();
                gameData.getPlayers().get(playerId).getHand().addCard(firstCard);

        }
    }

}
