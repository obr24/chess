package chess;

import java.util.Collection;
import chess.BishopMovesCalculator;
import chess.RookMovesCalculator;

public class QueenMovesCalculator implements PieceMovesCalculator {
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        BishopMovesCalculator bishopPiece = new BishopMovesCalculator();
        RookMovesCalculator rookPiece = new RookMovesCalculator();

        Collection<ChessMove> queenMoves = bishopPiece.pieceMoves(board, position);
        queenMoves.addAll(rookPiece.pieceMoves(board, position));
        return queenMoves;
    }

}
