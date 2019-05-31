package server.game.cardcontainers;

import server.game.cards.Card;
import server.game.cards.Minion;

import java.io.Serializable;
import java.util.LinkedList;

public class Board implements Serializable {

    private LinkedList<Minion> minions;
    private int maxSize;

    public Board(LinkedList<Minion> minions, int maxSize){
        this.minions = minions;
        this.maxSize = maxSize;
    }

    public Board(int maxSize){
        this(new LinkedList<>(), maxSize);
    }

    public LinkedList<Minion> getMinions(){
        return this.minions;
    }

    public int getMaxSize(){
        return this.maxSize;
    }

    public boolean isFull(){
        return this.minions.size() >= this.maxSize;
    }

    public void addMinion(Card card){
        if (card.getClass().equals(Minion.class)){
            Minion minion = (Minion)card;
            this.minions.add(minion);
        }
    }
}
