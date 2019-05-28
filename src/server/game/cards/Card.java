package server.game.cards;

import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

import java.awt.geom.Point2D;
import java.io.Serializable;

public abstract class Card implements Serializable {

    protected int cost;
    protected String cardName;
    protected String cardText;
    protected transient Point2D position;

    public Card(int cost, String cardName, String cardText){
        this.cost = cost;
        this.cardName = cardName;
        this.cardText = cardText;
    }

    public int getCost() {
        return cost;
    }

    public String getCardName() {
        return cardName;
    }

    public String getCardText() {
        return cardText;
    }

    public abstract void drawInHand(FXGraphics2D g2d, ResizableCanvas canvas, Point2D position);

    public abstract void drawOnBoard(FXGraphics2D g2d);

    public Point2D getPosition(){
        return this.position;
    }

    public void setPosition(Point2D position){
        this.position = position;
    }
}
