package com.mankomania.game.core.network.messages.servertoclient.slots;

/**
 * This message gets sent from the server to the client, when a player moves over (or onto) the casino minigame field.
 */
public class StartSlotsMsg {
    private int playerIndex;

    public StartSlotsMsg() {
        // empty ctor to enable kryonet deserialization
    }

    public StartSlotsMsg(int playerIndex) {
        this.playerIndex = playerIndex;
    }

    public int getPlayerIndex() {
        return playerIndex;
    }
}
