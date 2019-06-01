package client.actionObjects;

import server.game.cards.Minion;

import java.io.Serializable;

public class AttackMinion implements Serializable {

    private int minionAttackingIndex;
    private int minionAttackedIndex;

    public AttackMinion(int minionAttackedIndex, int minionAttackingIndex){
        this.minionAttackedIndex = minionAttackedIndex;
        this.minionAttackingIndex = minionAttackingIndex;
    }

    public int getMinionAttackingIndex() {
        return minionAttackingIndex;
    }

    public int getMinionAttackedIndex() {
        return minionAttackedIndex;
    }
}
