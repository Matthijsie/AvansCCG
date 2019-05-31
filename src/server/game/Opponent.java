package server.game;

import server.game.cards.Minion;

import java.awt.*;
import java.io.Serializable;
import java.util.List;

public class Opponent implements Serializable {

    private int cardAmountInHand;
    private int cardAmountInGraveYard;
    private int cardAmountInDeck;
    private List<Minion> cardsOnEnemyBoard;
    private int health;
    private int totalMana;
    private int mana;
    private Color color;

    public Opponent(int cardAmountInhand, int cardAmountInGraveYard, int cardAmountInDeck, List<Minion> cardsOnEnemyBoard, int health, int mana, Color color, int totalMana) {
        this.cardAmountInHand = cardAmountInhand;
        this.cardAmountInGraveYard = cardAmountInGraveYard;
        this.cardAmountInDeck = cardAmountInDeck;
        this.cardsOnEnemyBoard = cardsOnEnemyBoard;
        this.health = health;
        this.mana = mana;
        this.color = color;
        this.totalMana = totalMana;
    }

    public int getCardAmountInGraveYard() {
        return cardAmountInGraveYard;
    }

    public int getCardAmountInDeck() {
        return cardAmountInDeck;
    }

    public List<Minion> getCardsOnEnemyBoard() {
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

    public int getTotalMana(){
        return this.totalMana;
    }
}
