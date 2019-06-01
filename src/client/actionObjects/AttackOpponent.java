package client.actionObjects;

import server.game.cards.Minion;

import java.io.Serializable;

public class AttackOpponent implements Serializable {

    private Minion attackingMinion;

    public AttackOpponent(Minion attackingMinion){
        this.attackingMinion = attackingMinion;
    }

    public Minion getAttackingMinion() {
        return attackingMinion;
    }
}
