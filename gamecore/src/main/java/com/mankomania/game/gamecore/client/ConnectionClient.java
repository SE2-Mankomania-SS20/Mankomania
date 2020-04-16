package com.mankomania.game.gamecore.client;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.mankomania.game.core.network.ChatMessage;
import java.io.IOException;
import static com.mankomania.game.core.network.NetworkConstants.*;

/*********************************
 Created by Fabian Oraze on 16.04.20
 *********************************/

public class ConnectionClient {

    Client client;

    public ConnectionClient() {
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

        client.addListener(new Listener() {
            public void received(Connection connection, Object object) {
                if (object instanceof ChatMessage) {
                    ChatMessage response = (ChatMessage) object;
                    System.out.println(response.text);
                }
            }
        });
    }

}
