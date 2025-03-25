package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import model.GameData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import static ui.EscapeSequences.*;

public class PrintBoard {
    public static String print(GameData gameData, String perspective) {

        StringBuilder outputString = new StringBuilder(SET_BG_COLOR_LIGHT_GREY + SET_BG_COLOR_BLACK);
        Collection<String> boardArray = new ArrayList<String>();

        ChessBoard board = gameData.game().getBoard();
        ChessPiece[][] squares = board.getSquares();

        String[] top = rowHeaders();
        if (Objects.equals(perspective, "white") || Objects.equals(perspective, "observer")) {
            for (String s : top) {
                outputString.append(s);
            }
        } else {
            for (int i = top.length-1; i >= 0; i--) {
                outputString.append(top[i]);
            }
        }

        if (Objects.equals(perspective, "white") || Objects.equals(perspective, "observer")) {
            for (int i = squares.length-1; i >= 0; i--) {
                outputString.append("\n");
                outputString.append(SET_BG_COLOR_BLACK);
                outputString.append(String.format(" %d ", i+1));
                for (int j = 1; j <= squares.length; j++) {
                    if ((i+j) % 2 == 0) {
                        outputString.append(SET_BG_COLOR_WHITE);
                    } else {
                        outputString.append(SET_BG_COLOR_RED);
                    }
                    outputString.append(getRow(squares, i)[j]);
                }
                outputString.append(SET_BG_COLOR_BLACK);
                outputString.append(String.format(" %d ", i + 1));
            }
        } else {
            for (int i = 0; i < squares.length; i++) {
                outputString.append("\n");
                outputString.append(SET_BG_COLOR_BLACK);
                outputString.append(String.format(" %d ", i+1));
                for (int j = 1; j <= squares.length; j++) {
                    if ((i+j) % 2 == 0) {
                        outputString.append(SET_BG_COLOR_RED);
                    } else {
                        outputString.append(SET_BG_COLOR_WHITE);
                    }
                    outputString.append(getRowReverse(squares, i)[j]);
                }
                outputString.append(SET_BG_COLOR_BLACK);
                outputString.append(String.format(" %d ", i + 1));
            }
        }
        outputString.append("\n");

        if (Objects.equals(perspective, "white") || Objects.equals(perspective, "observer")) {
            for (String s : top) {
                outputString.append(s);
            }
        } else {
            for (int i = top.length-1; i >= 0; i--) {
                outputString.append(top[i]);
            }
        }
        return outputString.toString();
    }

    private static String[] rowHeaders() {
        String[] outputStrings = new String[10];
        String[] headers = {" ", "a", "b", "c", "d", "e", "f", "g", "h", " "};
        for (int i = 0; i < headers.length; i++) {
            outputStrings[i] = String.format(" %s ", headers[i]);
        }
        return outputStrings;
    }

    private static String[] getRow(ChessPiece[][] squares, int row) {
        String[] outputStrings = new String[10];
        String[] rowCells = new String[8];
        for (int i = 0; i < squares.length; i++) {
            rowCells[i] = getPieceChar(squares[row][i]);
        }
        for (int i = 0; i < squares.length; i++) {
            outputStrings[i+1] = String.format(" %s ", rowCells[i]);
        }
        outputStrings[0] = String.format(" %d ", row+1);
        outputStrings[9] = String.format(" %d ", row+1);
        return outputStrings;
    }

    private static String[] getRowReverse(ChessPiece[][] squares, int row) {
        String[] outputStrings = new String[10];
        String[] rowCells = new String[8];
        for (int i = 0; i < squares.length; i++) {
            rowCells[i] = getPieceChar(squares[row][i]);
        }
        for (int i = 0; i < squares.length; i++) {
            outputStrings[8-i] = String.format(" %s ", rowCells[i]);
        }
        outputStrings[0] = String.format(" %d ", row+1);
        outputStrings[9] = String.format(" %d ", row+1);
        return outputStrings;
    }

    private static String getPieceChar(ChessPiece chessPiece) {
        if (chessPiece == null) {
            return " ";
        } else if (chessPiece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            char pieceChar = switch (chessPiece.getPieceType()) {
                case ROOK -> 'R';
                case BISHOP -> 'B';
                case KNIGHT -> 'N';
                case KING -> 'K';
                case QUEEN -> 'Q';
                case PAWN -> 'P';
                case null, default -> 'X';
            };
            return String.valueOf(pieceChar);
        } else {
            char pieceChar = switch (chessPiece.getPieceType()) {
                case ROOK -> 'r';
                case BISHOP -> 'b';
                case KNIGHT -> 'n';
                case KING -> 'k';
                case QUEEN -> 'q';
                case PAWN -> 'p';
                case null, default -> 'x';
            };
            return String.valueOf(pieceChar);
        }
    }
}
