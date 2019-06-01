package server.game.cards;

import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class Minion extends Card {

    private int attack;
    private int defense;
    private Shape shape;
    private boolean selectedOnBoard;

    public Minion(int cost, int attack, int defense, String cardName, String cardText) {
        super(cost, cardName, cardText);
        this.attack = attack;
        this.defense = defense;
    }

    //todo optimize draw method
    public void drawInHand(FXGraphics2D g2d, ResizableCanvas canvas, Point2D position){

        //Base Rectangle
        Rectangle2D cardBase = new Rectangle2D.Double(
                position.getX(),
                position.getY(),
                canvas.getWidth() * 0.07,
                canvas.getHeight() * 0.2);
        this.shape = cardBase;

        g2d.draw(cardBase);
        g2d.setColor(Color.white);
        g2d.fill(cardBase);

        //Ellipse top left of base
        Ellipse2D costEllipse = new Ellipse2D.Double(
                cardBase.getX(),
                cardBase.getY(),
                cardBase.getWidth() * 0.3,
                cardBase.getWidth() * 0.3);

        g2d.setColor(Color.CYAN);
        g2d.fill(costEllipse);
        g2d.setColor(Color.black);
        g2d.draw(costEllipse);
        g2d.drawString(String.valueOf(this.cost), (int)costEllipse.getCenterX(), (int)costEllipse.getCenterY());

        //Ellipse bottom left of base
        Ellipse2D attackEllipse = new Ellipse2D.Double(
                cardBase.getX(),
                cardBase.getY() + cardBase.getWidth()*0.7*(cardBase.getHeight()/cardBase.getWidth()),
                cardBase.getWidth()*0.3,
                cardBase.getWidth()*0.3);

        g2d.setColor(Color.yellow);
        g2d.fill(attackEllipse);
        g2d.setColor(Color.black);
        g2d.draw(attackEllipse);
        g2d.drawString(String.valueOf(this.attack), (int)attackEllipse.getCenterX(), (int)attackEllipse.getCenterY());

        //Ellipse bottom right of base
        Ellipse2D healthEllipse = new Ellipse2D.Double(
                cardBase.getX() + cardBase.getWidth()*0.7,
                cardBase.getY() + cardBase.getWidth()*0.7*(cardBase.getHeight()/cardBase.getWidth()),
                cardBase.getWidth()*0.3,
                cardBase.getWidth()*0.3);

        g2d.setColor(Color.red);
        g2d.fill(healthEllipse);
        g2d.setColor(Color.black);
        g2d.draw(healthEllipse);
        g2d.drawString(String.valueOf(this.defense), (int)healthEllipse.getCenterX(), (int)healthEllipse.getCenterY());

        //textbox in the center of the base
        g2d.setColor(Color.black);
        Rectangle2D cardNameBox = new Rectangle2D.Double(cardBase.getX(), cardBase.getY()+cardBase.getHeight()/2, cardBase.getWidth(), cardBase.getHeight()*0.2);
        g2d.draw(cardNameBox);
        g2d.drawString(this.cardName, (int)(cardNameBox.getCenterX()-cardNameBox.getWidth()*this.cardName.length()*0.04), (int)(cardNameBox.getCenterY()));
    }

    //todo optimize this draw method as well
    public void drawOnBoard(FXGraphics2D g2d, Point2D position, ResizableCanvas canvas){
        if (this.selectedOnBoard){
            g2d.setColor(Color.red);
            Rectangle2D selectedRectangle = new Rectangle2D.Double(position.getX()-3, position.getY()-3, canvas.getWidth()*0.07+6, canvas.getHeight()*0.16+6);
            g2d.fill(selectedRectangle);
        }

        Rectangle2D baseRectangle = new Rectangle2D.Double(position.getX(), position.getY(), canvas.getWidth()*0.07, canvas.getHeight()*0.15);
        g2d.setColor(Color.white);
        g2d.fill(baseRectangle);
        g2d.setColor(Color.black);
        g2d.draw(baseRectangle);

        this.shape = baseRectangle;



        Ellipse2D attackEllipse = new Ellipse2D.Double(
                baseRectangle.getX(),
                baseRectangle.getY() + baseRectangle.getWidth()*0.75*(baseRectangle.getHeight()/baseRectangle.getWidth()),
                baseRectangle.getWidth()*0.3,
                baseRectangle.getWidth()*0.3);

        g2d.setColor(Color.yellow);
        g2d.fill(attackEllipse);
        g2d.setColor(Color.black);
        g2d.draw(attackEllipse);
        g2d.drawString(String.valueOf(this.attack), (int)attackEllipse.getCenterX(), (int)attackEllipse.getCenterY());

        Ellipse2D healthEllipse = new Ellipse2D.Double(
                baseRectangle.getX() + baseRectangle.getWidth()*0.7,
                baseRectangle.getY() + baseRectangle.getWidth()*0.75*(baseRectangle.getHeight()/baseRectangle.getWidth()),
                baseRectangle.getWidth()*0.3,
                baseRectangle.getWidth()*0.3);

        g2d.setColor(Color.red);
        g2d.fill(healthEllipse);
        g2d.setColor(Color.black);
        g2d.draw(healthEllipse);
        g2d.drawString(String.valueOf(this.defense), (int)healthEllipse.getCenterX(), (int)healthEllipse.getCenterY());

    }

    public Shape getShape(){
        return this.shape;
    }

    public boolean isSelectedOnBoard() {
        return selectedOnBoard;
    }

    public void setSelectedOnBoard(boolean selectedOnBoard) {
        this.selectedOnBoard = selectedOnBoard;
    }
}
