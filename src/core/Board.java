package core;

import com.google.common.collect.ImmutableList;
import pieces.*;
import gui.Game;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

public class Board {

    private LinkedList<Piece> pieces = new LinkedList<>();
    private Piece selected = null;
    private Point2D hoverEffect = null;

    private Player wPlayer;
    private Player bPlayer;

    private int i = 0;
    private Player currentPlayer;

    public Board() {
        bPlayer = new Player(this, false, null, 1);
        wPlayer = new Player(this, true, null, 0);
        bPlayer.setKing(new King(4, 0, this));
        wPlayer.setKing(new King(4, 7, this));

        currentPlayer = wPlayer;
        initPieces();
    }

    private void initPieces() {
        new Queen(3, 0, this);
        new Queen(3, 7, this);

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
    private void makeMove(int x, int y, Piece piece) {
        if (selected != null) {
            int alignedX = x - (x % Game.TILES_SIZE);
            int alignedY = y - (y % Game.TILES_SIZE);

            if (piece.isLegalMove(alignedX, alignedY)) {
                Piece p = getPiece(x / Game.TILES_SIZE, y / Game.TILES_SIZE);
                if (p != null && p != selected) getPieces().remove(p);

                // Si la piece selected est un roi -> verifier si le castle est possible
                if (piece instanceof King k) {
                    System.out.println("Castling possible: " + k.isCastlingPossible());

                    for (CastleMove cm : k.getCastlingMoves()) {
                        if (cm.getMove().getX() == alignedX && cm.getMove().getY() == alignedY) {
                            cm.getRook().updatePosition(alignedX - Game.TILES_SIZE, alignedY);
                        }
                    }

                }
                piece.updatePosition(alignedX, alignedY);
                selected.setDidMove(true);
                updatePlayerTurn();
            } else {
                piece.updatePosition(piece.getXp() * Game.TILES_SIZE, piece.getYp() * Game.TILES_SIZE);
            }
        }
    }

    private void updatePlayerTurn() {
        i = 1 - i;

        if (wPlayer.getTurn() == i) currentPlayer = wPlayer;
        else currentPlayer = bPlayer;
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

    public void mouseClicked(MouseEvent e) {

    }

    /**
     * Permet d'obtenir les pièces authorisées à bouger en cas de mise en échec du roi
     *
     * @param pieces Collection de pièces
     * @param k Roi
     * @return Liste
     */
    private Collection<Piece> getAuthorizedPieces(ArrayList<Piece> pieces, King k) {
        ArrayList<Piece> legalPieces = new ArrayList<>();
        for (Piece p : k.attackingPieces()) {
            for (Piece playerPiece : pieces) {
                for (Move m : playerPiece.getLegalMoves()) {
                    if (m.getXp() == p.getXp() && m.getYp() == p.getYp()) {
                        playerPiece.forceMove(m);
                        legalPieces.add(playerPiece);
                    }
                }
            }
        }
        return ImmutableList.copyOf(legalPieces);
    }

    private void onMousePressed(Collection<Piece> pieces, int x, int y, boolean reset) {
        for (Piece p : pieces) {
            if (reset) p.forceMove(null);
            Rectangle hitbox = new Rectangle(p.getX(), p.getY(), Game.TILES_SIZE, Game.TILES_SIZE);

            if (hitbox.contains(x, y)){
                if (p.isWhite() == currentPlayer.isWhite()) {
                    selected = p;
                    int alignedX = x - (x % Game.TILES_SIZE);
                    int alignedY = y - (y % Game.TILES_SIZE);
                    hoverEffect = new Point(alignedX, alignedY);
                }
            }
        }
    }

    public void mousePressed(MouseEvent e) {
        // Si le joueur est en échec,
        // 1.) Trouver la ou les pièces qui l'attaque
        // 2.) Trouver les pieces alliés qui attaquent la ou les pièces menacentes
        // 3.) Ajouter ces pièces aux mouvements possibles
        if (currentPlayer.isInCheck()) {
            System.out.println("ECHEC");
            King king = currentPlayer.getKing();

            if (king.getLegalMoves().size() == 0) {
                System.out.println("ECHEC ET MATTE");
            }

            ArrayList<Piece> playerPieces = (ArrayList<Piece>) currentPlayer.getPieces();
            ArrayList<Piece> legalPieces = new ArrayList<>(getAuthorizedPieces(playerPieces, king));
            legalPieces.add(king);

            onMousePressed(legalPieces, e.getX(), e.getY(), false);

        } else {
            onMousePressed(pieces, e.getX(), e.getY(), true);
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
        makeMove(e.getX(), e.getY(), selected);
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

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public Player getwPlayer() {
        return wPlayer;
    }

    public Player getbPlayer() {
        return bPlayer;
    }
}
