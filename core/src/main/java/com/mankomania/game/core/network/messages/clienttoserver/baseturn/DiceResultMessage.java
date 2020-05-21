package com.mankomania.game.core.network.messages.clienttoserver.baseturn;

/**
 * Message to signalise the server what the dice roll result is.
 */
public class DiceResultMessage {
    private int playerId; // maybe check it against the connections id, so player cant cheat that easily
    private int diceResult;

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public int getDiceResult() {
        return diceResult;
    }

    public void setDiceResult(int diceResult) {
        this.diceResult = diceResult;
    }

    public static DiceResultMessage createDiceResultMessage(int playerId, int diceResult) {
        DiceResultMessage message = new DiceResultMessage();
        message.setPlayerId(playerId);
        message.setDiceResult(diceResult);

        return message;
    }
}
