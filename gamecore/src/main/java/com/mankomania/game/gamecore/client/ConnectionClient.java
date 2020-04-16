package com.mankomania.game.gamecore.client;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.mankomania.game.network.ChatMessage;

import java.io.IOException;

/*********************************
 Created by Fabian Oraze on 16.04.20
 *********************************/

public class ConnectionClient {

    Client client;

    public ConnectionClient() {
        client = new Client();
        client.start();

        // 178.113.7.88
        // se2-demo.aau.at
        try {
            client.connect(5000, "178.113.7.88", 53211);
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
