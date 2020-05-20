package com.mankomania.game.core.network;

import com.badlogic.gdx.graphics.Color;
import com.esotericsoftware.kryo.Kryo;
import com.mankomania.game.core.network.messages.*;
import com.mankomania.game.core.network.messages.clienttoserver.*;
import com.mankomania.game.core.network.messages.servertoclient.*;
import com.mankomania.game.core.network.messages.clienttoserver.baseturn.*;
import com.mankomania.game.core.network.messages.servertoclient.baseturn.*;
/*
 Created by Fabian Oraze on 06.05.20
 */

public class KryoHelper {

    public static void registerClasses(Kryo kryo) {

        kryo.register(ChatMessage.class);
        kryo.register(PlayerReady.class);
        kryo.register(InitPlayers.class);
        kryo.register(java.util.ArrayList.class);
        kryo.register(PlayerConnected.class);

        kryo.register(StartGame.class);
        kryo.register(PlayerCanRollDiceMessage.class);
        kryo.register(DiceResultMessage.class);
        kryo.register(MovePlayerToFieldMessage.class);
        kryo.register(MovePlayerOverLotteryMessage.class);
        kryo.register(MovePlayerToIntersectionMessage.class);
        kryo.register(IntersectionSelectedMessage.class);
        kryo.register(MovePlayerToFieldAfterIntersectionMessage.class);

        kryo.register(Notification.class);
        kryo.register(Color.class);
    }
}
