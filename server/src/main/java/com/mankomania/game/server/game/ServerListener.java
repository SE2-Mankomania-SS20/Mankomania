package com.mankomania.game.server.game;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.FrameworkMessage;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;
import com.mankomania.game.core.data.GameData;
import com.mankomania.game.core.network.messages.ChatMessage;
import com.mankomania.game.core.network.messages.clienttoserver.cheat.CheatedMessage;
import com.mankomania.game.core.network.messages.clienttoserver.roulette.RouletteStakeMessage;
import com.mankomania.game.core.network.messages.clienttoserver.roulette.StartRouletteClient;
import com.mankomania.game.core.network.messages.clienttoserver.stock.StockResultMessage;
import com.mankomania.game.core.network.messages.clienttoserver.trickyone.RollDiceTrickyOne;
import com.mankomania.game.core.network.messages.clienttoserver.trickyone.StopRollingDice;
import com.mankomania.game.core.network.messages.clienttoserver.hotel.PlayerBuyHotelDecision;
import com.mankomania.game.core.network.messages.servertoclient.*;
import com.mankomania.game.core.network.messages.clienttoserver.*;
import com.mankomania.game.core.network.messages.clienttoserver.baseturn.*;
import com.mankomania.game.server.data.ServerData;
import com.mankomania.game.server.minigames.RouletteHandler;

/**
 * This listener class that handles all events (like onReceive) of the network server.
 * Events are sorted and processed and then redirected to ServerData with their corresponding functions.
 * There the "real" consequences of the messages on the GameState, GameData and ServerData are handled.
 */
public class ServerListener extends Listener {
    private final Server server;
    private final ServerData serverData;
    private final RouletteHandler rouletteHandler;

    // refs
    private final GameData refGameData;

    public ServerListener(Server server, ServerData serverData) {
        this.server = server;
        this.serverData = serverData;

        refGameData = serverData.getGameData();

        this.rouletteHandler = new RouletteHandler(serverData, server);
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
            request.setText("Player " + connection.getID() + ": " + request.getText());

            Log.info("Incoming Message", "Chat message from " + connection.toString() + ": " + request.getText());

            server.sendToAllTCP(request);
        } else if (object instanceof PlayerReady) {

            serverData.playerReady(connection.getID());
            Log.info("Incoming Message", connection.toString() + " is ready!");

            server.sendToAllExceptTCP(connection.getID(), new Notification("Player " + (refGameData.getPlayerByConnectionId(connection.getID()).getPlayerIndex() + 1) + " is ready!"));
            //send joined player data

            // if all players are ready, start the game and notify all players
            if (serverData.checkForStart()) {
                Log.info("Game will be started now (all player are ready, player count: " + refGameData.getPlayers().size() + ")");

                /*
                 * initialize gameData and load it from json file the send all TCPs signal to start game
                 */

                // send game started message
                StartGame gameStartedMessage = new StartGame();
                gameStartedMessage.setPlayers(serverData.getGameData().getPlayers());
                server.sendToAllTCP(gameStartedMessage);

                // starting the game loop
                serverData.startGameLoop();
            }
        } else if (object instanceof DiceResultMessage) {
            DiceResultMessage message = (DiceResultMessage) object;

            Log.info("DiceResultMessage", "Got dice result message from player " + message.getPlayerIndex() +
                    ". Rolled a " + message.getDiceResult() + " (current turn player id: " + serverData.getCurrentPlayerTurnConnectionId() + ")");

            // handle the message to the "gamestate" handler
            serverData.gotDiceRollResult(message, connection.getID());
        } else if (object instanceof IntersectionSelection) {
            IntersectionSelection intersectionSelection = (IntersectionSelection) object;

            Log.info("IntersectionSelectedMessage", "Got intersection selection. Player " + intersectionSelection.getPlayerIndex() +
                    " chose to move to field " + intersectionSelection.getFieldIndex());

            serverData.gotIntersectionSelectionMessage(intersectionSelection, connection.getID());
        } else if (object instanceof TurnFinished) {
            if (serverData.getCurrentPlayerTurnConnectionId() == connection.getID()) {
                serverData.turnFinished();
            } else {
                Log.error("TurnFinished", "Player " + connection.getID() + " tied TurnFinish currentPlayerTurn: " + serverData.getCurrentPlayerTurnConnectionId());
            }
        } else if (object instanceof StockResultMessage) {
            StockResultMessage message = (StockResultMessage) object;

            Log.info("[StockResultMessage] Got Stock result message from player " + message.getPlayerId() +
                    ". got a " + message.getStockResult() + " (current turn player id: " + serverData.getCurrentPlayerTurnConnectionId() + ")");

            serverData.getStockHandler().gotStockResult(message);
        } else if (object instanceof RollDiceTrickyOne) {
            RollDiceTrickyOne message = (RollDiceTrickyOne) object;
            Log.info("MiniGame TrickyOne", "Player pressed button to continue rolling the dice");
            serverData.getTrickyOneHandler().rollDice(message, connection.getID());
        } else if (object instanceof StopRollingDice) {
            StopRollingDice message = (StopRollingDice) object;
            Log.info("MiniGame TrickyOne", "Player pressed button to stop rolling and end the miniGame");
            serverData.getTrickyOneHandler().stopMiniGame(message, connection.getID());
        } else if (object instanceof PlayerBuyHotelDecision) {
            PlayerBuyHotelDecision playerBuyHotelDecision = (PlayerBuyHotelDecision) object;

            Log.info("PlayerBuyHotelDecision", "Got PlayerBuyHotelDecision message from player " + playerBuyHotelDecision.getPlayerIndex() + " (from connection " +
                    connection.getID() + "). Wants " + (playerBuyHotelDecision.isHotelBought() ? "" : "NOT ") + " to buy hotel on field (" + playerBuyHotelDecision.getHotelFieldId() + ")");

            serverData.getHotelHandler().gotPlayerBuyHotelDecision(playerBuyHotelDecision, connection.getID());
        } else if (object instanceof RouletteStakeMessage) {
            RouletteStakeMessage rouletteStakeMessage = (RouletteStakeMessage) object;
            rouletteHandler.setInputPlayerBet(rouletteStakeMessage.getRsmPlayerIndex(), rouletteStakeMessage);

            Log.info("[RouletteStakeMessage] Roulette-Minigame: " + rouletteStakeMessage.getRsmPlayerIndex() + ". Player has choosen bet");
        } else if (object instanceof StartRouletteClient) {
            //ein Client hat Rouletteminigame gestartet
            rouletteHandler.startRouletteGame();
            Log.info("Minigame Roulette has started");
        } else if (object instanceof CheatedMessage) {
            //client pressed cheat button
            CheatedMessage msg = (CheatedMessage) object;
            serverData.getCheatHandler().gotCheatedMsg(msg.getPlayerIndex());
            Log.info("Player " + msg.getPlayerIndex() + " has pressed Cheat button");
        }
    }

    @Override
    public void disconnected(Connection connection) {
        Log.info("Player disconnected");
        serverData.disconnectPlayer(connection.getID());
    }
}
