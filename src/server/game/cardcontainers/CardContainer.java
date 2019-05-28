package server.game.cardcontainers;

import server.game.cards.Card;

import java.io.Serializable;
import java.util.LinkedList;

public abstract class CardContainer implements Serializable {

    protected LinkedList<Card> cards;

    public CardContainer(){
        this.cards = new LinkedList<>();
    }

    public CardContainer(LinkedList<Card> cards){
        this.cards = cards;
    }

    public LinkedList<Card> getCards(){
        return this.cards;
    }

    public void addCard(Card card){
        this.cards.add(card);
    }
}
