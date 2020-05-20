package com.mankomania.game.server.game;

import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;
import com.mankomania.game.core.network.KryoHelper;

import java.io.IOException;

import com.mankomania.game.core.network.NetworkConstants;
import com.mankomania.game.core.network.messages.clienttoserver.baseturn.DiceResultMessage;
import com.mankomania.game.server.data.ServerData;

public class NetworkServer {
    private final Server server;
    private final ServerData serverData;

    public NetworkServer() throws IOException {
        Log.info("Starting server, listening on port " + NetworkConstants.TCP_PORT);
        server = new Server();
        serverData = new ServerData(server);
        //call helper class to register classes
        KryoHelper.registerClasses(server.getKryo());
        server.addListener(new ServerListener(server, serverData));

        server.start();
        server.bind(NetworkConstants.TCP_PORT);

        Log.info("Server ready and listening on port " + NetworkConstants.TCP_PORT + "!");
    }

    public void processCommand(String command) {
        if (command.startsWith("move")) {
            String[] split = command.split(" ");

            try {
                String playerId = split[1];
                String moveCount = split[2];

                DiceResultMessage message = DiceResultMessage.createDiceResultMessage(Integer.parseInt(playerId), Integer.parseInt(moveCount));
                serverData.gotDiceRollResult(message);
            } catch (Exception e) {
                Log.error(e.getMessage());
            }
        }

        switch (command) {
            case "exit": {
                server.stop();
                Log.info("Console command: Server is shutting down...");
                System.exit(0);
                break;
            }
            default: {
                Log.info("Console command: Command \"" + command + "\" not recognized.");
                break;
            }
        }
    }
}
