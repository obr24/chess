package client;

import chess.ChessGame;
import model.GameData;

import java.util.Collection;
import java.util.HashMap;

public class ClientState {
    private ChessGame chessGame;

    public ChessGame getChessGame() {
        return chessGame;
    }

    public void setChessGame(ChessGame chessGame) {
        this.chessGame = chessGame;
    }

    public ChessGame.TeamColor getPlayerColor() {
        return playerColor;
    }

    public void setPlayerColor(ChessGame.TeamColor playerColor) {
        this.playerColor = playerColor;
    }

    private ChessGame.TeamColor playerColor;

    public Integer getObservingGameID() {
        return observingGameID;
    }

    public void setObservingGameID(Integer observingGameID) {
        this.observingGameID = observingGameID;
    }

    private Integer observingGameID;
    private HashMap<Integer, GameData> clientGameIdtoRealGameId;

    public Integer getGameID() {
        return gameID;
    }

    public void setGameID(Integer gameID) {
        this.gameID = gameID;
    }

    private Integer gameID;

    public HashMap<Integer, GameData> getClientGameIdtoRealGameId() {
        return clientGameIdtoRealGameId;
    }

    public void setClientGameIdtoRealGameId(HashMap<Integer, GameData> clientGameIdtoRealGameId) {
        this.clientGameIdtoRealGameId = clientGameIdtoRealGameId;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    private String authToken;

    public State getUserState() {
        return userState;
    }

    public void setUserState(State userState) {
        this.userState = userState;
    }

    private State userState;

    public ClientState() {
        userState = State.PRE_LOGIN;
    }
}

