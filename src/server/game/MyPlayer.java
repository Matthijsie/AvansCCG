package server.game;

import server.game.cardcontainers.Board;
import server.game.cardcontainers.Deck;
import server.game.cardcontainers.Hand;
import server.game.cards.Card;

import java.awt.*;
import java.io.Serializable;

public class MyPlayer implements Serializable {

    private Board board;
    private Hand hand;
    private Deck deck;
    private int health;
    private int mana;
    private int totalMana;
    private Color playerColor;
    private boolean myTurn;

    public MyPlayer(Board board, Hand hand, Deck deck, int health, int mana, Color color, int totalMana, boolean myTurn) {
        this.board = board;
        this.hand = hand;
        this.deck = deck;
        this.health = health;
        this.mana = mana;
        this.playerColor = color;
        this.totalMana = totalMana;
        this.myTurn = myTurn;
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

    public int getBoardSize(){
        return this.board.getMinions().size();
    }

    public Color getPlayerColor() {
        return this.playerColor;
    }

    public int getTotalMana() {
        return totalMana;
    }

    public boolean isMyTurn(){
        return this.myTurn;
    }

    public Board getBoard(){
        return this.board;
    }

    public void drawFromDeckToHand(int amount) {
        for (int i = 0; i < amount; i++) {
            if (this.deck.getCards().size() > 0) {
                Card drawnCard = this.deck.getCards().getFirst();
                this.deck.getCards().removeFirst();

                if (this.hand.getCards().size() + 1 <= 10) {
                    this.hand.getCards().add(drawnCard);
                }
            }
        }
    }

    @Override
    public String toString() {
        return "MyPlayer{" +
                "board=" + board +
                ", health=" + health +
                ", mana=" + mana +
                ", totalMana=" + totalMana +
                ", myTurn=" + myTurn +
                '}';
    }

    public void setMyTurn(boolean value){
        this.myTurn = value;
    }

    public void addMana(){
        if (this.totalMana < 10){
            this.totalMana++;
        }
    }

    public void subtractMana(int amount){
        this.mana -= amount;
    }

    public void refreshMana(){
        this.mana = this.totalMana;
    }
}
