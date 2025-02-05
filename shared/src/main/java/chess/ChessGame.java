package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private ChessBoard board;
    private TeamColor teamTurn;

    public ChessGame() {
        board = new ChessBoard();
        board.resetBoard();
        teamTurn = TeamColor.WHITE;
    }

    public ChessGame DeepCopy() {
        ChessGame newGame = new ChessGame();
        newGame.setBoard(this.board.DeepCopy());
        newGame.setTeamTurn(this.getTeamTurn());
        return newGame;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    private Collection<ChessMove> getTeamMoves(TeamColor teamColor) {
        HashMap<ChessPiece, ChessPosition> opposingTeamPieces = board.getTeamPieces(teamColor);
        Collection<ChessMove> opposingTeamMoves = new ArrayList<>();

        opposingTeamPieces.forEach(
                (piece, position)
                        -> opposingTeamMoves.addAll(piece.pieceMoves(board, position))
        );
        return opposingTeamMoves;
    }

    private Collection<ChessPosition> getTeamEndPositions(TeamColor teamColor) {
        Collection<ChessMove> opposingTeamMoves = getTeamMoves(teamColor);
        Collection<ChessPosition> endPositions = new ArrayList<>();
        opposingTeamMoves.forEach(move -> endPositions.add(move.getEndPosition()));
        return endPositions;
    }

    private boolean putsIntoCheck(ChessMove move, TeamColor curColor) {
        ChessGame tempGame = this.DeepCopy();
        doMove(tempGame, move);
        return tempGame.isInCheck(curColor);
    }

    private boolean blocksCheck(ChessMove move, TeamColor curColor) {
        ChessGame tempGame = this.DeepCopy();
        doMove(tempGame, move);
        return !tempGame.isInCheck(curColor);
    }

    private void doMove(ChessGame game, ChessMove move) {
        ChessBoard curBoard = game.getBoard();
        ChessPosition startPosition = move.getStartPosition();
        TeamColor curColor = board.getPiece(startPosition).getTeamColor();
        ChessPosition endPosition = move.getEndPosition();
        ChessPiece pieceToMove = null;
        if (move.getPromotionPiece() == null) {
            pieceToMove = curBoard.getPiece(startPosition);
        } else {
            ChessPiece.PieceType pieceToMoveType = move.getPromotionPiece();
            pieceToMove = new ChessPiece(curColor, pieceToMoveType);
        }
        curBoard.addPiece(startPosition, null);
        curBoard.addPiece(endPosition, pieceToMove);
    }

    private boolean isTeamTurn(TeamColor color) {
        return this.getTeamTurn() == color;
    }

    private boolean existsMoveToBlockCheck(TeamColor color) {
        var teamMoves = getTeamMoves(color);
        for (var move : teamMoves) {
            if (blocksCheck(move, color)) {
                return true;
            }
        }
        return false;
    }
    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece curPiece = board.getPiece(startPosition);
        if (curPiece == null) {
            return new ArrayList<>();
        }
        TeamColor curPieceColor = curPiece.getTeamColor();
        TeamColor opposingColor = switch (curPieceColor) { case BLACK -> TeamColor.WHITE; case WHITE -> TeamColor.BLACK; };
        Collection<ChessMove> moves = curPiece.pieceMoves(board, startPosition);
        Collection<ChessMove> movesToRemove = new ArrayList<>();
        if (isInCheck(curPieceColor)) {
            // switch statement checking for moves that block the check
            for (ChessMove move : moves) {
                if (!blocksCheck(move, curPieceColor)) {
                    movesToRemove.add(move);
                }
            }
            moves.removeAll(movesToRemove);
        } else {
            for (ChessMove move : moves) {
                if (putsIntoCheck(move, curPieceColor)) {
                    movesToRemove.add(move);
                }
            }
            moves.removeAll(movesToRemove);
        }
        if (curPiece.getPieceType() == ChessPiece.PieceType.KING) {
            Collection<ChessMove> opposingTeamMoves = getTeamMoves(opposingColor);
            opposingTeamMoves.forEach(opposingMove -> moves.removeIf(curMove -> curMove.endPositionEquals(opposingMove)));
        }
        return moves;
    }

    private boolean validMove(ChessMove move) {
        ChessPosition startPosition = move.getStartPosition();
        ChessPiece curPiece = board.getPiece(startPosition);
        TeamColor curColor = curPiece.getTeamColor();
        return validMoves(startPosition).contains(move);
    }

    private void changeTurn() {
        if (this.getTeamTurn() == TeamColor.WHITE) {
            this.setTeamTurn(TeamColor.BLACK);
        } else if (this.getTeamTurn() == TeamColor.BLACK) {
            this.setTeamTurn(TeamColor.WHITE);
        }
    }
    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPosition startPosition = move.getStartPosition();
        ChessPiece curPiece = board.getPiece(startPosition);
        if (curPiece == null) {
            throw new InvalidMoveException("bad move: no piece there");
        } else if (curPiece.getTeamColor() != this.getTeamTurn()) {
            throw new InvalidMoveException("uh uh: not your turn");
        }else if (validMove(move)) {
            doMove(this, move);
            changeTurn();
        }
        else {
            throw new InvalidMoveException("bad move");
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPosition = board.getKingPosition(teamColor);
        TeamColor opposingColor = switch (teamColor) { case BLACK -> TeamColor.WHITE; case WHITE -> TeamColor.BLACK; };
        Collection<ChessPosition> opposingEndPositions = getTeamEndPositions(opposingColor);
        return opposingEndPositions.contains(kingPosition);
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        ChessPosition kingPosition = board.getKingPosition(teamColor);

        return isInCheck(teamColor) && (this.validMoves(kingPosition).isEmpty())
                && !existsMoveToBlockCheck(teamColor);
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if (isInCheck(TeamColor.WHITE) || isInCheck(TeamColor.BLACK)) {
            return false;
        }
        HashMap<ChessPiece, ChessPosition> teamPieces = board.getTeamPieces(teamColor);
        for (ChessPosition curPosition : teamPieces.values()) {
            if (!validMoves(curPosition).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return this.board;
    }
}
