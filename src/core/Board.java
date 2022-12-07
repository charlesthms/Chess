package core;

import pieces.*;
import gui.Game;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.LinkedList;

public class Board {

    private LinkedList<Piece> pieces = new LinkedList<>();
    private Piece selected = null;
    private Point2D hoverEffect = null;

    public Board() {
        initPieces();
    }

    private void initPieces() {

        new Queen(3, 0, this);
        new Queen(3, 7, this);

        new King(4, 0, this);
        new King(4, 7, this);

        new Knight(1, 0, this);
        new Knight(6, 0, this);
        new Knight(1, 7, this);
        new Knight(6, 7, this);

        new Bishop(2, 0, this);
        new Bishop(5, 0, this);
        new Bishop(2, 7, this);
        new Bishop(5, 7, this);

        new Rook(0, 0, this);
        new Rook(7, 0, this);
        new Rook(0, 7, this);
        new Rook(7, 7, this);
    }

    public void draw(Graphics g) {
        for (int y=0; y<8; y++) {
            for (int x=0; x<8; x++) {
                if((x + y) % 2 == 0) {
                    g.setColor(new Color(116, 150, 86));
                } else {
                    g.setColor(new Color(238, 238, 210));
                }
                g.fillRect(x * Game.TILES_SIZE, y * Game.TILES_SIZE, Game.TILES_SIZE, Game.TILES_SIZE);
            }
        }

        pieces.forEach(p -> p.draw(g));

        if (selected != null) {
            drawAccentEffect(g);
            drawHoverEffect(g);

            selected.getLegalMoves().forEach(move -> {
                move.draw(g);
            });

            selected.draw(g);
        }
    }

    private void drawAccentEffect(Graphics g) {
        g.setColor(new Color(1f, 1f, 0f, .5f));
        g.fillRect(selected.getXp() * Game.TILES_SIZE, selected.getYp() * Game.TILES_SIZE, Game.TILES_SIZE, Game.TILES_SIZE);
    }

    private void drawHoverEffect(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(new Color(1f, 1f, 1f, .7f));
        float thickness = 2f;
        Stroke oldStroke = g2.getStroke();
        g2.setStroke(new BasicStroke(thickness));
        g2.drawRect((int) hoverEffect.getX(), (int) hoverEffect.getY(), Game.TILES_SIZE, Game.TILES_SIZE);
        g2.setStroke(oldStroke);
    }

    public void update() {

    }

    /**
     * Aligne la pièce sur l'échiquier
     *
     * @param x Coordonnée x en px
     * @param y Coordonnée y en px
     * @param piece Piece concernée
     */
    private void placePiece(int x, int y, Piece piece) {
        if (selected != null) {
            int alignedX = x - (x % Game.TILES_SIZE);
            int alignedY = y - (y % Game.TILES_SIZE);

            if (piece.isLegalMove(alignedX, alignedY)) {
                Piece p = getPiece(x / Game.TILES_SIZE, y / Game.TILES_SIZE);
                if (p != null && p != selected) getPieces().remove(p);

                piece.updatePosition(alignedX, alignedY);
            } else {
                piece.updatePosition(piece.getXp() * Game.TILES_SIZE, piece.getYp() * Game.TILES_SIZE);
            }
        }
    }

    /**
     * Permet de savoir si une case est vide
     *
     * @param xp Position x en case
     * @param yp Position y en case
     * @return true si aucune pièce se trouve aux coordonnées false sinon
     */
    public boolean isCaseEmpty(int xp, int yp) {
        for (Piece p : pieces) {
            if (p.getXp() == xp && p.getYp() == yp) return false;
        }
        return true;
    }

    public Piece getPiece(int xp, int yp) {
        for (Piece p : pieces) {
            if (p.getXp() == xp && p.getYp() == yp) return p;
        }
        return null;
    }

    public ArrayList<Move> getAllMoves() {
        ArrayList<Move> res = new ArrayList<>();

        for (Piece p : getPieces()) {
            if (p instanceof King k && p.isWhite() == !selected.isWhite()) {
                res.addAll(k.generateAllMoves());
            }
            else if (p.isWhite() == !selected.isWhite()){
                res.addAll(p.getLegalMoves());
            }
        }

        return res;
    }

    public void mouseClicked(MouseEvent e) {

    }

    public void mousePressed(MouseEvent e) {
        for (Piece p : pieces) {
            Rectangle hitbox = new Rectangle(p.getX(), p.getY(), Game.TILES_SIZE, Game.TILES_SIZE);

            if (hitbox.contains(e.getX(), e.getY())){
                selected = p;
                int alignedX = e.getX() - (e.getX() % Game.TILES_SIZE);
                int alignedY = e.getY() - (e.getY() % Game.TILES_SIZE);
                hoverEffect = new Point(alignedX, alignedY);
            }
        }
    }

    public void mouseDragged(MouseEvent e) {
        if (selected != null) {
            int alignedX = e.getX() - (e.getX() % Game.TILES_SIZE);
            int alignedY = e.getY() - (e.getY() % Game.TILES_SIZE);
            hoverEffect = new Point(alignedX, alignedY);

            selected.updateDisplayPosition(e.getX() - Game.TILES_SIZE / 2, e.getY() - Game.TILES_SIZE / 2);
        }
    }

    public void mouseReleased(MouseEvent e) {
        placePiece(e.getX(), e.getY(), selected);
        selected = null;
        hoverEffect = null;
    }

    public void mouseMoved(MouseEvent e) {

    }

    public void mouseEntered(MouseEvent e) {

    }

    public void mouseExited(MouseEvent e) {

    }

    public LinkedList<Piece> getPieces() {
        return pieces;
    }
}
