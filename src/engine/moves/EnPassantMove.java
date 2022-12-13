package engine.moves;

import pieces.Piece;

public class EnPassantMove extends Move {

    private Piece target;

    /**
     * Créer un mouvement
     *
     * @param piece    Pièce source
     * @param tx       Coordonnée x en cases de la cible
     * @param ty       Coordonnée y en cases de la cible
     * @param isLethal true si le coup engendre la mort d'une autre pièce
     */
    public EnPassantMove(Piece piece, int tx, int ty, boolean isLethal, Piece target) {
        super(piece, tx, ty, isLethal);
        this.target = target;
    }

    public void killTarget() {
        target.getBoard().getPieces()[target.getIndex()] = null;
    }

    public Piece getTarget() {
        return target;
    }
}
