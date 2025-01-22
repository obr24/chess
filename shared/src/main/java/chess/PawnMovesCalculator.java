package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMovesCalculator implements PieceMovesCalculator {
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> pawnMoves = new ArrayList<>();

        ChessPiece currPiece = board.getPiece(position);
        int row = position.getRow();
        int col = position.getColumn();

        if (currPiece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            ChessPosition aheadPiecePosition = new ChessPosition(row + 1, col);
            ChessPiece aheadPiece = board.getPiece(aheadPiecePosition);
            ChessPiece aheadLeftPiece = board.getPiece(new ChessPosition(row, col - 1)); // TODO check if at left side
            ChessPiece aheadRightPiece = board.getPiece(new ChessPosition(row, col + 1)); // TODO check if at right side
            if (row == 2) {     // If in starting position
                ChessPosition twoAheadPiecePosition = new ChessPosition(row + 2, col);
                ChessPiece twoAheadPiece = board.getPiece(twoAheadPiecePosition);
                if (aheadPiece == null) {
                    pawnMoves.add(new ChessMove(position, aheadPiecePosition, null));
                }
                if (twoAheadPiece == null) {
                    pawnMoves.add(new ChessMove(position, twoAheadPiecePosition, null));
                }
            } else if (row < 7) {   // If not in promotion range
                if (aheadPiece == null) {
                    pawnMoves.add(new ChessMove(position, new ChessPosition(row + 1, col), null));
                }
            } else {        // If in promotion range

            }
        }
        return pawnMoves;
    }
}
