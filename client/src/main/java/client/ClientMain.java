package client;

import chess.*;

public class ClientMain {
    public static void main(String[] args) {
        // todo what should the serverUrl be?

        var serverUrl = "http://127.0.0.1:8080";
        if (args.length == 1) {
            serverUrl = args[0];
        }

        new Repl(serverUrl).run();
    }
}