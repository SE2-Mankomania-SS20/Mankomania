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
    private Server refServer;
    private ServerData refServerData;

    private static final int WIN_AMOUNT_SINGLE = 100000; //win for single one
    private static final int WIN_AMOUNT_DOUBLE = 300000; //win for double ones
    private static final int BET = 5000; //integer that is the base for all further calculations
    private int pot; //pot that raises amount in it, related to the rollAmount
    private int rollAmount;


    /**
     * should be called in {@link ServerData} to initialize handler
     * which processes all messages related to trickyOne miniGame
     *
     * @param refServer     reference Object to Kryo Server
     * @param refServerData reference Object to ServerData which holds all data related to {@link GameData}
     */
    public TrickyOneHandler(Server refServer, ServerData refServerData) {
        this.refServer = refServer;
        this.refServerData = refServerData;
        this.pot = 0;
        this.rollAmount = 0;
    }

    public void startGame(int playerIndex) {
        //TODO: check for correct state
        refServer.sendToAllTCP(new StartTrickyOne(playerIndex));
        refServer.sendToAllTCP(new CanRollDiceTrickyOne(playerIndex, 0, 0, pot, rollAmount));
        refServer.sendToAllTCP(new Notification("Player " + (playerIndex + 1) + " startet Verflixte 1"));
        Log.info("MiniGame TrickyOne", "Player " + (playerIndex + 1) + " started TrickyOne miniGame");
        refServerData.setCurrentState(GameState.WAIT_FOR_PLAYER_ROLL_OR_STOP);
    }

    public void rollDice(RollDiceTrickyOne rollDiceTrickyOne, int connection) {
        if (connection != refServerData.getCurrentPlayerTurnConnectionId() && refServerData.getCurrentState() != GameState.WAIT_FOR_PLAYER_ROLL_OR_STOP) {
            Log.error("MiniGame TrickyOne", "Ignoring Player " + connection + " try to roll Dice");
            return;
        }
        int ones = 0;
        int[] rolledNum = getTwoRandNumbers();
        for (int value : rolledNum) {
            if (value == 1) ones++;
        }

        if (ones == 0) {
            for (int value : rolledNum) {
                rollAmount += value;
            }
            calcPot();
            CanRollDiceTrickyOne message = new CanRollDiceTrickyOne(rollDiceTrickyOne.getPlayerIndex(), rolledNum[0], rolledNum[1], pot, rollAmount);
            refServer.sendToAllTCP(message);
            Log.info("MiniGame TrickyOne", "Server rolled numbers: " + message.getFirstDice() + " " + message.getSecondDice() +
                    " Current Pot: " + message.getPot() + " Current amountRolled: " + message.getRolledAmount());
            refServerData.setCurrentState(GameState.WAIT_FOR_PLAYER_ROLL_OR_STOP);

        } else {
            clearInputs();
            int winAmount;
            if (ones == 1) winAmount = WIN_AMOUNT_SINGLE;
            else winAmount = WIN_AMOUNT_DOUBLE;
            winMoney(winAmount, rollDiceTrickyOne.getPlayerIndex());
            refServer.sendToAllTCP(new EndTrickyOne(rollDiceTrickyOne.getPlayerIndex(), winAmount));
            Log.info("MiniGame TrickyOne", "Player loses game and wins Money. Ending MiniGame");
            clearInputs();

            refServer.sendToAllExceptTCP(connection, new Notification(5, "Player " + (rollDiceTrickyOne.getPlayerIndex() + 1) + " gewinnt + " + winAmount));
            refServer.sendToTCP(connection, new Notification(5, "Ups!  Du gewinnst: " + winAmount, Color.RED, Color.WHITE));
            refServerData.setCurrentState(GameState.PLAYER_CAN_ROLL_DICE);
            refServerData.sendPlayerCanRollDice();
        }
    }

    public void stopRolling(StopRollingDice stopRollingDice, int connection) {
        if (connection != refServerData.getCurrentPlayerTurnConnectionId()) {
            Log.error("MiniGame TrickyOne", "Ignoring Player " + connection + " try stop MiniGame");
            return;
        }
        refServerData.getGameData().getPlayers().get(stopRollingDice.getPlayerIndex()).loseMoney(pot);
        Log.info("MiniGame TrickyOne", "Player wins game and loses " + pot + ". Ending MiniGame");

        refServer.sendToTCP(connection, new Notification(5, "Gratuliere!  Du verlierst: " + pot, Color.GREEN, Color.GRAY));
        refServer.sendToAllExceptTCP(connection, new Notification(5, "Player " + (stopRollingDice.getPlayerIndex() + 1) + " verliert + " + pot));

        refServerData.setCurrentState(GameState.PLAYER_CAN_ROLL_DICE);
        refServer.sendToAllTCP(new EndTrickyOne(stopRollingDice.getPlayerIndex(), -pot));
        refServerData.sendPlayerCanRollDice();
        clearInputs();

    }

    //used to calculate pot in relation to the Amount that has already been rolled
    private void calcPot() {
        pot = rollAmount * BET;
    }

    private void winMoney(int winAmount, int playerIndex) {
        refServerData.getGameData().getPlayers().get(playerIndex).addMoney(winAmount);
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
