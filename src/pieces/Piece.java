package pieces;

import engine.*;
import engine.moves.CastlingMove;
import engine.moves.EnPassantMove;
import engine.moves.Move;
import engine.moves.PromoteMove;
import engine.players.Player;
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


    /**
     * Instancie une pièce en forcant les paramètres de couleur et de mouvement
     *
     * @param xp Position x en cases
     * @param yp Position y en cases
     * @param board Instance du plateau
     * @param isWhite Définit la couleur
     * @param didMove Définit si la pièce a déjà bougée
     */
    public Piece(int xp, int yp, Board board, boolean isWhite, boolean didMove) {
        this.xp = xp;
        this.yp = yp;
        this.x = xp * Game.TILES_SIZE;
        this.y = yp * Game.TILES_SIZE;
        this.index = yp * 8 + xp;
        this.board = board;
        this.didMove = didMove;
        this.isWhite = isWhite;

        board.getPieces()[index] = this;
        loadImage();
    }

    public Piece(Piece piece) {
        this.index = piece.index;
        this.xp = piece.xp;
        this.yp = piece.yp;
        this.x = piece.x;
        this.y = piece.y;
        this.isWhite = piece.isWhite;
        this.didMove = piece.didMove;
        this.image = piece.image;
        this.board = piece.board;
        this.player = piece.player;
    }

    public abstract Collection<Move> getLegalMoves();
    
    public abstract void draw(Graphics g);
    
    public abstract String toString();

    protected abstract void loadImage();

    public Piece[] doMove(Piece[] pieces, Move m) {
        savedPiece = pieces[m.getTyp() * 8 + m.getTxp()];
        pieces[index] = null;
        index = m.getTyp() * 8 + m.getTxp();
        pieces[index] = this;

        return pieces;
    }

    public void undoMove(Piece[] pieces, int i) {
        pieces[index] = savedPiece;
        index = i;
        pieces[index] = this;
    }

    protected Collection<Move> simulateMoves(ArrayList<Move> pseudoLegalMoves) {
        ArrayList<Move> moves = new ArrayList<>();
        Piece[] pieces_backup = board.getPieces().clone();
        int initial_index = index;

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
        Piece[] pieces_backup = board.getPieces().clone();
        int initial_index = index;

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

    public void updatePos(int x, int y) {
        this.x = x;
        this.y = y;
        this.xp = x / Game.TILES_SIZE;
        this.yp = y / Game.TILES_SIZE;
        index = yp * 8 + xp;
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
            if (m instanceof CastlingMove && m.getTx() == x && m.getTy() == y) return (CastlingMove) m;
        }
        return null;
    }

    public PromoteMove isPromoteMove(int x, int y) {
        for (Move m : getLegalMoves()) {
            if (m instanceof PromoteMove && m.getTx() == x && m.getTy() == y) return (PromoteMove) m;
        }
        return null;
    }

    public EnPassantMove isEnPassantMove(int x, int y) {
        for (Move m : getLegalMoves()) {
            if (m instanceof EnPassantMove && m.getTx() == x && m.getTy() == y) return (EnPassantMove) m;
        }
        return null;
    }

    public boolean isLegalMove(int x, int y) {
        for (Move m : getLegalMoves()) {
            if (m.getTx() == x && m.getTy() == y) return true;
        }
        return false;
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
