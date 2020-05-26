package com.mankomania.game.core.network.messages.clienttoserver.baseturn;

/**
 * Message to signalise the server what the dice roll result is.
 */
public class DiceResultMessage {
    private int playerIndex; // maybe check it against the connections id, so player cant cheat that easily
    private int diceResult;

    public DiceResultMessage() {
        // empty kryonet
    }

    public DiceResultMessage(int playerIndex, int diceResult) {
        this.playerIndex = playerIndex;
        this.diceResult = diceResult;
    }

    public int getPlayerIndex() {
        return playerIndex;
    }

    public void setPlayerIndex(int playerIndex) {
        this.playerIndex = playerIndex;
    }

    public int getDiceResult() {
        return diceResult;
    }

    public void setDiceResult(int diceResult) {
        this.diceResult = diceResult;
    }

}
