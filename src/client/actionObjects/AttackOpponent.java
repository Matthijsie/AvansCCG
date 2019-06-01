package client.actionObjects;

import server.game.cards.Minion;

import java.io.Serializable;

public class AttackOpponent implements Serializable {

    private int attackingMinionIndex;

    public AttackOpponent(int attackingMinionIndex){
        this.attackingMinionIndex = attackingMinionIndex;
    }

    public int getAttackingMinionIndex() {
        return attackingMinionIndex;
    }
}
