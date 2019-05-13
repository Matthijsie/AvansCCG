package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ServerClient implements Runnable {

    private Socket socket;
    private Session session;
    private DataInputStream in;
    private DataOutputStream out;
    private String name;
    private int playerNumber;

    public ServerClient(Socket socket, Session session, int playerNumber){
        this.socket = socket;
        this.session = session;
        this.playerNumber = playerNumber;
    }

    public void writeUTF(String text){
        try {
            out.writeUTF(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void run() {
        try {
            this.in = new DataInputStream(this.socket.getInputStream());
            this.out = new DataOutputStream(this.socket.getOutputStream());

            this.out.writeInt(this.playerNumber);

            this.out.writeUTF("Welcome to the session! A game will be starting once another player has joined");

            this.name = in.readUTF();
            System.out.println("#### " + this.name + " joined the game!");

            while (true){
                String message = in.readUTF();
                System.out.println("client send message: " + message);
                this.session.sendToAllClients("(" + this.name + ") " + message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
