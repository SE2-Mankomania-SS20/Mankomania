package com.mankomania.game.core.network.messages.servertoclient.baseturn;

/**
 * This message helps to move the player in the right direction after choosing a intersection path.
 * It only moves the player to the next field after the intersection.
 * This helps the client moving the player, the client must do no path finding to chose the right way to go therefore.
 */
public class MovePlayerToFieldAfterIntersectionMessage {
    private int playerId;
    private int fieldToMoveTo;

    public MovePlayerToFieldAfterIntersectionMessage() {
        // empty ctor needed for kryonet deserialization
    }

    public MovePlayerToFieldAfterIntersectionMessage(int playerId, int fieldToMoveTo) {
        this.playerId = playerId;
        this.fieldToMoveTo = fieldToMoveTo;
    }

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
