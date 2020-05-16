package com.mankomania.game.core.network;

import com.badlogic.gdx.graphics.Color;
import com.esotericsoftware.kryo.Kryo;
import com.mankomania.game.core.network.messages.ChatMessage;
import com.mankomania.game.core.network.messages.PlayerGameReady;
import com.mankomania.game.core.network.messages.clienttoserver.PlayerDisconnected;
import com.mankomania.game.core.network.messages.servertoclient.Notification;
import com.mankomania.game.core.network.messages.servertoclient.PlayerConnected;
import com.mankomania.game.core.network.messages.servertoclient.InitPlayers;

/*********************************
 Created by Fabian Oraze on 06.05.20
 *********************************/

public class KryoHelper {


    public static void registerClasses(Kryo kryo) {

        kryo.register(ChatMessage.class);
        kryo.register(PlayerGameReady.class);
        kryo.register(InitPlayers.class);
        kryo.register(java.util.ArrayList.class);
        kryo.register(PlayerConnected.class);
        kryo.register(PlayerDisconnected.class);
        kryo.register(Notification.class);
        kryo.register(Color.class);
    }
}
