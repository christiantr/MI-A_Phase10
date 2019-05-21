package com.mia.phase10.classes;

public abstract class Card {
    protected int id;
    protected String imagePath;
    protected int countCard;

    public Card(int id, String imagePath, int countCard) {
        this.id = id;
        this.imagePath = imagePath;
        this.countCard = countCard;
    }

    public Card(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getcountCard() {
        return countCard;
    }

    public void setcountCard(int count) {
        this.countCard = count;
    }

    @Override
    public boolean equals(Object o){
         /* Check if o is an instance of Complex or not
          "null instanceof [type]" also returns false */
        if (!(o instanceof Card)) {
            return false;
        }
        return Integer.compare(this.id, ((Card) o).getId())==0;
    }

    public String toString(){
        return "ID: "+this.id+";";
    }
}
