package pieces;

import com.google.common.collect.ImmutableList;
import core.Board;
import core.Move;
import gui.Game;
import utils.Loader;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;

public class King extends Piece {

    private boolean isCheck;

    /**
     * Classe héritée par toutes les pièces
     *
     * @param xp    Position x en cases
     * @param yp    Position y en cases
     * @param board Instance du plateau
     */
    public King(int xp, int yp, Board board) {
        super(xp, yp, board);
        isCheck = false;
    }

    @Override
    public Collection<Move> getLegalMoves() {
        // TODO: gérer les coups légaux pour le roi adverse
        int initial_x = xp;
        int initial_y = yp;

        ArrayList<Move> legalMoves = new ArrayList<>();

        // 1.) générer tous les coups possibles
        for (int i = yp - 1; i <= yp + 1; i++) {
            for (int j = xp - 1; j <= xp + 1; j++) {
                if (board.isCaseEmpty(j, i)) legalMoves.add(new Move(j, i, false));
                else {
                    if (board.getPiece(j, i).isWhite == !isWhite) {
                        legalMoves.add(new Move(j, i, true));
                        break;
                    }
                }
            }
        }

        // 2.) pour chaque coup, faire le coup
        ArrayList<Move> copy = (ArrayList<Move>) legalMoves.clone();
        for (Move m : legalMoves) {
            xp = m.getXp();
            yp = m.getYp();
            // 3.) générer tous les coups des opposants
            ArrayList<Move> allMoves = board.getAllMoves();

            // 4.) pour chaque coup de chaque opposant, verifier s'il attaque la case
            for (Move om : allMoves) {
                if (om.getXp() == xp && om.getYp() == yp) {
                    // 5.) si la case est attaquée, le coup n'est pas légal
                    copy.remove(m);
                }
            }
        }
        xp = initial_x;
        yp = initial_y;

        return copy;
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
