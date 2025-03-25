package client;

public class InGameClient {
    private final ServerFacade facade;
    private final ClientState clientState;

    public InGameClient(ServerFacade facade, ClientState clientState) {
        this.facade = facade;
        this.clientState = clientState;
    }

    public String eval(String input) {
    return "not implemented: in game client";
    }
}
