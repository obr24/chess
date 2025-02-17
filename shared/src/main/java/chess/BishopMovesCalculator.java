package chess;

import java.util.ArrayList;
import java.util.Collection;

import static chess.RookMovesCalculator.nextMoveValidRookBishop;

public class BishopMovesCalculator implements PieceMovesCalculator {
    private boolean nextMoveValid(int[] direction, ChessPosition curPosition) {
        return nextMoveValidRookBishop(direction, curPosition);
    }
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> bishopMoves = new ArrayList<>();
        ChessPiece curPiece = board.getPiece(myPosition);

        int curRow = myPosition.getRow();
        int curCol = myPosition.getColumn();

        ChessPosition newPosition = myPosition;
        ChessPiece newPositionPiece = null;
        ChessMove newMove = null;

        int[][] possibleDirections = new int[][] {{-1, -1}, {-1, 1}, {1, -1}, {1, 1}};

        for (int[] direction : possibleDirections) {
            int newRow = curRow;
            int newCol = curCol;
            newPosition = new ChessPosition(curRow, curCol);

            while (nextMoveValid(direction, newPosition)) {
                newRow += direction[0];
                newCol += direction[1];
                newPosition = new ChessPosition(newRow, newCol);
                newPositionPiece = board.getPiece(newPosition);
                newMove = new ChessMove(myPosition, newPosition, null);

                if (newPositionPiece == null) {
                    bishopMoves.add(newMove);
                } else if (newPositionPiece.getTeamColor() != curPiece.getTeamColor()) {
                    bishopMoves.add(newMove);
                    break;
                } else if (newPositionPiece.getTeamColor() == curPiece.getTeamColor()) {
                    break;
                }



            }
        }
        return bishopMoves;
    }
}