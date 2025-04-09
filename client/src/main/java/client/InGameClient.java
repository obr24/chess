package client;

import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import client.websocket.WebSocketFacade;
import exception.ResponseException;
import ui.PrintBoard;

import java.util.Arrays;
import java.util.Objects;

public class InGameClient {
    private final ServerFacade facade;
    private final WebSocketFacade wsFacade;
    private final ClientState clientState;
    private boolean wantsToResign = false;

    public InGameClient(WebSocketFacade wsFacade, ServerFacade facade, ClientState clientState) {
        this.facade = facade;
        this.wsFacade = wsFacade;
        this.clientState = clientState;
    }

    public void initializeWebSocket() {
        try {
            wsFacade.connect(clientState.getAuthToken(), clientState.getGameID());
        } catch (ResponseException e) {
            System.out.println("big issue connecting in inGameclient");
            throw new RuntimeException(e);
        }
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            if (wantsToResign) {
                wantsToResign = false;
                return switch (cmd) {
                    case "y" -> acutallyResign();
                    default -> "not resigning.";
                };
            }
            return switch (cmd) {
                case "help" -> help();
                case "redraw" -> redraw();
                case "leave" -> leave();
                case "move" -> move(params);
                case "resign" -> resign();
                case "highlight" -> highlight(params);
                default -> unknownInput();
            };
//        } catch (ResponseException ex) {
        } catch (Exception ex) {
            return String.format("error in prelogin1: %s", ex.getMessage());
        }
    }

    public String redraw() {
        String colorString = "observer";
        if (!Objects.equals(clientState.getPlayerColor(), null)) {
            colorString = clientState.getPlayerColor().toString();
        }
        return PrintBoard.print(clientState.getChessGame(), colorString, null);
    }

    public String leave() throws ResponseException {
        wsFacade.leave(clientState.getAuthToken(), clientState.getGameID());
        clientState.setChessGame(null);
        clientState.setGameID(null);
        clientState.setPlayerColor(null);
        clientState.setObservingGameID(null);
        clientState.setUserState(State.POST_LOGIN);
        return "Leaving now";
    }

    public String move(String[] params) throws ResponseException {
        if (params.length < 2 || params.length > 3) {
            throw new ResponseException(500, "Invalid move. try f5 e4 q");
        }
        ChessPosition startPosition = positionTranslator(params[0]);
        ChessPosition endPosition = positionTranslator(params[1]);
        ChessPiece.PieceType promotionPiece = null;
        if (params.length == 3) {
            promotionPiece = pieceTranslator(params[2]);
        }
        ChessMove newMove = new ChessMove(startPosition, endPosition, promotionPiece);
        wsFacade.makeMove(clientState.getAuthToken(), clientState.getGameID(), newMove);
        return "making move";
    }

    private ChessPiece.PieceType pieceTranslator(String pieceString) throws ResponseException {
        char[] chars = pieceString.toCharArray();
        if (chars.length != 1) {
            throw new ResponseException(500, "Invalid move. try f5 e4 q");
        }
        char pieceChar = chars[0];
        return switch (pieceChar) {
            case 'r' -> ChessPiece.PieceType.ROOK;
            case 'n' -> ChessPiece.PieceType.KNIGHT;
            case 'b' -> ChessPiece.PieceType.BISHOP;
            case 'q' -> ChessPiece.PieceType.QUEEN;
            case 'k' -> ChessPiece.PieceType.KING;
            case 'p' -> ChessPiece.PieceType.PAWN;
            default -> throw new ResponseException(500, "bad piece type. Use: r, n, b, q, k, p");
        };
    }

    private ChessPosition positionTranslator(String moveString) throws ResponseException {
        char[] chars = moveString.toCharArray();
        if (!(chars.length == 2)) {
            throw new ResponseException(500, "Invalid move. try f5 e4 q");
        }
        if ((int)chars[0] < 97 || (int)chars[0] > 104 || (int)chars[1] < 48 || (int)chars[1] > 56) { // checks if in range of ascii
            throw new ResponseException(500, "Invalid move. try pattern f5 e4 q");
        }
        int col = chars[0] - 'a' + 1;
        int row = chars[1] - '1' + 1;

        return new ChessPosition(row, col);
    }

    public String resign() throws ResponseException {
        wantsToResign = true;
        // todo add in reset of client state?
        return "Are you sure you want to resign (y/n)";
    }

    public String acutallyResign() throws ResponseException {

        wsFacade.resign(clientState.getAuthToken(), clientState.getGameID());
        return "Resigning from the game...";
    }

    public String highlight(String[] params) throws ResponseException {
        if (params.length != 1) {
            throw new ResponseException(500, "Invalid argument. try highlight f5");
        }
        String positionString = params[0];
        String colorString = "observer";
        if (!Objects.equals(clientState.getPlayerColor(), null)) {
            colorString = clientState.getPlayerColor().toString();
        }
        return PrintBoard.print(clientState.getChessGame(), colorString, positionTranslator(positionString));
    }

    public String help() {
        return """
            Options:
              Help: "help"
              Redraw board: "redraw"
              Leave game: "leave"
              Make move: "move <from> <to> <optional promotion>" (e.g. f5 e4 q)
              Resign: "resign"
              Highlight legal moves: "highlight"
            """;
    }

    public String unknownInput() {
        return String.format("Unknown input:\n%s", help());
    }

}
