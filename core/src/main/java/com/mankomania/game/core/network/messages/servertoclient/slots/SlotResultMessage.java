package com.mankomania.game.core.network.messages.servertoclient.slots;

/**
 * This message gets sent from the server to the client after the client has sent his SpinRollsMessage.
 * It contains the spin result.
 */
public class SlotResultMessage {
    private int playerIndex;

    /**
     * storing the roll result, rolls icons are between 0 and 8 (9 different icons)
     * the array has a length of 3, since there are three separate rolls
     */
    private int[] rollResult;
    private int winAmount;

    public SlotResultMessage() {
        // empty ctor needed for kryonet deserialization
    }

    public SlotResultMessage(int playerIndex, int[] rollResult, int winAmount) {
        this.playerIndex = playerIndex;
        this.rollResult = rollResult;
        this.winAmount = winAmount;
    }

    public int getPlayerIndex() {
        return playerIndex;
    }

    public int[] getRollResult() {
        return rollResult;
    }

    public int getWinAmount() {
        return winAmount;
    }
}
