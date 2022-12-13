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

    public Knight(int xp, int yp, Board board, boolean isWhite) {
        super(xp, yp, board, isWhite);
    }


    @Override
    public Collection<Move> getLegalMoves() {
        ArrayList pseudoLegalMoves = new ArrayList(generateMoves(this));
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
            image = Loader.getImage(Loader.W_KNIGHT);
        else
            image = Loader.getImage(Loader.B_KNIGHT);
    }

    @Override
    public String toString() {
        return isWhite ? "♘" : "♞";
    }
}
