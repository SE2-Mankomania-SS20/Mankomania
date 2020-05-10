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
import java.io.InputStream;

import com.mankomania.game.core.network.messages.clienttoserver.PlayerDisconnected;
import com.mankomania.game.core.network.messages.servertoclient.DisconnectPlayer;
import com.mankomania.game.core.network.messages.servertoclient.InitPlayers;
import com.mankomania.game.core.network.NetworkConstants;
import com.mankomania.game.core.network.messages.PlayerGameReady;
import com.mankomania.game.server.data.ServerData;

public class NetworkServer {
    private Server server;
    private ServerData serverData;
    private GameData gameData;
    private GameStateLogic game;


    public NetworkServer() throws IOException {
        Log.info("Starting server, listening on port " + NetworkConstants.TCP_PORT);

        server = new Server();
        //call helper class to register classes
        KryoHelper.registerClasses(server.getKryo());

        server.start();
        server.bind(NetworkConstants.TCP_PORT);


        serverData = new ServerData();

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

                    //TODO: send notification to all TCPs that player is ready

                    // if all players are ready, start the game and notify all players
                    if (serverData.checkForStart()) {
                        Log.info("Game will be started now (all player are ready, player count: " + serverData.initPlayerList().size() + ")");
                        state.gameReady = true;
                        server.sendToAllTCP(state);
                        InitPlayers listIDs = new InitPlayers();
                        listIDs.playerIDs = serverData.initPlayerList();

                        /**
                         * initialize gameData and load it from json file the send all TCPs signal to start game
                         */
                        gameData = new GameData();
                        gameData.intPlayers(listIDs.playerIDs);
                        server.sendToAllTCP(listIDs);
                        gameData.loadData(NetworkServer.class.getResourceAsStream("/resources/data.json"));
                        //TODO: start game loop

                    }
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
                Log.info("Player disconnected: " + connection.toString() +
                        " from endpoint " + connection.getRemoteAddressTCP().toString());

                DisconnectPlayer disCon = new DisconnectPlayer();
                disCon.errTxt = "Client disconnected unexpectedly";
                server.sendToTCP(connection.getID(), disCon);
                serverData.disconnectPlayer(connection);
                // TODO: send notification to all clients
                super.disconnected(connection);
            }
        });

        Log.info("Server ready and listening on port " + NetworkConstants.TCP_PORT + "!");
    }

    public void processCommand(String command) {
        switch (command) {
            case "exit": {
                server.stop();
                Log.info("Console command: Server is shutting down...");
                System.exit(0);
            }
            default: {
                Log.info("Console command: Command \"" + command + "\" not recognized.");
                break;
            }
        }
    }
}
