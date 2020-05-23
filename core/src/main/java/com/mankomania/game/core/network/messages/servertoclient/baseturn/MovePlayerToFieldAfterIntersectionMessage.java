package com.mankomania.game.core.network.messages.servertoclient.baseturn;

/**
 * This message helps to move the player in the right direction after choosing a intersection path.
 * It only moves the player to the next field after the intersection.
 * This helps the client moving the player, the client must do no path finding to chose the right way to go therefore.
 */
public class MovePlayerToFieldAfterIntersectionMessage {
    private int playerIndex;
    private int dieldIndex;

    public MovePlayerToFieldAfterIntersectionMessage() {
        // empty ctor needed for kryonet deserialization
    }

    public MovePlayerToFieldAfterIntersectionMessage(int playerIndex, int fieldToMoveTo) {
        this.playerIndex = playerIndex;
        this.dieldIndex = fieldToMoveTo;
    }

    public int getPlayerIndex() {
        return playerIndex;
    }

    public void setPlayerIndex(int playerIndex) {
        this.playerIndex = playerIndex;
    }

    public int getDieldIndex() {
        return dieldIndex;
    }

    public void setDieldIndex(int dieldIndex) {
        this.dieldIndex = dieldIndex;
    }
}
