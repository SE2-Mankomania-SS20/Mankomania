package com.mankomania.game.core.network.messages.servertoclient.baseturn;

/**
 * Signalises the clients which player has to roll the dice now.
 */
public class PlayerCanRollDiceMessage {
    private int playerId;

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public static PlayerCanRollDiceMessage createPlayerCanRollDiceMessage(int playerId) {
        PlayerCanRollDiceMessage message = new PlayerCanRollDiceMessage();
        message.playerId = playerId;

        return message;
    }
}
