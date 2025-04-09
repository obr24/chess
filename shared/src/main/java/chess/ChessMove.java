package chess;

import java.util.Objects;

/**
 * Represents moving a chess piece on a chessboard
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessMove {
    private ChessPosition startPosition;
    private ChessPosition endPosition;
    private ChessPiece.PieceType promotionPiece = null;

    @Override
    public String toString() {
        return String.format("%s -> %s; promotionPiece: %s\n",
                positionToString(this.getStartPosition()), positionToString(this.getEndPosition()),
                pieceToString(this.getPromotionPiece()));
    }

    private String positionToString(ChessPosition position) {
        int row = position.getRow();
        int col = position.getColumn();
        char[] chars = {
                (char) (col + 'a'),
                (char) (row + '1')
        };
        return String.valueOf(chars);
    }

    private String pieceToString(ChessPiece.PieceType pieceType) {
        return switch (pieceType) {
            case ROOK -> "rook";
            case PAWN -> "pawn";
            case QUEEN -> "queen";
            case KING -> "king";
            case KNIGHT -> "knight";
            case BISHOP -> "bishop";
            case null, default -> "";
        };
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessMove chessMove = (ChessMove) o;
        return Objects.equals(getStartPosition(), chessMove.getStartPosition())
                && Objects.equals(getEndPosition(), chessMove.getEndPosition())
                && getPromotionPiece() == chessMove.getPromotionPiece();
    }

    public boolean endPositionEquals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessMove chessMove = (ChessMove) o;
        return Objects.equals(getEndPosition(), chessMove.getEndPosition());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStartPosition(), getEndPosition(), getPromotionPiece());
    }

    public ChessMove(ChessPosition startPosition, ChessPosition endPosition,
                     ChessPiece.PieceType promotionPiece) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.promotionPiece = promotionPiece;
    }

    /**
     * @return ChessPosition of starting location
     */
    public ChessPosition getStartPosition() {
        return startPosition;
    }

    /**
     * @return ChessPosition of ending location
     */
    public ChessPosition getEndPosition() {
        return endPosition;
    }

    /**
     * Added by @me
     */
    public void setPromotionPiece(ChessPiece.PieceType promotionPiece) {
        this.promotionPiece = promotionPiece;
    }
                                  /**
     * Gets the type of piece to promote a pawn to if pawn promotion is part of this
     * chess move
     *
     * @return Type of piece to promote a pawn to, or null if no promotion
     */
    public ChessPiece.PieceType getPromotionPiece() {
        return promotionPiece;
    }
}
