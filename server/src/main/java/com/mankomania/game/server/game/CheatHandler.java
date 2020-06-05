package com.mankomania.game.server.game;

/*
 Created by Fabian Oraze on 02.06.20
 */

import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;
import com.mankomania.game.core.data.GameData;
import com.mankomania.game.server.data.GameState;
import com.mankomania.game.server.data.ServerData;

/**
 * Handler class to process cheating actions
 */
public class CheatHandler {

    private ServerData refServerData;
    private Server refServer;

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
            if (!refServerData.getGameData().getCurrentPlayer().getHasCheated()) {
                playerCheat(playerIndex);
            } else {
                Log.info("Player " + playerIndex + " has already cheated!");
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

    }

    /**
     * if state is correct and player hasn't already cheated
     *
     * @param playerIndex index of the player that pressed the button
     */
    public void playerCheat(int playerIndex) {
        int moneyOfCheater = refServerData.getGameData().getCurrentPlayer().getMoney();
        //attempt will be ignored if player has the smallest amount of money
        if (!isSmallestAmount(moneyOfCheater)) return;

        //set cheated boolean in player to true, which means he has now cheated once
        refServerData.getGameData().getCurrentPlayer().setHasCheated(true);

    }

    /**
     * if player that cheats has already the smallest amount of money his attempt will be ignored
     * @param moneyOfCheater money amount of player that has cheated
     * @return true if he has the smallest amount of money
     */
    public boolean isSmallestAmount(int moneyOfCheater) {
        boolean smallestAmount = true;
        for (int i = 0; i < refServerData.getGameData().getPlayers().size(); i++) {
            if (moneyOfCheater > refServerData.getGameData().getPlayers().get(i).getMoney()) {
                smallestAmount = false;
                break;
            }
        }
        return smallestAmount;
    }
}
