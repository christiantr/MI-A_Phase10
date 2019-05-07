package com.mia.phase10.classes;

public class SimpleCard extends Card {

    private Colour color;
    private int number;

    public SimpleCard(int id, Colour color, int number) {
        super(id, "imagePath");
        this.color = color;
        this.number = number;

    }

    public SimpleCard(int id, String imagePath, Colour color, int number) {
        super(id, imagePath);
        this.color = color;
        this.number = number;
    }

    public Colour getColor() {
        return color;
    }

    public void setColor(Colour color) {
        this.color = color;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String toString(){
        return super.toString()+"Color: "+this.color + "; Number: "+this.number;
    }
}
