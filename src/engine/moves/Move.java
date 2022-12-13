package engine.moves;

import gui.Game;
import pieces.Piece;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;

public class Move {

    private Piece piece;
    private int tx, ty, txp, typ;
    private boolean isLethal;

    /**
     * Créer un mouvement
     *
     * @param piece Pièce source
     * @param tx Coordonnée x en cases de la cible
     * @param ty Coordonnée y en cases de la cible
     * @param isLethal true si le coup engendre la mort d'une autre pièce
     */
    public Move(Piece piece, int tx, int ty, boolean isLethal) {
        this.piece = piece;
        this.txp = tx;
        this.typ = ty;
        this.tx = txp * Game.TILES_SIZE;
        this.ty = typ * Game.TILES_SIZE;
        this.isLethal = isLethal;
    }

    public void draw(Graphics g){
        if (isLethal) drawTake((Graphics2D) g);
        else drawTip((Graphics2D) g);
    }

    private void drawTip(Graphics2D g2) {
        g2.setColor(new Color(0, 0, 0, 50));
        int size = (int) (10 * Game.SCALE);
        g2.fillOval(tx + size + Game.OFFSET, ty + size + Game.OFFSET, size, size);
    }

    private void drawTake(Graphics2D g2) {
        g2.setColor(new Color(0, 0, 0, 50));
        Shape t = createRingShape(tx + Game.TILES_SIZE / 2f + Game.OFFSET, ty + Game.TILES_SIZE / 2f + Game.OFFSET, Game.TILES_SIZE / 2f, 8);
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

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public int getTx() {
        return tx;
    }

    public void setTx(int tx) {
        this.tx = tx;
    }

    public int getTy() {
        return ty;
    }

    public void setTy(int ty) {
        this.ty = ty;
    }

    public int getTxp() {
        return txp;
    }

    public void setTxp(int txp) {
        this.txp = txp;
    }

    public int getTyp() {
        return typ;
    }

    public void setTyp(int typ) {
        this.typ = typ;
    }

    public boolean isLethal() {
        return isLethal;
    }

    public void setLethal(boolean lethal) {
        isLethal = lethal;
    }
}
