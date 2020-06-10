package com.mankomania.game.server.game;

import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;
import com.mankomania.game.core.network.messages.servertoclient.slots.SlotResultMessage;
import com.mankomania.game.core.network.messages.servertoclient.slots.StartSlotsMessage;
import com.mankomania.game.server.data.GameState;
import com.mankomania.game.server.data.ServerData;

import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

/**
 * This handler should live in ServerData and handle all things related to the Slot machine minigame.
 */
public class SlotHandler {
    // needed references
    private Server server;
    private ServerData serverData;

    /**
     * SlotHandler constructor should be called in ServerData.
     *
     * @param server     reference of the server object
     * @param serverData reference of the server data object
     */
    public SlotHandler(Server server, ServerData serverData) {
        this.serverData = serverData;
        this.server = server;
    }

    /**
     * Starts the slots minigame, setting the state and sending a start message to the clients.
     */
    public void startSlotsGame() {
        int currentPlayerIndex = this.serverData.getGameData().getCurrentPlayerTurnIndex();

        // send a message that the game has started and clients should open their slots minigame screen
        this.server.sendToAllTCP(new StartSlotsMessage(currentPlayerIndex));

        // go into waiting state for the current player to start rolling the slots
        this.serverData.setCurrentState(GameState.WAIT_SLOTS_INPUT);
    }

    public void gotSpinRollsMessage(int connectionId) {
        // check if we are in the right state to receive a spinRolls message
        if (this.serverData.getCurrentState() != GameState.WAIT_SLOTS_INPUT) {
            Log.error("SpinRollsMessage", "Got SpinRollsMessage while not in state WAIT_SLOTS_INPUT, ignore message! Current state is " + this.serverData.getCurrentState());
            return;
        }

        if (this.serverData.getCurrentPlayerTurnConnectionId() != connectionId) {
            Log.error("SpinRollsMessage", "Got SpinRollsMessage from a player thats not on turn, ignore it.");
            return;
        }
        Log.info("SpinRollsMessage", "Player index " + this.serverData.getGameData().getCurrentPlayerTurnIndex() + " has spinned the rolls!");

        int[] randomRollValues = this.generateRandomSlotValues();
        this.sendSlotResultMessage(this.serverData.getGameData().getCurrentPlayerTurnIndex(), randomRollValues);
    }

    /**
     * Generates a random slot roll result and sends it to the clients.
     * Also updates the money amounts accordingly and sent them to the players as well.
     */
    public void sendSlotResultMessage(int playerIndex, int[] randomRolls) {
        int winnings = this.caluclateWinnings(randomRolls);
        this.server.sendToAllTCP(new SlotResultMessage(playerIndex, randomRolls, winnings));

        Log.info("SlotResultMessage", "About to send a SlotResultMessage to player " + playerIndex +
                ". Values: " + Arrays.toString(randomRolls) + ", price: " + winnings);

        // TODO: move the payment for playing the slot machien somewere else and notify the player somehow
        // pay the money for playing the slot machine
        this.serverData.getGameData().getPlayers().get(playerIndex).loseMoney(20000);
        Log.info("SlotResultMessage", "Player with idx " + playerIndex + " lost 20.000$ for playing the slot machine.");
        if (winnings > 0) {
            this.serverData.getGameData().getPlayers().get(playerIndex).addMoney(winnings);
            // TODO: maybe remove this if not needed later on
            this.serverData.sendGameData();
            Log.info("SlotResultMessage", "Player " + playerIndex + " won " + winnings + "! Added it to his money.");
        }

        // TODO: remove timer and implement a return message from the clients
        // TODO: move the constants to a central constants file
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                serverData.setCurrentState(GameState.WAIT_FOR_ALL_ROULETTE_BET);
                serverData.getRouletteHandler().startGame();
            }
        }, 13000);
    }

    /**
     * Looks at the given roll result and check whether it's a win to return the according price.
     *
     * @param rollValues the rolls that should be used
     * @return the amount won with this icon combination
     */
    private int caluclateWinnings(int[] rollValues) {
        for (int iconId = 0; iconId < 9; iconId++) {
            int occurence = 0;

            for (int roll : rollValues) {
                if (roll == iconId) {
                    occurence += 1;
                }
            }

            // handle price won
            if (occurence == 2) {
                return 50000;
            } else if (occurence == 3) {
                // check if we rolled the 7 icon three times, that way the winning is especially high
                if (iconId == 4) {
                    return 250000;
                } else {
                    return 150000;
                }
            }
        }
        return 0;
    }

    private int[] generateRandomSlotValues() {
        int[] slotVals = new int[3];

        for (int i = 0; i < slotVals.length; i++) {
            // generate random number between 0 and 8
            slotVals[i] = (int) (Math.random() * 9);
        }

        return slotVals;
    }
}
