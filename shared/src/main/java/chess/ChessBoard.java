package chess;

import java.util.*;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private ChessPiece[][] squares = new ChessPiece[8][8];
    public ChessBoard() {
        
    }

    public ChessBoard deepCopy() {
        ChessBoard newBoard = new ChessBoard();
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPosition curPosition = new ChessPosition(row, col);
                ChessPiece oldPiece = this.getPiece(curPosition);
                if (oldPiece != null) {
                    ChessPiece copiedPiece = oldPiece.deepCopy();
                    newBoard.addPiece(curPosition, copiedPiece);
                }
            }
        }
        return newBoard;
    } // TODO need to do deep copy of board and pieces
    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        squares[position.getRow()-1][position.getColumn()-1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return squares[position.getRow()-1][position.getColumn()-1];
    }

    public HashMap<ChessPiece, ChessPosition> getTeamPieces(ChessGame.TeamColor color) {
        var teamPieces = new HashMap<ChessPiece, ChessPosition>();

        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPosition curPosition = new ChessPosition(row, col);
                ChessPiece curPiece = this.getPiece(curPosition);
                if (curPiece != null && curPiece.getTeamColor().equals(color)) {
                    teamPieces.put(curPiece, curPosition);
                }
            }
        }
        return teamPieces;
    }

    public ChessPosition getKingPosition(ChessGame.TeamColor color) {
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPosition curPosition = new ChessPosition(row, col);
                ChessPiece curPiece = this.getPiece(curPosition);
                if (curPiece != null && curPiece.getTeamColor().equals(color) &&
                        curPiece.getPieceType() == ChessPiece.PieceType.KING) {
                    return curPosition;
                }
            }
        }
        return null; // if ur here ur board is messed up
    }
    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        /**
         * setup white side pawns
         */
        ChessGame.TeamColor teamColor = ChessGame.TeamColor.WHITE;

        ChessPiece.PieceType[] pieceOrder = new ChessPiece.PieceType[] {ChessPiece.PieceType.ROOK, ChessPiece.PieceType.KNIGHT,
                ChessPiece.PieceType.BISHOP, ChessPiece.PieceType.QUEEN, ChessPiece.PieceType.KING, ChessPiece.PieceType.BISHOP,
                ChessPiece.PieceType.KNIGHT, ChessPiece.PieceType.ROOK};
        int curRow = 1;

        for (int curCol = 1; curCol <= 8; curCol++) {
            ChessPosition curPosition = new ChessPosition(curRow, curCol);
            ChessPiece newPiece = new ChessPiece(teamColor, pieceOrder[curCol - 1]);
            this.addPiece(curPosition, newPiece);
        }

        curRow = 2;

        for (int curCol = 1; curCol <= 8; curCol++) {
            ChessPosition curPosition = new ChessPosition(curRow, curCol);
            ChessPiece newPiece = new ChessPiece(teamColor, ChessPiece.PieceType.PAWN);
            this.addPiece(curPosition, newPiece);
        }

        teamColor = ChessGame.TeamColor.BLACK;

        curRow = 8;

        for (int curCol = 1; curCol <= 8; curCol++) {
            ChessPosition curPosition = new ChessPosition(curRow, curCol);
            ChessPiece newPiece = new ChessPiece(teamColor, pieceOrder[curCol - 1]);
            this.addPiece(curPosition, newPiece);
        }

        curRow = 7;

        for (int curCol = 1; curCol <= 8; curCol++) {
            ChessPosition curPosition = new ChessPosition(curRow, curCol);
            ChessPiece newPiece = new ChessPiece(teamColor, ChessPiece.PieceType.PAWN);
            this.addPiece(curPosition, newPiece);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(squares, that.squares);
    }

    @Override
    public String toString() {
        return "ChessBoard{" +
                "squares=" + Arrays.deepToString(squares) +
                '}';
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(squares);
    }
}
