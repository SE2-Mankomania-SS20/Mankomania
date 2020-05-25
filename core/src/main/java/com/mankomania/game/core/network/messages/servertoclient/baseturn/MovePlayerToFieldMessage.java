package com.mankomania.game.core.network.messages.servertoclient.baseturn;

/**
 * Just moves the given player to the given field.
 */
public class MovePlayerToFieldMessage {
    private int playerIndex;
    private int fieldIndex;

    public int getPlayerIndex() {
        return playerIndex;
    }

    public void setPlayerIndex(int playerIndex) {
        this.playerIndex = playerIndex;
    }

    public int getFieldIndex() {
        return fieldIndex;
    }

    public void setFieldIndex(int fieldIndex) {
        this.fieldIndex = fieldIndex;
    }

    public MovePlayerToFieldMessage() {
        // empty for kryonet
    }

    public MovePlayerToFieldMessage(int playerIndex, int fieldToMoveTo) {
        this.playerIndex = playerIndex;
        this.fieldIndex = fieldToMoveTo;
    }
}
