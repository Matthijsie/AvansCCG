package server.game.cards;

public class Minion extends Card {

    private int attack;
    private int defense;

    public Minion(int cost, int attack, int defense, String cardName, String cardText) {
        super(cost, cardName, cardText);
        this.attack = attack;
        this.defense = defense;
    }
}
