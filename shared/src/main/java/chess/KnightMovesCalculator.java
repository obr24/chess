package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KnightMovesCalculator implements PieceMovesCalculator {
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> knightMoves = new ArrayList<>();
        for (int cur_row_direction : new int[]{-1, 1}) {
            for (int cur_col_direction : new int[]{-1, 1}) {
                int new_row = position.getRow() + 1*cur_row_direction;
                int new_col = position.getColumn() + 2*cur_col_direction;
                if (new_row >= 1 && new_row <= 8 && new_col >= 1 && new_col <= 8) {
                    ChessPosition newPosition = new ChessPosition(new_row, new_col);
                    knightMoves.add(new ChessMove(position, newPosition, null));
                }
                new_row = position.getRow() + 2*cur_row_direction;
                new_col = position.getColumn() + 1*cur_col_direction;
                if (new_row >= 1 && new_row <= 8 && new_col >= 1 && new_col <= 8) {
                    ChessPosition newPosition = new ChessPosition(new_row, new_col);
                    knightMoves.add(new ChessMove(position, newPosition, null));
                }
            }
        }
        return knightMoves;
    }
}
