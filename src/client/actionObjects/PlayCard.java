package client.actionObjects;

import server.game.cards.Card;

public class PlayCard {

    private Card card;

    public PlayCard(Card card){
        this.card = card;
    }

    public Card getCard() {
        return card;
    }
}
