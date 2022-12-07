package pieces;

import com.google.common.collect.ImmutableList;
import core.Board;
import core.Move;
import gui.Game;
import utils.Loader;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;

public class Bishop extends Piece {

    /**
     * Classe héritée par toutes les pièces
     *
     * @param xp    Position x en cases
     * @param yp    Position y en cases
     * @param board Instance du plateau
     */
    public Bishop(int xp, int yp, Board board) {
        super(xp, yp, board);
    }

    @Override
    public Collection<Move> getLegalMoves() {
        ArrayList<Move> res = new ArrayList<>();
        if (forcedMove != null) {
            res.add(forcedMove);
            return ImmutableList.copyOf(res);
        } else {
            return legalMoves();
        }
    }

    private Collection<Move> legalMoves() {
        ArrayList<Move> legalMoves = new ArrayList<>();

        topRight:
        for (int i = yp - 1; i >= 0; i--) {
            for (int j = xp + 1; j < 8; j++) {
                if (processMove(legalMoves, i, j)) break topRight;
            }
        }

        topLeft:
        for (int i = yp - 1; i >= 0; i--) {
            for (int j = xp - 1; j >= 0; j--) {
                if (processMove(legalMoves, i, j)) break topLeft;
            }
        }

        bottomRight:
        for (int i = yp + 1; i < 8; i++) {
            for (int j = xp + 1; j < 8; j++) {
                if (processMove(legalMoves, i, j)) break bottomRight;
            }
        }

        bottomLeft:
        for (int i = yp + 1; i < 8; i++) {
            for (int j = xp - 1; j >= 0; j--) {
                if (processMove(legalMoves, i, j)) break bottomLeft;
            }
        }

        return ImmutableList.copyOf(legalMoves);
    }


    /**
     * Détermine si les mouvements n'entrainent pas une situation d'échec du roi,
     * si oui supprime ces mouvements
     *
     * @param moves Liste de mouvements
     * @return La liste dépourvue de mouvements entrainant un échec
     */
    private Collection<Move> canMove(Collection<Move> moves) {
        int initial_x = xp;
        int initial_y = yp;

        // 1.) Simuler les moves
        for (Move m : moves) {
            // 2.) Si le move met le roi en echec, le supprimer
            //if (simulateMove(m.getXp(), m.getYp())) moves.remove(m);
        }
        // 3.) annuler le mouvement
        xp = initial_x;
        yp = initial_y;

        return moves;
    }

    /**
     * Permet d'ajouter les coups légaux et de déterminer si un coup est léthal
     * afin de pouvoir arreter l'itération si c'est le cas
     *
     * @param legalMoves Liste des mouvements légaux
     * @param i          Coordonnée y en cases
     * @param j          Coordonnée x en cases
     * @return           Vrai si un coup est ajouté
     */
    private boolean processMove(Collection<Move> legalMoves, int i, int j) {
        if (Math.abs(i - yp) == Math.abs(j - xp)){
            if (board.isCaseEmpty(j, i))
                legalMoves.add(new Move(j, i, false));
            else {
                if (board.getPiece(j, i).isWhite == !isWhite) {
                    legalMoves.add(new Move(j, i, true));
                }
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
            image = Loader.getImage(Loader.W_BISHOP);
        else
            image = Loader.getImage(Loader.B_BISHOP);
    }
}
