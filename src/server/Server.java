package server;

import client.Client;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

    private int port;
    private Thread serverThread;
    private ServerSocket serverSocket;
    private ArrayList<Thread> sessions;
    private ArrayList<ServerClient> clients;
    private int playerTypeTracker;

    public Server ( int port ) {
        this.port = port;
        this.clients = new ArrayList<>();
        this.sessions = new ArrayList<>();
        this.playerTypeTracker = 1;
    }

    public void sendToAllClients(String text){
        for (ServerClient client : this.clients){
            client.writeUTF(text);
        }
    }

    public void start(){
        try {
            this.serverSocket = new ServerSocket(this.port);

            this.serverThread = new Thread(()->{
                Session session = null;
                while ( true ) {
                    System.out.println("Waiting for clients to connect");

                    try {
                        if (session == null || this.playerTypeTracker == 1){     //creates a new session if the last one was full
                            session = new Session();
                        }

                        Socket socket = this.serverSocket.accept();
                        System.out.println("Client connected from " + socket.getInetAddress().getHostAddress() + ".");

                        ServerClient client = new ServerClient(socket, session, this.playerTypeTracker);

                        this.clients.add(client);
                        if (this.playerTypeTracker == 2){                        //starts the session if there are enough players
                            session.addPlayer2(client);
                            Thread game = new Thread(session);

                            game.start();
                            this.sessions.add(game);
                            System.out.println("Total sessions:" + this.sessions.size());

                        }else {                                                   //adds one player to the session if there arent enough player yet
                            session.addPlayer1(client);
                        }

                        this.playerTypeTracker = (this.playerTypeTracker % 2) + 1;

                        System.out.println("Total clients connected: " + this.clients.size());
                        System.out.println();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        Thread.sleep(100);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Thread.yield();
                }

            });

            this.serverThread.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
