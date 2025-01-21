package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.NoSuchElementException;

public class BishopMovesCalculator implements PieceMovesCalculator {
    private boolean validMove(int col_inc_forward, int row_inc_up, int col, int row) {
        if (col_inc_forward == 1 && col >= 8) {
            return false;
        } else if (col_inc_forward != 1 && col <= 1) {
            return false;
        }
        if (row_inc_up == 1 && row >= 8) {
            return false;
        } else if (row_inc_up != 1 && row <= 1) {
            return false;
        }
        return true;
    }
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> chess_moves = new ArrayList<>();

        int col_inc_forward = 0;
        int row_inc_up = 0;

        ChessPiece currPiece = board.getPiece(position);

        for (col_inc_forward = 0; col_inc_forward <= 1; col_inc_forward++) {
            for (row_inc_up = 0; row_inc_up <= 1; row_inc_up++) {
                int col = position.getColumn();
                int row = position.getRow();

                //while (col < 8 && col > 1 && row < 8 && row > 1) {
                while (validMove(col_inc_forward, row_inc_up, col, row)) {
                    if (col_inc_forward == 1) { // TODO: turn this whole if else into ternary
                        col++;
                    } else {
                        col--;
                    }
                    if (row_inc_up == 1) {
                        row++;
                    } else {
                        row--;
                    }
                    ChessPosition newPosition = new ChessPosition(row, col);
                    ChessPiece newPositionPiece = board.getPiece(newPosition);

                    ChessMove newChessMove = new ChessMove(position, newPosition, null);
                    if (newPositionPiece == null) {
                        chess_moves.add(newChessMove);
                    } else if (currPiece.getTeamColor() != newPositionPiece.getTeamColor()) {
                        chess_moves.add(newChessMove);
                        break;
                    } else if (currPiece.getTeamColor() == newPositionPiece.getTeamColor()) { // TODO: change to .equals()
                        break;
                    }
                }
            }
        }
        return chess_moves;
    }
}
