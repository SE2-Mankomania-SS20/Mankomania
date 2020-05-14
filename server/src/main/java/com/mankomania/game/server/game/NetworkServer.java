package com.mankomania.game.server.game;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
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
        server = new Server();
        //call helper class to register classes
        KryoHelper.registerClasses(server.getKryo());

        server.start();
        server.bind(NetworkConstants.TCP_PORT);


        serverData = new ServerData();


        server.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                if (object instanceof PlayerDisconnected) {
                    connection.close();
                }

                if (object instanceof ChatMessage) {
                    ChatMessage request = (ChatMessage) object;
                    request.text = "Player " + connection.getID() + ": " + request.text;
                    System.out.println(request.text);

                    server.sendToAllTCP(request);
                }

                if (object instanceof PlayerGameReady) {
                    PlayerGameReady state = (PlayerGameReady) object;
                    boolean ready = state.playerReady;

                    serverData.playerReady(connection, ready);
                    System.out.println("Player " + connection.getID() + " is ready!");

                    //TODO: send notification to all TCPs that player is ready

                    if (serverData.checkForStart()) {
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
                if (serverData.connectPlayer(connection)) {
                    System.out.println("Player has connected");
                } else {
                    System.err.println("Player could not connect!");
                    DisconnectPlayer disCon = new DisconnectPlayer();
                    disCon.errTxt = "Server already full!";
                    server.sendToTCP(connection.getID(), disCon);


                }
            }

            @Override
            public void disconnected(Connection connection) {
                DisconnectPlayer disCon = new DisconnectPlayer();
                disCon.errTxt = "Client disconnected unexpectedly";
                server.sendToTCP(connection.getID(), disCon);
                serverData.disconnectPlayer(connection);
                super.disconnected(connection);
            }
        });
        System.out.println("Server is ready...");
    }

    public void processCommand(String command) {
        switch (command) {
            case "exit": {
                server.stop();
                System.out.println("Server is shutting down...");
                System.exit(0);
                break;
            }
            default: {
                System.out.println("Command \"" + command + "\" not recognized.");
                break;
            }
        }
    }
}
