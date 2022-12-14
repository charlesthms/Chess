package utils;

import engine.Board;
import engine.moves.Move;
import engine.moves.PromoteMove;
import pieces.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class Helpers {

    private static final int[] knightMoves = new int[] { 6, -6, 10, -10, 15, -15, 17, -17 };
    private static final int[][] pawnAttacks = { {-9, -7}, {7, 9} };

    private static Piece findKing(Piece p) {
        return p.isWhite() ? p.getBoard().getWhitePlayer().getKing() : p.getBoard().getBlackPlayer().getKing();
    }

    public static boolean isKingChecked(Piece p, boolean isKing) {
        Piece king = p;
        if (!isKing) king = findKing(p);

        boolean[] threatsMap = getThreatMap(p.getBoard().getPieces(), king);

        return threatsMap[king.getYp() * 8 + king.getXp()];
    }

    private static Collection<Move> generateKnightMoves(Piece p) {
        ArrayList<Move> moves = new ArrayList<>();

        for (int knightMove : knightMoves) {
            int targetIndex = p.getIndex() + knightMove;

            int x = targetIndex % 8;
            int y = targetIndex / 8;

            int maxDist = Math.max(Math.abs(p.getXp() - x), Math.abs(p.getYp() - y));

            if (targetIndex >= 0 && targetIndex < 64 && maxDist == 2) {
                Piece target = p.getBoard().getPiece(targetIndex);

                if (target != null) {
                    if (target.isWhite() == !p.isWhite()) {
                        moves.add(new Move(p, targetIndex % 8, targetIndex / 8, true));
                    }
                } else {
                    moves.add(new Move(p,targetIndex % 8, targetIndex / 8, false));
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
                        moves.add(new Move(p,targetIndex % 8, targetIndex / 8, true));
                    }
                } else {
                    moves.add(new Move(p,targetIndex % 8, targetIndex / 8, false));
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
                    moves.add(new Move(p,targetIndex % 8, targetIndex / 8, true));
                    break;
                } else {
                    moves.add(new Move(p,targetIndex % 8, targetIndex / 8, false));
                }
            }
        }

        return moves;
    }

    private static ArrayList<Move> generatePawnMoves(Piece p) {
        ArrayList<Move> moves = new ArrayList<>();
        if (p.getYp() == 0) return moves;
        int startRow = p.isWhite() ? 6 : 1;
        int colorIndex = p.isWhite() ? 0 : 1;

        generatePathMoves(moves, p, 1);

        if (p.getYp() == startRow && p.getBoard().getPiece(p.isWhite() ? p.getIndex() - 8 : p.getIndex() + 8) == null) {
            generatePathMoves(moves, p, 2);
        }

        // Lethal moves
        for (int i = 0; i < 2; i++) {
            // Check si une case existe en diagonale du pion
            int targetIndex = p.getIndex() + pawnAttacks[colorIndex][i];
            Piece target = null;
            if (targetIndex >= 0 && targetIndex < 64)
                target = p.getBoard().getPiece(targetIndex);

            int x = targetIndex % 8;
            int y = targetIndex / 8;
            int maxDist = Math.max(Math.abs(p.getXp() - x), Math.abs(p.getYp() - y));

            if (target != null && maxDist == 1 && target.isWhite() == !p.isWhite()) {
                if (inRange(targetIndex, 0, 8) || inRange(targetIndex, 56, 64)) {
                    moves.add(new PromoteMove(p,targetIndex % 8, targetIndex / 8, true));
                } else {
                    moves.add(new Move(p,targetIndex % 8, targetIndex / 8, true));
                }
            }
        }

        return moves;
    }

    private static void generatePathMoves(ArrayList<Move> moves, Piece p, int n){
        int offset = p.isWhite() ? -8 : 8;
        int targetIndex = p.getIndex() + (offset * n);
        Piece target = p.getBoard().getPiece(targetIndex);

        if (target == null) {
            if (inRange(targetIndex, 0, 8) || inRange(targetIndex, 56, 64)) {
                moves.add(new PromoteMove(p,targetIndex % 8, targetIndex / 8, false));
            } else {
                moves.add(new Move(p,targetIndex % 8, targetIndex / 8, false));
            }
        }
    }

    public static Collection<Move> generateMoves(Piece p) {

        ArrayList<Move> moves;

        if (p instanceof Knight) {
            moves = new ArrayList<>(generateKnightMoves(p));
        } else if (p instanceof Pawn) {
            moves = new ArrayList<>(generatePawnMoves(p));
        } else if (p instanceof King) {
            moves = new ArrayList<>(generateKingMoves(p));
        } else {
            moves = new ArrayList<>(generateMultiMoves(p));
        }

        moves = removeKingFromMoves(moves);

        return moves;
    }

    private static ArrayList<Move> removeKingFromMoves(ArrayList<Move> moves) {
        ArrayList<Move> res = new ArrayList<>(moves);
        for (Move m : moves) {
            if (m.getPiece().getBoard().getPiece(m.getTyp() * 8 + m.getTxp()) instanceof King) {
                m.setLethal(false);
            }
        }
        return res;
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

                if (opponent instanceof Pawn pawn) {
                    int colorIndex = pawn.isWhite() ? 0 : 1;
                    int targetIndex1 = pawn.getIndex() + pawnAttacks[colorIndex][0];
                    int targetIndex2 = pawn.getIndex() + pawnAttacks[colorIndex][1];

                    int x1 = targetIndex1 % 8;
                    int y1 = targetIndex1 / 8;
                    int minDist1 = Math.max(Math.abs(pawn.getXp() - x1), Math.abs(pawn.getYp() - y1));

                    int x2 = targetIndex2 % 8;
                    int y2 = targetIndex2 / 8;
                    int minDist2= Math.max(Math.abs(pawn.getXp() - x2), Math.abs(pawn.getYp() - y2));

                    if (minDist1 == 1) {
                        map[targetIndex1] = true;
                    }

                    if (minDist2 == 1) {
                        map[targetIndex2] = true;
                    }
                }

                ArrayList<Move> opponentMoves = (ArrayList<Move>) generateMoves(opponent);
                for (Move m : opponentMoves) {
                    if (opponent instanceof Pawn && !m.isLethal()) {

                    } else
                        map[m.getTyp() * 8 + m.getTxp()] = true;
                }
            }
        }

        return map;
    }

    public static boolean inRange(int n, int min, int max) {
        return n >= min && n < max;
    }
}
