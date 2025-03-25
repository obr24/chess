package client;

public class PreLoginClient {
    private final ServerFacade server;
    private final String serverUrl;

    public PreLoginClient(String serverUrl) {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
    }

    public String help() {
        // todo add in for other clients (like if logged in or not)
        return """
                Options:
                Login: "login" <USERNAME> <PASSWORD>
                Register: "register" <USERNAME> <PASSWORD> <EMAIL>
                Exit: "quit"
                Help: "help"
                """;
    }
}
