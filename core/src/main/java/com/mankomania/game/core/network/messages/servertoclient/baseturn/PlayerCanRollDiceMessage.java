package com.mankomania.game.core.network.messages.servertoclient.baseturn;

/**
 * Signalises the clients which player has to roll the dice now.
 */
public class PlayerCanRollDiceMessage {
    private int playerIndex;

    public PlayerCanRollDiceMessage(int playerIndex) {
        this.playerIndex = playerIndex;
    }

    public PlayerCanRollDiceMessage() {
        // empty kryonet
    }

    public int getPlayerIndex() {
        return playerIndex;
    }

    public void setPlayerIndex(int playerIndex) {
        this.playerIndex = playerIndex;
    }
}
