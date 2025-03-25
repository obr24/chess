package client;

import java.util.Scanner;
import static ui.EscapeSequences.*;

public class Repl {
    // each repl is sort of nested in the other. ie. A user starts in client.PreLoginClient. Then they login and
    // go to client.PostLoginClient. Then they choose a game and go to the inGameCleint.
    // once they exit, they first go to the postloginclient, then the preloginclient after logging out

    private final PreLoginClient client;

    public Repl(String serverUrl) {
        client = new PreLoginClient("http://127.0.0.1");
        // todo next time make sure to start server before starting client
        // u need to work on the preloginclient, finsih that, then set the logged in state to true
        // then create post loginclient
        // then after joinging game, call post join client
        // then print the board. good luck
    }

    public void run() {
        System.out.println("Welcome to Chess. Sign in to start.");
        System.out.print(client.help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = client.eval(line);
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
        return String.format(RESET_TEXT_COLOR);
    }
}
