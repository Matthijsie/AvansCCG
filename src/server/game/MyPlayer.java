package server.game;

import server.game.cardcontainers.Deck;
import server.game.cardcontainers.Hand;

import java.io.Serializable;

public class MyPlayer implements Serializable {

    private Hand hand;
    private Deck deck;

    public MyPlayer(Deck deck){
        this.hand = new Hand(10);
        this.deck = deck;
    }

    public Hand getHand(){
        return this.hand;
    }

    public Deck getDeck(){
        return this.deck;
    }
}
