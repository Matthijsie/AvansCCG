package server;

import client.actionObjects.AttackMinion;
import client.actionObjects.AttackOpponent;
import client.actionObjects.EndTurn;
import client.actionObjects.PlayCard;
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

        Opponent player1Opponent = new Opponent(
                this.player2Game.getMyPlayer().getHandSize(),
                this.player2Game.getMyPlayer().getDeckSize(),
                this.player2Game.getMyPlayer().getBoard().getMinions(),
                this.player2Game.getMyPlayer().getHealth(),
                this.player2Game.getMyPlayer().getMana(),
                this.player2Game.getMyPlayer().getPlayerColor(),
                this.player2Game.getMyPlayer().getTotalMana());
        this.player1Game.setOpponent(player1Opponent);

        Opponent player2Opponent = new Opponent(
                this.player1Game.getMyPlayer().getHandSize(),
                this.player1Game.getMyPlayer().getDeckSize(),
                this.player1Game.getMyPlayer().getBoard().getMinions(),
                this.player1Game.getMyPlayer().getHealth(),
                this.player1Game.getMyPlayer().getMana(),
                this.player1Game.getMyPlayer().getPlayerColor(),
                this.player1Game.getMyPlayer().getTotalMana());
        this.player2Game.setOpponent(player2Opponent);

        this.player1.writeObject(this.player1Game);
        this.player2.writeObject(this.player2Game);
        System.out.println("send game to all clients");
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
        for (int i = 1; i < 10; i++){
            cardsPlayer1.add(new Minion(i,i,i,"DummyPlayer1", "", false));
            cardsPlayer1.add(new Minion(i, i, i, "SecondDummy", "", false));
        }
        Deck deckPlayer1 = new Deck(cardsPlayer1);
        Collections.shuffle(deckPlayer1.getCards());

        MyPlayer firstPlayerView = new MyPlayer(new Board(7), new Hand(10), deckPlayer1, 30, 1, Color.red, 1, true);
        firstPlayerView.drawFromDeckToHand(4);

        //player 2
        LinkedList<Card> cardsPlayer2 = new LinkedList<>();
        for (int i = 1; i < 10; i++){
            cardsPlayer2.add(new Minion(i,i,i,"DummyPlayer2", "", false));
            cardsPlayer2.add(new Minion(i, i, i, "AnotherDummy", "", false));
        }

        Deck deckPlayer2 = new Deck(cardsPlayer2);
        Collections.shuffle(deckPlayer2.getCards());
        MyPlayer secondPlayerView = new MyPlayer(new Board(7), new Hand(10), deckPlayer2, 30, 0, Color.blue, 0, false);
        secondPlayerView.drawFromDeckToHand(4);

        //===============setting information players know from one another=====================
        //player 1
        Opponent firstPlayersOpponent = new Opponent(
                secondPlayerView.getHandSize(),
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
        int playerNumber = sender.getPlayerNumber();

        if (object.getClass().equals(PlayCard.class)) {             //handles card played
            System.out.println("received playCard Object");
            handleCardPlayed((PlayCard)object, playerNumber);

        }else if (object.getClass().equals(String.class)){          //handles message sent
            String message = (String) object;
            System.out.println("client send message: " + message);
            sendToAllClients("(" + sender.getName() + ") " + message);

        }else if (object.getClass().equals(EndTurn.class)){         //handles end of turn
            System.out.println("received EndTurn Object");
            handleEndTurn(playerNumber);

        }else if (object.getClass().equals(AttackMinion.class)){    //handles minions attack each other
            System.out.println("received AttackMinion Object");
            handleAttackMinion((AttackMinion)object, playerNumber);

        }else if (object.getClass().equals(AttackOpponent.class)){  //handles minion attacking opponent
            System.out.println("received AttackOpponent Object");
            handleAttackOpponent((AttackOpponent)object);

        }else {                                                     //sends error if none of the above were found
            System.out.println("Error: Unknown action Object");
        }
    }

    //todo optimize logic
    private void handleCardPlayed(PlayCard cardPlayed, int playerNumber){
        Card playedCard = cardPlayed.getCard();
        int positionInHand = cardPlayed.getPositionInHand();

        if (playerNumber == 1){
            if (this.player1Game.getMyPlayer().getHand().getCards().contains(playedCard)){
                if (this.player1Game.getMyPlayer().getMana() >= playedCard.getCost() && !this.player1Game.getMyPlayer().getBoard().isFull()){
                    this.player1Game.getMyPlayer().getBoard().addMinion(playedCard);
                    this.player1Game.getMyPlayer().getHand().getCards().remove(positionInHand);
                    this.player1Game.getMyPlayer().subtractMana(playedCard.getCost());
                    updateAllClientGames();
                }
            }
        }else if (playerNumber == 2){
            if (this.player2Game.getMyPlayer().getHand().getCards().contains(playedCard)) {
                if (this.player2Game.getMyPlayer().getMana() >= playedCard.getCost() && !this.player2Game.getMyPlayer().getBoard().isFull()) {
                    this.player2Game.getMyPlayer().getBoard().addMinion(playedCard);
                    this.player2Game.getMyPlayer().getHand().getCards().remove(positionInHand);
                    this.player2Game.getMyPlayer().subtractMana(playedCard.getCost());
                    updateAllClientGames();
                }
            }
        }else {
            System.out.println("Error: Unknown Player number");
        }
    }

    private void handleEndTurn(int playerNumber){
        if (playerNumber == 1){
            if (this.player1Game.getMyPlayer().isMyTurn()){
                this.player1Game.getMyPlayer().setMyTurn(false);
                this.player2Game.getMyPlayer().setMyTurn(true);

                this.player2Game.getMyPlayer().addMana();
                this.player2Game.getMyPlayer().refreshMana();

                this.player2Game.getMyPlayer().drawFromDeckToHand(1);
                updateAllClientGames();
            }
        }else if(playerNumber == 2){
            if (this.player2Game.getMyPlayer().isMyTurn()){
                this.player1Game.getMyPlayer().setMyTurn(true);
                this.player2Game.getMyPlayer().setMyTurn(false);

                this.player1Game.getMyPlayer().addMana();
                this.player1Game.getMyPlayer().refreshMana();

                this.player1Game.getMyPlayer().drawFromDeckToHand(1);
                updateAllClientGames();
            }
        }else {
            System.out.println("Error: Unknown Player number");
        }
    }

    //todo implement method logic
    private void handleAttackMinion(AttackMinion attackMinion, int playerNumber){
        int attackingMinionIndex = attackMinion.getMinionAttackingIndex();
        int attackedMinionIndex = attackMinion.getMinionAttackedIndex();

        if (playerNumber == 1){

            if (this.player1Game.getMyPlayer().getBoard().getMinions().get(attackingMinionIndex) != null){
                Minion attackingMinion = this.player1Game.getMyPlayer().getBoard().getMinions().get(attackingMinionIndex);
                if (this.player2Game.getMyPlayer().getBoard().getMinions().get(attackedMinionIndex) != null){
                    Minion attackedMinion = this.player2Game.getMyPlayer().getBoard().getMinions().get(attackedMinionIndex);

                    attackingMinion.subtractHealth(attackedMinion.getAttack());
                    attackedMinion.subtractHealth(attackingMinion.getAttack());

                    if (attackingMinion.getDefense() <= 0){
                        this.player1Game.getMyPlayer().getBoard().getMinions().remove(attackingMinionIndex);
                    }
                    if (attackedMinion.getDefense() <= 0){
                        this.player2Game.getMyPlayer().getBoard().getMinions().remove(attackedMinionIndex);
                    }
                    updateAllClientGames();
                }
            }

        }else if (playerNumber == 2){

            if (this.player2Game.getMyPlayer().getBoard().getMinions().get(attackingMinionIndex) != null){
                Minion attackingMinion = this.player2Game.getMyPlayer().getBoard().getMinions().get(attackingMinionIndex);
                if (this.player1Game.getMyPlayer().getBoard().getMinions().get(attackedMinionIndex) != null){
                    Minion attackedMinion = this.player1Game.getMyPlayer().getBoard().getMinions().get(attackedMinionIndex);

                    attackingMinion.subtractHealth(attackedMinion.getAttack());
                    attackedMinion.subtractHealth(attackingMinion.getAttack());

                    if (attackingMinion.getDefense() <= 0){
                        this.player2Game.getMyPlayer().getBoard().getMinions().remove(attackingMinionIndex);
                    }
                    if (attackedMinion.getDefense() <= 0){
                        this.player1Game.getMyPlayer().getBoard().getMinions().remove(attackedMinionIndex);
                    }
                    updateAllClientGames();
                }
            }

        }else {
            System.out.println("Error: Unknown player number");
        }
    }

    //todo implement method logic
    private void handleAttackOpponent(AttackOpponent attackOpponent){

    }
}
