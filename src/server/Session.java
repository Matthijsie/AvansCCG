package server;

import java.util.ArrayList;

public class Session implements Runnable {

    private ServerClient player1, player2;
    private Thread firstPlayer, secondPlayer;

    public Session() {
    }

    @Override
    public void run() {

        while (true) {
            try {

                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Thread.yield();
        }
    }

    public void sendToAllClients(String text) {
        this.player1.writeUTF(text);
        if (this.player2 != null) {
            this.player2.writeUTF(text);
        }
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

}
