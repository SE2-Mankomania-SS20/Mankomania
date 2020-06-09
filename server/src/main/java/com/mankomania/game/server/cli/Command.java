package com.mankomania.game.server.cli;

import com.esotericsoftware.kryonet.Server;
import com.mankomania.game.server.data.ServerData;

public abstract class Command implements ICommand {
    // references to be used by the commands
    private Server server;
    private ServerData serverData;

    // implement three getter that ICommand requires and set the values using  the ctor, instead of having to implement
    // them in each and every class only returning a string. that way there is less code duplication (looking at you, sonarcloud)
    private String mainCommand;
    private int numberOfParams;
    private String helpText;

    public Command(Server server, ServerData serverData, String mainCommand, int numberOfParams, String helpText) {
        this.server = server;
        this.serverData = serverData;

        this.mainCommand = mainCommand;
        this.numberOfParams = numberOfParams;
        this.helpText = helpText;
    }

    public Server getServer() {
        return server;
    }

    public ServerData getServerData() {
        return serverData;
    }

    @Override
    public String getMainCommand() {
        return this.mainCommand;
    }

    @Override
    public String getHelpText() {
        return this.helpText;
    }

    @Override
    public int getNumberOfParams() {
        return this.numberOfParams;
    }
}
