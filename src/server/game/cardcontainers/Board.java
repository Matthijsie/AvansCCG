package server.game.cardcontainers;

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
}
