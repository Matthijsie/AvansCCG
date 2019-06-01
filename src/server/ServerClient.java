package server;

import server.game.Game;

import java.io.*;
import java.net.Socket;

public class ServerClient implements Runnable {

    private Socket socket;
    private Session session;
    private DataInputStream in;
    private DataOutputStream out;
    private ObjectInputStream inO;
    private ObjectOutputStream outO;
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

    public void writeObject(Object object){
        try {

            this.outO.writeObject(object);
            this.outO.reset();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void run() {
        try {
            this.in = new DataInputStream(this.socket.getInputStream());
            this.out = new DataOutputStream(this.socket.getOutputStream());

            this.outO = new ObjectOutputStream(this.socket.getOutputStream());
            this.inO = new ObjectInputStream(this.socket.getInputStream());

            this.out.writeInt(this.playerNumber);

            this.out.writeUTF("Welcome to the session! A game will be starting once another player has joined");

            this.name = in.readUTF();
            System.out.println("#### " + this.name + " joined the server!");

            new Thread(()->{
                while (true) {
                    try {

                        Object object = this.inO.readObject();

                        this.session.objectReceived(object, this);

                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getPlayerNumber(){
        return this.playerNumber;
    }

    public String getName(){
        return this.name;
    }
}
