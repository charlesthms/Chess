package pieces;

import com.google.common.collect.ImmutableList;
import engine.Board;
import engine.moves.CastlingMove;
import engine.moves.Move;
import gui.Game;
import utils.Loader;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;

import static utils.Helpers.getThreatMap;
import static utils.Helpers.generateMoves;
import static utils.Helpers.isKingChecked;

public class King extends Piece {

    /**
     * Classe héritée par toutes les pièces
     *
     * @param xp    Position x en cases
     * @param yp    Position y en cases
     * @param board Instance du plateau
     */
    public King(int xp, int yp, Board board) {
        super(xp, yp, board);
    }

    public King(int xp, int yp, Board board, boolean isWhite, boolean didMove) {
        super(xp, yp, board, isWhite, didMove);
    }

    public King(Piece k) {
        super(k);
    }

    @Override
    public Collection<Move> getLegalMoves() {
        ArrayList<Move> pseudoLegalMoves = new ArrayList<>(generateMoves(this));
        ArrayList<Move> legalMoves = new ArrayList<>();

        Piece[] pieces_backup = board.getPieces().clone();
        int initial_index = index;

        for (Move m : pseudoLegalMoves) {
            Piece[] pieces;
            pieces = doMove(board.getPieces(), m);
            boolean[] threatMap = getThreatMap(pieces, this);

            if (!threatMap[index]) {
                legalMoves.add(m);
            }

            undoMove(pieces, initial_index);
        }

        board.setPieces(pieces_backup);

        processCastling(legalMoves);

        return ImmutableList.copyOf(legalMoves);
    }

    private void processCastling(ArrayList<Move> legalMoves) {
        boolean[] threatMap = getThreatMap(board.getPieces(), this);
        boolean toAdd = true;

        if (!didMove && !isKingChecked(this, true)) {
            // Si le roi n'a pas bougé et n'est pas en échec
            // Right rook
            Piece rightRook = board.getPieces()[isWhite ? 63 : 7];
            if (rightRook instanceof Rook r && !r.didMove && r.getYp() == yp) {
                // Si la tour existe, n'as pas bougée et est au même niveau y
                for (int i = xp + 1; i < xp + 3; i++) {
                    // si les cases sont vides et ne sont pas attaquées
                    if (yp * 8 + i > 63 || board.getPieces()[yp * 8 + i] != null || threatMap[yp * 8 + i]) {
                        toAdd = false;
                    }
                }

                if (toAdd) legalMoves.add(new CastlingMove(this, xp + 2, yp, false, rightRook));
            }
            //Left rook
            Piece leftRook = board.getPieces()[isWhite ? 56 : 0];
            toAdd = true;
            if (leftRook instanceof Rook r && !r.didMove && r.getYp() == yp) {
                // Si la tour existe, n'as pas bougée et est au même niveau y
                for (int i = xp - 1; i > xp - 4; i--) {
                    // si les cases sont vides et ne sont pas attaquées
                    if (board.getPieces()[yp * 8 + i] != null || threatMap[yp * 8 + i]) toAdd = false;
                }
                if (toAdd) legalMoves.add(new CastlingMove(this, xp - 2, yp, false, leftRook));
            }
        }
    }

    @Override
    public Move isLegalMove(int x, int y) {
        for (Move m : getLegalMoves()) {
            if (m.getTx() == x && m.getTy() == y) return m;
        }
        return null;
    }

    @Override
    public String toString() {
        return isWhite ? "♔" : "♚";
    }

    @Override
    public String toFen() {
        return isWhite ? "K" : "k";
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(image, x + Game.OFFSET, y + Game.OFFSET, Game.TILES_SIZE, Game.TILES_SIZE, null);
    }

    @Override
    protected void loadImage() {
        if (isWhite)
            image = Loader.getImage(Loader.W_KING);
        else
            image = Loader.getImage(Loader.B_KING);
    }

}
