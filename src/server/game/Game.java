package server.game;

import java.io.Serializable;

public class Game implements Serializable {

    private MyPlayer myPlayer;
    private Opponent opponent;

    public Game(MyPlayer myPlayer, Opponent opponent){
        this.myPlayer = myPlayer;
        this.opponent = opponent;
    }

    public MyPlayer getMyPlayer(){
        return this.myPlayer;
    }

    public Opponent getOpponent(){
        return this.opponent;
    }

    public void setOpponent(Opponent opponent){
        this.opponent = opponent;
    }
}
