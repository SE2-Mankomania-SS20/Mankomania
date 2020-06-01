package com.mankomania.game.core.network;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.IntArray;
import com.esotericsoftware.kryo.Kryo;
import com.mankomania.game.core.network.messages.*;
import com.mankomania.game.core.network.messages.clienttoserver.*;
import com.mankomania.game.core.network.messages.clienttoserver.stock.StockResultMessage;
import com.mankomania.game.core.network.messages.clienttoserver.trickyone.RollDiceTrickyOne;
import com.mankomania.game.core.network.messages.clienttoserver.trickyone.StopRollingDice;
import com.mankomania.game.core.network.messages.servertoclient.*;
import com.mankomania.game.core.network.messages.clienttoserver.baseturn.*;
import com.mankomania.game.core.network.messages.servertoclient.baseturn.*;
import com.mankomania.game.core.player.Hotel;
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

        kryo.register(int[].class);
        kryo.register(IntArray.class);
        kryo.register(ArrayList.class);
        kryo.register(HashMap.class);
        kryo.register(Color.class);
        kryo.register(Vector3.class);
        kryo.register(Stock.class);
        kryo.register(Hotel.class);

        kryo.register(Notification.class);
        kryo.register(Player.class);

        kryo.register(PlayerConnected.class);
        kryo.register(PlayerReady.class);
        kryo.register(StartGame.class);
        kryo.register(ChatMessage.class);

        kryo.register(GameUpdate.class);
        kryo.register(PlayerCanRollDiceMessage.class);
        kryo.register(DiceResultMessage.class);
        kryo.register(PlayerMoves.class);
        kryo.register(IntersectionSelectedMessage.class);
        kryo.register(TurnFinished.class);
        kryo.register(StockResultMessage.class);
        kryo.register(EndStockMessage.class);
        kryo.register(RollDiceTrickyOne.class);
        kryo.register(StopRollingDice.class);
        kryo.register(CanRollDiceTrickyOne.class);
        kryo.register(EndTrickyOne.class);
        kryo.register(StartTrickyOne.class);
    }
}
