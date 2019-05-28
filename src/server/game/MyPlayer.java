package server.game;

import server.game.cardcontainers.Deck;
import server.game.cardcontainers.Hand;
import server.game.cards.Card;

import java.awt.*;
import java.io.Serializable;

public class MyPlayer implements Serializable {

    private Hand hand;
    private Deck deck;
    private int health;
    private int mana;
    private Color playerColor;

    public MyPlayer(Deck deck, int health, int mana, Color color) {
        this.hand = new Hand(10);
        this.deck = deck;
        this.health = health;
        this.mana = mana;
        this.playerColor = color;
    }

    public Hand getHand() {
        return this.hand;
    }

    public Deck getDeck() {
        return this.deck;
    }

    public int getHealth() {
        return this.health;
    }

    public int getMana() {
        return this.mana;
    }

    public int getDeckSize() {
        return this.deck.getCards().size();
    }

    public int getHandSize() {
        return this.hand.getCards().size();
    }

    public Color getPlayerColor() {
        return this.playerColor;
    }

    public void drawFromDeckToHand(int amount) {
        for (int i = 0; i < amount; i++) {
            Card drawnCard = this.deck.getCards().getFirst();
            this.deck.getCards().removeFirst();

            if (this.hand.getCards().size() + 1 <= 10) {
                this.hand.getCards().add(drawnCard);
            }
        }
    }
}
