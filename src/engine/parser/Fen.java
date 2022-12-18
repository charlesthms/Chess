package engine.parser;

import engine.Board;
import pieces.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Fen {

    public static final String DEFAULT_FEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

    public static String getFen(Board board) {
        StringBuilder fen = new StringBuilder();
        for (int y = 0; y < 8; y++) {

            int emptyCase = 0;
            StringBuilder row = new StringBuilder();

            for (int x = 0; x <= 7; x++) {
                Piece p = board.getPiece(y * 8 + x);

                if (x == 7 && emptyCase > 0 && p == null) {
                    row.append(emptyCase + 1);
                    emptyCase = 0;
                }

                if (p != null) {
                    if (emptyCase > 0) {
                        row.append(emptyCase);
                        emptyCase = 0;
                    }
                    row.append(p.toFen());
                } else {
                    emptyCase++;
                }
            }

            if (y != 7) {
                row.append("/");
            } else {
                row.append(" ");
            }

            fen.append(row);
        }

        if (board.getCurrentPlayer().isWhite()){
            fen.append("w ");
        } else {
            fen.append("b ");
        }

        boolean castle = false;
        if (!board.getWhitePlayer().getKing().didMove()) {
            if (board.getPiece(63) instanceof Rook && !board.getPiece(63).didMove()) {
                fen.append("K");
                castle = true;
            }

            if (board.getPiece(56) instanceof Rook && !board.getPiece(56).didMove()) {
                fen.append("Q");
                castle = true;
            }

        }

        if (!board.getBlackPlayer().getKing().didMove()) {
            if (board.getPiece(7) instanceof Rook && !board.getPiece(7).didMove()) {
                fen.append("k");
                castle = true;
            }

            if (board.getPiece(0) instanceof Rook && !board.getPiece(0).didMove()) {
                fen.append("q");
                castle = true;
            }

        }

        if (!castle) fen.append("- ");
        else fen.append(" ");

        fen.append("- ");
        fen.append("0 0 ");


        return fen.toString();
    }

    /**
     * Initialise le plateau via la notation de FEN
     *
     * @param fen String FEN
     * @param board Instance du plateau
     */
    public static void parseFen(String fen, Board board)
    {
        String fenPattern = "^((([pnbrqkPNBRQK1-8]{1,8})\\/?){8})\\s+(b|w)\\s+(-|K?Q?k?q)\\s+(-|[a-h][3-6])\\s*((\\d*)\\s*(\\d*))*$";
        Pattern pattern = Pattern.compile(fenPattern);

        Matcher matcher = pattern.matcher(fen);

        if (matcher.matches()) {
            // Extract the individual pieces of information from the FEN string
            String pieces = matcher.group(1);
            String activeColor = matcher.group(4);

            String castlingRights = matcher.group(4);
            String enPassant = matcher.group(5);
            String halfmoveClock = matcher.group(6);
            String fullmoveNumber = matcher.group(7);

            String[] rows = pieces.split("/");

            King bk = null;
            King wk = null;

            for (int y = 0; y < rows.length; y++) {

                char[] splitedRow = rows[y].toCharArray();

                int rx = 0;
                for (char piece : splitedRow) {
                    if (piece < 58) {
                        rx += piece - 48;
                    } else {
                        switch (piece) {
                            case 'p' -> {
                                new Pawn(rx, y, board, false, false);
                            }
                            case 'q' -> {
                                new Queen(rx, y, board, false, false);
                            }
                            case 'k' -> {
                                bk = new King(rx, y, board, false, false);
                            }
                            case 'n' -> {
                                new Knight(rx, y, board, false, false);
                            }
                            case 'r' -> {
                                new Rook(rx, y, board, false, false);
                            }
                            case 'b' -> {
                                new Bishop(rx, y, board, false, false);
                            }
                            case 'P' -> {
                                new Pawn(rx, y, board, true, false);
                            }
                            case 'Q' -> {
                                new Queen(rx, y, board, true, false);
                            }
                            case 'K' -> {
                                wk = new King(rx, y, board, true, false);
                            }
                            case 'N' -> {
                                new Knight(rx, y, board, true, false);
                            }
                            case 'R' -> {
                                new Rook(rx, y, board, true, false);
                            }
                            case 'B' -> {
                                new Bishop(rx, y, board, true, false);
                            }
                        }
                        rx += 1;
                    }
                }
            }

            if (activeColor.equals("w")) {
                board.setCurrentPlayer(board.getWhitePlayer());
                board.getWhitePlayer().setKing(wk);
                board.getBlackPlayer().setKing(bk);
                board.setTurn(0);
            } else {
                board.setCurrentPlayer(board.getBlackPlayer());
                board.getWhitePlayer().setKing(wk);
                board.getBlackPlayer().setKing(bk);
                board.setTurn(1);
            }

        } else {
            System.out.println("Erreur de format FEN");
        }

    }
}
