package com.mankomania.game.core.network;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntArray;
import com.esotericsoftware.kryo.Kryo;
import com.mankomania.game.core.data.horserace.HorseRacePlayerInfo;
import com.mankomania.game.core.network.messages.*;
import com.mankomania.game.core.network.messages.clienttoserver.*;
import com.mankomania.game.core.network.messages.clienttoserver.horserace.HorseRaceSelection;
import com.mankomania.game.core.network.messages.clienttoserver.roulette.RouletteStakeMessage;
import com.mankomania.game.core.network.messages.clienttoserver.roulette.StartRouletteClient;
import com.mankomania.game.core.network.messages.clienttoserver.cheat.CheatedMessage;
import com.mankomania.game.core.network.messages.clienttoserver.slots.SlotsFinishedMsg;
import com.mankomania.game.core.network.messages.clienttoserver.slots.SpinRollsMessage;
import com.mankomania.game.core.network.messages.clienttoserver.stock.StockResultMessage;
import com.mankomania.game.core.network.messages.clienttoserver.trickyone.RollDiceTrickyOne;
import com.mankomania.game.core.network.messages.clienttoserver.trickyone.StopRollingDice;
import com.mankomania.game.core.network.messages.servertoclient.*;
import com.mankomania.game.core.network.messages.clienttoserver.baseturn.*;
import com.mankomania.game.core.network.messages.servertoclient.baseturn.*;
import com.mankomania.game.core.network.messages.servertoclient.stock.StartStockMessage;
import com.mankomania.game.core.player.Hotel;
import com.mankomania.game.core.network.messages.servertoclient.slots.*;
import com.mankomania.game.core.network.messages.servertoclient.roulette.RouletteResultAllPlayer;
import com.mankomania.game.core.network.messages.servertoclient.roulette.RouletteResultMessage;
import com.mankomania.game.core.network.messages.servertoclient.roulette.StartRouletteServer;
import com.mankomania.game.core.network.messages.servertoclient.stock.EndStockMessage;
import com.mankomania.game.core.network.messages.servertoclient.trickyone.CanRollDiceTrickyOne;
import com.mankomania.game.core.network.messages.servertoclient.trickyone.EndTrickyOne;
import com.mankomania.game.core.network.messages.servertoclient.trickyone.StartTrickyOne;
import com.mankomania.game.core.network.messages.clienttoserver.hotel.PlayerBuyHotelDecision;
import com.mankomania.game.core.network.messages.servertoclient.horserace.HorseRaceStart;
import com.mankomania.game.core.network.messages.servertoclient.horserace.HorseRaceUpdate;
import com.mankomania.game.core.network.messages.servertoclient.horserace.HorseRaceWinner;
import com.mankomania.game.core.network.messages.servertoclient.hotel.PlayerBoughtHotelMessage;
import com.mankomania.game.core.network.messages.servertoclient.hotel.PlayerCanBuyHotelMessage;
import com.mankomania.game.core.network.messages.servertoclient.hotel.PlayerPaysHotelRentMessage;
import com.mankomania.game.core.player.Player;
import com.mankomania.game.core.player.Stock;

import java.util.ArrayList;
import java.util.HashMap;
/*
 Created by Fabian Oraze on 06.05.20
 */

public class KryoHelper {

    private KryoHelper() {
    }

    public static void registerClasses(Kryo kryo) {

        kryo.register(int[].class);
        kryo.register(Object[].class);
        kryo.register(Array.class);
        kryo.register(Array.ArrayIterable.class);
        kryo.register(IntArray.class);
        kryo.register(ArrayList.class);
        kryo.register(Hotel.class);

        kryo.register(PlayerConnected.class);
        kryo.register(PlayerReady.class);
        kryo.register(StartGame.class);
        kryo.register(ChatMessage.class);

        kryo.register(GameUpdate.class);
        kryo.register(PlayerCanRollDiceMessage.class);
        kryo.register(DiceResultMessage.class);
        kryo.register(PlayerMoves.class);
        kryo.register(IntersectionSelection.class);
        kryo.register(TurnFinished.class);

        // stock market minigame
        kryo.register(StartStockMessage.class);
        kryo.register(StockResultMessage.class);
        kryo.register(EndStockMessage.class);
        kryo.register(PlayerWon.class);


        kryo.register(SpecialNotification.class);
        kryo.register(Notification.class);
        kryo.register(Color.class);
        kryo.register(Player.class);
        kryo.register(HashMap.class);
        kryo.register(Vector3.class);
        kryo.register(Stock.class);

        // tricky one
        kryo.register(RollDiceTrickyOne.class);
        kryo.register(StopRollingDice.class);
        kryo.register(CanRollDiceTrickyOne.class);
        kryo.register(EndTrickyOne.class);
        kryo.register(StartTrickyOne.class);

        // hotels
        kryo.register(PlayerCanBuyHotelMessage.class);
        kryo.register(PlayerBuyHotelDecision.class);
        kryo.register(PlayerBoughtHotelMessage.class);
        kryo.register(PlayerPaysHotelRentMessage.class);

        // roulette minigame
        kryo.register(StartRouletteClient.class);
        kryo.register(RouletteStakeMessage.class);
        kryo.register(RouletteResultMessage.class);
        kryo.register(StartRouletteServer.class);
        kryo.register(RouletteResultAllPlayer.class);

        // slots minigame
        kryo.register(SpinRollsMessage.class);
        kryo.register(SlotResultMessage.class);
        kryo.register(StartSlotsMessage.class);
        kryo.register(SlotsFinishedMsg.class);

        // cheating
        kryo.register(CheatedMessage.class);

        // HorseRace
        kryo.register(HorseRaceStart.class);
        kryo.register(HorseRaceUpdate.class);
        kryo.register(HorseRaceWinner.class);
        kryo.register(HorseRacePlayerInfo.class);
        kryo.register(HorseRaceSelection.class);
    }
}
