package com.mia.phase10.classes;

public abstract class Card {
    private int id;

    public Card(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
