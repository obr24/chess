package chess;

import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private PieceType type;
    private ChessGame.TeamColor pieceColor;
    private PieceMovesCalculator movesCalculator;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
        this.movesCalculator = switch (type) {
            case BISHOP -> new BishopMovesCalculator();
            case ROOK -> new RookMovesCalculator();
            case QUEEN -> new QueenMovesCalculator();
            case KNIGHT -> new KnightMovesCalculator();
            case PAWN -> new PawnMovesCalculator();
            case KING -> new KingMovesCalculator();
            case null, default -> null;
        };

    }

    public ChessPiece deepCopy() {
        ChessPiece newPiece = new ChessPiece(this.getTeamColor(), this.getPieceType());
        return newPiece;
    }
    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        //return type == that.type && pieceColor == that.pieceColor && Objects.equals(movesCalculator, that.movesCalculator);
        return type == that.type && pieceColor == that.pieceColor;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, pieceColor, movesCalculator);
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return this.pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return this.type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        return this.movesCalculator.pieceMoves(board, myPosition);
    }

    @Override
    public String toString() {
        return "ChessPiece{" +
                "pieceColor=" + pieceColor +
                ", type=" + type +
                "}\n";
    }
}
