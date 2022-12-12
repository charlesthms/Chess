package core;

import pieces.Piece;

public class EnPassantMove extends Move {

    private Piece target;

    /**
     * Créer un mouvement
     *
     * @param x        Coordonnée x en cases
     * @param y        Coordonnée y en cases
     * @param isLethal true si le coup engendre la mort d'une autre pièce
     */
    public EnPassantMove(int x, int y, boolean isLethal, Piece target) {
        super(x, y, isLethal);
        this.target = target;
    }

    public void killTarget() {
        target.getBoard().getPieces()[target.getIndex()] = null;
    }

    public Piece getTarget() {
        return target;
    }
}
