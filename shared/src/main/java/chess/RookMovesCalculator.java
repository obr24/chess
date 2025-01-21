package chess;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class RookMovesCalculator implements PieceMovesCalculator {
    private boolean validMove(String direction, int row, int col) {
       switch (direction) {
           case "up":
               if (row >= 8) {
                   return false;
               }
               break;
           case "down":
               if (row <= 1) {
                   return false;
               }
               break;
           case "right":
               if (col >= 8) {
                   return false;
               }
               break;
           case "left":
               if (col <= 1) {
                   return false;
               }
               break;
        }
    return true;
    }
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        ChessPiece currPiece = board.getPiece(position);

        Collection<ChessMove> chessMoves = new ArrayList<>();

        List<String> directions = Arrays.asList("up", "down", "right", "left");

        for (String direction : directions) {
            int col = position.getColumn();
            int row = position.getRow();

            //while (col < 8 && col > 1 && row < 8 && row > 1) { // replace with validMoves function call
            while (validMove(direction, row, col)) { // replace with validMoves function call
                switch (direction) {
                    case "up":
                        row++;
                        break;
                    case "down":
                        row--;
                        break;
                    case "right":
                        col++;
                        break;
                    case "left":
                        col--;
                        break;
                }
                ChessPosition newPosition = new ChessPosition(row, col);
                ChessPiece newPositionPiece = board.getPiece(newPosition);

                ChessMove newChessMove = new ChessMove(position, newPosition, null);

                if (newPositionPiece == null) {
                    chessMoves.add(newChessMove);
                } else if (currPiece.getTeamColor() != newPositionPiece.getTeamColor()) {
                    chessMoves.add(newChessMove);
                    break;
                } else if (currPiece.getTeamColor() == newPositionPiece.getTeamColor()) { // TODO: change to .equals()
                    break;
                }
            }
        }
        return chessMoves;
    }
}
