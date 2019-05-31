package client.actionObjects;

import server.game.cards.Minion;

import java.io.Serializable;

public class AttackMinion implements Serializable {

    private Minion minionAttacked;
    private Minion minionAttacking;

    public AttackMinion(Minion minionAttacked, Minion minionAttacking){
        this.minionAttacked = minionAttacked;
        this.minionAttacking = minionAttacking;
    }

    public Minion getMinionAttacked() {
        return minionAttacked;
    }

    public Minion getMinionAttacking() {
        return minionAttacking;
    }
}
