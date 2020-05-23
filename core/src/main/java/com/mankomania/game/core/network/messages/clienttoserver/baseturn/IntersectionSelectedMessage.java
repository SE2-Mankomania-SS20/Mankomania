package com.mankomania.game.core.network.messages.clienttoserver.baseturn;

/**
 * Message to signalise the server what path the player selected.
 */
public class IntersectionSelectedMessage {
    private int playerIndex;
    private int fieldChosen;

    public int getPlayerIndex() {
        return playerIndex;
    }

    public void setPlayerIndex(int playerIndex) {
        this.playerIndex = playerIndex;
    }

    public int getFieldChosen() {
        return fieldChosen;
    }

    public void setFieldChosen(int fieldChosen) {
        this.fieldChosen = fieldChosen;
    }
}
