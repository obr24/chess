package client;

public class Repl {
    // each repl is sort of nested in the other. ie. A user starts in client.PreLoginClient. Then they login and
    // go to client.PostLoginClient. Then they choose a game and go to the inGameCleint.
    // once they exit, they first go to the postloginclient, then the preloginclient after logging out

    private final PreLoginClient client;

    public Repl(String serverUrl) {
        client = new PreLoginClient(serverUrl);
    }

    public void run() {
        System.out.println("Welcome to Chess. Sign in to start.");
        System.out.print(client.help());
    }
}
