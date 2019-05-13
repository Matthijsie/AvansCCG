package client;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

import java.awt.*;
import java.awt.geom.Rectangle2D;


public class ClientApplication extends Application {

    private ResizableCanvas canvas;
    private Client client;
    private VBox chatBox;
    private ScrollPane scrollPane;
    private TextField chatInput;

    public static void main(String[] args) {
        launch(ClientApplication.class);
    }

    @Override
    public void start(Stage stage) throws Exception {
        //============Login screen================
        BorderPane firstPane = new BorderPane();
        VBox loginBox = new VBox();
        TextField loginField = new TextField("Enter a name");
        Button loginButton = new Button("Log in");
        firstPane.setCenter(loginBox);
        loginBox.getChildren().addAll(loginField, loginButton);
        loginBox.setFillWidth(true);

        //============Game screen=================
        //canvas stuff
        BorderPane mainPane = new BorderPane();
        this.canvas = new ResizableCanvas(g -> draw(g), mainPane);
        mainPane.setCenter(this.canvas);

        //chat stuff
        BorderPane chatContainer = new BorderPane();
        this.chatBox = new VBox();
        this.scrollPane = new ScrollPane(this.chatBox);
        this.scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        this.chatBox.setPrefWidth(300);
        this.chatInput = new TextField();

        chatContainer.setCenter(this.scrollPane);
        chatContainer.setBottom(this.chatInput);
        mainPane.setRight(chatContainer);

        FXGraphics2D g2d = new FXGraphics2D(this.canvas.getGraphicsContext2D());
        new AnimationTimer() {
            long last = -1;
            @Override
            public void handle(long now) {
                if(last == -1)
                    last = now;
                update((now - last) / 1000000000.0);
                last = now;
                draw(g2d);
            }
        }.start();

        //============action events================
        this.canvas.setOnMousePressed(e -> onMousePressed(e));
        this.canvas.setOnMouseDragged(e -> onMouseDragged(e));
        this.canvas.setOnMouseReleased(e -> onMouseReleased(e));

        //send message if enter is pressed
        this.chatInput.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER && !this.chatInput.getText().trim().equals("")) {
                this.client.writeUTF(this.chatInput.getText());

                this.chatInput.clear();
            }
        });

        //opens the play screen when the login button is pressed
        loginButton.setOnAction(event -> {
            if (!loginField.getText().trim().equals("")) {
                this.client = new Client("localhost", 10000, this);
                this.client.connect(loginField.getText());
                this.chatInput.setPromptText("Chat as " + loginField.getText());
                stage.setScene(new Scene(mainPane, 720, 720));

            }
        });

        stage.setScene(new Scene(firstPane, 500, 500));
        stage.setTitle("Avans card game");
        stage.show();
        draw(g2d);

    }

    public void draw(FXGraphics2D g2d){
        Rectangle2D screen = new Rectangle2D.Double(0,0, this.canvas.getWidth(), this.canvas.getHeight());
        g2d.setColor(Color.white);
        g2d.fill(screen);
    }

    public void update(double deltaTime){

    }

    private void onMousePressed(MouseEvent e){

    }

    private void onMouseDragged(MouseEvent e){

    }

    private void onMouseReleased(MouseEvent e){

    }

    //adds new label to the chatBox
    public void messageReceived(String message){
        Label label = new Label(message);
        label.setWrapText(true);
        label.setPrefWidth(250);
        label.setPadding(new Insets(1,10,1,10));

        this.chatBox.getChildren().add(label);
        this.client.writeUTF(this.chatInput.getText());
        this.chatInput.clear();
        this.scrollPane.setVvalue(1.0);

    }
}
