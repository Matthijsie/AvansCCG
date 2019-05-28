package server;

import server.game.Game;
import server.game.MyPlayer;
import server.game.Opponent;
import server.game.cardcontainers.Deck;
import server.game.cards.Card;
import server.game.cards.Minion;

import java.awt.*;
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
        //=============setting information players know themselves===========================
        //player 1
        LinkedList<Card> cardsPlayer1 = new LinkedList<>();
        for (int i = 0; i < 30; i++){
            cardsPlayer1.add(new Minion(0,0,0,"Dummy", ""));
        }
        Deck deckPlayer1 = new Deck(cardsPlayer1);
        MyPlayer firstPlayerView = new MyPlayer(deckPlayer1, 30, 0, Color.red);

        //player 2
        LinkedList<Card> cardsPlayer2 = new LinkedList<>();
        for (int i = 0; i < 30; i++){
            cardsPlayer2.add(new Minion(0,0,0,"Dummy", ""));
        }

        Deck deckPlayer2 = new Deck(cardsPlayer2);
        MyPlayer secondPlayerView = new MyPlayer(deckPlayer2, 30, 0, Color.blue);

        //===============setting information players know from one another=====================
        //player 1
        Opponent firstPlayersOpponent = new Opponent(0,
                0,
                secondPlayerView.getDeckSize(),
                new LinkedList<>(),
                secondPlayerView.getHealth(),
                secondPlayerView.getMana(),
                secondPlayerView.getPlayerColor());

        this.player1Game = new Game(firstPlayerView, firstPlayersOpponent);

        //player 2
        Opponent secondPlayersOpponent = new Opponent(0,
                0,
                firstPlayerView.getDeckSize(),
                new LinkedList<>(),
                firstPlayerView.getHealth(),
                firstPlayerView.getMana(),
                firstPlayerView.getPlayerColor());

        this.player2Game = new Game(secondPlayerView, secondPlayersOpponent);



        //=================sending games to clients and starting game============================
        updateAllClientGames();
        this.gameHasStarted = true;
    }

}
