package com.mia.phase10.classes;

import java.io.Serializable;

public class SpecialCard extends Card implements Serializable {

    private SpecialCardValue value;

    public SpecialCard(int id, SpecialCardValue value) {
        super(id);
        this.value = value;
    }

    public SpecialCard(int id, String imagePath, SpecialCardValue value, int countCard) {
        super(id, imagePath, countCard);
        this.value = value;
    }

    public SpecialCardValue getValue() {
        return value;
    }

    public void setValue(SpecialCardValue value) {
        this.value = value;
    }
    public String toString(){
        return super.toString()+"Value: "+this.value+";";
    }
}

