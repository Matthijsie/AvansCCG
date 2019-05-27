package server.game;

import java.io.Serializable;

public class Game implements Serializable {

    private String name;

    public Game(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }
}
