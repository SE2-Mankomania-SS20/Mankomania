package com.mankomania.game.server.game;

import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;
import com.mankomania.game.core.data.GameData;
import com.mankomania.game.core.network.messages.servertoclient.trickyone.CanRollDiceTrickyOne;
import com.mankomania.game.core.network.messages.servertoclient.trickyone.EndTrickyOne;
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
        ref_server.sendToAllTCP(new CanRollDiceTrickyOne(playerIndex, 0, 0, pot, rollAmount));
    }

    public void rollDice() {
        int ones = 0;
        int[] rolledNum = getTwoRandNumbers();
        for (int i = 0; i < rolledNum.length; i++) {
            if (rolledNum[i] == 1) ones++;
        }

        int playerIndex = ref_serverData.getGameData().getPlayerByConnectionId(ref_serverData.getCurrentPlayerTurnConnectionId()).getPlayerIndex();
        if (ones == 0) {
            for (int i = 0; i < rolledNum.length; i++) {
                rollAmount += rolledNum[i];
            }
            calcPot();
            CanRollDiceTrickyOne message = new CanRollDiceTrickyOne(playerIndex, rolledNum[0], rolledNum[1], pot, rollAmount);
            ref_server.sendToAllTCP(message);
            Log.info("MiniGame TrickyOne", "Server rolled numbers: " + message.getFirstDice() + " " + message.getSecondDice() +
                    " Current Pot: " + message.getPot() + " Current amountRolled: " + message.getRolledAmount());
            ref_serverData.setCurrentState(GameState.WAIT_FOR_PLAYER_ROLL_OR_STOP);

        } else {
            clearInputs();
            if (ones == 1) winMoney(WIN_AMOUNT_SINGLE);
            else if (ones == 2) winMoney(WIN_AMOUNT_DOUBLE);
            ref_server.sendToAllTCP(new EndTrickyOne(playerIndex));
            Log.info("MiniGame TrickyOne", "Player loses game and wins Money. Ending MiniGame");
            clearInputs();
            ref_serverData.setCurrentState(GameState.TRICKY_ONE_END);
        }
    }

    public void stopRolling() {
        ref_serverData.getGameData().getPlayerByConnectionId(ref_serverData.getCurrentPlayerTurnConnectionId()).loseMoney(pot);
        Log.info("MiniGame TrickyOne", "Player wins game and loses " + pot + ". Ending MiniGame");
        clearInputs();
        ref_serverData.setCurrentState(GameState.TRICKY_ONE_END);
    }

    //used to calculate pot in relation to the Amount that has already been rolled
    private void calcPot() {
        pot = rollAmount * BET;
    }

    private void winMoney(int winAmount) {
        ref_serverData.getGameData().getPlayerByConnectionId(ref_serverData.getCurrentPlayerTurnConnectionId()).addMoney(winAmount);
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
