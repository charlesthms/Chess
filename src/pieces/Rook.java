package pieces;

import com.google.common.collect.ImmutableList;
import core.Board;
import core.Move;
import gui.Game;
import utils.Loader;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;

import static utils.Helpers.generateMoves;

public class Rook extends Piece {

    private boolean didMove;

    public Rook(int x, int y, Board board) {
        super(x, y, board);
        didMove = false;
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
        g.drawImage(image, x, y, Game.TILES_SIZE, Game.TILES_SIZE, null);
    }

    @Override
    public void update() {

    }

    /**
     * Renvoie la liste des coups légaux depuis la position actuelle
     *
     * @return La liste des coups légaux
     */
    @Override
    public Collection<Move> getLegalMoves() {
        ArrayList<Move> pseudoLegalMoves = new ArrayList<>(generateMoves(this));
        ArrayList<Move> legalMoves = new ArrayList<>();

        return ImmutableList.copyOf(pseudoLegalMoves);
    }

    public boolean didMove() {
        return didMove;
    }

    public void setDidMove(boolean didMove) {
        this.didMove = didMove;
    }
}
