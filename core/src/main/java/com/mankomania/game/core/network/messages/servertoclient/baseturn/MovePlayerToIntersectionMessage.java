package com.mankomania.game.core.network.messages.servertoclient.baseturn;

/**
 * Moves the player immediately before the intersection, signaling the client that a decision must be made
 * and transmitted to the server.
 */
public class MovePlayerToIntersectionMessage {
    private int playerIndex;
    private int fieldIndex; // the field before the intersection
    private int selectionOption1; // field id of one of the next field, maybe not needed, but for now added
    private int selectionOption2; // field id of the other next field

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

    public int getSelectionOption1() {
        return selectionOption1;
    }

    public void setSelectionOption1(int selectionOption1) {
        this.selectionOption1 = selectionOption1;
    }

    public int getSelectionOption2() {
        return selectionOption2;
    }

    public void setSelectionOption2(int selectionOption2) {
        this.selectionOption2 = selectionOption2;
    }
}
