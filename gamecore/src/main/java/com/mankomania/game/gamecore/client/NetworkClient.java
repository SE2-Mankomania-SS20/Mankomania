package com.mankomania.game.gamecore.client;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.mankomania.game.core.data.GameData;
import com.mankomania.game.core.network.KryoHelper;
import com.mankomania.game.core.network.messages.ChatMessage;
import com.mankomania.game.core.network.messages.PlayerGameReady;
import com.mankomania.game.core.network.messages.servertoclient.InitPlayers;
import com.mankomania.game.gamecore.util.ScreenEnum;
import com.mankomania.game.gamecore.util.ScreenManager;

import java.io.IOException;
import java.io.InputStream;

import static com.mankomania.game.core.network.NetworkConstants.*;

/*********************************
 Created by Fabian Oraze on 16.04.20
 *********************************/

public class NetworkClient extends Client {

    private Client client;
    private GameData gameData;

    public NetworkClient() {
        client = new Client();
        client.start();

        try {
            /**
             * client gets connection parameters form NetworkConstants class from core module
             */
            client.connect(TIMEOUT, IP_HOST, TCP_PORT);

        } catch (IOException e) {
            e.printStackTrace();
        }

        KryoHelper.registerClasses(client.getKryo());


        client.addListener(new Listener() {
            public void received(Connection connection, Object object) {
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
                                ScreenManager.getInstance().switchScreen(ScreenEnum.MAIN_GAME);
                            }
                        });
                    }
                }

                if (object instanceof InitPlayers) {

                    // once game starts each player gets a list from server
                    // and creates a hashMap with the IDs and player objects
                    InitPlayers list = (InitPlayers) object;
                    getGameData().intPlayers(list.playerIDs);
                    gameData.loadData(Gdx.files.internal("data.json").read());
                }
            }

            public void connected(Connection connection) {
                System.out.println("Connected to the server");
            }
        });

    }

    private GameData getGameData(){
        return ScreenManager.getInstance().getGame().getGameData();
    }

    public void sendMsgToServer(ChatMessage msg) {
        client.sendTCP(msg);
    }

    public void sendClientState(PlayerGameReady ready) {
        client.sendTCP(ready);
    }


}
