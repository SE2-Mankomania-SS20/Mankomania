package com.mankomania.game.server.data;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;
import com.mankomania.game.core.data.GameData;
import com.mankomania.game.core.fields.types.Field;
import com.mankomania.game.core.fields.types.*;
import com.mankomania.game.core.network.messages.clienttoserver.baseturn.DiceResultMessage;
import com.mankomania.game.core.network.messages.clienttoserver.baseturn.IntersectionSelectedMessage;
import com.mankomania.game.core.network.messages.servertoclient.Notification;
import com.mankomania.game.core.network.messages.servertoclient.baseturn.MovePlayerToFieldMessage;
import com.mankomania.game.core.network.messages.servertoclient.baseturn.MovePlayerToIntersectionMessage;
import com.mankomania.game.core.network.messages.servertoclient.baseturn.PlayerCanRollDiceMessage;
import com.mankomania.game.core.player.Player;

import java.util.*;

/*
 Created by Fabian Oraze on 03.05.20
 */

public class ServerData {

    /**
     * max players allowed in the game (should be 4 all the time since board and the rest of the game is designed for only 4 )
     */
    private static final int MAX_PLAYERS = 4;

    /**
     * min players required to start a game (more can join and click ready)
     */
    private static final int MIN_PLAYERS = 1;

    /**
     * playerIndex from players array in gamedata tha is currently at turn
     */
    private int currentPlayerTurn = 0;

    /**
     * stores the fields left to move after a player reaches an intersection, which needs a decision from the player
     */
    private int movesLeftAfterIntersection = -1;

    /**
     * state is true when the game has not yet started
     */
    private boolean gameOpen;

    /**
     * {@link GameData}
     */
    private final GameData gameData;

    /**
     * the current {@link GameState} representing the game and the action that can be performed
     */
    private GameState currentState;

    /**
     * Connection holds the player connection
     * Boolean indicates whether the player is ready to play
     */
    private final List<Integer> playersReady;

    private final Server server;

    public ServerData(Server server) {
        playersReady = new ArrayList<>();
        gameData = new GameData();
        currentState = GameState.PLAYER_CAN_ROLL_DICE;

        gameOpen = true;
        this.server = server;
    }

    public GameState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(GameState currentState) {
        this.currentState = currentState;
    }

    public GameData getGameData() {
        return gameData;
    }

    public synchronized boolean connectPlayer(Connection con) {
        if (gameOpen && gameData.getPlayers().size() <= MAX_PLAYERS) {
            int playerIndex = gameData.getPlayers().size();
            int fieldIndex = gameData.getStartFieldsIndices()[playerIndex];
            gameData.getPlayers().add(new Player(fieldIndex, con.getID(), gameData.getFieldByIndex(fieldIndex).getPositions()[0], playerIndex));
            return true;
        }
        return false;
    }

    public void disconnectPlayer(int connId) {
        playersReady.remove((Integer) connId);
        for (Player player : gameData.getPlayers()) {
            if (player.getConnectionId() == connId) {
                gameData.getPlayers().remove(player);
                break;
            }
        }
        if (playersReady.isEmpty()) {
            gameOpen = true;
        }
    }

    public void playerReady(int connId) {
        playersReady.add(connId);
    }

