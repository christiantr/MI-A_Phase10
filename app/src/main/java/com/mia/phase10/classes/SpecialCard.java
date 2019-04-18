package com.mia.phase10.classes;

public class SpecialCard extends Card {

    private SpecialCardValue value;

    public SpecialCard(int id) {
        super(id);
    }

    public SpecialCard(int id, SpecialCardValue value) {
        super(id);
        this.value = value;
    }

    public SpecialCard(int id, String imagePath, SpecialCardValue value) {
        super(id, imagePath);
        this.value = value;
    }

    public SpecialCardValue getValue() {
        return value;
    }

    public void setValue(SpecialCardValue value) {
        this.value = value;
    }
}
