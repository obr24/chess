package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KnightMovesCalculator implements PieceMovesCalculator {
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> knightMoves = new ArrayList<>();
        for (int cur_row_direction : new int[]{-1, 1}) {
            for (int cur_col_direction : new int[]{-1, 1}) {
                for (int[] multiplierDirection : new int[][]{{1, 2}, {2, 1}}) {
                    int new_row = position.getRow() + multiplierDirection[0] * cur_row_direction;
                    int new_col = position.getColumn() + multiplierDirection[1] * cur_col_direction;
                    if (new_row >= 1 && new_row <= 8 && new_col >= 1 && new_col <= 8) {
                        ChessPosition newPosition = new ChessPosition(new_row, new_col);
                        ChessPiece currPiece = board.getPiece(position);
                        ChessPiece pieceInNewPosition = board.getPiece(newPosition);
                        if ((pieceInNewPosition == null) || (pieceInNewPosition.getTeamColor() != currPiece.getTeamColor())) {
                            knightMoves.add(new ChessMove(position, newPosition, null));
                        }
                    }
                }
            }
        }
        return knightMoves;
    }
}
