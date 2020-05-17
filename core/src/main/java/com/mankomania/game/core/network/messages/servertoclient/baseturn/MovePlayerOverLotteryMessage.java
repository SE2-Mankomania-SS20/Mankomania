package com.mankomania.game.core.network.messages.servertoclient.baseturn;

/**
 * Instructs the clients to move a player to a given field id. The player passes by the lottery
 * while moving to the field, buying lottery tickets.
 *
 * Maybe extend from MovePlayerToFieldMessage?
 */
public class MovePlayerOverLotteryMessage {
    private int fieldId;
    private int playerId;


    public int getFieldId() {
        return fieldId;
    }

    public void setFieldId(int fieldId) {
        this.fieldId = fieldId;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }
}
