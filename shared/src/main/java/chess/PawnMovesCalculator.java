package chess;

import java.util.ArrayList;
import java.util.Collection;

import static chess.ChessPiece.PieceType;

public class PawnMovesCalculator implements PieceMovesCalculator {
    private Collection<ChessMove> pieceMovesByColor(ChessBoard board, ChessPosition position, ChessGame.TeamColor color) {
        Collection<ChessMove> pawnMoves = new ArrayList<>();
        ChessPiece currPiece = board.getPiece(position);
        int row = position.getRow();
        int col = position.getColumn();

        ChessPosition aheadPiecePosition;

        if (color == ChessGame.TeamColor.WHITE) {
            aheadPiecePosition = new ChessPosition(row + 1, col);
        } else {
            aheadPiecePosition = new ChessPosition(row - 1, col);
        }

        ChessPiece aheadPiece = board.getPiece(aheadPiecePosition);

        ChessPosition aheadLeftPiecePosition = null;
        ChessPiece aheadLeftPiece = null; // TODO check if at left side

        if (col > 1) {
            if (color == ChessGame.TeamColor.WHITE) {
                aheadLeftPiecePosition = new ChessPosition(row + 1, col - 1);
            } else if (color == ChessGame.TeamColor.BLACK) {
                aheadLeftPiecePosition = new ChessPosition(row - 1, col - 1);
            }
            assert aheadLeftPiecePosition != null;
            aheadLeftPiece = board.getPiece(aheadLeftPiecePosition); // TODO check if at left side
        }

        ChessPosition aheadRightPiecePosition = null;
        ChessPiece aheadRightPiece = null; // TODO check if at right side

        if (col < 8) {
            if (color == ChessGame.TeamColor.WHITE) {
                aheadRightPiecePosition = new ChessPosition(row + 1, col + 1);
            } else {
                aheadRightPiecePosition = new ChessPosition(row - 1, col + 1);
            }
            aheadRightPiece = board.getPiece(aheadRightPiecePosition); // TODO check if at right side
        }

        if ((color == ChessGame.TeamColor.WHITE && row == 2) || (color == ChessGame.TeamColor.BLACK && row == 7)) {     // If in starting position
            ChessPosition twoAheadPiecePosition;
            if (color == ChessGame.TeamColor.WHITE) {
                twoAheadPiecePosition = new ChessPosition(row + 2, col);
            } else {
                twoAheadPiecePosition = new ChessPosition(row - 2, col);
            }
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

        if (( color == ChessGame.TeamColor.WHITE && row == 7) || (color == ChessGame.TeamColor.BLACK && row == 2)) {        // If in promotion range
            Collection<ChessMove> newPawnMoves = new ArrayList<>();
            for (ChessMove currMove : pawnMoves) {
                for (PieceType possiblePiece : new PieceType[] {PieceType.QUEEN, PieceType.BISHOP, PieceType.KNIGHT, PieceType.ROOK}) {
                    newPawnMoves.add(new ChessMove(position, currMove.getEndPosition(), possiblePiece));
                }
            }
            pawnMoves = newPawnMoves;
        }
        return pawnMoves;
    }
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> pawnMoves = new ArrayList<>();

        ChessPiece currPiece = board.getPiece(position);
        int row = position.getRow();
        int col = position.getColumn();

        ChessGame.TeamColor color = currPiece.getTeamColor();
        pawnMoves = pieceMovesByColor(board, position, color);
        return pawnMoves;
    }
}
