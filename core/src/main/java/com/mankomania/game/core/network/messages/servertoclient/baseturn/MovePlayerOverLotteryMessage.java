package com.mankomania.game.core.network.messages.servertoclient.baseturn;

/**
 * Instructs the clients to move a player to a given field id. The player passes by the lottery
 * while moving to the field, buying lottery tickets.
 *
 * Maybe extend from MovePlayerToFieldMessage?
 */
public class MovePlayerOverLotteryMessage {
    private int fieldIndex;
    private int playerIndex;


    public int getFieldIndex() {
        return fieldIndex;
    }

    public void setFieldIndex(int fieldIndex) {
        this.fieldIndex = fieldIndex;
    }

    public int getPlayerIndex() {
        return playerIndex;
    }

    public void setPlayerIndex(int playerIndex) {
        this.playerIndex = playerIndex;
    }
}
