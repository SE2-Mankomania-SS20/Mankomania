package com.mankomania.game.server.game;

import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;
import com.mankomania.game.core.data.GameData;
import com.mankomania.game.core.fields.types.Field;
import com.mankomania.game.core.network.messages.clienttoserver.baseturn.DiceResultMessage;
import com.mankomania.game.core.network.messages.servertoclient.baseturn.MovePlayerToFieldMessage;
import com.mankomania.game.core.network.messages.servertoclient.baseturn.PlayerCanRollDiceMessage;
import com.mankomania.game.core.player.Player;
import com.mankomania.game.server.data.GameState;
import com.mankomania.game.server.data.ServerData;

/*********************************
 Created by Fabian Oraze on 05.05.20
 *********************************/

/**
 * Class to handle the current GameState and message receiving and sending according to the current GameState.
 */
public class GameStateLogic {
    private GameState currentState;
    private ServerData serverData;
    private GameData gameData;
    private Server server; // maybe make a handler to communicate with the server instead of using a property

    public GameStateLogic(ServerData serverData, GameData gameData, Server server) {
        this.serverData = serverData;
        this.gameData = gameData;
        this.server = server;
    }

    public void startGameLoop() {
        // starting the game loop -> first player should roll the dice
        int currentPlayerTurn = this.serverData.getCurrentPlayerTurn();
        int currentPlayerId = this.serverData.initPlayerList().get(currentPlayerTurn);

        Log.info("[PlayerCanRollDiceMessage@Startup] Sending a PlayerCanRollDiceMessage. playerTurn = " + currentPlayerTurn + ", playerId = " + currentPlayerId);

        PlayerCanRollDiceMessage message = PlayerCanRollDiceMessage.createPlayerCanRollDiceMessage(currentPlayerId);
        this.server.sendToAllTCP(message);

        this.currentState = GameState.WAIT_FOR_DICE_RESULT;
    }

    public void sendPlayerCanRollDice() {
        if (this.currentState != GameState.PLAYER_CAN_ROLL_DICE) {
            Log.error("[PlayerCanRollDiceMessage] Trying to send CAN_ROLL_DICE but state is " + this.currentState);
            return;
        }

        int currentPlayerTurn = this.serverData.getCurrentPlayerTurn();
        int currentPlayerId = this.serverData.initPlayerList().get(currentPlayerTurn);

        Log.info("[PlayerCanRollDiceMessage] Sending a PlayerCanRollDiceMessage. playerTurn = " + currentPlayerTurn + ", playerId = " + currentPlayerId);

        PlayerCanRollDiceMessage message = PlayerCanRollDiceMessage.createPlayerCanRollDiceMessage(currentPlayerId);
        this.server.sendToAllTCP(message);

        this.currentState = GameState.WAIT_FOR_DICE_RESULT;
    }

    public void gotDiceRollResult(DiceResultMessage diceResultMessage) {
        if (this.currentState != GameState.WAIT_FOR_DICE_RESULT) {
            Log.error("[DiceResultMessage] Got DiceResultMessage while not in state WAIT_FOR_DICE_RESULT, ignore message! Current state is " + this.currentState);
            return;
        }

        if (this.serverData.getCurrentPlayerTurnConnectionId() != diceResultMessage.getPlayerId()) {
            Log.error("[DiceResultMessage] Got DiceResultMessage from a player thats not on turn, ignore it.");
            return;
        }
        Log.info("[DiceResultMessage] Player " + diceResultMessage.getPlayerId() + " is going to move " + diceResultMessage.getDiceResult() + " fields.");

        // sending move message(s)
        sendMovePlayerMessages(diceResultMessage.getPlayerId(), diceResultMessage.getDiceResult());

        // TODO: create a end turn function
        // go into new state (maybe introduce a WAIT_MOVED_PLAYER state and END_TURN state)
        Log.info("Finished turn of player " + this.serverData.getCurrentPlayerTurn() + " (" + this.serverData.getCurrentPlayerTurnConnectionId() + "). Going to finish turn now.");

        this.serverData.setNextPlayerTurn();
        Log.info("New turn is now " + this.serverData.getCurrentPlayerTurn() + " (" + this.serverData.getCurrentPlayerTurnConnectionId() + "). Going to CAN_ROLL_DICE now.");

        this.currentState = GameState.PLAYER_CAN_ROLL_DICE;
        this.sendPlayerCanRollDice();
    }

    public void sendMovePlayerMessages(int playerId, int fieldsToMove) {
        // getting current player and its current field position
        Player movingPlayer = this.gameData.getPlayerByConnectionId(playerId);
        int originalFieldIndex = movingPlayer.getCurrentField();
        int fieldsStillToGo = fieldsToMove;

        // TODO: check for special fields, intersection, lottery

        // move the player field for field forwards
        while (fieldsStillToGo >= 1) {
            Field currentField = this.gameData.getFieldById(movingPlayer.getCurrentField());
            int nextFieldId = currentField.getNextField();
            int optionalNextFieldId = currentField.getOptionalNextField();

            // check if the current field has two paths to choose from
            if (optionalNextFieldId != -1) {
                // 1) save how many fields the player can still move (or send it with the message?)
                // 2) send MovePlayerToIntersectionMessage, go into state WAIT_FOR_INTERSECTION_RESULT
                // 3) <wait for result to arrive>
                // 4) got IntersectionSelectedMessage
                // 5) take saved left to move amount, move the player and send MovePlayerToFieldMessage
                // 6) continue as usual -> action/endturn

                // CARE FOR THE CASE GOING OVER LOTTERY AND REACH INTERSECTION
                // -> add "crossedLottery" field to all move messages?
                // return;
            }

            Log.debug("[DiceResultMessage] Moving player: " + movingPlayer.getCurrentField() + " -> " + nextFieldId);

            // move player to the new field
            movingPlayer.movePlayer(nextFieldId);

            fieldsStillToGo--;
        }

        Log.info("[MovePlayerToFieldMessage] Sending MovePlayerToFieldMessage moving player " + playerId + "from field " + originalFieldIndex + " to field " + movingPlayer.getCurrentField() + " (= field amount to move was " + fieldsToMove + ")");

        // send move message to all clients
        MovePlayerToFieldMessage movePlayerToFieldMessage = MovePlayerToFieldMessage.createMovePlayerToFieldMessage(playerId, movingPlayer.getCurrentField());
        this.server.sendToAllTCP(movePlayerToFieldMessage);

        // call function that handles field actions here
        // ...
    }

}
