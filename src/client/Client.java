package client;


import javafx.application.Platform;
import server.game.Game;

import java.io.*;
import java.net.Socket;

public class Client {

    private String host;
    private int port;
    private Socket socket;
    private String name;
    private int playerNumber;
    private DataInputStream in;
    private DataOutputStream out;
    private ObjectInputStream inO;
    private ObjectOutputStream outO;
    private ClientApplication gui;
    private transient boolean isRunning;

    public Client(String host, int port, ClientApplication gui){
        this.host = host;
        this.port = port;
        this.gui = gui;
    }

    public void connect(String name){
        try {
            this.socket = new Socket(this.host, this.port);

            this.in = new DataInputStream(this.socket.getInputStream());
            this.out = new DataOutputStream(this.socket.getOutputStream());

            this.inO = new ObjectInputStream(this.socket.getInputStream());
            this.outO = new ObjectOutputStream(this.socket.getOutputStream());

            this.playerNumber = this.in.readInt();
            System.out.println("you are number " + this.playerNumber);

            String welcomeMessage = this.in.readUTF();
            System.out.println(welcomeMessage);

            this.name = name;
            this.out.writeUTF(this.name);

            //Handling incoming game Objects
            new Thread(() -> {
                this.isRunning = true;

                while (isRunning) {
                    try {
                        if (socket.isConnected()) {
                            Object object = this.inO.readObject();

                            if (object.getClass().equals(Game.class)) {
                                this.gui.setGame((Game) object);

                            } else if (object.getClass().equals(String.class)) {
                                String message = (String) object;

                                Platform.runLater(() -> this.gui.messageReceived(message));
                            } else {
                                System.out.println("Error: unknown object received");
                            }

                        } else {
                            break;
                        }

                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                        break;
                    }
                }
            }).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //sends message to the server
    public void writeUTF(String text){
        try {
            this.out.writeUTF(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeObject(Object object){
        try {
            this.outO.writeObject(object);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeSocket(){
        try {
            //this.inputThread.stop();
            this.isRunning = false;
            this.socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isConnected(){
        return this.socket.isConnected();
    }
}
