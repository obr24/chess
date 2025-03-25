package client;

import java.util.Scanner;
import static ui.EscapeSequences.*;

public class Repl {
    // each repl is sort of nested in the other. ie. A user starts in preLoginClient.PreLoginClient. Then they login and
    // go to preLoginClient.PostLoginClient. Then they choose a game and go to the inGameCleint.
    // once they exit, they first go to the postloginclient, then the preloginclient after logging out

    private final ServerFacade facade;
    private final PreLoginClient preLoginClient;
    private final PostLoginClient postLoginClient;
    private final InGameClient inGameClient;
    private ClientState clientState;

    public Repl(String serverUrl) {
        facade = new ServerFacade("http://127.0.0.1:8080");
        clientState = new ClientState();
        preLoginClient = new PreLoginClient(facade, clientState);
        postLoginClient = new PostLoginClient(facade, clientState);
        inGameClient = new InGameClient(facade, clientState);

        // todo next time make sure to start server before starting preLoginClient
        // u need to work on the preloginclient, finish that, then set the logged in state to true
        // then create post loginclient
        // then after joinging game, call post join preLoginClient
        // then print the board. good luck
    }

    public void run() {
        System.out.println("Welcome to Chess. Sign in to start.");
        System.out.print(preLoginClient.help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = switch (clientState.getUserState()) {
                    case PRE_LOGIN -> preLoginClient.eval(line);
                    case POST_LOGIN -> postLoginClient.eval(line);
                    case IN_GAME -> inGameClient.eval(line);
                };
                System.out.print(SET_TEXT_COLOR_BLUE + result);
            } catch (Exception e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

    private void printPrompt() {
        System.out.print("\n" +  resetAll() + ">>> " + SET_TEXT_COLOR_GREEN);
    }

    private String resetAll() {
        return RESET_TEXT_COLOR + RESET_BG_COLOR;
    }
}
