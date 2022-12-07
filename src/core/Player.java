package core;

import pieces.King;
import pieces.Piece;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;

public class Player {

    private boolean isWhite;
    private Board board;
    private King king;
    private boolean isInCheck;
    private int turn;

    public Player(Board board, boolean isWhite, King king, int turn) {
        this.isWhite = isWhite;
        this.board = board;
        this.king = king;
        this.turn = turn;
        isInCheck = false;
    }

    public Collection<Piece> getPieces() {
        ArrayList<Piece> res = new ArrayList<>();
        for (Piece p : board.getPieces()) {
            if (p.isWhite() == isWhite) res.add(p);
        }
        return res;
    }

    public King getKing() {
        return king;
    }

    public boolean isInCheck() {
        return king.isInCheck();
    }

    public void setInCheck(boolean inCheck) {
        isInCheck = inCheck;
    }

    public int getTurn() {
        return turn;
    }

    public boolean isWhite() {
        return isWhite;
    }

    public void setKing(King king) {
        this.king = king;
    }
}
