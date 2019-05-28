package server.game;

import server.game.cardcontainers.Deck;
import server.game.cardcontainers.Hand;

import java.awt.*;
import java.io.Serializable;

public class MyPlayer implements Serializable {

    private Hand hand;
    private Deck deck;
    private int health;
    private int mana;
    private Color playerColor;

    public MyPlayer(Deck deck, int health, int mana, Color color){
        this.hand = new Hand(10);
        this.deck = deck;
        this.health = health;
        this.mana = mana;
        this.playerColor = color;
    }

    public Hand getHand(){
        return this.hand;
    }

    public Deck getDeck(){
        return this.deck;
    }

    public int getHealth() {
        return health;
    }

    public int getMana() {
        return mana;
    }

    public int getDeckSize(){
        return this.deck.getCards().size();
    }

    public Color getPlayerColor() {
        return playerColor;
    }
}
