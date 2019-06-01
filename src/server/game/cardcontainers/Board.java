package server.game.cardcontainers;

import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;
import server.game.cards.Card;
import server.game.cards.Minion;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.LinkedList;

public class Board implements Serializable {

    private LinkedList<Minion> minions;
    private int maxSize;
    private int selectedMinionIndex;

    public Board(LinkedList<Minion> minions, int maxSize){
        this.minions = minions;
        this.maxSize = maxSize;
    }

    public Board(int maxSize){
        this(new LinkedList<>(), maxSize);
    }

    public LinkedList<Minion> getMinions(){
        return this.minions;
    }

    public int getMaxSize(){
        return this.maxSize;
    }

    public boolean isFull(){
        return this.minions.size() >= this.maxSize;
    }

    public void addMinion(Card card){
        if (card.getClass().equals(Minion.class)){
            Minion minion = (Minion)card;
            this.minions.add(minion);
        }
    }

    public String toString(){
        return "Amount of minions: " + this.minions.size();
    }

    public void deselectAllMinions(){
        for (Minion minion : this.minions){
            minion.setSelectedOnBoard(false);
        }
    }

    public void setAllMinionsCanAttack(){
        for (Minion minion : this.minions){
            minion.setCanAttack(true);
        }
    }

    public boolean hasMinionSelected(){
        for (Minion minion : this.minions){
            if (minion.isSelectedOnBoard()){
                return true;
            }
        }
        return false;
    }

    public Minion getSelectedMinion(){
        for (Minion minion : this.minions){
            if (minion.isSelectedOnBoard()){
                return minion;
            }
        }
        return null;
    }

    public void drawBoard(FXGraphics2D g2d, ResizableCanvas canvas){
        //drawing my board
        Rectangle2D myBoardContainer = new Rectangle2D.Double(
                canvas.getWidth()*0.2,
                canvas.getHeight()* 0.5,
                canvas.getWidth()*0.6,
                canvas.getHeight()*0.2);

        g2d.setColor(Color.pink);
        g2d.fill(myBoardContainer);
        g2d.setColor(Color.black);
        g2d.draw(myBoardContainer);

        //drawing the cards on my board
        for(int i = 0; i < this.minions.size(); i++){
            Minion minion = this.minions.get(i);
            minion.drawOnBoard(
                    g2d,
                    new Point2D.Double(canvas.getWidth()*0.21 + canvas.getWidth()*0.08*i, canvas.getHeight()*0.52),
                    canvas);
        }
    }

    public void setSelectedMinionIndex(int index){
        this.selectedMinionIndex = index;
    }

    public int getSelectedMinionIndex(){
        return this.selectedMinionIndex;
    }
}
