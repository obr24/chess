package chess;

import java.util.ArrayList;
import java.util.Collection;

import static chess.ChessPiece.PieceType;

public class PawnMovesCalculator implements PieceMovesCalculator {
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> pawnMoves = new ArrayList<>();

        ChessPiece currPiece = board.getPiece(position);
        int row = position.getRow();
        int col = position.getColumn();

        if (currPiece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            ChessPosition aheadPiecePosition = new ChessPosition(row + 1, col);
            ChessPiece aheadPiece = board.getPiece(aheadPiecePosition);

            ChessPosition aheadLeftPiecePosition = new ChessPosition(row + 1, col - 1);
            ChessPiece aheadLeftPiece = board.getPiece(aheadLeftPiecePosition); // TODO check if at left side

            ChessPosition aheadRightPiecePosition = new ChessPosition(row + 1, col + 1);
            ChessPiece aheadRightPiece = board.getPiece(aheadRightPiecePosition); // TODO check if at right side

            if (row == 2) {     // If in starting position
                ChessPosition twoAheadPiecePosition = new ChessPosition(row + 2, col);
                ChessPiece twoAheadPiece = board.getPiece(twoAheadPiecePosition);
                if (twoAheadPiece == null) {
                    pawnMoves.add(new ChessMove(position, twoAheadPiecePosition, null));
                }
            }
            if (row < 7) {   // If not in promotion range
                if (aheadPiece == null) {
                    pawnMoves.add(new ChessMove(position, aheadPiecePosition, null));
                }
                if (aheadLeftPiece != null && aheadLeftPiece.getTeamColor() != currPiece.getTeamColor()) {
                    pawnMoves.add(new ChessMove(position, aheadLeftPiecePosition, null));
                }
                if (aheadRightPiece != null && aheadRightPiece.getTeamColor() != currPiece.getTeamColor()) {
                    pawnMoves.add(new ChessMove(position, aheadRightPiecePosition, null));
                }
            } else {        // If in promotion range
                if (aheadPiece == null) { // todo also check for if right and left sides are empty?
                    for(PieceType possiblePiece : new PieceType[] {PieceType.QUEEN, PieceType.BISHOP, PieceType.KNIGHT, PieceType.ROOK}) {
                        pawnMoves.add(new ChessMove(position, aheadPiecePosition, possiblePiece));
                    }
                }
            }
        }
        return pawnMoves;
    }
}
