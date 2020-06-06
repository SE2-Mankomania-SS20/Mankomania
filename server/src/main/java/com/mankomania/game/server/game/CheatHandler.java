package com.mankomania.game.server.game;

/*
 Created by Fabian Oraze on 02.06.20
 */

import com.badlogic.gdx.graphics.Color;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;
import com.mankomania.game.core.data.GameData;
import com.mankomania.game.core.network.messages.servertoclient.Notification;
import com.mankomania.game.server.data.GameState;
import com.mankomania.game.server.data.ServerData;

/**
 * Handler class to process cheating actions
 */
public class CheatHandler {
    private static final int PENALTY = 100000;
    private static final int MAX_CHEAT_AMOUNT = 3;

    private ServerData refServerData;
    private Server refServer;
    private boolean someOneCheated = false;
    private int playerIndexOfCheater = -1;
    private int indexOfPlayerWithLeastMoney = -1;

    /**
     * should be called in {@link ServerData} to initialize handler
     *
     * @param refServer     reference Object to Kryo Server
     * @param refServerData reference Object to ServerData which holds all data related to {@link GameData}
     */
    public CheatHandler(Server refServer, ServerData refServerData) {
        this.refServerData = refServerData;
        this.refServer = refServer;
    }

    /**
     * is invoked when a player presses cheat button
     *
     * @param playerIndex index of the player that pressed the button
     */
    public void gotCheatedMsg(int playerIndex) {
        //check if msg is by the player that is on turn
        if (playerIndex == refServerData.getGameData().getCurrentPlayerTurnIndex()) {
            playerTriesToCheat(playerIndex);
        } else {
            playerAssumedCheat(playerIndex);
        }
    }

    /**
     * if player that pressed cheat button is also at turn, this method will be called
     *
     * @param playerIndex index of the player that pressed the button
     */
    public void playerTriesToCheat(int playerIndex) {
        //check if gameData in correct state
        if (refServerData.getCurrentState() == GameState.WAIT_FOR_DICE_RESULT) {
            //check if player has already cheated once
            if (refServerData.getGameData().getCurrentPlayer().getCheatAmount() < MAX_CHEAT_AMOUNT) {
                playerCheat();
            } else {
                refServer.sendToTCP(refServerData.getCurrentPlayerTurnConnectionId(),
                        new Notification(4f, "You have already cheated " + MAX_CHEAT_AMOUNT + " times!"));
                Log.info("Player " + playerIndex + " has already cheated " + MAX_CHEAT_AMOUNT + " times!");
            }
        } else {
            Log.info("Player " + playerIndex + " tries to cheat, but wrong state on server");
        }
    }


    /**
     * if player that pressed cheat button is not at turn, this method will be called
     *
     * @param playerIndex index of the player that pressed the button
     */
    public void playerAssumedCheat(int playerIndex) {
        //first check for correct states
        if (refServerData.getCurrentState() == GameState.WAIT_FOR_DICE_RESULT
                || refServerData.getCurrentState() == GameState.WAIT_INTERSECTION_SELECTION
                || refServerData.getCurrentState() == GameState.WAIT_FOR_TURN_FINISHED
                || refServerData.getCurrentState() == GameState.WAIT_HOTELBUY_DECISION) {

            //check also if msg is from player that has been cheated on
            if (playerIndex == indexOfPlayerWithLeastMoney && someOneCheated) {
                correctAssumption();
                Log.info("Player " + playerIndex + " been cheated on, assumption justified!");
            } else {
                wrongAssumedCheat(playerIndex);
                Log.info("Player " + playerIndex + " has not been cheated on! Penalty for assumption 100.000");
            }
        } else {
            Log.info("Player " + playerIndex + " tries to AssumeCheating, but wrong state on server");
        }
    }

    /**
     * if state is correct and player hasn't already cheated
     */
    public void playerCheat() {
        int moneyOfCheater = refServerData.getGameData().getCurrentPlayer().getMoney();
        //attempt will be ignored if player has the smallest amount of money
        if (isSmallestAmount(moneyOfCheater)) {
            refServer.sendToTCP(refServerData.getCurrentPlayerTurnConnectionId(),
                    new Notification(4f, "You already have the smallest amount of money!"));
            return;
        }

        //get index of player to switch money afterwards
        locatePlayerWithLeastMoney();

        //increase cheat amount in player
        refServerData.getGameData().getCurrentPlayer().addCheatAmount();
        //switch money of cheater and the other player
        int moneyNewOfCheater = refServerData.getGameData().getPlayers().get(indexOfPlayerWithLeastMoney).getMoney();
        refServerData.getGameData().getCurrentPlayer().setMoney(moneyNewOfCheater);
        refServerData.getGameData().getPlayers().get(indexOfPlayerWithLeastMoney).setMoney(moneyOfCheater);

        //set some variables
        someOneCheated = true;
        playerIndexOfCheater = refServerData.getGameData().getCurrentPlayerTurnIndex();

        //send notification to cheater
        refServer.sendToTCP(refServerData.getCurrentPlayerTurnConnectionId(),
                new Notification(5f, "You switched money with player " + (indexOfPlayerWithLeastMoney + 1), Color.GREEN, Color.WHITE));
        refServerData.sendGameData();
    }

