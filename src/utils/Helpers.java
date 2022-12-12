package utils;

import core.Board;
import core.Move;
import pieces.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class Helpers {

    private final static int[] knightMoves = new int[] { 6, -6, 10, -10, 15, -15, 17, -17 };

    private static Piece findKing(Piece p) {
        for (Piece candidat : p.getBoard().getPieces()) {
            if (candidat instanceof King && candidat.isWhite() == p.isWhite()) return candidat;
        }
        return null;
    }

    public static boolean isKingChecked(Piece p, boolean isKing) {
        Piece king = p;
        if (!isKing) king = findKing(p);

        boolean[] threatsMap = getThreatMap(p.getBoard().getPieces(), king);

        return threatsMap[king.getYp() * 8 + king.getXp()];
    }

    private static Collection<Move> generateKnightMoves(Piece p) {
        ArrayList<Move> moves = new ArrayList<>();

        for (int i = 0; i < knightMoves.length; i++) {
            int targetIndex = p.getIndex() + knightMoves[i];

            int x = targetIndex % 8;
            int y = targetIndex / 8;

            int maxDist = Math.max(Math.abs(p.getXp() - x), Math.abs(p.getYp() - y));

            if (targetIndex >= 0 && targetIndex < 64 && maxDist == 2) {
                Piece target = p.getBoard().getPiece(targetIndex);

                if (target != null) {
                    if (target.isWhite() == !p.isWhite()) {
                        moves.add(new Move(targetIndex % 8, targetIndex / 8, true));
                    }
                } else {
                    moves.add(new Move(targetIndex % 8, targetIndex / 8, false));
                }
            }
        }

        return moves;
    }

    private static Collection<Move> generateKingMoves(Piece p) {
        ArrayList<Move> moves = new ArrayList<>();

        for (int offset : Board.DIRECTION_OFFSETS) {
            int targetIndex = p.getIndex() + offset;

            int x = targetIndex % 8;
            int y = targetIndex / 8;

            int maxDist = Math.max(Math.abs(p.getXp() - x), Math.abs(p.getYp() - y));

            if (targetIndex >= 0 && targetIndex < 64 && maxDist == 1) {
                Piece target = p.getBoard().getPiece(targetIndex);
                if (target != null) {
                    if (target.isWhite() == !p.isWhite()) {
                        moves.add(new Move(targetIndex % 8, targetIndex / 8, true));
                    }
                } else {
                    moves.add(new Move(targetIndex % 8, targetIndex / 8, false));
                }
            }
        }

        return moves;
    }

    private static Collection<Move> generateMultiMoves(Piece p) {
        int startDirIndex = (p instanceof Bishop) ? 4 : 0;
        int endDirIndex = (p instanceof Rook) ? 4 : 8;

        ArrayList<Move> moves = new ArrayList<>();

        for (int directionIndex = startDirIndex; directionIndex < endDirIndex; directionIndex++) {
            for (int n = 0; n < Board.distanceToEdge[p.getIndex()][directionIndex]; n++) {

                int targetIndex = p.getIndex() + Board.DIRECTION_OFFSETS[directionIndex] * (n + 1);
                Piece target = p.getBoard().getPiece(targetIndex);

                if (target != null) {
                    if (target.isWhite() == p.isWhite()) break;
                    moves.add(new Move(targetIndex % 8, targetIndex / 8, true));
                    break;
                } else {
                    moves.add(new Move(targetIndex % 8, targetIndex / 8, false));
                }
            }
        }

        return moves;
    }

    public static Collection<Move> generateMoves(Piece p) {

        ArrayList<Move> moves;

        if (p instanceof Knight) {
            moves = new ArrayList<>(generateKnightMoves(p));
        } else if (p instanceof King) {
            moves = new ArrayList<>(generateKingMoves(p));
        } else {
            moves = new ArrayList<>(generateMultiMoves(p));
        }

        return moves;
    }

    /**
     * Génère la carte des menaces à partir d'une pièce donnée
     *
     * @param pieces Tableau des pièces
     * @param p Pièce source
     * @return Tableau boolean
     */
    public static boolean[] getThreatMap(Piece[] pieces, Piece p) {
        boolean[] map = new boolean[64];
        Arrays.fill(map, false);

        for (Piece opponent : pieces) {
            if (opponent != null && opponent.isWhite() == !p.isWhite()) {
                ArrayList<Move> opponentMoves = (ArrayList<Move>) generateMoves(opponent);
                for (Move m : opponentMoves) {
                    map[m.getYp() * 8 + m.getXp()] = true;
                }
            }
        }

        return map;
    }
}
