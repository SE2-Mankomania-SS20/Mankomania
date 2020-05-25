package com.mankomania.game.core.network.messages.servertoclient.baseturn;

/**
 * Just moves the given player to the given field.
 */
public class MovePlayerToFieldMessage {
    private int playerIndex;
    private int fieldToMoveTo;

    public int getPlayerIndex() {
        return playerIndex;
    }

    public void setPlayerIndex(int playerIndex) {
        this.playerIndex = playerIndex;
    }

    public int getFieldToMoveTo() {
        return fieldToMoveTo;
    }

    public void setFieldToMoveTo(int fieldToMoveTo) {
        this.fieldToMoveTo = fieldToMoveTo;
    }

    public MovePlayerToFieldMessage() {
        // empty for kryonet
    }

    public MovePlayerToFieldMessage(int playerIndex, int fieldToMoveTo) {
        this.playerIndex = playerIndex;
        this.fieldToMoveTo = fieldToMoveTo;
    }
}
