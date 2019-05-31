package server;

public class ServerApplication {

    public static void main(String[] args) {

        int port = 10000;
        Server server = new Server(port);
        server.start();

        while ( true ) {
            try {
                Thread.sleep(10);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Thread.yield();
        }
    }
}
