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

        // 1.) generer tous les coups possibles
        // 2.) pour chaque coup, faire le coup
        // 3.) générer tous les coups des opposants
        // 4.) pour chaque coup de chaque opposant, verifier s'il attaque la case
        // 5.) si la case est attaquée, le coup n'est pas légal

        ArrayList<Move> legalMoves = new ArrayList<>();
        ArrayList<Move> allMoves = board.getAllMoves();

        for (int i = yp - 1; i <= yp + 1; i++) {
            for (int j = xp - 1; j <= xp + 1; j++) {
                // TODO: Prendre en compte les coups qui mettraient en position d'échec pour les exclures
                if (!isIn(allMoves, new Move(j, i, false)) && board.isCaseEmpty(j, i)) legalMoves.add(new Move(j, i, false));
                else {
                    if (board.getPiece(j, i) != null && board.getPiece(j, i).isWhite == !isWhite) {
                        legalMoves.add(new Move(j, i, true));
                        break;
                    }
                }
            }
        }

        return ImmutableList.copyOf(legalMoves);
    }

    /**
     * Teste si le mouvement m se trouve dans les moves
     *
     * @param moves Tous les mouvements légaux des pièces adverses
     * @param m Mouvement à tester
     * @return vrai si m est dans moves
     */
    private boolean isIn(Collection<Move> moves, Move m){
        for (Move move : moves) {
            if (move.getX() == m.getX() && move.getY() == m.getY()) return true;
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
            image = Loader.getImage(Loader.W_KING);
        else
            image = Loader.getImage(Loader.B_KING);
    }
}
