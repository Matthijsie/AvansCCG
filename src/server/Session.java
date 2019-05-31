package server;

import server.game.Game;
import server.game.MyPlayer;
import server.game.Opponent;
import server.game.cardcontainers.Board;
import server.game.cardcontainers.Deck;
import server.game.cardcontainers.Hand;
import server.game.cards.Card;
import server.game.cards.Minion;

import java.awt.*;
import java.util.Collections;
import java.util.LinkedList;

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
            }else if (this.gameHasStarted){
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void sendToAllClients(Object object){
        this.player1.writeObject(object);
        if (this.player2 != null){
            this.player2.writeObject(object);
        }
    }

    private void updateAllClientGames(){
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

    private void setUpGame(){
        //=============setting information players know themselves===========================
        //player 1
        LinkedList<Card> cardsPlayer1 = new LinkedList<>();
        for (int i = 0; i < 15; i++){
            cardsPlayer1.add(new Minion(0,0,0,"DummyPlayer1", ""));
            cardsPlayer1.add(new Minion(10, 10, 10, "SecondDummy", ""));
        }
        Deck deckPlayer1 = new Deck(cardsPlayer1);
        Collections.shuffle(deckPlayer1.getCards());
        LinkedList<Minion> testBoard = new LinkedList<>();
        testBoard.add(new Minion(0,1,1,"",""));
        testBoard.add(new Minion(0,2,2,"",""));
        testBoard.add(new Minion(0,3,3,"",""));
        testBoard.add(new Minion(0,4,4,"",""));
        testBoard.add(new Minion(0,5,5,"",""));
        testBoard.add(new Minion(0,6,6,"",""));
        testBoard.add(new Minion(0,7,7,"",""));

        MyPlayer firstPlayerView = new MyPlayer(new Board(testBoard, 7), new Hand(10), deckPlayer1, 30, 0, Color.red, 0);
        firstPlayerView.drawFromDeckToHand(3);

        //player 2
        LinkedList<Card> cardsPlayer2 = new LinkedList<>();
        for (int i = 0; i < 15; i++){
            cardsPlayer2.add(new Minion(0,0,0,"DummyPlayer2", ""));
            cardsPlayer2.add(new Minion(10, 10, 10, "AnotherDummy", ""));
        }

        Deck deckPlayer2 = new Deck(cardsPlayer2);
        Collections.shuffle(deckPlayer2.getCards());
        MyPlayer secondPlayerView = new MyPlayer(new Board(7), new Hand(10), deckPlayer2, 30, 0, Color.blue, 0);
        secondPlayerView.drawFromDeckToHand(4);

        //===============setting information players know from one another=====================
        //player 1
        Opponent firstPlayersOpponent = new Opponent(
                secondPlayerView.getHandSize(),
                0,
                secondPlayerView.getDeckSize(),
                secondPlayerView.getBoard().getMinions(),
                secondPlayerView.getHealth(),
                secondPlayerView.getMana(),
                secondPlayerView.getPlayerColor(),
                secondPlayerView.getTotalMana());

        this.player1Game = new Game(firstPlayerView, firstPlayersOpponent);

        //player 2
        Opponent secondPlayersOpponent = new Opponent(
                firstPlayerView.getHandSize(),
                0,
                firstPlayerView.getDeckSize(),
                firstPlayerView.getBoard().getMinions(),
                firstPlayerView.getHealth(),
                firstPlayerView.getMana(),
                firstPlayerView.getPlayerColor(),
                firstPlayerView.getTotalMana());

        this.player2Game = new Game(secondPlayerView, secondPlayersOpponent);



        //=================sending games to clients and starting game============================
        updateAllClientGames();
        this.gameHasStarted = true;
    }

    public void objectReceived(Object object, ServerClient sender){
        if (object.getClass().equals(Game.class)) {
            Game game = (Game) object;

            updateAllClientGames();

        }else if (object.getClass().equals(String.class)){
            String message = (String) object;
            System.out.println("client send message: " + message);
            sendToAllClients("(" + sender.getName() + ") " + message);
        }
    }
}
