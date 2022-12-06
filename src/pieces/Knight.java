package pieces;

import com.google.common.collect.ImmutableList;
import core.Board;
import core.Move;
import gui.Game;
import utils.Loader;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;

public class Knight extends Piece {

    /**
     * Classe héritée par toutes les pièces
     *
     * @param xp    Position x en cases
     * @param yp    Position y en cases
     * @param board Instance du plateau
     */
    public Knight(int xp, int yp, Board board) {
        super(xp, yp, board);
    }

    @Override
    public Collection<Move> getLegalMoves() {
        ArrayList<Move> legalMoves = new ArrayList<>();
        var knightMoves = new int[][] { {1, 2}, {1, -2}, {-1, 2}, {-1, -2}, {-2, -1}, {-2, 1}, {2, -1}, {2, 1} };

        for (int[] knightMove : knightMoves) {
            if (board.isCaseEmpty(xp + knightMove[0], yp + knightMove[1]))
                legalMoves.add(new Move(xp + knightMove[0], yp + knightMove[1], false));
            else {
                if (board.getPiece(xp + knightMove[0], yp + knightMove[1]).isWhite == !isWhite) {
                    legalMoves.add(new Move(xp + knightMove[0], yp + knightMove[1], true));
                }
            }
        }

        return ImmutableList.copyOf(legalMoves);
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
            image = Loader.getImage(Loader.W_KNIGHT);
        else
            image = Loader.getImage(Loader.B_KNIGHT);
    }
}
