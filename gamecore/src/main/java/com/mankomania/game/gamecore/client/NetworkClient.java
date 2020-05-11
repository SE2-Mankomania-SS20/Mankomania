package com.mankomania.game.gamecore.client;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.mankomania.game.core.data.GameData;
import com.mankomania.game.core.network.KryoHelper;
import com.mankomania.game.core.network.messages.ChatMessage;
import com.mankomania.game.core.network.messages.PlayerGameReady;
import com.mankomania.game.core.network.messages.clienttoserver.PlayerDisconnected;
import com.mankomania.game.core.network.messages.servertoclient.DisconnectPlayer;
import com.mankomania.game.core.network.messages.servertoclient.GameStartedMessage;
import com.mankomania.game.core.network.messages.servertoclient.InitPlayers;
import com.mankomania.game.gamecore.util.Screen;
import com.mankomania.game.gamecore.util.ScreenManager;

import java.io.IOException;

import static com.mankomania.game.core.network.NetworkConstants.*;

/*********************************
 Created by Fabian Oraze on 16.04.20
 *********************************/

public class NetworkClient extends Client {

    private Client client;

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
            e.printStackTrace();
        }


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

                    System.out.println("[ChatMessage] received chat message (connection id: " + connection.getID() + "), text: '" + response.text + "'");
                }

                if (object instanceof PlayerGameReady) {
                    PlayerGameReady ready = (PlayerGameReady) object;
                    System.out.println("[PlayerGameReady] got PlayerGameReady message, gameReady = " + ready.gameReady);
                    if (ready.gameReady) {
                        //if game is ready switch to MainGameScreen
                        System.out.println("[PlayerGameReady] game is ready, so switching to MainGameScreen now!");
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


                if (object instanceof GameStartedMessage) {
                    // once game starts each player gets a list from server
                    // and creates a hashMap with the IDs and player objects
                    GameStartedMessage gameStartedMessage = (GameStartedMessage) object;
                    getGameData().initializePlayers(gameStartedMessage.getPlayerIds());

                    System.out.println("[GameStartedMessage] got GameStartedMessage, player array size: " + gameStartedMessage.getPlayerIds().size());
                    System.out.println("[GameStartedMessage] Initialized GameData with player id's");

                    // read field data already at startup, not here
//                    getGameData().loadData(Gdx.files.internal("data.json").read());
                }
            }

            public void connected(Connection connection) {
                System.out.println("Successfully connected to server! (id: " + connection.getID() + ")");
            }
        });


    }


    private GameData getGameData() {
        return ScreenManager.getInstance().getGame().getGameData();
    }

    public void sendMsgToServer(ChatMessage msg) {
        client.sendTCP(msg);
    }

    public void sendClientState(PlayerGameReady ready) {
        client.sendTCP(ready);
    }


}
