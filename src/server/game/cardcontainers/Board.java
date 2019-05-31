package server.game.cardcontainers;

import server.game.cards.Minion;

import java.io.Serializable;
import java.util.LinkedList;

public class Board implements Serializable {

    private LinkedList<Minion> minions;

    public Board(LinkedList<Minion> minions){
        this.minions = minions;
    }

    public Board(){
        this(new LinkedList<>());
    }

    public LinkedList<Minion> getMinions(){
        return this.minions;
    }
}
