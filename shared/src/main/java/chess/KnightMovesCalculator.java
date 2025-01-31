package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KnightMovesCalculator implements PieceMovesCalculator {
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> knightMoves = new ArrayList<>();

        ChessPiece curPiece = board.getPiece(myPosition);

        int curRow = myPosition.getRow();
        int curCol = myPosition.getColumn();

        ChessPosition newPosition = myPosition;
        ChessPiece newPositionPiece = null; // TODO: can be null?
        ChessMove newMove = null; // TODO: can be null?

        int[][] possibleDirections = new int[][] {{-1, -2}, {-1, 2}, {-2, -1}, {-2, 1},
                {1, -2}, {1, 2}, {2, -1}, {2, 1}};

        for (int[] direction : possibleDirections) {
            int newRow = curRow + direction[0];
            int newCol = curCol + direction[1];

            if (newRow <= 8 && newRow >= 1 && newCol <= 8 && newCol >= 1) {
                newPosition = new ChessPosition(newRow, newCol);

                newPositionPiece = board.getPiece(newPosition);

                if (newPositionPiece == null) {
                    newMove = new ChessMove(myPosition, newPosition, null);
                    knightMoves.add(newMove);
                } else if (newPositionPiece.getTeamColor() != curPiece.getTeamColor()) {
                    newMove = new ChessMove(myPosition, newPosition, null);
                    knightMoves.add(newMove);
                }
            }
        }
        return knightMoves;
    }
}