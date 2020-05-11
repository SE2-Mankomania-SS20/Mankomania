package com.mankomania.game.core.network.messages.servertoclient.baseturn;

/**
 * Just moves the given player to the given field.
 */
public class MovePlayerToFieldMessage {
    private int playerId;
    private int fieldToMoveTo;

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public int getFieldToMoveTo() {
        return fieldToMoveTo;
    }

    public void setFieldToMoveTo(int fieldToMoveTo) {
        this.fieldToMoveTo = fieldToMoveTo;
    }
}
