package com.mankomania.game.server.game;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.FrameworkMessage;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;
import com.mankomania.game.core.data.GameData;
import com.mankomania.game.core.network.messages.ChatMessage;
import com.mankomania.game.core.network.messages.servertoclient.*;
import com.mankomania.game.core.network.messages.clienttoserver.*;
import com.mankomania.game.core.network.messages.clienttoserver.baseturn.*;
import com.mankomania.game.server.data.ServerData;

public class MankomaniaListener extends Listener {
    private final Server server;
    private final ServerData serverData;

    // refs
    private final GameData refGameData;

    public MankomaniaListener(Server server, ServerData serverData) {
        this.server = server;
        this.serverData = serverData;
        refGameData = serverData.getGameData();
    }

    @Override
    public void connected(Connection connection) {
        Log.info("Player connected: " + connection.toString() +
                " from endpoint " + connection.getRemoteAddressTCP().toString());

        if (serverData.connectPlayer(connection)) {
            Log.info("Player (" + connection.toString() + ") accepted on server.");
            connection.sendTCP(new PlayerConnected());
        } else {
            Log.error("Player (" + connection.toString() + ") could not connect! Lobby already full? Sending DisconnectPlayerMessage...");
            connection.sendTCP(new Notification("Server full"));
            connection.close();
        }
    }

    @Override
    public void received(Connection connection, Object object) {
        /*
         * Handle incoming messages
         * handle general messages above the switch and put the others in the correct case depending on the state what you want to be handled
         */

        // keep KeepAlive messages from spamming the log
        if (!(object instanceof FrameworkMessage.KeepAlive)) {
            Log.info("Message received from " + connection.toString() +
                    " at endpoint " + connection.getRemoteAddressTCP().toString() +
                    "; message class = " + object.getClass().getTypeName());
        }

        /*switch (serverData.getCurrentState()) {
            case PLAYER_CAN_ROLL_DICE: {
                // handle PLAYER_CAN_ROLL_DICE catch

                break;
            }
            case WAIT_FOR_DICE_RESULT: {
                // handle WAIT_FOR_DICE_RESULT

                break;
            }
            case MOVE_PLAYER_TO_FIELD: {
                // handle MOVE_PLAYER_TO_FIELD

                break;
            }
            case MOVE_PLAYER_TO_INTERSECTION: {
                // handle MOVE_PLAYER_TO_INTERSECTION

                break;
            }
            case WAIT_INTERSECTION_SELECTION: {
                // handle WAIT_INTERSECTION_SELECTION

                break;
            }
            case MOVE_PLAYER_TO_FIELD_OVER_LOTTERY: {
                // handle MOVE_PLAYER_TO_FIELD_OVER_LOTTERY

                break;
            }
            case DO_ACTION: {
                // handle DO_ACTION

                break;
            }
            case DONE_ACTION: {
                // handle DONE_ACTION

                break;
            }
            case END_TURN: {
                // handle END_TURN

                break;
            }
        }*/

        if (object instanceof ChatMessage) {
            ChatMessage request = (ChatMessage) object;
            request.text = "Player " + connection.getID() + ": " + request.text;

            Log.info("Chat message from " + connection.toString() + ": " + request.text);

            server.sendToAllTCP(request);
        } else if (object instanceof PlayerReady) {

            serverData.playerReady(connection);
            Log.info(connection.toString() + " is ready!");

            server.sendToAllExceptTCP(connection.getID(), new Notification("Player " + connection.getID() + " is ready!"));
            //send jonied player data

            // if all players are ready, start the game and notify all players
            if (serverData.checkForStart()) {
                Log.info("Game will be started now (all player are ready, player count: " + serverData.getUserMap().size() + ")");

                // MERGE: remove? can be removed when join player is send on each connection
                InitPlayers listIDs = new InitPlayers();
                listIDs.playerIDs = serverData.getPlayerList();

                /*
                 * initialize gameData and load it from json file the send all TCPs signal to start game
                 */

                refGameData.loadData(NetworkServer.class.getResourceAsStream("/resources/data.json"));
                refGameData.intPlayers(listIDs.playerIDs);
                server.sendToAllTCP(listIDs); // MERGE: necessary?

                refGameData.intPlayers(serverData.getPlayerList());


                // send game started message
                StartGame gameStartedMessage = new StartGame();
                gameStartedMessage.setPlayerIds(serverData.getPlayerList());
                server.sendToAllTCP(gameStartedMessage);

                // starting the game loop
                serverData.startGameLoop();

            }
        } else if (object instanceof DiceResultMessage) {
            DiceResultMessage message = (DiceResultMessage) object;

            Log.info("[DiceResultMessage] Got dice result message from player " + message.getPlayerId() +
                    ". Rolled a " + message.getDiceResult() + " (current turn player id: " + serverData.getCurrentPlayerTurnConnectionId() + ")");

            // handle the message to the "gamestate" handler
            serverData.gotDiceRollResult(message);
        } else if (object instanceof IntersectionSelectedMessage) {
            IntersectionSelectedMessage intersectionSelectedMessage = (IntersectionSelectedMessage) object;

            Log.info("[IntersectionSelectedMessage] Got intersection selection. Player " + intersectionSelectedMessage.getPlayerId() +
                    " chose to move to field " + intersectionSelectedMessage.getFieldChosen());

            serverData.gotIntersectionSelectionMessage(intersectionSelectedMessage);
        }
    }

    @Override
    public void disconnected(Connection connection) {
        Log.info("Player disconnected");
        serverData.disconnectPlayer(connection);
    }

}