    /**
     * get index of player with the least amount of money
     */
    public void locatePlayerWithLeastMoney() {
        indexOfPlayerWithLeastMoney = 0;
        int tempMoney = refServerData.getGameData().getPlayers().get(0).getMoney();
        for (int i = 1; i < refServerData.getGameData().getPlayers().size(); i++) {
            if (tempMoney > refServerData.getGameData().getPlayers().get(i).getMoney()) {
                tempMoney = refServerData.getGameData().getPlayers().get(i).getMoney();
                indexOfPlayerWithLeastMoney = i;
            }
        }
    }

    /**
     * if player that cheats has already the smallest amount of money his attempt will be ignored
     *
     * @param moneyOfCheater money amount of player that has cheated
     * @return true if he has the smallest amount of money
     */
    public boolean isSmallestAmount(int moneyOfCheater) {
        boolean isSmallestAmount = true;
        for (int i = 0; i < refServerData.getGameData().getPlayers().size(); i++) {
            if (moneyOfCheater > refServerData.getGameData().getPlayers().get(i).getMoney()) {
                isSmallestAmount = false;
                break;
            }
        }
        return isSmallestAmount;
    }

    /**
     * if player has not been cheated on but still assumes it, he receives a penalty
     *
     * @param playerIndex index of player that assumes cheating
     */
    public void wrongAssumedCheat(int playerIndex) {
        refServer.sendToTCP(refServerData.getGameData().getPlayers().get(playerIndex).getConnectionId(),
                new Notification(3f, "Penalty for wrong assumption: 100.000", Color.RED, Color.WHITE));
        refServerData.getGameData().getPlayers().get(playerIndex).addMoney(PENALTY);
        refServerData.sendGameData();
    }

    /**
     * if player has been cheated on and the assumption is justified the cheater receives a penalty
     * and the money amount will be switched back
     */
    public void correctAssumption() {
        //switch money back
        int moneyTemp = refServerData.getGameData().getPlayers().get(indexOfPlayerWithLeastMoney).getMoney();
        int moneyCheat = refServerData.getGameData().getPlayers().get(playerIndexOfCheater).getMoney();
        refServerData.getGameData().getPlayers().get(playerIndexOfCheater).setMoney(moneyTemp);
        refServerData.getGameData().getPlayers().get(indexOfPlayerWithLeastMoney).setMoney(moneyCheat);

        //add penalty to cheater
        refServerData.getGameData().getPlayers().get(playerIndexOfCheater).addMoney(PENALTY);

        //display notifications
        refServer.sendToTCP(refServerData.getGameData().getPlayers().get(playerIndexOfCheater).getConnectionId(),
                new Notification(4f, "You were caught cheating! Penalty: 100.000", Color.RED, Color.WHITE));
        refServer.sendToAllExceptTCP(refServerData.getGameData().getPlayers().get(playerIndexOfCheater).getConnectionId(),
                new Notification(4f, "Player " + (indexOfPlayerWithLeastMoney + 1) + " has caught Player" + (playerIndexOfCheater + 1) + " cheating", Color.GREEN, Color.WHITE));
        //send gameData update to clients
        refServerData.sendGameData();
        clearHistory();
    }

    /**
     * should be called in {@link ServerData} after move is finished/or at the very start of next turn
     * clears old cheat history
     */
    public void clearHistory() {
        playerIndexOfCheater = -1;
        indexOfPlayerWithLeastMoney = -1;
        someOneCheated = false;
    }

    //for testing purposes
    public boolean isSomeOneCheated() {
        return someOneCheated;
    }

    //for testing purposes
    public int getPlayerIndexOfCheater() {
        return playerIndexOfCheater;
    }

    //for testing purposes
    public int getIndexOfPlayerWithLeastMoney() {
        return indexOfPlayerWithLeastMoney;
    }

    //for testing purposes
    public void setSomeOneCheated(boolean someOneCheated) {
        this.someOneCheated = someOneCheated;
    }
}
