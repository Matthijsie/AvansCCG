package server;

import server.game.Game;

import java.util.ArrayList;

public class Session implements Runnable {

    private ServerClient player1, player2;
    private Thread firstPlayer, secondPlayer;
    private Game player1Game, player2Game;

    public Session() {
    }

    @Override
    public void run() {

        while (true) {
            if (this.player2 != null){

            }
        }
    }

    //sends a message to all clients in the session
    public void sendToAllClients(String text) {
        this.player1.writeUTF(text);
        if (this.player2 != null) {
            this.player2.writeUTF(text);
        }
    }

    public void updateAllClientGames(Game game){
        this.player1.writeObject(game);
        this.player2.writeObject(game);
    }

    public void addPlayer1(ServerClient player1){
        this.player1 = player1;
        this.firstPlayer = new Thread(this.player1);
        this.firstPlayer.start();
    }

    public void addPlayer2(ServerClient player2){
        this.player2 = player2;
        this.secondPlayer = new Thread(this.player2);
        this.secondPlayer.start();
    }

    public void setPlayerGames(Game game, int playerNumber){
        if (playerNumber == 1){
            this.player1Game = game;
        }else if(playerNumber == 2){
            this.player2Game = game;
        }else {
            System.out.println("Error: could not find player");
        }
    }

}
