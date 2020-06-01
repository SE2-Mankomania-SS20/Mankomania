package com.mankomania.game.core.network;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.esotericsoftware.kryo.Kryo;
import com.mankomania.game.core.network.messages.*;
import com.mankomania.game.core.network.messages.clienttoserver.*;
import com.mankomania.game.core.network.messages.clienttoserver.minigames.RouletteStakeMessage;
import com.mankomania.game.core.network.messages.clienttoserver.minigames.StartRouletteClient;
import com.mankomania.game.core.network.messages.clienttoserver.stock.StockResultMessage;
import com.mankomania.game.core.network.messages.clienttoserver.trickyone.RollDiceTrickyOne;
import com.mankomania.game.core.network.messages.clienttoserver.trickyone.StopRollingDice;
import com.mankomania.game.core.network.messages.servertoclient.*;
import com.mankomania.game.core.network.messages.clienttoserver.baseturn.*;
import com.mankomania.game.core.network.messages.servertoclient.baseturn.*;
import com.mankomania.game.core.network.messages.servertoclient.minigames.EndRouletteResultMessage;
import com.mankomania.game.core.network.messages.servertoclient.minigames.RouletteResultAllPlayer;
import com.mankomania.game.core.network.messages.servertoclient.minigames.RouletteResultMessage;
import com.mankomania.game.core.network.messages.servertoclient.minigames.StartRouletteServer;
import com.mankomania.game.core.network.messages.servertoclient.minigames.EndStockMessage;
import com.mankomania.game.core.network.messages.servertoclient.trickyone.CanRollDiceTrickyOne;
import com.mankomania.game.core.network.messages.servertoclient.trickyone.EndTrickyOne;
import com.mankomania.game.core.network.messages.servertoclient.trickyone.StartTrickyOne;
import com.mankomania.game.core.network.messages.servertoclient.stock.EndStockMessage;
import com.mankomania.game.core.player.Player;
import com.mankomania.game.core.player.Stock;

import java.util.ArrayList;
import java.util.HashMap;
/*
 Created by Fabian Oraze on 06.05.20
 */

public class KryoHelper {

    public static void registerClasses(Kryo kryo) {

        kryo.register(ChatMessage.class);
        kryo.register(PlayerReady.class);
        kryo.register(java.util.ArrayList.class);
        kryo.register(java.util.HashMap.class);
        kryo.register(ArrayList.class);
        kryo.register(PlayerConnected.class);

        kryo.register(StartGame.class);
        kryo.register(PlayerCanRollDiceMessage.class);
        kryo.register(DiceResultMessage.class);
        kryo.register(MovePlayerToFieldMessage.class);
        kryo.register(MovePlayerOverLotteryMessage.class);
        kryo.register(MovePlayerToIntersectionMessage.class);
        kryo.register(IntersectionSelectedMessage.class);
        kryo.register(MovePlayerToFieldAfterIntersectionMessage.class);
        kryo.register(StockResultMessage.class);
        kryo.register(EndStockMessage.class);


        kryo.register(Notification.class);
        kryo.register(Color.class);

        kryo.register(Player.class);
        kryo.register(HashMap.class);
        kryo.register(Vector3.class);
        kryo.register(Stock.class);

        kryo.register(StartRouletteClient.class);
        kryo.register(RouletteStakeMessage.class);
        kryo.register(RouletteResultMessage.class);
        kryo.register(StartRouletteServer.class);
        kryo.register(RouletteResultAllPlayer.class);
        kryo.register(EndRouletteResultMessage.class);

        kryo.register(RollDiceTrickyOne.class);
        kryo.register(StopRollingDice.class);
        kryo.register(CanRollDiceTrickyOne.class);
        kryo.register(EndTrickyOne.class);
        kryo.register(StartTrickyOne.class);
    }
}