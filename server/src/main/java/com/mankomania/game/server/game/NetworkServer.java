package com.mankomania.game.server.game;

import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;
import com.mankomania.game.core.network.KryoHelper;

import java.io.IOException;

import com.mankomania.game.core.network.NetworkConstants;
import com.mankomania.game.server.cli.ServerCLI;
import com.mankomania.game.server.cli.commands.ExitCmd;
import com.mankomania.game.server.cli.commands.GetMoneyCmd;
import com.mankomania.game.server.data.ServerData;

public class NetworkServer {
    private final Server server;
    private final ServerData serverData;

    private ServerCLI serverCLI;

    public NetworkServer() throws IOException {
        Log.info("Starting server, listening on port " + NetworkConstants.TCP_PORT);
        server = new Server();
        serverData = new ServerData(server);
        //call helper class to register classes
        KryoHelper.registerClasses(server.getKryo());
        server.addListener(new MankomaniaListener(server, serverData));

        server.start();
        server.bind(NetworkConstants.TCP_PORT);

        // instantiate the cli and register all commands
        serverCLI = new ServerCLI();
        registerCommands();

        Log.info("Server ready and listening on port " + NetworkConstants.TCP_PORT + "!");
    }

    public void processCommand(String command) {
        serverCLI.processCommand(command);
    }

    private void registerCommands() {
        serverCLI.addCommand(new ExitCmd());
        serverCLI.addCommand(new GetMoneyCmd(server, serverData));
    }
}
