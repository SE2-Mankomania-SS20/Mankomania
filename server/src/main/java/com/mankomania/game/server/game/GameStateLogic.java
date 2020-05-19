package com.mankomania.game.server.game;

import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;
import com.mankomania.game.core.data.GameData;
import com.mankomania.game.core.fields.types.Field;
import com.mankomania.game.core.network.messages.clienttoserver.baseturn.DiceResultMessage;
import com.mankomania.game.core.network.messages.clienttoserver.baseturn.IntersectionSelectedMessage;
import com.mankomania.game.core.network.messages.servertoclient.baseturn.MovePlayerToFieldAfterIntersectionMessage;
import com.mankomania.game.core.network.messages.servertoclient.baseturn.MovePlayerToFieldMessage;
import com.mankomania.game.core.network.messages.servertoclient.baseturn.MovePlayerToIntersectionMessage;
import com.mankomania.game.core.network.messages.servertoclient.baseturn.PlayerCanRollDiceMessage;
import com.mankomania.game.core.player.Player;
import com.mankomania.game.server.data.GameState;
import com.mankomania.game.server.data.ServerData;

/*
 Created by Fabian Oraze on 05.05.20
 */

/**
 * Class to handle the current GameState and message receiving and sending according to the current GameState.
 */
public class GameStateLogic {
    private GameState currentState;

    private ServerData serverData;
    private Server server; // maybe make a handler to communicate with the server instead of using a property

    // refs
    private final GameData refGameData;

    public GameStateLogic(ServerData serverData, Server server) {
        this.serverData = serverData;
        this.server = server;
        refGameData = serverData.getGameData();
    }

    public GameState getCurrentState() {
        return currentState;
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

        // sending move message(s), handling intersections, lottery, actions there
        sendMovePlayerMessages(diceResultMessage.getPlayerId(), diceResultMessage.getDiceResult());
    }

    public void sendMovePlayerMessages(int playerId, int fieldsToMove) {
        // getting current player and its current field position
        Player movingPlayer = refGameData.getPlayerByConnectionId(playerId);
        int originalFieldIndex = movingPlayer.getCurrentField();
        int fieldsStillToGo = fieldsToMove;

        // TODO: check for special fields, intersection, lottery

        // move the player field for field forwards
        while (fieldsStillToGo >= 1) {
            Field currentField = refGameData.getFieldById(movingPlayer.getCurrentField());
            int nextFieldId = currentField.getNextField();
            int optionalNextFieldId = currentField.getOptionalNextField();

            // check if the current field has two paths to choose from
            if (optionalNextFieldId >= 0) {
                // 1) save how many fields the player can still move (or send it with the message?)
                // 2) send MovePlayerToIntersectionMessage, go into state WAIT_FOR_INTERSECTION_RESULT
                // 3) <wait for result to arrive>
                // 4) got IntersectionSelectedMessage
                // 5) take saved left to move amount, move the player and send MovePlayerToFieldMessage
                // 6) continue as usual -> action/endturn

                // CARE FOR THE CASE GOING OVER LOTTERY AND REACH INTERSECTION
                // -> add "crossedLottery" field to all move messages?

                Log.info("[Any move message] arrived at an intersection with player " + movingPlayer.getOwnConnectionId() +
                        " on field " + originalFieldIndex + "! Fields left to move afterwards: " + fieldsStillToGo);
                this.serverData.setMovesLeftAfterIntersection(fieldsStillToGo);
                this.currentState = GameState.WAIT_INTERSECTION_SELECTION;
                this.sendMovePlayerToIntersectionMessage(movingPlayer.getOwnConnectionId(), movingPlayer.getCurrentField(), nextFieldId, optionalNextFieldId);
                // exit this function, so we dont move any further and send no MovePlayerToFieldMessage
                return;
            }

            Log.debug("[Any move message] Moving player: " + movingPlayer.getCurrentField() + " -> " + nextFieldId);

            // move player to the new field
            movingPlayer.movePlayer(nextFieldId);

            fieldsStillToGo--;
        }

        Log.info("[MovePlayerToFieldMessage] Sending MovePlayerToFieldMessage moving player " + playerId + "from field " + originalFieldIndex + " to field " + movingPlayer.getCurrentField() + " (= field amount to move was " + fieldsToMove + ")");

        // send move message to all clients
        MovePlayerToFieldMessage movePlayerToFieldMessage = MovePlayerToFieldMessage.createMovePlayerToFieldMessage(playerId, movingPlayer.getCurrentField());
        this.server.sendToAllTCP(movePlayerToFieldMessage);

        // TODO@Dilli: check for field action here ...
        // call function that handles field actions here
        // ...

        // TODO: handle starting minigame here ...

        // TODO: create a end turn function
        // go into new state (maybe introduce a WAIT_MOVED_PLAYER state and END_TURN state)
        Log.info("Finished turn of player " + this.serverData.getCurrentPlayerTurn() + " (" + this.serverData.getCurrentPlayerTurnConnectionId() + "). Going to finish turn now.");

        this.serverData.setNextPlayerTurn();
        Log.info("New turn is now " + this.serverData.getCurrentPlayerTurn() + " (" + this.serverData.getCurrentPlayerTurnConnectionId() + "). Going to CAN_ROLL_DICE now.");

        this.currentState = GameState.PLAYER_CAN_ROLL_DICE;
        this.sendPlayerCanRollDice();
    }

