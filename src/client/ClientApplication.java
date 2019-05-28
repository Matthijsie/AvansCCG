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
import server.game.Game;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;


public class ClientApplication extends Application {

    private ResizableCanvas canvas;
    private Client client;
    private VBox chatBox;
    private ScrollPane scrollPane;
    private TextField chatInput;
    private Game game;
    private Rectangle2D endTurnButton;
    private double previousCanvasWidth, previousCanvasHeigth;

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
        this.endTurnButton = new Rectangle2D.Double(canvas.getWidth()*0.95, canvas.getHeight()*0.45, canvas.getWidth()*0.05, canvas.getHeight()*0.1);

        previousCanvasWidth = canvas.getWidth();
        previousCanvasHeigth = canvas.getHeight();
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
                this.client.writeObject(this.chatInput.getText());
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

        g2d.setColor(Color.black);
        g2d.draw(this.endTurnButton);

        if (this.game != null){
            drawPlayerPortraits(g2d);
            drawDecks(g2d);
        }
    }

    public void update(double deltaTime){
        if (this.previousCanvasWidth != this.canvas.getWidth() || this.previousCanvasHeigth != this.canvas.getHeight()){
            this.previousCanvasHeigth = this.canvas.getHeight();
            this.previousCanvasWidth = this.canvas.getWidth();

            this.endTurnButton = new Rectangle2D.Double(canvas.getWidth()*0.90, canvas.getHeight()*0.47, canvas.getWidth()*0.10, canvas.getHeight()*0.06);
        }
    }

    private void onMousePressed(MouseEvent e){
        Point2D mousePosition = new Point2D.Double(e.getX(), e.getY());

        if(this.endTurnButton.contains(mousePosition)){
            System.out.println("pressed end turn button");
        }
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
//        this.client.writeStringObject(this.chatInput.getText());
        this.chatInput.clear();
        this.scrollPane.setVvalue(1.0);

    }

    public void setGame(Game game){
        this.game = game;
    }

    private void drawPlayerPortraits(FXGraphics2D g2d){
        Rectangle2D myPlayerPortrait = new Rectangle2D.Double(this.canvas.getWidth()*0.45,
                this.canvas.getHeight()*0.8,
                this.canvas.getWidth()*0.1,
                this.canvas.getHeight()*0.2);

        g2d.setColor(this.game.getMyPlayer().getPlayerColor());
        g2d.fill(myPlayerPortrait);

        Rectangle2D opponentPortrait = new Rectangle2D.Double(this.canvas.getWidth()*0.45,
                0,
                this.canvas.getWidth()*0.1,
                this.canvas.getHeight()*0.2);

        g2d.setColor(this.game.getOpponent().getColor());
        g2d.fill(opponentPortrait);

        g2d.setColor(Color.black);
        g2d.drawString(String.valueOf(this.game.getMyPlayer().getHealth()), (int)myPlayerPortrait.getCenterX(), (int)myPlayerPortrait.getCenterY());
        g2d.draw(myPlayerPortrait);

        g2d.drawString(String.valueOf(this.game.getOpponent().getHealth()), (int)opponentPortrait.getCenterX(), (int)opponentPortrait.getCenterY());
        g2d.draw(opponentPortrait);
    }

    private void drawDecks(FXGraphics2D g2d){
        Rectangle2D myDeck = new Rectangle2D.Double(this.canvas.getWidth()*0.8,
                this.canvas.getHeight()*0.8,
                this.canvas.getWidth()*0.1,
                this.canvas.getHeight()*0.1);

        g2d.setColor(Color.lightGray);
        g2d.fill(myDeck);

        Rectangle2D opponentDeck = new Rectangle2D.Double(this.canvas.getWidth()*0.8,
                this.canvas.getHeight()*0.1,
                this.canvas.getWidth()*0.1,
                this.canvas.getHeight()*0.1);

        g2d.fill(opponentDeck);

        g2d.setColor(Color.black);
        g2d.draw(myDeck);
        g2d.draw(opponentDeck);

        g2d.drawString(String.valueOf(this.game.getMyPlayer().getDeck().getCards().size()), (int)myDeck.getCenterX(), (int)myDeck.getCenterY());
        g2d.drawString(String.valueOf(this.game.getMyPlayer().getDeck().getCards().size()), (int)opponentDeck.getCenterX(), (int)opponentDeck.getCenterY());

    }
}
