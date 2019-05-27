package server.game;

import server.game.cards.Card;

import java.util.List;

public class Opponent {

    private int cardAmountInhand;
    private int cardAmountInGraveYard;
    private int cardAmountInDeck;
    private List<Card> cardsOnEnemyBoard;

    public Opponent(int cardAmountInhand, int cardAmountInGraveYard, int cardAmountInDeck, List<Card> cardsOnEnemyBoard) {
        this.cardAmountInhand = cardAmountInhand;
        this.cardAmountInGraveYard = cardAmountInGraveYard;
        this.cardAmountInDeck = cardAmountInDeck;
        this.cardsOnEnemyBoard = cardsOnEnemyBoard;
    }

    public int getCardAmountInhand() {
        return cardAmountInhand;
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
}
