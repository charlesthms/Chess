package pieces;

import com.google.common.collect.ImmutableList;
import engine.Board;
import engine.moves.EnPassantMove;
import engine.moves.Move;
import gui.Game;
import utils.Loader;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;

import static utils.Helpers.generateMoves;

public class Pawn extends Piece {

    private int previousYp;
    private int lastMoveIndex;

    /**
     * Classe héritée par toutes les pièces
     *
     * @param xp    Position x en cases
     * @param yp    Position y en cases
     * @param board Instance du plateau
     */
    public Pawn(int xp, int yp, Board board) {
        super(xp, yp, board);
    }

    public Pawn(int xp, int yp, Board board, boolean isWhite, boolean didMove) {
        super(xp, yp, board, isWhite, didMove);
    }

    public Pawn(Piece p) {
        super(p);
    }

    @Override
    public Collection<Move> getLegalMoves() {

        ArrayList<Move> pseudoLegalMoves = new ArrayList<>(generateMoves(this));
        // En passant
        int vect = isWhite ? -1 : 1;

        if (yp == 4 || yp == 3) {
            Piece leftTarget = board.getPiece(index - 1);
            if (leftTarget instanceof Pawn p && (p.getPreviousYp() == 1 || p.getPreviousYp() == 6) && Board.lastMovedPiece == leftTarget && p.getLastMoveIndex() == index - 1) {
                pseudoLegalMoves.add(new EnPassantMove(this, xp - 1, yp + vect, true, board.getPiece(index - 1)));
            }

            Piece rightTarget = board.getPiece(index + 1);
            if (rightTarget instanceof Pawn p && (p.getPreviousYp() == 1 || p.getPreviousYp() == 6) && Board.lastMovedPiece == rightTarget && p.getLastMoveIndex() == index + 1) {
                pseudoLegalMoves.add(new EnPassantMove(this, xp + 1, yp + vect, true, board.getPiece(index + 1)));
            }
            pseudoLegalMoves = simulateEnPassant(pseudoLegalMoves);
        }

        ArrayList<Move> legalMoves = new ArrayList<>(simulateMoves(pseudoLegalMoves));

        return ImmutableList.copyOf(legalMoves);
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(image, x + Game.OFFSET, y + Game.OFFSET, Game.TILES_SIZE, Game.TILES_SIZE, null);
    }

    @Override
    protected void loadImage() {
        if (isWhite)
            image = Loader.getImage(Loader.W_PAWN);
        else
            image = Loader.getImage(Loader.B_PAWN);
    }

    public void setLastMoveIndex(int lastMoveIndex) {
        this.lastMoveIndex = lastMoveIndex;
    }

    public int getLastMoveIndex() {
        return lastMoveIndex;
    }

    public int getPreviousYp() {
        return previousYp;
    }

    public void setPreviousYp(int previousYp) {
        this.previousYp = previousYp;
    }

    @Override
    public String toString() {
        return isWhite ? "♙" : "♟︎";
    }

    @Override
    public String toFen() {
        return isWhite ? "P" : "p";
    }
}
