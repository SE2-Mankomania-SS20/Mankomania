package com.mankomania.game.gamecore.client;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.minlog.Log;
import com.mankomania.game.core.network.KryoHelper;
import com.mankomania.game.core.network.messages.ChatMessage;
import com.mankomania.game.core.network.messages.clienttoserver.PlayerReady;
import com.mankomania.game.core.network.messages.servertoclient.*;
import com.mankomania.game.gamecore.MankomaniaGame;

import java.io.IOException;

import static com.mankomania.game.core.network.NetworkConstants.*;

/*
 Created by Fabian Oraze on 16.04.20
 */

public class NetworkClient {

    private final Client client;
    private final MessageHandler messageHandler;

    public NetworkClient() {
        client = new Client();
        KryoHelper.registerClasses(client.getKryo());

        messageHandler = new MessageHandler(client);

        client.start();
        client.addListener(new ClientListener(client, messageHandler)); // pass a new instance of the ClientListener to the client to handle all received messages.
    }

    public void tryConnectClient() {

        try {
            /*
             * client gets connection parameters form NetworkConstants class from core module
             */
            client.connect(TIMEOUT, IP_HOST, TCP_PORT);

        } catch (IOException e) {
            Log.trace("Client connection error: ", e);
            MankomaniaGame.getMankomaniaGame().getNotifier().add(new Notification("error connecting"));
        }
    }

    // old implementation for ChatScreen, maybe direct it to message handler
    public void sendMsgToServer(ChatMessage msg) {
        client.sendTCP(msg);
    }

    public void sendClientState(PlayerReady ready) {
        client.sendTCP(ready);
    }

    public void disconnect() {
        client.close();
    }

    /**
     * Gets the message handler. Used to get hold of the reference to MessageHandler over the MankomaniGame, so it
     * can be used game wide as kind of a wrapper for network messaging.
     *
     * @return the instance of MessageHandler
     */
    public MessageHandler getMessageHandler() {
        return messageHandler;
    }
}
