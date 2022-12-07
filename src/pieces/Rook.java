package pieces;

import com.google.common.collect.ImmutableList;
import core.Board;
import core.Move;
import core.Type;
import gui.Game;
import utils.Loader;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;

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
        if (forcedMove != null) {
            ArrayList<Move> res = new ArrayList<>();
            res.add(forcedMove);
            return res;
        } else {
            return legalMoves();
        }
    }

    private Collection<Move> legalMoves() {
        ArrayList<Move> legalMoves = new ArrayList<>();

        // Axe horizontal - droit
        right:
        for (int i = xp + 1; i < 8; i++) {
            if (processMove(legalMoves, i, yp)) break right;
        }
        // Axe horizontal - gauche
        left:
        for (int i = xp - 1; i >= 0; i--) {
            if (processMove(legalMoves, i, yp)) break left;
        }
        // Axe vertical - bas
        bottom:
        for (int i = yp + 1; i < 8; i++) {
            if (processMove(legalMoves, xp, i)) break bottom;
        }
        // Axe vertical - haut
        top:
        for (int i = yp - 1; i >= 0; i--) {
            if (processMove(legalMoves, xp, i)) break top;
        }

        return ImmutableList.copyOf(legalMoves);
    }

    private boolean processMove(ArrayList<Move> legalMoves, int a, int b) {
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

    @Override
    public boolean isLegalMove(int x, int y) {
        for (Move m : getLegalMoves()) {
            if (m.getX() == x && m.getY() == y) return true;
        }
        return false;
    }

    public boolean didMove() {
        return didMove;
    }

    public void setDidMove(boolean didMove) {
        this.didMove = didMove;
    }
}
