package server.game.cards;

import java.io.Serializable;

public abstract class Card implements Serializable {

    protected int cost;
    protected String cardName;
    protected String cardText;

    public Card(int cost, String cardName, String cardText){
        this.cost = cost;
        this.cardName = cardName;
        this.cardText = cardText;
    }

    public int getCost() {
        return cost;
    }

    public String getCardName() {
        return cardName;
    }

    public String getCardText() {
        return cardText;
    }
}
