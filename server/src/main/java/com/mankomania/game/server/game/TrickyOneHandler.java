package com.mankomania.game.server.game;

import com.badlogic.gdx.graphics.Color;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;
import com.mankomania.game.core.data.GameData;
import com.mankomania.game.core.network.messages.clienttoserver.trickyone.RollDiceTrickyOne;
import com.mankomania.game.core.network.messages.clienttoserver.trickyone.StopRollingDice;
import com.mankomania.game.core.network.messages.servertoclient.Notification;
import com.mankomania.game.core.network.messages.servertoclient.trickyone.CanRollDiceTrickyOne;
import com.mankomania.game.core.network.messages.servertoclient.trickyone.EndTrickyOne;
import com.mankomania.game.core.network.messages.servertoclient.trickyone.StartTrickyOne;
import com.mankomania.game.server.data.GameState;
import com.mankomania.game.server.data.ServerData;


/*********************************
 Created by Fabian Oraze on 23.05.20
 *********************************/

/*
 Handler class for everything related to the miniGame TrickyOne
 */
public class TrickyOneHandler {

    //necessary reference objects
    private Server ref_server;
    private ServerData ref_serverData;

    private static final int WIN_AMOUNT_SINGLE = 100000; //win for single one
    private static final int WIN_AMOUNT_DOUBLE = 300000; //win for double ones
    private static final int BET = 5000; //integer that is the base for all further calculations
    private int pot; //pot that raises amount in it, related to the rollAmount
    private int rollAmount;


    /**
     * should be called in {@link ServerData} to initialize handler
     * which processes all messages related to trickyOne miniGame
     *
     * @param ref_server     reference Object to Kryo Server
     * @param ref_serverData reference Object to ServerData which holds all data related to {@link GameData}
     */
    public TrickyOneHandler(Server ref_server, ServerData ref_serverData) {
        this.ref_server = ref_server;
        this.ref_serverData = ref_serverData;
        this.pot = 0;
        this.rollAmount = 0;
    }

    public void startGame(int playerIndex) {
        //TODO: check for correct state
        ref_server.sendToAllTCP(new StartTrickyOne(playerIndex));
        ref_server.sendToAllTCP(new CanRollDiceTrickyOne(playerIndex, 0, 0, pot, rollAmount));
        ref_server.sendToAllTCP(new Notification("Player " + playerIndex + " startet Verflixte 1"));
        Log.info("MiniGame TrickyOne", "Player " + playerIndex + " started TrickyOne miniGame");
    }

    public void rollDice(RollDiceTrickyOne rollDiceTrickyOne, int connection) {
        if (connection != ref_serverData.getCurrentPlayerTurnConnectionId()) {
            Log.error("MiniGame TrickyOne", "Ignoring Player " + connection + " try to roll Dice");
            return;
        }
        int ones = 0;
        int[] rolledNum = getTwoRandNumbers();
        for (int i = 0; i < rolledNum.length; i++) {
            if (rolledNum[i] == 1) ones++;
        }

        if (ones == 0) {
            for (int i = 0; i < rolledNum.length; i++) {
                rollAmount += rolledNum[i];
            }
            calcPot();
            CanRollDiceTrickyOne message = new CanRollDiceTrickyOne(rollDiceTrickyOne.getPlayerIndex(), rolledNum[0], rolledNum[1], pot, rollAmount);
            ref_server.sendToAllTCP(message);
            Log.info("MiniGame TrickyOne", "Server rolled numbers: " + message.getFirstDice() + " " + message.getSecondDice() +
                    " Current Pot: " + message.getPot() + " Current amountRolled: " + message.getRolledAmount());
            ref_serverData.setCurrentState(GameState.WAIT_FOR_PLAYER_ROLL_OR_STOP);

        } else {
            clearInputs();
            int winAmount;
            if (ones == 1) winAmount = WIN_AMOUNT_SINGLE;
            else winAmount = WIN_AMOUNT_DOUBLE;
            winMoney(winAmount, rollDiceTrickyOne.getPlayerIndex());
            ref_server.sendToAllTCP(new EndTrickyOne(rollDiceTrickyOne.getPlayerIndex(), winAmount));
            Log.info("MiniGame TrickyOne", "Player loses game and wins Money. Ending MiniGame");
            clearInputs();

            ref_server.sendToAllExceptTCP(connection, new Notification(5, "Player " + rollDiceTrickyOne.getPlayerIndex() + " gewinnt + " + winAmount + " bei Verflixte 1"));
            ref_server.sendToTCP(connection, new Notification(5, "Ups!  Du gewinnst: " + winAmount, Color.RED, Color.WHITE));
            ref_serverData.setCurrentState(GameState.PLAYER_CAN_ROLL_DICE);
            ref_serverData.sendPlayerCanRollDice();
        }
    }

    public void stopRolling(StopRollingDice stopRollingDice, int connection) {
        if (connection != ref_serverData.getCurrentPlayerTurnConnectionId()) {
            Log.error("MiniGame TrickyOne", "Ignoring Player " + connection + " try stop MiniGame");
            return;
        }
        ref_serverData.getGameData().getPlayers().get(stopRollingDice.getPlayerIndex()).loseMoney(pot);
        Log.info("MiniGame TrickyOne", "Player wins game and loses " + pot + ". Ending MiniGame");

        ref_server.sendToTCP(connection, new Notification(5, "Gratuliere!  Du verlierst: " + pot, Color.GREEN, Color.GRAY));
        ref_server.sendToAllExceptTCP(connection, new Notification(5, "Player " + stopRollingDice.getPlayerIndex() + " verliert + " + pot + " bei Verflixte 1"));

        ref_serverData.setCurrentState(GameState.PLAYER_CAN_ROLL_DICE);
        ref_server.sendToAllTCP(new EndTrickyOne(stopRollingDice.getPlayerIndex(), -pot));
        ref_serverData.sendPlayerCanRollDice();
        clearInputs();

    }

    //used to calculate pot in relation to the Amount that has already been rolled
    private void calcPot() {
        pot = rollAmount * BET;
    }

    private void winMoney(int winAmount, int playerIndex) {
        ref_serverData.getGameData().getPlayers().get(playerIndex).addMoney(winAmount);
    }

    /**
     * @return returns an array with two random integers from 1 to 6
     */
    private int[] getTwoRandNumbers() {
        int[] num = new int[2];

        int max = 6;
        int min = 1;
        int range = max - min + 1;
        for (int i = 0; i < num.length; i++) {
            num[i] = (int) (Math.random() * range) + min;
        }
        return num;
    }

    //should be invoked when miniGame is about to end
    private void clearInputs() {
        this.rollAmount = 0;
        this.pot = 0;
    }


}
