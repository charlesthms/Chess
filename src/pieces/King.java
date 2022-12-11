package pieces;

import com.google.common.collect.ImmutableList;
import core.Board;
import core.Move;
import gui.Game;
import utils.Loader;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;

import static utils.Helpers.getThreatMap;
import static utils.Helpers.generateMoves;

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
            Piece[] pieces = board.getPieces();

            Piece save = pieces[m.getYp() * 8 + m.getXp()];
            pieces[index] = null;
            index = m.getYp() * 8 + m.getXp();
            pieces[index] = this;

            boolean[] threatMap = getThreatMap(pieces, this);

            if (!threatMap[index]) {
                legalMoves.add(m);
            }

            pieces[index] = save;
            index = initial_index;
            pieces[index] = this;
        }

        board.setPieces(pieces_backup);

        return ImmutableList.copyOf(legalMoves);
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
        g.drawImage(image, x, y, Game.TILES_SIZE, Game.TILES_SIZE, null);
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
