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
            ChessPiece aheadPiece = board.getPiece(new ChessPosition(row + 1, col));
            ChessPiece aheadLeftPiece = board.getPiece(new ChessPosition(row, col - 1)); // TODO check if at left side
            ChessPiece aheadRightPiece = board.getPiece(new ChessPosition(row, col + 1)); // TODO check if at right side
            if (row == 2) {     // If in starting position

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
