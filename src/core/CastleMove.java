package core;

import pieces.Rook;

public class CastleMove {

    private Rook rook;
    private Move move;

    /**
     * Représente le mouvement d'un castle
     *
     * @param move Mouvement associé
     * @param rook Rook associé
     */
    public CastleMove(Move move, Rook rook) {
        this.rook = rook;
        this.move = move;
    }

    public Rook getRook() {
        return rook;
    }

    public Move getMove() {
        return move;
    }
}
