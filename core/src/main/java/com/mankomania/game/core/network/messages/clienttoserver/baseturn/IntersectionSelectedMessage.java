package com.mankomania.game.core.network.messages.clienttoserver.baseturn;

/**
 * Message to signalise the server what path the player selected.
 */
public class IntersectionSelectedMessage {
    private int playerId;
    private int fieldChosen;

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public int getFieldChosen() {
        return fieldChosen;
    }

    public void setFieldChosen(int fieldChosen) {
        this.fieldChosen = fieldChosen;
    }
}
