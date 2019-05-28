package server.game.cardcontainers;

import server.game.cards.Card;

import java.io.Serializable;

public class Hand extends CardContainer {

    private int maxSize;

    public Hand(int maxSize){
        super();
        this.maxSize = maxSize;
    }

    public void addCard(Card card){
        if (this.cards.size() + 1 <= this.maxSize){
            this.cards.add(card);
        }
    }
}
