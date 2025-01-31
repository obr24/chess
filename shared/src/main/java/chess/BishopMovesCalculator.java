package chess;

import java.util.ArrayList;
import java.util.Collection;

public class BishopMovesCalculator implements PieceMovesCalculator {
    private boolean nextMoveValid(int[] direction, ChessPosition curPosition) {
        int curRow = curPosition.getRow();
        int curCol = curPosition.getColumn();

        if (curRow >= 8 && direction[0] == 1) {
            return false;
        } else if (curRow <= 1 && direction[0] == -1) {
            return false;
        } else if (curCol >= 8 && direction[1] == 1) {
            return false;
        } else if (curCol <= 1 && direction[1] == -1) {
            return false;
        }
        return true;
    }
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> bishopMoves = new ArrayList<>();
        ChessPiece curPiece = board.getPiece(myPosition);

        int curRow = myPosition.getRow();
        int curCol = myPosition.getColumn();

        ChessPosition newPosition = myPosition;
        ChessPiece newPositionPiece = null; // TODO: can be null?
        ChessMove newMove = null; // TODO: can be null?

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