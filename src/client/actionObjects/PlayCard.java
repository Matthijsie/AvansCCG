package client.actionObjects;

import server.game.cards.Card;

import java.io.Serializable;

public class PlayCard implements Serializable {

    private Card card;

    public PlayCard(Card card){
        this.card = card;
    }

    public Card getCard() {
        return card;
    }
}
