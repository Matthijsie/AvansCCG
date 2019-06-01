package client.actionObjects;

import server.game.cards.Card;

import java.io.Serializable;

public class PlayCard implements Serializable {

    private Card card;
    private int positionInHand;

    public PlayCard(Card card, int positionInHand){
        this.card = card;
        this.positionInHand = positionInHand;
    }

    public int getPositionInHand(){
        return this.positionInHand;
    }

    public Card getCard() {
        return card;
    }
}
