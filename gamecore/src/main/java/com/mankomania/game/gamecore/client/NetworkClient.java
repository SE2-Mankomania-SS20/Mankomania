package com.mankomania.game.gamecore.client;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;
import com.mankomania.game.core.network.KryoHelper;
import com.mankomania.game.core.network.messages.ChatMessage;
import com.mankomania.game.core.network.messages.PlayerGameReady;
import com.mankomania.game.core.network.messages.servertoclient.Notification;
import com.mankomania.game.core.network.messages.servertoclient.PlayerConnected;
import com.mankomania.game.core.network.messages.servertoclient.InitPlayers;
import com.mankomania.game.gamecore.MankomaniaGame;
import com.mankomania.game.gamecore.util.Screen;
import com.mankomania.game.gamecore.util.ScreenManager;

import java.io.IOException;

import static com.mankomania.game.core.network.NetworkConstants.*;

/*********************************
 Created by Fabian Oraze on 16.04.20
 *********************************/

public class NetworkClient {

    private Client client;

    public NetworkClient() {
        client = new Client();
        KryoHelper.registerClasses(client.getKryo());
        client.start();
        client.addListener(new Listener() {

            @Override
            public void received(Connection connection, Object object) {
                Log.info(object.getClass().getSimpleName());
                if (object instanceof PlayerConnected) {
                    Log.info("player connected");
                    MankomaniaGame.getMankomaniaGame().getNotifier().add(new Notification("player connected"));
                    Gdx.app.postRunnable(new Runnable() {
                        @Override
                        public void run() {
                            ScreenManager.getInstance().switchScreen(Screen.LOBBY);
                        }
                    });

                } else if (object instanceof ChatMessage) {
                    ChatMessage response = (ChatMessage) object;
                    //chat will be updated if message received
                    ClientChat.addText(response.text);

                } else if (object instanceof PlayerGameReady) {
                    PlayerGameReady ready = (PlayerGameReady) object;
                    if (ready.gameReady) {
                        //if game is ready switch to MainGameScreen
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
                } else if (object instanceof InitPlayers) {
                    // once game starts each player gets a list from server
                    // and creates a hashMap with the IDs and player objects
                    InitPlayers list = (InitPlayers) object;
                    MankomaniaGame.getMankomaniaGame().getGameData().intPlayers(list.playerIDs);
                } else if (object instanceof Notification) {
                    Notification notification = (Notification) object;
                    MankomaniaGame.getMankomaniaGame().getNotifier().add(notification);
                }
            }

            @Override
            public void connected(Connection connection) {
                super.connected(connection);
                Log.info("client connected");
                MankomaniaGame.getMankomaniaGame().getNotifier().add(new Notification("client connected"));
            }
        });
    }

    public void tryConnectClient() {

        try {
            /**
             * client gets connection parameters form NetworkConstants class from core module
             */
            client.connect(TIMEOUT, IP_HOST, TCP_PORT);

        } catch (IOException e) {
            Log.trace("Client connection error: ", e);
            MankomaniaGame.getMankomaniaGame().getNotifier().add(new Notification("error connecting"));
        }
    }

    public void sendMsgToServer(ChatMessage msg) {
        client.sendTCP(msg);
    }

    public void sendClientState(PlayerGameReady ready) {
        client.sendTCP(ready);
    }

    public void disconnect() {
        client.close();
    }
}