    public boolean checkForStart() {
        if (playersReady.size() >= MIN_PLAYERS && gameData.getPlayers().size() == playersReady.size()) {
            gameOpen = false;
            // reset the current player turn
            currentPlayerTurn = 0;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Gets the connection id of the player whos turn it is currently.
     *
     * @return the connection id of said player
     */
    public int getCurrentPlayerTurnConnectionId() {
        return gameData.getPlayers().get(currentPlayerTurn).getConnectionId();
    }

    /**
     * Sets the player who is currently on turn to the next player.
     *
     * @return the new player id
     */
    public int setNextPlayerTurn() {
        currentPlayerTurn = (currentPlayerTurn + 1) % gameData.getPlayers().size();
        return currentPlayerTurn;
    }

    public void startGameLoop() {
        // starting the game loop -> first player should roll the dice

        Log.info("PlayerCanRollDiceMessage", "Sending a PlayerCanRollDiceMessage @ startup. playerTurn = " + currentPlayerTurn);

        PlayerCanRollDiceMessage message = new PlayerCanRollDiceMessage(currentPlayerTurn);
        server.sendToAllTCP(message);

        setCurrentState(GameState.WAIT_FOR_DICE_RESULT);
    }

    public void sendPlayerCanRollDice() {
        if (getCurrentState() != GameState.PLAYER_CAN_ROLL_DICE) {
            Log.error("PlayerCanRollDiceMessage", "Trying to send CAN_ROLL_DICE but state is " + getCurrentState());
            return;
        }


        Log.info("PlayerCanRollDiceMessage", "Sending a PlayerCanRollDiceMessage. playerTurn = " + currentPlayerTurn);

        PlayerCanRollDiceMessage message = new PlayerCanRollDiceMessage(currentPlayerTurn);
        server.sendToAllTCP(message);

        setCurrentState(GameState.WAIT_FOR_DICE_RESULT);
    }

    public void gotDiceRollResult(DiceResultMessage diceResultMessage, int connId) {
        if (getCurrentState() != GameState.WAIT_FOR_DICE_RESULT) {
            Log.error("DiceResultMessage", "Got DiceResultMessage while not in state WAIT_FOR_DICE_RESULT, ignore message! Current state is " + getCurrentState());
            return;
        }

        if (getCurrentPlayerTurnConnectionId() != connId) {
            Log.error("DiceResultMessage", "Got DiceResultMessage from a player thats not on turn, ignore it.");
            return;
        }
        Log.info("DiceResultMessage", "Player " + diceResultMessage.getPlayerIndex() + " is going to move " + diceResultMessage.getDiceResult() + " fields.");

        // sending move message(s), handling intersections, lottery, actions there
        sendMovePlayerMessages(diceResultMessage.getPlayerIndex(), diceResultMessage.getDiceResult());
    }

    public void sendMovePlayerMessages(int playerIndex, int fieldsToMove) {
        // getting current player and its current field position

        Player movingPlayer = gameData.getPlayers().get(playerIndex);
        int originalFieldIndex = movingPlayer.getCurrentField();
        int fieldsStillToGo = fieldsToMove;

        // move the player field for field forwards
        while (fieldsStillToGo >= 1) {
            Field currentField = gameData.getFields()[movingPlayer.getCurrentField()];
            int nextFieldId = currentField.getNextField();
            int optionalNextFieldId = currentField.getOptionalNextField();

            // buy lottery tickets when moving by and moving onto LotteryField only win the lottery when jumpField sends you to lottery
            if (currentField instanceof LotterieField) {
                int ticketPrice = ((LotterieField) currentField).getTicketPrice();
                gameData.buyLotteryTickets(movingPlayer.getPlayerIndex(), ticketPrice);
                server.sendToAllExceptTCP(movingPlayer.getConnectionId(), new Notification("Player " + (movingPlayer.getPlayerIndex() + 1) + " bought lottery tickets for: " + ticketPrice + "$"));
                server.sendToTCP(movingPlayer.getConnectionId(), new Notification("You bought lottery tickets for: " + ticketPrice + "$"));
            }

            // check if the current field has two paths to choose from
            if (optionalNextFieldId >= 0) {
                Log.info("MoveMessage", "arrived at an intersection with player " + movingPlayer.getConnectionId() +
                        " on field " + originalFieldIndex + "! Fields left to move afterwards: " + fieldsStillToGo);
                movesLeftAfterIntersection = fieldsStillToGo;
                setCurrentState(GameState.WAIT_INTERSECTION_SELECTION);
                sendMovePlayerToIntersectionMessage(movingPlayer.getPlayerIndex(), movingPlayer.getCurrentField(), nextFieldId, optionalNextFieldId);
                // exit this function, so we dont move any further and send no MovePlayerToFieldMessage
                return;
            }

            Log.debug("MoveMessage", "Moving player: " + movingPlayer.getCurrentField() + " -> " + nextFieldId);

            // move player to the new field
            movingPlayer.updateField_S(gameData.getFieldByIndex(nextFieldId));

            fieldsStillToGo--;
        }

        Log.info("MovePlayerToFieldMessage", "Sending MovePlayerToFieldMessage moving player " + playerIndex + "from field " + originalFieldIndex + " to field " + movingPlayer.getCurrentField() + " (= field amount to move was " + fieldsToMove + ")");

        // send move message to all clients
        MovePlayerToFieldMessage movePlayerToFieldMessage = new MovePlayerToFieldMessage(playerIndex, movingPlayer.getCurrentField());
        server.sendToAllTCP(movePlayerToFieldMessage);

        Log.info("Turn", "Finished turn of player " + currentPlayerTurn + " (" + getCurrentPlayerTurnConnectionId() + "). Going to finish turn now.");

        setCurrentState(GameState.WAIT_FOR_TURN_FINISHED);
    }

    public void sendMovePlayerToIntersectionMessage(int playerIndex, int fieldIndex, int firstOptionField, int secondOptionField) {
        MovePlayerToIntersectionMessage message = new MovePlayerToIntersectionMessage();
        message.setPlayerIndex(playerIndex);
        message.setFieldIndex(fieldIndex);
        message.setSelectionOption1(firstOptionField);
        message.setSelectionOption2(secondOptionField);

        Log.info("MovePlayerToIntersectionMessage", "Sending MovePlayerToIntersectionMessage, moving player " + playerIndex +
                " to field " + fieldIndex + ". Intersection options to chose from: (1) = " + firstOptionField + ", (2) = " + secondOptionField);

        server.sendToAllTCP(message);
    }

    public void gotIntersectionSelectionMessage(IntersectionSelectedMessage message, int connectionId) {
        // check if we are actually waiting for this kind of message
        if (getCurrentState() != GameState.WAIT_INTERSECTION_SELECTION) {
            Log.error("gotIntersectionSelectionMessage", "Got IntersectionSelectionMessage while not in state WAIT_INTERSECTION_SELECTION, ignore message! Current state is " + getCurrentState());
            return;
        }

        // check if the message came from the player thats currently on turn
        if (connectionId != getCurrentPlayerTurnConnectionId()) {
            Log.error("gotIntersectionSelectionMessage", "Got IntersectionSelectedMessage from a player thats not on turn, ignore it.");
            return;
        }

        Log.info("gotIntersectionSelectionMessage", "Got IntersectionSelectedMessage from player " + message.getPlayerIndex() + " with field chosen (" + message.getFieldIndex() + ")");

        // send a message that moves the player only to the next field after the chosen intersection
        // this helps player movement implementation on the client
        gameData.getPlayers().get(message.getPlayerIndex()).updateField(gameData.getFields()[message.getFieldIndex()]);
        server.sendToAllTCP(new MovePlayerToFieldMessage(message.getPlayerIndex(), message.getFieldIndex()));

        // afterwards (if there are fields left to move) send another message to move the player onto its final field
        // reduce it by one, since we went a field already above
        movesLeftAfterIntersection -= 1;

        if (movesLeftAfterIntersection > 0) {
            sendMovePlayerMessages(message.getPlayerIndex(), movesLeftAfterIntersection);
            // ending turn gets handled in sendMovePlayerMessage for this execution path
        } else {
            Log.info("Turn", "Finished turn of player " + currentPlayerTurn + " (" + getCurrentPlayerTurnConnectionId() + "). Going to finish turn now.");
            if (currentState != GameState.WAIT_FOR_TURN_FINISHED) {
                setCurrentState(GameState.WAIT_FOR_TURN_FINISHED);
            }
        }
        movesLeftAfterIntersection = -1; // reset movesLeft just to be sure
    }

    public void turnFinished() {
        if (currentState == GameState.WAIT_FOR_TURN_FINISHED) {
            Player player = gameData.getPlayers().get(currentPlayerTurn);

            int fieldIndex = player.getCurrentField();
            Field field = gameData.getFieldByIndex(fieldIndex);

            if (field instanceof GainMoneyField) {
                GainMoneyField gainMoneyField = (GainMoneyField) field;
                player.addMoney(gainMoneyField.getAmountMoney());
            } else if (field instanceof HotelField) {
                HotelField hotelField = (HotelField) field;

            } else if (field instanceof JumpField) {
                JumpField jumpField = (JumpField) field;


            } else if (field instanceof LoseMoneyField) {
                LoseMoneyField loseMoneyField = (LoseMoneyField) field;
                player.loseMoney(loseMoneyField.getAmountMoney());
            } else if (field instanceof LotterieField) {
                LotterieField lotterieField = (LotterieField) field;

            } else if (field instanceof MinigameField) {
                MinigameField minigameField = (MinigameField) field;

            } else if (field instanceof PayLotterieField) {
                PayLotterieField payLotterieField = (PayLotterieField) field;
                player.loseMoney(payLotterieField.getAmountToPay());
            } else if (field instanceof SpecialField) {
                SpecialField specialField = (SpecialField) field;

            } else if (field instanceof StockField) {
                StockField stockField = (StockField) field;

            } else {
                Log.error("Coult not determine Field Type and associated action");
            }

            setNextPlayerTurn();
            setCurrentState(GameState.PLAYER_CAN_ROLL_DICE);
            sendPlayerCanRollDice();
        }
    }
}
