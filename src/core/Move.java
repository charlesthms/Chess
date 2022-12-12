package core;

import gui.Game;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;

public class Move {

    private int x, y, xp, yp;
    private boolean isLethal;

    /**
     * Créer un mouvement
     *
     * @param x Coordonnée x en cases
     * @param y Coordonnée y en cases
     * @param isLethal true si le coup engendre la mort d'une autre pièce
     */
    public Move(int x, int y, boolean isLethal) {
        this.xp = x;
        this.yp = y;
        this.x = xp * Game.TILES_SIZE;
        this.y = yp * Game.TILES_SIZE;
        this.isLethal = isLethal;
    }

    public boolean isEqual(Move m) {
        return m.getX() == x && m.getY() == y;
    }

    public void draw(Graphics g){
        if (isLethal) drawTake((Graphics2D) g);
        else drawTip((Graphics2D) g);
    }

    private void drawTip(Graphics2D g2) {
        g2.setColor(new Color(0, 0, 0, 50));
        int size = (int) (10 * Game.SCALE);
        g2.fillOval(x + size + Game.OFFSET, y + size + Game.OFFSET, size, size);
    }

    private void drawTake(Graphics2D g2) {
        g2.setColor(new Color(0, 0, 0, 50));
        Shape t = createRingShape(x + Game.TILES_SIZE / 2f + Game.OFFSET, y + Game.TILES_SIZE / 2f + Game.OFFSET, Game.TILES_SIZE / 2f, 8);
        g2.draw(t);
        g2.fill(t);
    }

    public static Shape createRingShape(double centerX, double centerY, double outerRadius, double thickness) {
        Ellipse2D outer = new Ellipse2D.Double(
                centerX - outerRadius,
                centerY - outerRadius,
                outerRadius + outerRadius,
                outerRadius + outerRadius);
        Ellipse2D inner = new Ellipse2D.Double(
                centerX - outerRadius + thickness,
                centerY - outerRadius + thickness,
                outerRadius + outerRadius - thickness - thickness,
                outerRadius + outerRadius - thickness - thickness);
        Area area = new Area(outer);
        area.subtract(new Area(inner));
        return area;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public int getYp() {
        return yp;
    }

    public void setYp(int yp) {
        this.yp = yp;
    }

    public boolean isLethal() {
        return isLethal;
    }

    public void setLethal(boolean lethal) {
        isLethal = lethal;
    }
}
