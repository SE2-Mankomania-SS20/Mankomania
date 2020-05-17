package com.mankomania.game.gamecore.client;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;
import com.mankomania.game.core.data.GameData;
import com.mankomania.game.core.network.KryoHelper;
import com.mankomania.game.core.network.messages.ChatMessage;
import com.mankomania.game.core.network.messages.PlayerGameReady;
import com.mankomania.game.core.network.messages.clienttoserver.PlayerDisconnected;
import com.mankomania.game.core.network.messages.servertoclient.DisconnectPlayer;
import com.mankomania.game.core.network.messages.servertoclient.GameStartedMessage;
import com.mankomania.game.core.network.messages.servertoclient.baseturn.MovePlayerToFieldAfterIntersectionMessage;
import com.mankomania.game.core.network.messages.servertoclient.baseturn.MovePlayerToFieldMessage;
import com.mankomania.game.core.network.messages.servertoclient.baseturn.MovePlayerToIntersectionMessage;
import com.mankomania.game.core.network.messages.servertoclient.baseturn.PlayerCanRollDiceMessage;
import com.mankomania.game.gamecore.util.Screen;
import com.mankomania.game.gamecore.util.ScreenManager;

import java.io.IOException;

import static com.mankomania.game.core.network.NetworkConstants.*;

/*********************************
 Created by Fabian Oraze on 16.04.20
 *********************************/

public class NetworkClient extends Client {

    private Client client;
    private MessageHandler messageHandler;

    public NetworkClient() {
        client = new Client();
        KryoHelper.registerClasses(client.getKryo());
    }

    public void tryConnectClient() {
        client.start();

        try {
            /**
             * client gets connection parameters form NetworkConstants class from core module
             */
            client.connect(TIMEOUT, IP_HOST, TCP_PORT);

        } catch (IOException e) {
            Log.trace("Client connection error: ",e);
        }

        this.messageHandler = new MessageHandler(client);

        client.addListener(new Listener() {
            public void received(Connection connection, Object object) {
                if (object instanceof DisconnectPlayer) {
                    DisconnectPlayer disCon = (DisconnectPlayer) object;
                    Gdx.app.postRunnable(new Runnable() {
                        @Override
                        public void run() {
                            ScreenManager.getInstance().switchScreen(Screen.LAUNCH, disCon.errTxt);
                        }
                    });
                    //notify server that player can be disconnected
                    client.sendTCP(new PlayerDisconnected());

                }
                if (object instanceof ChatMessage) {
                    ChatMessage response = (ChatMessage) object;
                    //chat will be updated if message received
                    ClientChat.addText(response.text);

                    Log.info("[ChatMessage] received chat message (connection id: " + connection.getID() + "), text: '" + response.text + "'");
                }

                if (object instanceof PlayerGameReady) {
                    PlayerGameReady ready = (PlayerGameReady) object;
                    Log.info("[PlayerGameReady] got PlayerGameReady message, gameReady = " + ready.gameReady);
                    if (ready.gameReady) {
                        //if game is ready switch to MainGameScreen
                        Log.info("[PlayerGameReady] game is ready, so switching to MainGameScreen now!");
                        /**
                         * post a Runnable from networking thread to the libgdx rendering thread
                         */
                        Gdx.app.postRunnable(new Runnable() {
                            @Override
                            public void run() {
                                ScreenManager.getInstance().switchScreen(Screen.MAIN_GAME);
                            }
                        });
                    }
                }

                /* ==== GameStartedMessage ==== */
                if (object instanceof GameStartedMessage) {
                    // once game starts each player gets a list from server
                    // and creates a hashMap with the IDs and player objects
                    GameStartedMessage gameStartedMessage = (GameStartedMessage) object;
                    getGameData().intPlayers(gameStartedMessage.getPlayerIds());
                    getGameData().setLocalPlayer(client.getID());

                    Log.info("[GameStartedMessage] got GameStartedMessage, player array size: " + gameStartedMessage.getPlayerIds().size());
                    Log.info("[GameStartedMessage] Initialized GameData with player id's");
                }

                /* ==== PlayerCanRollDiceMessage ==== */
                if (object instanceof PlayerCanRollDiceMessage) {
                    PlayerCanRollDiceMessage playerCanRollDiceMessage = (PlayerCanRollDiceMessage) object;

                    Log.info("[PlayerCanRollDiceMessage] Player " + playerCanRollDiceMessage.getPlayerId() + " can roll the dice now!");

                    messageHandler.gotPlayerCanRollDiceMessage(playerCanRollDiceMessage);
                }

                /* ==== MovePlayerToFieldMessage ==== */
                if (object instanceof MovePlayerToFieldMessage) {
                    MovePlayerToFieldMessage movePlayerToFieldMessage = (MovePlayerToFieldMessage) object;

                    Log.info("[MovePlayerToFieldMessage] Player " + movePlayerToFieldMessage.getPlayerId() + " got move to " + movePlayerToFieldMessage.getFieldToMoveTo() + " message");

                    messageHandler.gotMoveToFieldMessage(movePlayerToFieldMessage);
                }

                /* ==== MovePlayerToIntersectionMessage ==== */
                if (object instanceof MovePlayerToIntersectionMessage) {
                    MovePlayerToIntersectionMessage movePlayerToIntersectionMessage = (MovePlayerToIntersectionMessage) object;

                    Log.info("[MovePlayerToIntersectionMessage] Player " + movePlayerToIntersectionMessage.getPlayerId() + " got to move to field " +
                            movePlayerToIntersectionMessage.getFieldToMoveTo() + " and has to choose between path 1 = (" + movePlayerToIntersectionMessage.getSelectionOption1() +
                            ") and path 2 = (" + movePlayerToIntersectionMessage.getSelectionOption2() + ")");

                    messageHandler.gotMoveToIntersectionMessage(movePlayerToIntersectionMessage);
                }

                if (object instanceof MovePlayerToFieldAfterIntersectionMessage) {
                    MovePlayerToFieldAfterIntersectionMessage movePlayerAfterIntersectionMsg = (MovePlayerToFieldAfterIntersectionMessage) object;

                    Log.info("[MovePlayerToFieldAfterIntersectionMessage] Player " + movePlayerAfterIntersectionMsg.getPlayerId() + " got to move on the field " +
                            movePlayerAfterIntersectionMsg.getFieldToMoveTo() + " directly after the intersection.");

                    messageHandler.gotMoveAfterIntersectionMessage(movePlayerAfterIntersectionMsg);
                }
            }

            public void connected(Connection connection) {
                Log.info("Successfully connected to server! (id: " + connection.getID() + ")");
            }
        });


    }


    private GameData getGameData() {
        return ScreenManager.getInstance().getGame().getGameData();
    }

    // old implementation for ChatScreen, maybe direct it to message handler
    public void sendMsgToServer(ChatMessage msg) {
        client.sendTCP(msg);
    }

    public void sendClientState(PlayerGameReady ready) {
        client.sendTCP(ready);
    }

    /**
     * Gets the message handler. Used to get hold of the reference to MessageHandler over the MankomaniGame, so it
     * can be used game wide as kind of a wrapper for network messaging.
     * @return the instance of MessageHandler
     */
    public MessageHandler getMessageHandler() {
        return messageHandler;
    }
}
