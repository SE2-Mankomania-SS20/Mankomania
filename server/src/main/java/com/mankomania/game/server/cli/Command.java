package com.mankomania.game.server.cli;

import com.esotericsoftware.kryonet.Server;
import com.mankomania.game.server.data.ServerData;

public abstract class Command implements ICommand {
    // references to be used by the commands
    private Server server;
    private ServerData serverData;

    public Command(Server server, ServerData serverData) {
        this.server = server;
        this.serverData = serverData;
    }

    public Server getServer() {
        return server;
    }

    public ServerData getServerData() {
        return serverData;
    }
}
