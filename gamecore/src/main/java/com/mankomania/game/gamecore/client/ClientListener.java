package com.mankomania.game.gamecore.client;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;
import com.mankomania.game.core.network.messages.ChatMessage;
import com.mankomania.game.core.network.messages.servertoclient.InitPlayers;
import com.mankomania.game.core.network.messages.servertoclient.Notification;
import com.mankomania.game.core.network.messages.servertoclient.PlayerConnected;
import com.mankomania.game.core.network.messages.servertoclient.StartGame;
import com.mankomania.game.core.network.messages.servertoclient.baseturn.MovePlayerToFieldAfterIntersectionMessage;
import com.mankomania.game.core.network.messages.servertoclient.baseturn.MovePlayerToFieldMessage;
import com.mankomania.game.core.network.messages.servertoclient.baseturn.MovePlayerToIntersectionMessage;
import com.mankomania.game.core.network.messages.servertoclient.baseturn.PlayerCanRollDiceMessage;
import com.mankomania.game.core.network.messages.servertoclient.minigames.RouletteResultMessage;
import com.mankomania.game.core.network.messages.servertoclient.minigames.StartRouletteServer;
import com.mankomania.game.gamecore.MankomaniaGame;
import com.mankomania.game.gamecore.screens.RouletteMinigameScreen;
import com.mankomania.game.gamecore.util.Screen;
import com.mankomania.game.gamecore.util.ScreenManager;

import java.util.LinkedHashMap;

/**
 * The listener class that handles all onReceived events of the network client.
 * Events are sorted and processed and redirected to the MessageHandler, that actually
 * manages the consequences of the messages on the gamestate and GameData.
 */
public class ClientListener extends Listener {
    private final Client client;
    private final MessageHandler messageHandler;

    public ClientListener(Client client, MessageHandler messageHandler) {
        this.client = client;
        this.messageHandler = messageHandler;
    }

    @Override
    public void received(Connection connection, Object object) {
        Log.info(object.getClass().getSimpleName());

        if (object instanceof PlayerConnected) {
            Log.info("player connected");
            Gdx.app.postRunnable(() -> ScreenManager.getInstance().switchScreen(Screen.LOBBY));

        } else if (object instanceof ChatMessage) {
            ChatMessage response = (ChatMessage) object;
            //chat will be updated if message received
            ClientChat.addText(response.text);

            Log.info("[ChatMessage] received chat message (connection id: " + connection.getID() + "), text: '" + response.text + "'");
        } else if (object instanceof InitPlayers) {
            // once game starts each player gets a list from server
            // and creates a hashMap with the IDs and player objects
            InitPlayers list = (InitPlayers) object;
            MankomaniaGame.getMankomaniaGame().getGameData().intPlayers(list.getPlayerIDs());

        } else if (object instanceof Notification) {
            Notification notification = (Notification) object;
            MankomaniaGame.getMankomaniaGame().getNotifier().add(notification);

        } else if (object instanceof StartGame) {
            /*
             * post a Runnable from networking thread to the libgdx rendering thread
             */
            Gdx.app.postRunnable(() -> ScreenManager.getInstance().switchScreen(Screen.MAIN_GAME));
            // once game starts each player gets a list from server
            // and creates a hashMap with the IDs and player objects
            StartGame gameStartedMessage = (StartGame) object;
            MankomaniaGame.getMankomaniaGame().getGameData().intPlayers(gameStartedMessage.getPlayerIds());
            MankomaniaGame.getMankomaniaGame().getGameData().setLocalPlayer(client.getID());

            Log.info("[GameStartedMessage] got GameStartedMessage, player array size: " + gameStartedMessage.getPlayerIds().size());
            Log.info("[GameStartedMessage] Initialized GameData with player id's");
        } else if (object instanceof PlayerCanRollDiceMessage) {
            PlayerCanRollDiceMessage playerCanRollDiceMessage = (PlayerCanRollDiceMessage) object;

            Log.info("[PlayerCanRollDiceMessage] Player " + playerCanRollDiceMessage.getPlayerId() + " can roll the dice now!");

            messageHandler.gotPlayerCanRollDiceMessage(playerCanRollDiceMessage);
        } else if (object instanceof MovePlayerToFieldMessage) {
            MovePlayerToFieldMessage movePlayerToFieldMessage = (MovePlayerToFieldMessage) object;

            Log.info("[MovePlayerToFieldMessage] Player " + movePlayerToFieldMessage.getPlayerId() + " got move to " + movePlayerToFieldMessage.getFieldToMoveTo() + " message");

            messageHandler.gotMoveToFieldMessage(movePlayerToFieldMessage);
        } else if (object instanceof MovePlayerToIntersectionMessage) {
            MovePlayerToIntersectionMessage movePlayerToIntersectionMessage = (MovePlayerToIntersectionMessage) object;

            Log.info("[MovePlayerToIntersectionMessage] Player " + movePlayerToIntersectionMessage.getPlayerId() + " got to move to field " +
                    movePlayerToIntersectionMessage.getFieldToMoveTo() + " and has to choose between path 1 = (" + movePlayerToIntersectionMessage.getSelectionOption1() +
                    ") and path 2 = (" + movePlayerToIntersectionMessage.getSelectionOption2() + ")");

            messageHandler.gotMoveToIntersectionMessage(movePlayerToIntersectionMessage);
        } else if (object instanceof MovePlayerToFieldAfterIntersectionMessage) {
            MovePlayerToFieldAfterIntersectionMessage movePlayerAfterIntersectionMsg = (MovePlayerToFieldAfterIntersectionMessage) object;

            Log.info("[MovePlayerToFieldAfterIntersectionMessage] Player " + movePlayerAfterIntersectionMsg.getPlayerId() + " got to move on the field " +
                    movePlayerAfterIntersectionMsg.getFieldToMoveTo() + " directly after the intersection.");

            messageHandler.gotMoveAfterIntersectionMessage(movePlayerAfterIntersectionMsg);
        }
        else if (object instanceof StartRouletteServer) {
            //Client bekommt Message von Server, das Rouletterunde gestartet wird
            StartRouletteServer startRouletteServer = (StartRouletteServer) object;
            messageHandler.gotStartRouletteServer(startRouletteServer);
        }
        else if (object instanceof RouletteResultMessage) {
            //Verloren oder Gewonnen
            RouletteResultMessage rouletteResultMessage = (RouletteResultMessage) object;
            //von Server wird result in gamedata gespeichert
            MankomaniaGame.getMankomaniaGame().getGameData().setRouletteResults(rouletteResultMessage);


            //Update UI, WIN OR LOST
        }
    }
}
