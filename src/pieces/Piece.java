package pieces;

import core.Board;
import core.Move;
import gui.Game;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;

public abstract class Piece {

    protected int xp, yp;
    protected int x, y;
    protected boolean isWhite;
    protected BufferedImage image;
    protected Board board;

    /**
     * Classe héritée par toutes les pièces
     *
     * @param xp        Position x en cases
     * @param yp        Position y en cases
     * @param board     Instance du plateau
     */
    public Piece(int xp, int yp, Board board) {
        this.xp = xp;
        this.yp = yp;
        this.x = xp * Game.TILES_SIZE;
        this.y = yp * Game.TILES_SIZE;
        this.board = board;

        board.getPieces().add(this);
        setIsWhite();
        loadImage();
    }

    public abstract Collection<Move> getLegalMoves();

    public abstract boolean isLegalMove(int x, int y);

    public abstract void draw(Graphics g);

    public abstract void update();


    /**
     * Charge l'image de la pièce
     */
    protected abstract void loadImage();

    protected void setIsWhite() {
        isWhite = y > 2;
    }

    /**
     * Mise à jour de la position effective
     *
     * @param x Position x en pixels
     * @param y Position y en pixels
     */
    public void updatePosition(int x, int y) {
        this.x = x;
        this.y = y;
        this.xp = x / Game.TILES_SIZE;
        this.yp = y / Game.TILES_SIZE;
    }

    /**
     * Mise à jour de la position d'affichage uniquement
     *
     * @param x Position x en pixels
     * @param y Position y en pixels
     */
    public void updateDisplayPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public int getXp() {
        return xp;
    }
    public int getYp() {
        return yp;
    }

    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y = y;
    }

    public boolean isWhite() {
        return isWhite;
    }
}