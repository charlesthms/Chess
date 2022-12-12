package pieces;

import core.*;
import gui.Game;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;

import static utils.Helpers.isKingChecked;

public abstract class Piece {

    protected int index;
    protected int xp, yp;
    protected int x, y;
    protected boolean isWhite;
    protected boolean didMove;
    protected BufferedImage image;
    protected Board board;
    protected Player player;

    private Piece savedPiece;

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
        this.index = yp * 8 + xp;
        this.board = board;

        this.didMove = false;

        board.getPieces()[index] = this;
        setIsWhite();
        loadImage();
    }

    public Piece(int xp, int yp, Board board, boolean isWhite) {
        this.xp = xp;
        this.yp = yp;
        this.x = xp * Game.TILES_SIZE;
        this.y = yp * Game.TILES_SIZE;
        this.index = yp * 8 + xp;
        this.board = board;

        this.didMove = false;

        board.getPieces()[index] = this;
        this.isWhite = isWhite;
        loadImage();
    }

    public abstract Collection<Move> getLegalMoves();
    public abstract void draw(Graphics g);

    protected abstract void loadImage();

    protected Piece[] doMove(Piece[] pieces, Move m) {
        savedPiece = pieces[m.getYp() * 8 + m.getXp()];
        pieces[index] = null;
        index = m.getYp() * 8 + m.getXp();
        pieces[index] = this;

        return pieces;
    }

    protected void undoMove(Piece[] pieces, int i) {
        pieces[index] = savedPiece;
        index = i;
        pieces[index] = this;
    }

    protected Collection<Move> simulateMoves(ArrayList<Move> pseudoLegalMoves) {
        ArrayList<Move> moves = new ArrayList<>();
        int initial_index = index;
        Piece[] pieces_backup = board.getPieces().clone();

        for (Move m : pseudoLegalMoves) {
            Piece[] pieces;
            pieces = doMove(board.getPieces(), m);

            if (!isKingChecked(this, false)) {
                moves.add(m);
            }

            undoMove(pieces, initial_index);
        }
        board.setPieces(pieces_backup);

        return moves;
    }

    protected ArrayList<Move> simulateEnPassant(ArrayList<Move> pseudoLegalMoves) {
        ArrayList<Move> moves = new ArrayList<>(pseudoLegalMoves);
        int initial_index = index;
        Piece[] pieces_backup = board.getPieces().clone();

        for (Move m : pseudoLegalMoves) {
            if (m instanceof EnPassantMove epm){
                Piece[] pieces;
                pieces = doMove(board.getPieces(), m);
                Piece tmp = pieces[epm.getTarget().getIndex()];
                pieces[epm.getTarget().getIndex()] = null;

                if (isKingChecked(this, false)) {
                    moves.remove(epm);
                }

                pieces[epm.getTarget().getIndex()] = tmp;
                undoMove(pieces, initial_index);
            }
        }
        board.setPieces(pieces_backup);

        return moves;
    }

    protected void setIsWhite() {
        isWhite = yp > 2;
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
        board.getPieces()[index] = null;
        index = yp * 8 + xp;
        board.getPieces()[index] = this;
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

    public CastlingMove isCastlingMove(int x, int y) {
        for (Move m : getLegalMoves()) {
            if (m instanceof CastlingMove && m.getX() == x && m.getY() == y) return (CastlingMove) m;
        }
        return null;
    }

    public PromoteMove isPromoteMove(int x, int y) {
        for (Move m : getLegalMoves()) {
            if (m instanceof PromoteMove && m.getX() == x && m.getY() == y) return (PromoteMove) m;
        }
        return null;
    }

    public EnPassantMove isEnPassantMove(int x, int y) {
        for (Move m : getLegalMoves()) {
            if (m instanceof EnPassantMove && m.getX() == x && m.getY() == y) return (EnPassantMove) m;
        }
        return null;
    }

    public boolean isLegalMove(int x, int y) {
        for (Move m : getLegalMoves()) {
            if (m.getX() == x && m.getY() == y) return true;
        }
        return false;
    }

    public String toString() {
        String col = Character.toString(97 + xp);
        int row = 8 - yp;
        return col+row;
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

    public Player getPlayer() {
        return player;
    }

    public int getIndex() {
        return index;
    }

    public Board getBoard() {
        return board;
    }

    public void setDidMove(boolean didMove) {
        this.didMove = didMove;
    }

    public boolean didMove() {
        return didMove;
    }
}
