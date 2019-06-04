package com.mia.phase10.classes;

import com.mia.phase10.exceptionClasses.EmptyCardStackException;

import java.util.ArrayList;
import java.util.Collections;
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
        int countCard = 0;
        for (int k = 1; k <= 2; k++) {
            for (int i = 1; i <= 4; i++) {
                for (int j = 1; j <= 12; j++) {
                    if (j <= 5) {
                        countCard = 5;
                    } else {
                        countCard = 10;
                    }
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
                    cardList.add(new SimpleCard(count, imagePath, colour, j, countCard));
                    count += 1;
                }
            }
        }

        //generating SpecialCards
        for(int i=0; i<8; i++){
            cardList.add(new SpecialCard(count, "card_joker", SpecialCardValue.JOKER,  20));
            count ++;
        }
        for(int i=0; i<4; i++){
            cardList.add(new SpecialCard(count, "card_expose",SpecialCardValue.EXPOSE, 15));
            count ++;
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

    public Card drawLastCard() {
        Card c = null;

        if (!this.cardList.isEmpty()) {
            c = cardList.remove(cardList.size() - 1);
        }

        return c;
    }

    public Card getLastCard() {
        Card c = null;

        if (!this.cardList.isEmpty()) {
            c = cardList.get(cardList.size() - 1);
        }

        return c;
    }

    public Card drawCard() throws EmptyCardStackException {

        if (this.cardList.isEmpty()) {
            throw new EmptyCardStackException("tried to draw from empty CardStack");
        }
        return cardList.remove(0);
    }

    public List<Card> getCardList() {
        return this.cardList;
    }

}
