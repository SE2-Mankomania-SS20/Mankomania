package com.mankomania.game.core.network.messages.clienttoserver.baseturn;

/**
 * Message to signalise the server what path the player selected.
 */
public class IntersectionSelection {
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
}
