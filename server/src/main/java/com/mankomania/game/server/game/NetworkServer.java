package com.mankomania.game.server.game;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.FrameworkMessage;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;
import com.mankomania.game.core.data.GameData;
import com.mankomania.game.core.network.KryoHelper;
import com.mankomania.game.core.network.messages.ChatMessage;

import java.io.IOException;

import com.mankomania.game.core.network.messages.clienttoserver.PlayerDisconnected;
import com.mankomania.game.core.network.messages.clienttoserver.baseturn.DiceResultMessage;
import com.mankomania.game.core.network.messages.clienttoserver.baseturn.IntersectionSelectedMessage;
import com.mankomania.game.core.network.messages.servertoclient.DisconnectPlayer;
import com.mankomania.game.core.network.messages.servertoclient.GameStartedMessage;
import com.mankomania.game.core.network.NetworkConstants;
import com.mankomania.game.core.network.messages.PlayerGameReady;
import com.mankomania.game.server.data.ServerData;

public class NetworkServer {
    private Server server;
    private ServerData serverData;
    private GameData gameData;
    private GameStateLogic gameStateLogic;


    public NetworkServer() throws IOException {
        Log.info("Starting server, listening on port " + NetworkConstants.TCP_PORT);

        server = new Server();
        //call helper class to register classes
        KryoHelper.registerClasses(server.getKryo());

        server.start();
        server.bind(NetworkConstants.TCP_PORT);


        serverData = new ServerData();

        // TODO: extract this anonymous class into its own real class and extract methods to reduce complexity
        server.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                // keep KeepAlive messages from spamming the log
                if (!(object instanceof FrameworkMessage.KeepAlive)) {
                    Log.info("Message received from " + connection.toString() +
                            " at endpoint " + connection.getRemoteAddressTCP().toString() +
                            "; message class = " + object.getClass().getTypeName());
                }

                /* ==== PlayerDisconnected ==== */
                if (object instanceof PlayerDisconnected) {
                    connection.close();
                }

                /* ==== ChatMessage ==== */
                if (object instanceof ChatMessage) {
                    ChatMessage request = (ChatMessage) object;
                    request.text = "Player " + connection.getID() + ": " + request.text;

                    Log.info("Chat message from " + connection.toString() + ": " + request.text);

                    server.sendToAllTCP(request);
                }

                /* ==== PlayerGameReady ==== */
                if (object instanceof PlayerGameReady) {
                    PlayerGameReady state = (PlayerGameReady) object;
                    boolean ready = state.playerReady;

                    serverData.playerReady(connection, ready);
                    Log.info(connection.toString() + " is ready!");

                    // TODO: send notification to all TCPs that player is ready

                    // if all players are ready, start the game and notify all players
                    if (serverData.checkForStart()) {
                        Log.info("Game will be started now (all player are ready, player count: " + serverData.getUserMap().size() + ")");
                        state.gameReady = true;
                        server.sendToAllTCP(state);

                        // initialize gameData and load it from json  (maybe do the json loading at startup time?)
                        gameData = new GameData();
                        gameData.loadData(NetworkServer.class.getResourceAsStream("/resources/data.json"));
                        gameData.intPlayers(serverData.initPlayerList());

                        gameStateLogic = new GameStateLogic(serverData, gameData, server);

                        GameStartedMessage gameStartedMessage = new GameStartedMessage();
                        gameStartedMessage.setPlayerIds(serverData.initPlayerList());
                        server.sendToAllTCP(gameStartedMessage);

                        // starting the game loop
                        gameStateLogic.startGameLoop();
                    }
                }

                /* ==== DiceResultMessage ==== */
                if (object instanceof DiceResultMessage) {
                    DiceResultMessage message = (DiceResultMessage) object;

                    Log.info("[DiceResultMessage] Got dice result message from player " + message.getPlayerId() +
                            ". Rolled a " + message.getDiceResult() + " (current turn player id: " + serverData.getCurrentPlayerTurnConnectionId() + ")");

                    // handle the message to the "gamestate" handler
                    gameStateLogic.gotDiceRollResult(message);
                }

                /* ==== IntersectionSelectedMessage ==== */
                if (object instanceof IntersectionSelectedMessage) {
                    IntersectionSelectedMessage intersectionSelectedMessage = (IntersectionSelectedMessage) object;

                    Log.info("[IntersectionSelectedMessage] Got intersection selection. Player " + intersectionSelectedMessage.getPlayerId() +
                            " chose to move to field " + intersectionSelectedMessage.getFieldChosen());

                    gameStateLogic.gotIntersectionSelectionMessage(intersectionSelectedMessage);
                }
            }

            @Override
            public void connected(Connection connection) {
                Log.info("Player connected: " + connection.toString() +
                        " from endpoint " + connection.getRemoteAddressTCP().toString());

                if (serverData.connectPlayer(connection)) {
                    Log.info("Player (" + connection.toString() + ") accepted on server.");
                } else {
                    Log.error("Player (" + connection.toString() + ") could not connect! Lobby already full? Sending DisconnectPlayerMessage...");

                    DisconnectPlayer disCon = new DisconnectPlayer();
                    disCon.errTxt = "Server already full!";
                    server.sendToTCP(connection.getID(), disCon);
                }
            }

            @Override
            public void disconnected(Connection connection) {
//                Log.info("Player disconnected: " + connection.toString() +
//                        " from endpoint " + connection.getRemoteAddressTCP().toString());
                Log.info("Player disconnected");

                // i think its not possible to send messages after client disconnected -> null pointer exception
                DisconnectPlayer disCon = new DisconnectPlayer();
                disCon.errTxt = "Client disconnected unexpectedly";
                server.sendToTCP(connection.getID(), disCon);
                serverData.disconnectPlayer(connection);

                // TODO: send notification to all clients
                // TODO: if using back button on phone, player does not get disconnected
                super.disconnected(connection);
            }
        });

        Log.info("Server ready and listening on port " + NetworkConstants.TCP_PORT + "!");
    }

    public void processCommand(String command) {
        if (command.startsWith("move")) {
            String[] split = command.split(" ");

            try {
                String playerId = split[1];
                String moveCount = split[2];

                DiceResultMessage message = DiceResultMessage.createDiceResultMessage(Integer.parseInt(playerId), Integer.parseInt(moveCount));
                this.gameStateLogic.gotDiceRollResult(message);
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
