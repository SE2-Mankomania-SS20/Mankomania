package com.mankomania.game.core.network.messages.servertoclient.baseturn;

/**
 * Just moves the given player to the given field.
 */
public class MovePlayerToFieldMessage {
    private int playerId;
    private int fieldToMoveTo;
    private int fieldMoveAmount;

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

    public int getFieldMoveAmount() {
        return fieldMoveAmount;
    }

    public void setFieldMoveAmount(int fieldMoveAmount) {
        this.fieldMoveAmount = fieldMoveAmount;
    }

    public static MovePlayerToFieldMessage createMovePlayerToFieldMessage(int playerId, int fieldToMoveTo, int fieldMoveAmount) {
        MovePlayerToFieldMessage message = new MovePlayerToFieldMessage();
        message.setPlayerId(playerId);
        message.setFieldToMoveTo(fieldToMoveTo);
        message.setFieldMoveAmount(fieldMoveAmount);

        return message;
    }
}
