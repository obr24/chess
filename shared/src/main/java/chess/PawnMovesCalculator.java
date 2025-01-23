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

            ChessPosition aheadLeftPiecePosition = null;
            ChessPiece aheadLeftPiece = null; // TODO check if at left side

            if (col > 1) {
                aheadLeftPiecePosition = new ChessPosition(row + 1, col - 1);
                aheadLeftPiece = board.getPiece(aheadLeftPiecePosition); // TODO check if at left side
            }

            ChessPosition aheadRightPiecePosition = null;
            ChessPiece aheadRightPiece = null; // TODO check if at right side

            if (col < 8) {
                aheadRightPiecePosition = new ChessPosition(row + 1, col + 1);
                aheadRightPiece = board.getPiece(aheadRightPiecePosition); // TODO check if at right side

            }
            if (row == 2) {     // If in starting position
                ChessPosition twoAheadPiecePosition = new ChessPosition(row + 2, col);
                ChessPiece twoAheadPiece = board.getPiece(twoAheadPiecePosition);
                if (aheadPiece == null && twoAheadPiece == null) {
                    pawnMoves.add(new ChessMove(position, twoAheadPiecePosition, null));
                }
            }
            if (aheadPiece == null) {
                pawnMoves.add(new ChessMove(position, aheadPiecePosition, null));
            }
            if (aheadLeftPiece != null && aheadLeftPiece.getTeamColor() != currPiece.getTeamColor()) {
                pawnMoves.add(new ChessMove(position, aheadLeftPiecePosition, null));
            }
            if (aheadRightPiece != null && aheadRightPiece.getTeamColor() != currPiece.getTeamColor()) {
                pawnMoves.add(new ChessMove(position, aheadRightPiecePosition, null));
            }
            if (row == 7) {        // If in promotion range
                Collection<ChessMove> newPawnMoves = new ArrayList<>();
                for (ChessMove currMove : pawnMoves) {
                    for (PieceType possiblePiece : new PieceType[] {PieceType.QUEEN, PieceType.BISHOP, PieceType.KNIGHT, PieceType.ROOK}) {
                        newPawnMoves.add(new ChessMove(position, currMove.getEndPosition(), possiblePiece));
                    }
                }
                pawnMoves = newPawnMoves;
            }
        } else {
            ChessPosition aheadPiecePosition = new ChessPosition(row - 1, col);
            ChessPiece aheadPiece = board.getPiece(aheadPiecePosition);

            ChessPosition aheadLeftPiecePosition = null;
            ChessPiece aheadLeftPiece = null; // TODO check if at left side

            if (col > 1) {
                aheadLeftPiecePosition = new ChessPosition(row - 1, col - 1);
                aheadLeftPiece = board.getPiece(aheadLeftPiecePosition); // TODO check if at left side
            }

            ChessPosition aheadRightPiecePosition = null;
            ChessPiece aheadRightPiece = null; // TODO check if at right side

            if (col < 8) {
                aheadRightPiecePosition = new ChessPosition(row - 1, col + 1);
                aheadRightPiece = board.getPiece(aheadRightPiecePosition); // TODO check if at right side

            }
            if (row == 7) {     // If in starting position
                ChessPosition twoAheadPiecePosition = new ChessPosition(row - 2, col);
                ChessPiece twoAheadPiece = board.getPiece(twoAheadPiecePosition);
                if (aheadPiece == null && twoAheadPiece == null) {
                    pawnMoves.add(new ChessMove(position, twoAheadPiecePosition, null));
                }
            }
            if (aheadPiece == null) {
                pawnMoves.add(new ChessMove(position, aheadPiecePosition, null));
            }
            if (aheadLeftPiece != null && aheadLeftPiece.getTeamColor() != currPiece.getTeamColor()) {
                pawnMoves.add(new ChessMove(position, aheadLeftPiecePosition, null));
            }
            if (aheadRightPiece != null && aheadRightPiece.getTeamColor() != currPiece.getTeamColor()) {
                pawnMoves.add(new ChessMove(position, aheadRightPiecePosition, null));
            }
            if (row == 2) {        // If in promotion range
                Collection<ChessMove> newPawnMoves = new ArrayList<>();
                for (ChessMove currMove : pawnMoves) {
                    for (PieceType possiblePiece : new PieceType[] {PieceType.QUEEN, PieceType.BISHOP, PieceType.KNIGHT, PieceType.ROOK}) {
                        newPawnMoves.add(new ChessMove(position, currMove.getEndPosition(), possiblePiece));
                    }
                }
                pawnMoves = newPawnMoves;
            }
        }
        return pawnMoves;
    }
}
