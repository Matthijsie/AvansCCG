package client;

import client.actionObjects.AttackMinion;
import client.actionObjects.AttackOpponent;
import client.actionObjects.EndTurn;
import client.actionObjects.PlayCard;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;
import server.game.Game;
import server.game.cards.Card;
import server.game.cards.Minion;

import java.awt.*;
import java.awt.geom.Ellipse2D;
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
    private Rectangle2D opponentPortrait;

    public static void main(String[] args) {
        launch(ClientApplication.class);
    }

    @Override
    public void start(Stage stage) {
        //============Login screen================
        BorderPane firstPane = new BorderPane();
        VBox loginBox = new VBox();
        TextField loginField = new TextField();
        loginField.setPromptText("Enter a name");
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
                stage.setFullScreen(true);
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

        if (this.game != null){
            drawEndRunButton(g2d);
            drawPlayerPortraits(g2d);
            drawDecks(g2d);
            drawHands(g2d);
            drawBoard(g2d);
            drawMana(g2d);
        }
    }

    public void update(double deltaTime){

    }

    //todo add function to select minions and attack targets
    private boolean onMousePressed(MouseEvent e){
        Point2D mousePosition = new Point2D.Double(e.getX(), e.getY());

        if (this.game.getMyPlayer().isMyTurn()) {

            //checks if the end turn button has been pressed
            if (this.endTurnButton.contains(mousePosition)) {
                this.client.writeObject(new EndTurn());
                return true;
            }

            //checks if a card in hand has been pressed
            int i = 0;
            for (Card card : this.game.getMyPlayer().getHand().getCards()) {
                if (card.getShape().contains(mousePosition)) {
                    if (card.getCost() <= this.game.getMyPlayer().getMana() && this.game.getMyPlayer().getBoardSize() < 7) {
                        this.client.writeObject(new PlayCard(card, i));
                        return true;
                    }
                }
                i++;
            }

            //checks if a card on own board has been pressed
            i = 0;
            for (Minion minion : this.game.getMyPlayer().getBoard().getMinions()) {
                if (minion.getShape().contains(mousePosition)) {
                    if (!minion.isSelectedOnBoard()) {
                        this.game.getMyPlayer().getBoard().deselectAllMinions();
                        this.game.getMyPlayer().getBoard().setSelectedMinionIndex(i);
                        minion.setSelectedOnBoard(true);
                    } else {
                        minion.setSelectedOnBoard(false);
                        this.game.getMyPlayer().getBoard().setSelectedMinionIndex(-1);
                    }
                    return true;
                }
            }

            i = 0;
            for (Minion minion : this.game.getOpponent().getCardsOnEnemyBoard()){
                if (minion.getShape().contains(mousePosition)){
                    if (this.game.getMyPlayer().getBoard().hasMinionSelected()){
                        this.client.writeObject(new AttackMinion(i, this.game.getMyPlayer().getBoard().getSelectedMinionIndex()));
                        return true;
                    }
                }
                i++;
            }

            if (this.opponentPortrait.contains(mousePosition)){
                if (this.game.getMyPlayer().getBoard().hasMinionSelected()){
                    this.client.writeObject(new AttackOpponent(this.game.getMyPlayer().getBoard().getSelectedMinionIndex()));
                    return true;
                }
            }
        }

        return false;
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
        this.chatInput.clear();
        this.scrollPane.setVvalue(1.0);

    }

    public void setGame(Game game){
        this.game = game;
    }

    private void drawPlayerPortraits(FXGraphics2D g2d){

        //drawing my portrait
        Rectangle2D myPlayerPortrait = new Rectangle2D.Double(this.canvas.getWidth()*0.45,
                this.canvas.getHeight()*0.75,
                this.canvas.getWidth()*0.1,
                this.canvas.getHeight()*0.2);

        g2d.setColor(this.game.getMyPlayer().getPlayerColor());
        g2d.fill(myPlayerPortrait);


        Ellipse2D healthContainer1 = new Ellipse2D.Double(myPlayerPortrait.getX() + myPlayerPortrait.getWidth()*0.3,
                myPlayerPortrait.getY() - myPlayerPortrait.getHeight()*0.2,
                myPlayerPortrait.getWidth()*0.4,
                myPlayerPortrait.getWidth()*0.4);
        g2d.setColor(Color.white);
        g2d.fill(healthContainer1);

        //drawing opponent portrait
        Rectangle2D opponentPortrait = new Rectangle2D.Double(this.canvas.getWidth()*0.45,
                this.canvas.getHeight()*0.05,
                this.canvas.getWidth()*0.1,
                this.canvas.getHeight()*0.2);
        this.opponentPortrait = opponentPortrait;

        g2d.setColor(this.game.getOpponent().getColor());
        g2d.fill(opponentPortrait);

        Ellipse2D healthContainer2 = new Ellipse2D.Double(opponentPortrait.getX() + opponentPortrait.getWidth()*0.3,
                opponentPortrait.getY() + opponentPortrait.getHeight()*0.8,
                opponentPortrait.getWidth()*0.4,
                opponentPortrait.getWidth()*0.4);

        g2d.setColor(Color.white);
        g2d.fill(healthContainer2);

        //drawing strings
        g2d.setColor(Color.black);
        g2d.drawString(String.valueOf(this.game.getMyPlayer().getHealth()), (int)healthContainer1.getCenterX(), (int)healthContainer1.getCenterY());
        g2d.draw(myPlayerPortrait);
        g2d.draw(healthContainer1);

        g2d.drawString(String.valueOf(this.game.getOpponent().getHealth()), (int)healthContainer2.getCenterX(), (int)healthContainer2.getCenterY());
        g2d.draw(opponentPortrait);
        g2d.draw(healthContainer2);
    }

    private void drawDecks(FXGraphics2D g2d){

        //drawing my deck
        Rectangle2D myDeck = new Rectangle2D.Double(this.canvas.getWidth()*0.8,
                this.canvas.getHeight()*0.8,
                this.canvas.getWidth()*0.1,
                this.canvas.getHeight()*0.1);

        g2d.setColor(Color.lightGray);
        g2d.fill(myDeck);

        //drawing opponent deck
        Rectangle2D opponentDeck = new Rectangle2D.Double(this.canvas.getWidth()*0.8,
                this.canvas.getHeight()*0.1,
                this.canvas.getWidth()*0.1,
                this.canvas.getHeight()*0.1);

        g2d.fill(opponentDeck);

        //draw method and adding numbers for the deck sizes
        g2d.setColor(Color.black);
        g2d.draw(myDeck);
        g2d.draw(opponentDeck);

        g2d.drawString(String.valueOf(this.game.getMyPlayer().getDeck().getCards().size()), (int)myDeck.getCenterX(), (int)myDeck.getCenterY());
        g2d.drawString(String.valueOf(this.game.getOpponent().getCardAmountInDeck()), (int)opponentDeck.getCenterX(), (int)opponentDeck.getCenterY());

    }

    private void drawHands(FXGraphics2D g2d){

        //drawing my hand
        int i = 0;
        for (Card card : this.game.getMyPlayer().getHand().getCards()) {

            Point2D cardPosition = new Point2D.Double(this.canvas.getWidth()*0.07*i, this.canvas.getHeight()*0.8);
            card.drawInHand(g2d, this.canvas, cardPosition);

            i++;
        }

        //drawing opponent's hand
        for (int j = 0; j < this.game.getOpponent().getCardAmountInHand(); j++){
            Rectangle2D card = new Rectangle2D.Double(this.canvas.getWidth()*0.07*j, 0, this.canvas.getWidth()*0.07, this.canvas.getHeight()*0.2);
            g2d.setColor(Color.black);
            g2d.draw(card);
            g2d.setColor(Color.white);
            g2d.fill(card);
        }
    }

    //todo make it so the minions get drawn relative to the board container rather than the canvas (need to change method drawOnBoard() in Minion as well)
    private void drawBoard(FXGraphics2D g2d){

        //draw my board
        this.game.getMyPlayer().getBoard().drawBoard(g2d, this.canvas);

        //drawing opponent's board
        Rectangle2D opponentBoardContainer = new Rectangle2D.Double(
                this.canvas.getWidth()*0.2,
                this.canvas.getHeight()*0.3,
                this.canvas.getWidth()*0.6,
                this.canvas.getHeight()*0.2);

        g2d.setColor(Color.pink);
        g2d.fill(opponentBoardContainer);
        g2d.setColor(Color.black);
        g2d.draw(opponentBoardContainer);

        //drawing the cards on the opponent's board
        for (int i = 0; i < this.game.getOpponent().getCardsOnEnemyBoard().size(); i++){
            Minion minion = this.game.getOpponent().getCardsOnEnemyBoard().get(i);
            minion.drawOnBoard(
                    g2d,
                    new Point2D.Double(this.canvas.getWidth()*0.21 + this.canvas.getWidth()*0.08*i, this.canvas.getHeight()*0.32),
                    this.canvas);
        }
    }

    //todo optimize setColor()
    private void drawMana(FXGraphics2D g2d){

        //draw my mana
        Rectangle2D manaContainer = new Rectangle2D.Double(
                this.canvas.getWidth()*0.6,
                this.canvas.getHeight()*0.92,
                this.canvas.getWidth()*0.35,
                this.canvas.getHeight()*0.06);

        g2d.setColor(Color.black);
        g2d.draw(manaContainer);
        g2d.setColor(Color.white);
        g2d.fill(manaContainer);

        int i = 0;
        for (int j = 0 ; j < this.game.getMyPlayer().getMana(); j++){
            Ellipse2D manaCrystal = new Ellipse2D.Double(
                    manaContainer.getX() + manaContainer.getWidth()/10 *i,
                    manaContainer.getY(),
                    manaContainer.getHeight(),
                    manaContainer.getHeight());

            g2d.setColor(Color.cyan);
            g2d.fill(manaCrystal);
            g2d.setColor(Color.black);
            g2d.draw(manaCrystal);
            i++;
        }

        for (int j = 0; j < this.game.getMyPlayer().getTotalMana()-this.game.getMyPlayer().getMana(); j++) {
            Ellipse2D spendManaCrystal = new Ellipse2D.Double(
                    manaContainer.getX() + manaContainer.getWidth()/10 * i,
                    manaContainer.getY(),
                    manaContainer.getHeight(),
                    manaContainer.getHeight());

            g2d.setColor(Color.blue);
            g2d.fill(spendManaCrystal);
            g2d.setColor(Color.black);
            g2d.draw(spendManaCrystal);
            i++;

        }

        //draw opponent mana
        Rectangle2D opponentManaContainer = new Rectangle2D.Double(
                this.canvas.getWidth()*0.6,
                this.canvas.getHeight()*0.03,
                this.canvas.getWidth()*0.35,
                this.canvas.getHeight()*0.06);

        g2d.setColor(Color.black);
        g2d.draw(opponentManaContainer);

        i = 0;
        for (int j = 0 ; j < this.game.getOpponent().getMana(); j++){
            Ellipse2D manaCrystal = new Ellipse2D.Double(
                    opponentManaContainer.getX() + opponentManaContainer.getWidth()/10 *i,
                    opponentManaContainer.getY(),
                    opponentManaContainer.getHeight(),
                    opponentManaContainer.getHeight());

            g2d.setColor(Color.cyan);
            g2d.fill(manaCrystal);
            g2d.setColor(Color.black);
            g2d.draw(manaCrystal);
            i++;
        }

        for (int j = 0; j < this.game.getOpponent().getTotalMana()-this.game.getOpponent().getMana(); j++) {
            Ellipse2D spendManaCrystal = new Ellipse2D.Double(
                    opponentManaContainer.getX() + opponentManaContainer.getWidth()/10 * i,
                    opponentManaContainer.getY(),
                    opponentManaContainer.getHeight(),
                    opponentManaContainer.getHeight());

            g2d.setColor(Color.blue);
            g2d.fill(spendManaCrystal);
            g2d.setColor(Color.black);
            g2d.draw(spendManaCrystal);
            i++;
        }
    }

    private void drawEndRunButton(FXGraphics2D g2d){
        this.endTurnButton = new Rectangle2D.Double(canvas.getWidth()*0.90, canvas.getHeight()*0.47, canvas.getWidth()*0.10, canvas.getHeight()*0.06);

        if (this.game.getMyPlayer().isMyTurn()){
            g2d.setColor(Color.green);
        }else {
            g2d.setColor(Color.yellow);
        }

        g2d.fill(this.endTurnButton);
        g2d.setColor(Color.black);
        g2d.draw(this.endTurnButton);
        g2d.drawString(
                "End turn",
                (int)(this.endTurnButton.getX()+this.endTurnButton.getWidth()*0.2),
                (int)(this.endTurnButton.getY() + this.endTurnButton.getHeight()*0.6));
    }
}
