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
    public static Piece lastMovedPiece;

    private Piece[] pieces = new Piece[64];
    private Piece selected = null;
    private Point2D hoverEffect = null;

    private Game game;

    public Board(Game game) {
        this.game = game;
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

        new Pawn(0, 1, this);
        new Pawn(1, 1, this);
        new Pawn(2, 1, this);
        new Pawn(3, 1, this);
        new Pawn(4, 1, this);
        new Pawn(5, 1, this);
        new Pawn(6, 1, this);
        new Pawn(7, 1, this);

        new Pawn(0, 6, this);
        new Pawn(1, 6, this);
        new Pawn(2, 6, this);
        new Pawn(3, 6, this);
        new Pawn(4, 6, this);
        new Pawn(5, 6, this);
        new Pawn(6, 6, this);
        new Pawn(7, 6, this);
    }

    public void draw(Graphics g) {
        g.setColor(new Color(39, 39, 39));
        g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);

        for (int y=0; y<8; y++) {
            for (int x=0; x<8; x++) {
                if((x + y) % 2 == 0) {
                    g.setColor(new Color(238, 238, 210));
                } else {
                    g.setColor(new Color(116, 150, 86));
                }
                g.fillRect(x * Game.TILES_SIZE + 30, y * Game.TILES_SIZE + 30, Game.TILES_SIZE, Game.TILES_SIZE);
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

        drawCoords(g);
    }

    private void drawCoords(Graphics g) {
        g.setColor(new Color(238, 238, 210));
        Font font = new Font("SansSerif", Font.PLAIN, 12);
        font = font.deriveFont(20f); // set font size to 24pt
        g.setFont(font);

        int index = 0;
        for (char c = 'a'; c < 'i'; c++) {
            g.drawString(Character.toString(c), index * Game.TILES_SIZE + Game.OFFSET + Game.TILES_SIZE/2 - 10, 22);
            g.drawString(Character.toString(c), index * Game.TILES_SIZE + Game.OFFSET + Game.TILES_SIZE/2 - 10, Game.HEIGHT - 8);

            index++;
        }

        for (int i = 0; i < 8; i++) {
            g.drawString(String.valueOf(8 - i), 8, i * Game.TILES_SIZE + Game.OFFSET + Game.TILES_SIZE/2 + 5);
            g.drawString(String.valueOf(8 - i), Game.WIDTH - 20, i * Game.TILES_SIZE + Game.OFFSET + Game.TILES_SIZE/2 + 5);

        }


    }

    private void drawAccentEffect(Graphics g) {
        g.setColor(new Color(1f, 1f, 0f, .5f));
        g.fillRect(selected.getXp() * Game.TILES_SIZE + Game.OFFSET, selected.getYp() * Game.TILES_SIZE + Game.OFFSET, Game.TILES_SIZE, Game.TILES_SIZE);
    }

    private void drawHoverEffect(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(new Color(1f, 1f, 1f, .7f));
        float thickness = 2f;
        Stroke oldStroke = g2.getStroke();
        g2.setStroke(new BasicStroke(thickness));
        g2.drawRect((int) hoverEffect.getX() + Game.OFFSET, (int) hoverEffect.getY() + Game.OFFSET, Game.TILES_SIZE, Game.TILES_SIZE);
        g2.setStroke(oldStroke);
    }

    private void drawThreats(Graphics g) {
        boolean[] threatMap = Helpers.getThreatMap(pieces, selected);

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (threatMap[i * 8 + j]) {
                    g.setColor(new Color(1f, 0f, 0f, .5f));
                    g.fillRect(j * Game.TILES_SIZE + Game.OFFSET, i * Game.TILES_SIZE + Game.OFFSET, Game.TILES_SIZE, Game.TILES_SIZE);
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
                // Castle the rook
                CastlingMove cm = piece.isCastlingMove(alignedX, alignedY);
                if (cm != null) cm.moveRook();

                // Promote
                PromoteMove pm = piece.isPromoteMove(alignedX, alignedY);
                if (pm != null) piece = pm.showPromoteDialog(piece);

                // En passant
                EnPassantMove epm = piece.isEnPassantMove(alignedX, alignedY);
                if (epm != null) epm.killTarget();

                piece.updatePosition(alignedX, alignedY);
                piece.setDidMove(true);
                lastMovedPiece = piece;

                if (piece instanceof Pawn p)
                    p.setLastMoveIndex(piece.getIndex());

            } else {
                piece.updatePosition(piece.getXp() * Game.TILES_SIZE, piece.getYp() * Game.TILES_SIZE);
            }
        }
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
        onMousePressed(e.getX() - Game.OFFSET, e.getY() - Game.OFFSET);
    }

    public void mouseDragged(MouseEvent e) {
        if (selected != null) {
            int alignedX = e.getX() - (e.getX() % Game.TILES_SIZE);
            int alignedY = e.getY() - (e.getY() % Game.TILES_SIZE);
            hoverEffect = new Point(alignedX, alignedY);

            selected.updateDisplayPosition(e.getX() - Game.TILES_SIZE / 2 - Game.OFFSET, e.getY() - Game.TILES_SIZE / 2 - Game.OFFSET);
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

    public Game getGame() {
        return game;
    }
}
