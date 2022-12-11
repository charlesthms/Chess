package core;

import pieces.*;
import gui.Game;
import utils.Helpers;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

public class Board {

    public static final int[] DIRECTION_OFFSETS = new int[]{-8, 8, -1, 1, -9, 9, -7, 7};
    public static int[][] distanceToEdge = new int[64][8];

    private Piece[] pieces = new Piece[64];
    private Piece selected = null;
    private Point2D hoverEffect = null;


    public Board() {
        initPieces();
        precomputeMoveData();
    }

    private void precomputeMoveData() {
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                int north = y;
                int south = 7 - y;
                int west = x;
                int east = 7 - x;

                int squareIndex = y * 8 + x;

                distanceToEdge[squareIndex] = new int[]{
                        north,
                        south,
                        west,
                        east,
                        Math.min(north, west),
                        Math.min(south, east),
                        Math.min(north, east),
                        Math.min(south, west)
                };
            }
        }
    }

    private void initPieces() {
        new King(4, 7, this);
        new King(4, 0, this);

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

        for (Piece piece : pieces) {
            if (piece != null) piece.draw(g);
        }

        if (selected != null) {
            drawAccentEffect(g);
            drawHoverEffect(g);
            drawThreats(g);

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

    private void drawThreats(Graphics g) {
        boolean[] threatMap = Helpers.getThreatMap(pieces, selected);

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (threatMap[i * 8 + j]) {
                    g.setColor(new Color(1f, 0f, 0f, .5f));
                    g.fillRect(j * Game.TILES_SIZE, i * Game.TILES_SIZE, Game.TILES_SIZE, Game.TILES_SIZE);
                }
            }
        }
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

        return pieces[yp * 8 + xp] == null;
    }

    public Piece getPiece(int xp, int yp) {
        for (Piece p : pieces) {
            if (p != null)
                if (p.getXp() == xp && p.getYp() == yp) return p;
        }
        return null;
    }

    public Piece getPiece(int index) {
        return pieces[index];
    }

    public void mouseClicked(MouseEvent e) {

    }

    private void onMousePressed(int x, int y) {
        for (Piece p : pieces) {
            if (p != null) {
                Rectangle hitbox = new Rectangle(p.getX(), p.getY(), Game.TILES_SIZE, Game.TILES_SIZE);

                if (hitbox.contains(x, y)){
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
        /*if (currentPlayer.isInCheck()) {
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
        }*/
        onMousePressed(e.getX(), e.getY());
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

    public Piece[] getPieces() {
        return pieces;
    }

    public void setPieces(Piece[] pieces) {
        this.pieces = pieces;
    }
}
