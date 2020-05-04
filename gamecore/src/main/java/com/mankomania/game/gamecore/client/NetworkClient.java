package com.mankomania.game.gamecore.client;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.mankomania.game.core.network.ChatMessage;
import com.mankomania.game.core.network.GameState;
import com.mankomania.game.gamecore.util.ScreenEnum;
import com.mankomania.game.gamecore.util.ScreenManager;

import java.io.IOException;

import static com.mankomania.game.core.network.NetworkConstants.*;

/*********************************
 Created by Fabian Oraze on 16.04.20
 *********************************/

public class NetworkClient extends Client {

    Client client;

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

        Kryo kryoClient = client.getKryo();
        kryoClient.register(ChatMessage.class);
        kryoClient.register(GameState.class);

        client.addListener(new Listener() {
            public void received(Connection connection, Object object) {
                if (object instanceof ChatMessage) {
                    ChatMessage response = (ChatMessage) object;
                    //chat will be updated if message received
                    ClientChat.addText(response.getText());
                }

                if (object instanceof GameState) {
                    GameState ready = (GameState) object;
                    if (ready.getGameReady()) {
                        //if game is ready switch to MainGameScreen
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                /**
                                 * post a Runnable to the rendering thread that does something
                                 */
                                Gdx.app.postRunnable(new Runnable() {
                                    @Override
                                    public void run() {
                                        ScreenManager.getInstance().switchScreen(ScreenEnum.MAIN_GAME);
                                    }
                                });
                            }
                        }).start();
                    }
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

    public void sendClientState(GameState ready) {
        client.sendTCP(ready);
    }


}
