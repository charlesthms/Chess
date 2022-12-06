package pieces;

import com.google.common.collect.ImmutableList;
import core.Board;
import core.Move;
import gui.Game;
import utils.Loader;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;

public class Queen extends Piece {

    /**
     * Classe héritée par toutes les pièces
     *
     * @param xp    Position x en cases
     * @param yp    Position y en cases
     * @param board Instance du plateau
     */
    public Queen(int xp, int yp, Board board) {
        super(xp, yp, board);
    }

    @Override
    public Collection<Move> getLegalMoves() {
        ArrayList<Move> legalMoves = new ArrayList<>();

        // Axe horizontal - droit
        right:
        for (int i = xp + 1; i < 8; i++) {
            if (processRookMove(legalMoves, i, yp)) break right;
        }
        // Axe horizontal - gauche
        left:
        for (int i = xp - 1; i >= 0; i--) {
            if (processRookMove(legalMoves, i, yp)) break left;
        }
        // Axe vertical - bas
        bottom:
        for (int i = yp + 1; i < 8; i++) {
            if (processRookMove(legalMoves, xp, i)) break bottom;
        }
        // Axe vertical - haut
        top:
        for (int i = yp - 1; i >= 0; i--) {
            if (processRookMove(legalMoves, xp, i)) break top;
        }

        topRight:
        for (int i = yp - 1; i >= 0; i--) {
            for (int j = xp + 1; j < 8; j++) {
                if (processBishopMove(legalMoves, i, j)) break topRight;
            }
        }

        topLeft:
        for (int i = yp - 1; i >= 0; i--) {
            for (int j = xp - 1; j >= 0; j--) {
                if (processBishopMove(legalMoves, i, j)) break topLeft;
            }
        }

        bottomRight:
        for (int i = yp + 1; i < 8; i++) {
            for (int j = xp + 1; j < 8; j++) {
                if (processBishopMove(legalMoves, i, j)) break bottomRight;
            }
        }

        bottomLeft:
        for (int i = yp + 1; i < 8; i++) {
            for (int j = xp - 1; j >= 0; j--) {
                if (processBishopMove(legalMoves, i, j)) break bottomLeft;
            }
        }

        return legalMoves;
    }

    public Collection<Move> getIllegalMoves() {
        ArrayList<Move> legalMoves = new ArrayList<>();

        // Axe horizontal - droit
        for (int i = xp + 1; i < 8; i++) processRookMove(legalMoves, i, yp);
        // Axe horizontal - gauche
        for (int i = xp - 1; i >= 0; i--) processRookMove(legalMoves, i, yp);
        // Axe vertical - bas
        for (int i = yp + 1; i < 8; i++) processRookMove(legalMoves, xp, i);
        // Axe vertical - haut
        for (int i = yp - 1; i >= 0; i--) processRookMove(legalMoves, xp, i);

        for (int i = yp - 1; i >= 0; i--)
            for (int j = xp + 1; j < 8; j++) processBishopMove(legalMoves, i, j);

        for (int i = yp - 1; i >= 0; i--)
            for (int j = xp - 1; j >= 0; j--) processBishopMove(legalMoves, i, j);

        for (int i = yp + 1; i < 8; i++)
            for (int j = xp + 1; j < 8; j++) processBishopMove(legalMoves, i, j);

        for (int i = yp + 1; i < 8; i++)
            for (int j = xp - 1; j >= 0; j--) processBishopMove(legalMoves, i, j);

        return legalMoves;
    }

    private boolean processRookMove(ArrayList<Move> legalMoves, int a, int b) {
        if (board.isCaseEmpty(a, b))
            legalMoves.add(new Move(a, b, false));
        else {
            if (board.getPiece(a, b).isWhite == !isWhite) {
                legalMoves.add(new Move(a, b, true));
            }
            return true;
        }

        return false;
    }

    private boolean processBishopMove(Collection<Move> legalMoves, int i, int j) {
        if (Math.abs(i - yp) == Math.abs(j - xp)){
            if (board.isCaseEmpty(j, i))
                legalMoves.add(new Move(j, i, false));
            else
            if (board.getPiece(j, i).isWhite == !isWhite) {
                legalMoves.add(new Move(j, i, true));
                return true;
            }
        }
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
        g.drawImage(image, x, y, Game.TILES_SIZE, Game.TILES_SIZE, null);
    }

    @Override
    public void update() {

    }

    @Override
    protected void loadImage() {
        if (isWhite)
            image = Loader.getImage(Loader.W_QUEEN);
        else
            image = Loader.getImage(Loader.B_QUEEN);
    }
}
