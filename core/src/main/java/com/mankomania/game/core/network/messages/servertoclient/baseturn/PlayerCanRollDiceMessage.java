package com.mankomania.game.core.network.messages.servertoclient.baseturn;

/**
 * Signalises the clients which player has to roll the dice now.
 */
public class PlayerCanRollDiceMessage {
    private int playerId;

    public PlayerCanRollDiceMessage(int playerId) {
        this.playerId = playerId;
    }

    public PlayerCanRollDiceMessage() {
        // empty kryonet
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }
}
