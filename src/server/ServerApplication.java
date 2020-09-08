package server;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class ServerApplication extends Application {

    private final int PORT = 10000;

    public static void main(String[] args) {
        launch(ServerApplication.class);
    }

    @Override
    public void start(Stage stage) {
        Server server = new Server(PORT);
        server.start();

        BorderPane mainPane = new BorderPane();

        stage.setScene(new Scene(mainPane));
        stage.setTitle("AvansCCG Server");
        stage.show();
    }
}
