package server.game.cardcontainers;

import server.game.cards.Card;

import java.util.LinkedList;

public class Deck extends CardContainer {

    public Deck(LinkedList<Card> cards){
        super(cards);
    }

    public Deck(){
        super();
    }
}
