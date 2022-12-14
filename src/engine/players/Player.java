package engine.players;

import engine.Board;
import pieces.King;

public abstract class Player {

    protected Board board;
    protected King king;
    protected boolean isWhite;

    public Player(Board board, King king, boolean isWhite) {
        this.isWhite = isWhite;
        this.board = board;
        this.king = king;
    }

    public King getKing() {
        return king;
    }

    public void setKing(King king) {
        this.king = king;
    }

    public boolean isWhite() {
        return isWhite;
    }
}
