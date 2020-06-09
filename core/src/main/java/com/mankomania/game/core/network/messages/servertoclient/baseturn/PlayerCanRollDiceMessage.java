package com.mankomania.game.core.network.messages.servertoclient.baseturn;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerCanRollDiceMessage message = (PlayerCanRollDiceMessage) o;
        return playerIndex == message.playerIndex;
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerIndex);
    }
    public int getPlayerIndex() {
        return playerIndex;
    }

    public void setPlayerIndex(int playerIndex) {
        this.playerIndex = playerIndex;
    }

}
