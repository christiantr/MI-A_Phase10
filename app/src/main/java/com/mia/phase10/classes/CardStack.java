package com.mia.phase10.classes;

import android.graphics.Color;

import com.mia.phase10.R;
import com.mia.phase10.exceptionClasses.EmptyCardStackException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EmptyStackException;
import java.util.List;

public class CardStack {

    private List<Card> cardList;

    public CardStack() {
        cardList = new ArrayList<Card>();
    }

    public CardStack(List<Card> cardList) {
        this.cardList = cardList;
    }

    public void generateCardStack() {
        int count = 1;
        Colour colour = null;
        String imagePath = "";
        for (int i = 1; i <= 4; i++) {
            for (int j = 1; j <= 12; j++) {
                switch (i) {
                    case (1):
                        colour = Colour.BLUE;
                        imagePath = "card_b_" + j;
                        break;
                    case (2):
                        colour = Colour.GREEN;
                        imagePath = "card_g_" + j;
                        break;
                    case (3):
                        colour = Colour.RED;
                        imagePath = "card_r_" + j;
                        break;
                    case (4):
                        colour = Colour.YELLOW;
                        imagePath = "card_y_" + j;
                        break;
                }
                cardList.add(new SimpleCard(count, imagePath, colour, j));
                count += 1;
            }
        }
    }


    public Card getFirstCard() {
        return cardList.get(0);
    }

    public void mixStack() {
        Collections.shuffle(cardList);
    }

    public void addCard(Card c) {
        cardList.add(c);
    }

    public Card drawCard() throws EmptyCardStackException {
        if (this.cardList.isEmpty()) {
            throw new EmptyCardStackException("tried to draw from empty CardStack");
        }
        return cardList.remove(0);
    }

}
