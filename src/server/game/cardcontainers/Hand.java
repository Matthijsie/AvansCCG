package server.game.cardcontainers;

import server.game.cards.Card;

import java.awt.*;
import java.util.LinkedList;

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

    public LinkedList<Shape> getCardShapesInHand(){
        LinkedList<Shape> shapes = new LinkedList<>();
        for (Card card : this.cards){
            shapes.add(card.getShape());
        }
        return shapes;
    }

}
