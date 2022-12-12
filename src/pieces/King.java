package pieces;

import com.google.common.collect.ImmutableList;
import core.Board;
import core.CastlingMove;
import core.Move;
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

    @Override
    public Collection<Move> getLegalMoves() {
        ArrayList<Move> pseudoLegalMoves = new ArrayList<>(generateMoves(this));
        ArrayList<Move> legalMoves = new ArrayList<>();

        int initial_index = index;
        Piece[] pieces_backup = board.getPieces().clone();

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
                    if (board.getPieces()[yp * 8 + i] != null || threatMap[yp * 8 + i]) {
                        toAdd = false;

                    }
                }

                if (toAdd) legalMoves.add(new CastlingMove(xp + 2, yp, false, rightRook));
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
                if (toAdd) legalMoves.add(new CastlingMove(xp - 2, yp, false, leftRook));
            }
        }
    }

    /**
     * Détermine si le roi est en position d'échec
     *
     * @return Vrai si le roi est attaqué par une autre pièce
     */
    public boolean isInCheck() {

        return false;
    }

    @Override
    public boolean isLegalMove(int x, int y) {
        for (Move m : getLegalMoves()) {
            if (m.getX() == x && m.getY() == y) return true;
        }
        return false;
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(image, x + Game.OFFSET, y + Game.OFFSET, Game.TILES_SIZE, Game.TILES_SIZE, null);
    }

    @Override
    public void update() {

    }

    @Override
    protected void loadImage() {
        if (isWhite)
            image = Loader.getImage(Loader.W_KING);
        else
            image = Loader.getImage(Loader.B_KING);
    }
}
