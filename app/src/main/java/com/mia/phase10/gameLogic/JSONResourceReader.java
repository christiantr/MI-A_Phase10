package com.mia.phase10.gameLogic;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mia.phase10.R;
import com.mia.phase10.classes.Card;
import com.mia.phase10.classes.CardStack;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class JSONResourceReader {

    public static CardStack generateCards(Context context){
        InputStream inputStream = context.getResources().openRawResource(R.raw.simplecards);
        Gson gson = new Gson();
        CardStack cardStack = new CardStack();
        InputStreamReader inputreader = new InputStreamReader(inputStream);
        BufferedReader buffreader = new BufferedReader(inputreader);
        String line;
        StringBuilder text = new StringBuilder();

        try {
            while (( line = buffreader.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
        } catch (IOException e) {
            return null;
        }
        ArrayList<Card> cards;
        Type listType = new TypeToken<List<Card>>()
        {
        }.getType();
        cards = gson.fromJson(text.toString(),listType);
        for(Card c : cards){
            cardStack.addCard(c);
        }


        return cardStack;
    }
}
