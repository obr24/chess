package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class KingMovesCalculator implements PieceMovesCalculator {
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> kingMoves = new ArrayList<>();

        ChessPiece currPiece = board.getPiece(position);

        int row = position.getRow();
        int col = position.getColumn();

        for (int rowAddition = -1; rowAddition < 2; rowAddition++) {
            for (int colAddition = -1; colAddition < 2; colAddition++) {
                int newRow = row + rowAddition;
                int newCol = col + colAddition;

                if (newRow >= 1 && newRow <= 8 && newCol >= 1 && newCol <=8) {
                    ChessPosition newPosition = new ChessPosition(newRow, newCol);
                    ChessPiece newPositionPiece = board.getPiece(newPosition);
                    if (newPositionPiece == null || newPositionPiece.getTeamColor() != currPiece.getTeamColor()) {
                        ChessMove newMove = new ChessMove(position, newPosition, null);
                        kingMoves.add(newMove);
                    }
                }
            }
        }
        return kingMoves;
    }
}
