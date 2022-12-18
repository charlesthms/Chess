package engine;

import engine.moves.CastlingMove;
import engine.moves.EnPassantMove;
import engine.moves.Move;
import engine.moves.PromoteMove;
import engine.parser.Fen;
import engine.players.Bot;
import engine.players.Human;
import engine.players.Player;
import gui.GamePanel;
import pieces.*;
import gui.Game;
import utils.Helpers;
import utils.Loader;

import javax.sound.sampled.Clip;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import static engine.parser.Fen.parseFen;

public class Board {

    private GamePanel gp;

    public static boolean showThreats = false;
    public static boolean showTips = true;

    public static final int[] DIRECTION_OFFSETS = new int[]{-8, 8, -1, 1, -9, 9, -7, 7};
    public static int[][] distanceToEdge = new int[64][8];
    public static Piece lastMovedPiece;

    private Piece[] pieces = new Piece[64];
    private Piece selected = null;
    private Point2D hoverEffect = null;

    private Player currentPlayer;
    private Player whitePlayer;
    private Player blackPlayer;
    private int turn = 0; // 0 for white 1 for black

    private Clip moveSound, takeSound, castleSound, lastSound;

    public Board() {
        initPlayers();
        precomputeMoveData();
    }

    public Board(String fen) {
        initPlayers();
        parseFen(fen, this);
        precomputeMoveData();
        printBoard();

        System.out.println(whitePlayer.getKing());
        System.out.println(blackPlayer.getKing());

        Fen.getFen(this);

        moveSound = Loader.getSound(Loader.MOVE);
        takeSound = Loader.getSound(Loader.TAKE);
        castleSound = Loader.getSound(Loader.CASTLE);
    }

    private void initPlayers() {
        whitePlayer = new Human(this, findKing(true), true);
        blackPlayer = new Human(this, findKing(false), false);

        currentPlayer = whitePlayer;
    }

    private King findKing(boolean isWhite)
    {
        for (Piece p : pieces) {
            if (p instanceof King k && k.isWhite() == isWhite) return k;
        }
        return null;
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

    public void draw(Graphics g, GamePanel gp) {
        this.gp = gp;
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

            if (showThreats) drawThreats(g);
            if (showTips) {
                selected.getLegalMoves().forEach(move -> {
                    move.draw(g);
                });
            }

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

    public void updatePlayerTurn() {
        turn = 1 - turn;

        if (turn == 0) currentPlayer = whitePlayer;
        else currentPlayer = blackPlayer;

        if (currentPlayer == blackPlayer && blackPlayer instanceof Bot b) b.play();
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


            Move move = piece.isLegalMove(alignedX, alignedY);

            Clip soundToPlay;

            if (move != null) {

                if (move.isLethal()) {
                    soundToPlay = takeSound;
                    takeSound.stop();
                } else {
                    soundToPlay = moveSound;
                    moveSound.stop();
                }

                // Castle the rook
                CastlingMove cm = piece.isCastlingMove(alignedX, alignedY);
                if (cm != null) {
                    castleSound.stop();
                    soundToPlay = castleSound;
                    cm.moveRook();
                }

                // Promote
                PromoteMove pm = piece.isPromoteMove(alignedX, alignedY);
                if (pm != null) piece = pm.showPromoteDialog(piece);

                // En passant
                EnPassantMove epm = piece.isEnPassantMove(alignedX, alignedY);
                if (epm != null) epm.killTarget();

                if (piece instanceof Pawn p) {
                    p.setPreviousYp(piece.getYp());
                }

                piece.updatePosition(alignedX, alignedY);
                piece.setDidMove(true);
                lastMovedPiece = piece;

                if (piece instanceof Pawn p)
                    p.setLastMoveIndex(piece.getIndex());


                soundToPlay.setMicrosecondPosition(0);
                soundToPlay.start();

                updatePlayerTurn();
            } else {
                piece.updatePosition(piece.getXp() * Game.TILES_SIZE, piece.getYp() * Game.TILES_SIZE);
            }
        }
    }


    /**
     * Génère les mouvements possibles dans les conditions actuelles du jeu
     *
     * @return
     */
    public ArrayList<Move> generateMoves() {
        ArrayList<Move> moves = new ArrayList<>();

        for (Piece p : pieces) {
            if (p != null && currentPlayer.isWhite() == p.isWhite()) {
                moves.addAll(p.getLegalMoves());
            }
        }

        return moves;
    }

    public void doMove(Move m)
    {
        Piece p;
        Piece mp = m.getPiece();

        if (mp instanceof King) p = new King(mp);
        else if (mp instanceof Queen) p = new Queen(mp);
        else if (mp instanceof Bishop) p = new Bishop(mp);
        else if (mp instanceof Knight) p = new Knight(mp);
        else if (mp instanceof Rook) p = new Rook(mp);
        else p = new Pawn(mp);

        pieces[p.getYp() * 8 + p.getXp()] = null;
        pieces[m.getTyp() * 8 + m.getTxp()] = p;
        p.updatePos(m.getTx(), m.getTy());

        updatePlayerTurn();
    }

    public void undoMove(Move m)
    {
        Piece p = m.getPiece();
        pieces[m.getTyp() * 8 + m.getTxp()] = m.getTarget();
        pieces[p.getYp() * 8 + p.getXp()] = p;

        updatePlayerTurn();
    }

    public Piece getPiece(int index) {
        return pieces[index];
    }

    public void printBoard() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece p = pieces[i * 8 + j];
                if (p != null) {
                    System.out.print(" " + p + " ");
                } else {
                    System.out.print(" . ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    public void mouseClicked(MouseEvent e) {

    }

    private void onMousePressed(int x, int y) {
        for (Piece p : pieces) {
            if (p != null && currentPlayer.isWhite() == p.isWhite()) {
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

    public GamePanel getGp() {
        return gp;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public Player getWhitePlayer() {
        return whitePlayer;
    }

    public Player getBlackPlayer() {
        return blackPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }
}