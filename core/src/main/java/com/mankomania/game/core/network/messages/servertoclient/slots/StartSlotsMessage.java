package com.mankomania.game.core.network.messages.servertoclient.slots;

import java.util.Objects;

/**
 * This message gets sent from the server to the client, when a player moves over (or onto) the casino minigame field.
 * If this message gets received at the clients, the clients open up the slot minigame screen.
 */
public class StartSlotsMessage {
    private int playerIndex;

    public StartSlotsMessage() {
        // empty ctor to enable kryonet deserialization
    }

    public StartSlotsMessage(int playerIndex) {
        this.playerIndex = playerIndex;
    }

    public int getPlayerIndex() {
        return playerIndex;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StartSlotsMessage that = (StartSlotsMessage) o;
        return playerIndex == that.playerIndex;
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerIndex);
    }
}
