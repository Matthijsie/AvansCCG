package server.game;

import server.game.cards.Card;

import java.awt.*;
import java.io.Serializable;
import java.util.List;

public class Opponent implements Serializable {

    private int cardAmountInHand;
    private int cardAmountInGraveYard;
    private int cardAmountInDeck;
    private List<Card> cardsOnEnemyBoard;
    private int health;
    private int mana;
    private Color color;

    public Opponent(int cardAmountInhand, int cardAmountInGraveYard, int cardAmountInDeck, List<Card> cardsOnEnemyBoard, int health, int mana, Color color) {
        this.cardAmountInHand = cardAmountInhand;
        this.cardAmountInGraveYard = cardAmountInGraveYard;
        this.cardAmountInDeck = cardAmountInDeck;
        this.cardsOnEnemyBoard = cardsOnEnemyBoard;
        this.health = health;
        this.mana = mana;
        this.color = color;
    }

    public int getCardAmountInGraveYard() {
        return cardAmountInGraveYard;
    }

    public int getCardAmountInDeck() {
        return cardAmountInDeck;
    }

    public List<Card> getCardsOnEnemyBoard() {
        return cardsOnEnemyBoard;
    }

    public int getCardAmountInHand() {
        return cardAmountInHand;
    }

    public int getHealth() {
        return health;
    }

    public int getMana() {
        return mana;
    }

    public Color getColor() {
        return color;
    }
}
