package com.mankomania.game.gamecore.client;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;
import com.mankomania.game.core.network.KryoHelper;
import com.mankomania.game.core.network.messages.ChatMessage;
import com.mankomania.game.core.network.messages.PlayerGameReady;
import com.mankomania.game.core.network.messages.clienttoserver.PlayerDisconnected;
import com.mankomania.game.core.network.messages.servertoclient.DisconnectPlayer;
import com.mankomania.game.core.network.messages.servertoclient.InitPlayers;
import com.mankomania.game.gamecore.util.GameController;
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

    public void tryConnectClient(){
        client.start();

        try {
            /**
             * client gets connection parameters form NetworkConstants class from core module
             */
            client.connect(TIMEOUT, IP_HOST, TCP_PORT);

        } catch (IOException e) {
            Log.trace("Client connection error: ",e);
        }




        client.addListener(new Listener() {
            public void received(Connection connection, Object object) {
                if (object instanceof DisconnectPlayer){
                    DisconnectPlayer disCon = (DisconnectPlayer)object;
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
                }

                if (object instanceof PlayerGameReady) {
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
                }

                if (object instanceof InitPlayers) {

                    // once game starts each player gets a list from server
                    // and creates a hashMap with the IDs and player objects
                    InitPlayers list = (InitPlayers) object;
                    GameController.getGameData().intPlayers(list.playerIDs);
                }
            }

            public void connected(Connection connection) {
                System.out.println("Connected to the server");
            }
        });


    }



    public void sendMsgToServer(ChatMessage msg) {
        client.sendTCP(msg);
    }

    public void sendClientState(PlayerGameReady ready) {
        client.sendTCP(ready);
    }



}
