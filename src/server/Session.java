package server;

import server.game.Game;
import server.game.MyPlayer;
import server.game.Opponent;
import server.game.cards.Card;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Session implements Runnable {

    private ServerClient player1, player2;
    private Thread firstPlayer, secondPlayer;
    private Game player1Game, player2Game;
    private boolean gameHasStarted;

    public Session() {
        this.gameHasStarted = false;
    }

    @Override
    public void run() {

        while (true) {
            if (this.player2 != null && !this.gameHasStarted){
               setUpGame();
            }


        }
    }

    public void sendToAllClients(Object object){
        this.player1.writeObject(object);
        if (this.player2 != null){
            this.player2.writeObject(object);
        }
    }

    //sends a message to all clients in the session
//    public void sendToAllClients(String text) {
//        this.player1.writeUTF(text);
//        if (this.player2 != null) {
//            this.player2.writeUTF(text);
//        }
//    }

    public void updateAllClientGames(){
        this.player1.writeObject(this.player1Game);
        this.player2.writeObject(this.player2Game);
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
        try {
            if (playerNumber == 1) {
                this.player1Game = game;
            } else if (playerNumber == 2) {
                this.player2Game = game;
            }
        }catch (Exception e){
            System.out.println("Error: could not find player");
        }
    }

    private void setUpGame(){
        //sets the information players know from themselves
        MyPlayer firstPlayerView = new MyPlayer();
        MyPlayer secondPlayerView = new MyPlayer();

        //sets the information the players know from their opponent
        Opponent firstPlayersOpponent = new Opponent(0, 0, 0, new LinkedList<>(), 0, 0);
        Opponent secondPlayersOpponent = new Opponent(0, 0, 0, new LinkedList<>(), 0, 0);

        this.player1Game = new Game(firstPlayerView, firstPlayersOpponent);
        this.player2Game = new Game(secondPlayerView, secondPlayersOpponent);
        updateAllClientGames();

        this.gameHasStarted = true;
    }

}
