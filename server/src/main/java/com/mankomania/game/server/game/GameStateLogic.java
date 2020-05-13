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
        // TODO: check if the right player has sent this message
        // TODO: check for special fields, intersection, lottery

        // getting current player and its current field position
        Player movingPlayer = this.gameData.getPlayerByConnectionId(diceResultMessage.getPlayerId());
        int originalFieldIndex = movingPlayer.getCurrentField();
        int fieldsStillToGo = diceResultMessage.getDiceResult();

        // move the player field for field forwards
        while (fieldsStillToGo >= 1) {
            Field currentField = this.gameData.getFieldById(movingPlayer.getCurrentField());
            int nextFieldId = currentField.getNextField();
            // TODO: implement id property in fields for easier access to ids...
            /*Field nextField = this.gameData.getFieldById(currentField.getNextField()); */

            Log.debug("[DiceResultMessage] Moving player: " + movingPlayer.getCurrentField() + " -> " + nextFieldId);

            movingPlayer.movePlayer(nextFieldId);

            fieldsStillToGo--;
        }

        Log.info("[DiceResultMessage] Player " + diceResultMessage.getPlayerId() + " moves now from field " +
                originalFieldIndex + " to " + movingPlayer.getCurrentField());
        Log.info("[MovePlayerToFieldMessage] Sending MovePlayerToFieldMessage moving player " + diceResultMessage.getPlayerId() + " to field " + movingPlayer.getCurrentField() + " (= field amount to move is " + diceResultMessage.getDiceResult() + ")");

        // send move message to all clients
        MovePlayerToFieldMessage movePlayerToFieldMessage = MovePlayerToFieldMessage.createMovePlayerToFieldMessage(diceResultMessage.getPlayerId(), movingPlayer.getCurrentField(), diceResultMessage.getDiceResult());
        this.server.sendToAllTCP(movePlayerToFieldMessage);

        // go into new state (maybe introduce a WAIT_MOVED_PLAYER state and END_TURN state)
        Log.info("Finished turn of player " + this.serverData.getCurrentPlayerTurn() + " (" + this.serverData.getCurrentPlayerTurnId() + "). Going to finish turn now.");

        this.serverData.setNextPlayerTurn();
        Log.info("New turn is now " + this.serverData.getCurrentPlayerTurn() + " (" + this.serverData.getCurrentPlayerTurnId() + "). Going to CAN_ROLL_DICE now.");

        this.currentState = GameState.PLAYER_CAN_ROLL_DICE;
        this.sendPlayerCanRollDice();
    }

}
