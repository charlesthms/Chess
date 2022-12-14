package pieces;

import com.google.common.collect.ImmutableList;
import engine.Board;
import engine.moves.Move;
import gui.Game;
import utils.Loader;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;

import static utils.Helpers.generateMoves;

public class Rook extends Piece {

    public Rook(int x, int y, Board board) {
        super(x, y, board);
        didMove = false;
    }

    public Rook(int xp, int yp, Board board, boolean isWhite, boolean didMove) {
        super(xp, yp, board, isWhite, didMove);
    }

    public Rook(Piece p) {
        super(p);
    }

    @Override
    public void loadImage() {
        if (isWhite)
            image = Loader.getImage(Loader.W_TOWER);
        else
            image = Loader.getImage(Loader.B_TOWER);
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(image, x + Game.OFFSET, y + Game.OFFSET, Game.TILES_SIZE, Game.TILES_SIZE, null);
    }

    /**
     * Renvoie la liste des coups légaux depuis la position actuelle
     *
     * @return La liste des coups légaux
     */
    @Override
    public Collection<Move> getLegalMoves() {
        ArrayList<Move> pseudoLegalMoves = new ArrayList<>(generateMoves(this));
        ArrayList<Move> legalMoves = new ArrayList<>(simulateMoves(pseudoLegalMoves));

        return ImmutableList.copyOf(legalMoves);
    }

    @Override
    public String toString() {
        return isWhite ? "♖" : "♜";
    }

    @Override
    public String toFen() {
        return isWhite ? "R" : "r";
    }
}
