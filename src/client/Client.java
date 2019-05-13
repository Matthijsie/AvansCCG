package client;


import javafx.application.Platform;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client {

    private String host;
    private int port;
    private Socket socket;
    private String name;
    private int playerNumber;
    private DataInputStream in;
    private DataOutputStream out;
    private ClientApplication gui;

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

            this.playerNumber = this.in.readInt();
            System.out.println("you are number " + this.playerNumber);

            String welcomeMessage = this.in.readUTF();
            System.out.println(welcomeMessage);

            this.name = name;
            this.out.writeUTF(this.name);

            //Handling incoming messages
            new Thread ( () -> {
                while ( true ) {
                    try {
                        String message = this.in.readUTF();
                        System.out.println("Recieved Message: " + message);

                        Platform.runLater( () -> {
                            this.gui.messageReceived(message);
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
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
            if (!text.trim().equals("")) {
                this.out.writeUTF(text);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
