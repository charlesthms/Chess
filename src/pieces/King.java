package pieces;

import com.google.common.collect.ImmutableList;
import core.Board;
import core.CastleMove;
import core.Move;
import gui.Game;
import utils.Loader;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

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


    public Collection<CastleMove> getCastlingMoves() {
        ArrayList<CastleMove> castlingMoves = new ArrayList<>();

        int initial_x = xp;
        int initial_y = yp;

        // 1.) Le roi ET la tour ne doivent pas avoir bougé
        // 3.) Le roi n'est pas en position d'échec
        if (!didMove) {
            Rook leftRook;
            Rook rightRook;
            if (isWhite) {
                leftRook = (Rook) board.getPiece(0, 7);
                rightRook = (Rook) board.getPiece(7, 7);
            } else {
                leftRook = (Rook) board.getPiece(0, 0);
                rightRook = (Rook) board.getPiece(7, 0);
            }

            if (rightRook != null && !rightRook.didMove()) {
                // 2.) Il n'y a pas de pieces entre le roi et la tour
                if (board.isCaseEmpty(xp + 1, yp) && board.isCaseEmpty(xp + 2, yp)) {
                    // 4.) Le roi ne passe pas et n'atteri pas sur une case attaquée
                    if (!simulateMove(initial_x + 1, initial_y) && !simulateMove(initial_x + 2, initial_y)) {
                        castlingMoves.add(new CastleMove(new Move(initial_x + 2, initial_y, false), rightRook));
                    }
                }
            }
        }
        xp = initial_x;
        yp = initial_y;

        return castlingMoves;
    }

    public boolean isCastlingPossible() {
        return getCastlingMoves().size() > 0;
    }


    /**
     * Simule un mouvement et détermine s'il est en position d'échec ou non
     *
     * @param x Position x en cases
     * @param y Position y en cases
     * @return true si le coup simulé est en position d'échec
     */
    protected boolean simulateMove(int x, int y) {
        xp = x;
        yp = y;
        // générer tous les coups des opposants
        ArrayList<Move> allMoves = getOpponentsMoves();

        // pour chaque coup de chaque opposant, verifier s'il attaque le mouvement
        for (Move om : allMoves) {
            if (om.getXp() == xp && om.getYp() == yp) {
                // 5.) si la case est attaquée, le coup n'est pas légal
                return true;
            }
        }

        return false;
    }

    @Override
    public Collection<Move> getLegalMoves() {
        int initial_x = xp;
        int initial_y = yp;

        // 1.) générer tous les coups possibles
        ArrayList<Move> legalMoves = new ArrayList<>(generatePseudoLegalMoves());

        // 2.) pour chaque coup, faire le coup
        ArrayList<Move> copy = (ArrayList<Move>) legalMoves.clone();
        for (Move m : legalMoves) {
            if (simulateMove(m.getXp(), m.getYp())) copy.remove(m);
        }

        xp = initial_x;
        yp = initial_y;

        getCastlingMoves().forEach(cm -> {
            copy.add(cm.getMove());
        });

        return copy;
    }

    /**
     * Génère la liste des mouvements pseudo-légaux (cf: incluant les positions en échec)
     *
     * @return La liste des mouvements pseudo-légaux
     */
    private Collection<Move> generatePseudoLegalMoves() {

        ArrayList<Move> legalMoves = new ArrayList<>();

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
        return legalMoves;
    }

    /**
     * Génère la liste de tous les mouvements possibles sans aucune condition
     *
     * @return Liste
     */
    public Collection<Move> generateAllMoves() {

        ArrayList<Move> legalMoves = new ArrayList<>();

        for (int i = yp - 1; i <= yp + 1; i++) {
            for (int j = xp - 1; j <= xp + 1; j++) {
                legalMoves.add(new Move(j, i, false));
            }
        }

        return legalMoves;
    }


    /**
     * Permet de trouver la ou les pieces qui attaquent le roi
     *
     * @return La liste des pièces attanquant le roi
     */
    public Collection<Piece> attackingPieces() {
        ArrayList<Piece> res = new ArrayList<>();
        LinkedList<Piece> pieces = board.getPieces();

        for (Piece p : pieces) {
            if (p.isWhite == !isWhite) {
                for (Move m : p.getLegalMoves()) {
                    if (m.getXp() == xp && m.getYp() == yp) {
                        res.add(p);
                    }
                }
            }
        }

        return ImmutableList.copyOf(res);
    }

    /**
     * Détermine si le roi est en position d'échec
     *
     * @return Vrai si le roi est attaqué par une autre pièce
     */
    public boolean isInCheck() {
        ArrayList<Move> allMoves = getOpponentsMoves();

        for (Move m : allMoves) {
            if (m.getXp() == xp && m.getYp() == yp) return true;
        }

        return false;
    }

    /**
     * Génère la liste de tous les mouvements des pièces adverses
     *
     * @return Liste
     */
    protected ArrayList<Move> getOpponentsMoves() {
        ArrayList<Move> res = new ArrayList<>();

        for (Piece p : board.getPieces()) {
            if (p.isWhite() == !isWhite()) {
                if (p instanceof King k) res.addAll(k.generateAllMoves());
                else res.addAll(p.getLegalMoves());
            }
        }

        return res;
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