    public void sendMovePlayerToIntersectionMessage(int playerId, int fieldToMoveTo, int firstOptionField, int secondOptionField) {
        MovePlayerToIntersectionMessage message = new MovePlayerToIntersectionMessage();
        message.setPlayerId(playerId);
        message.setFieldToMoveTo(fieldToMoveTo);
        message.setSelectionOption1(firstOptionField);
        message.setSelectionOption2(secondOptionField);

        Log.info("[MovePlayerToIntersectionMessage] sending MovePlayerToIntersectionMessage, moving player " + playerId +
                " to field " + fieldToMoveTo + ". Intersection options to chose from: (1) = " + firstOptionField + ", (2) = " + secondOptionField);

        this.server.sendToAllTCP(message);
    }

    public void gotIntersectionSelectionMessage(IntersectionSelectedMessage message) {
        // check if we are actually waiting for this kind of message
        if (this.currentState != GameState.WAIT_INTERSECTION_SELECTION) {
            Log.error("[gotIntersectionSelectionMessage] Got IntersectionSelectionMessage while not in state WAIT_INTERSECTION_SELECTION, ignore message! Current state is " + this.currentState);
            return;
        }

        // check if the message came from the player thats currently on turn
        if (message.getPlayerId() != this.serverData.getCurrentPlayerTurnConnectionId()) {
            Log.error("[gotIntersectionSelectionMessage] Got IntersectionSelectedMessage from a player thats not on turn, ignore it.");
            return;
        }

        Log.info("[gotIntersectionSelectionMessage] Got IntersectionSelectedMessage from player " + message.getPlayerId() + " with field chosen (" + message.getFieldChosen() + ")");

        // send a message that moves the player only to the next field after the chosen intersection
        // this helps player movement implementation on the client
        refGameData.getPlayerByConnectionId(message.getPlayerId()).setCurrentField(message.getFieldChosen());
        this.sendMovePlayerToFieldAfterIntersectionMessage(message.getPlayerId(), message.getFieldChosen());

        Log.info("====== MOVES LEFT @ SENDING AFTER INTERSCTION FIELD: " + this.serverData.getMovesLeftAfterIntersection());

        // afterwards (if there are fields left to move) send another message to move the player onto its final field
        int movesLeftAfterIntersection = this.serverData.getMovesLeftAfterIntersection();
        movesLeftAfterIntersection -= 1; // reduce it by one, since we went a field already above
        this.serverData.setMovesLeftAfterIntersection(-1); // reset movesLeft just to be sure

        if (movesLeftAfterIntersection > 0) {
            this.sendMovePlayerMessages(message.getPlayerId(), movesLeftAfterIntersection);
            // ending turn gets handled in sendMovePlayerMessage for this execution path
        } else {
            // TODO@Dilli: check for field action here ...
            // TODO: create end turn function (duplicated code)
            Log.info("Finished turn of player " + this.serverData.getCurrentPlayerTurn() + " (" + this.serverData.getCurrentPlayerTurnConnectionId() + "). Going to finish turn now.");

            this.serverData.setNextPlayerTurn();
            Log.info("New turn is now " + this.serverData.getCurrentPlayerTurn() + " (" + this.serverData.getCurrentPlayerTurnConnectionId() + "). Going to CAN_ROLL_DICE now.");

            this.currentState = GameState.PLAYER_CAN_ROLL_DICE;
            this.sendPlayerCanRollDice();
        }
    }

    public void sendMovePlayerToFieldAfterIntersectionMessage(int playerId, int fieldToMoveTo) {
        MovePlayerToFieldAfterIntersectionMessage message = new MovePlayerToFieldAfterIntersectionMessage(playerId, fieldToMoveTo);
        Log.info("[sendMovePlayerToFieldAfterIntersectionMessage] sending MovePlayerToFieldAfterIntersectionMessage to move player " +
                playerId + " to field (" + fieldToMoveTo + ") after intersection.");

        this.server.sendToAllTCP(message);
    }

}
