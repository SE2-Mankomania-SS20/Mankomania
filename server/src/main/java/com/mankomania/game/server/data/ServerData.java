package com.mankomania.game.server.data;

import com.badlogic.gdx.utils.IntArray;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;
import com.mankomania.game.core.data.GameData;
import com.mankomania.game.core.fields.types.Field;
import com.mankomania.game.core.fields.types.*;
import com.mankomania.game.core.network.messages.clienttoserver.baseturn.DiceResultMessage;
import com.mankomania.game.core.network.messages.clienttoserver.baseturn.IntersectionSelectedMessage;
import com.mankomania.game.core.network.messages.clienttoserver.baseturn.SampleMinigame;
import com.mankomania.game.core.network.messages.servertoclient.GameUpdate;
import com.mankomania.game.core.network.messages.servertoclient.Notification;
import com.mankomania.game.core.network.messages.servertoclient.baseturn.PlayerCanRollDiceMessage;
import com.mankomania.game.core.network.messages.servertoclient.baseturn.PlayerMoves;
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
     * stores the fields left to move after a player reaches an intersection, which needs a decision from the player
     */
    private int currentPlayerMovesLeft = -1;

    private IntArray currentPlayerMoves;

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
        currentPlayerMoves = new IntArray();
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
            gameData.setCurrentPlayerTurn(0);
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
        return gameData.getPlayers().get(gameData.getCurrentPlayerTurnIndex()).getConnectionId();
    }

    /**
     * Sets the player who is currently on turn to the next player.
     */
    public void setNextPlayerTurn() {
        gameData.setNextPlayerTurn();
    }

    public void startGameLoop() {
        // starting the game loop -> first player should roll the dice

        Log.info("PlayerCanRollDiceMessage", "Sending a PlayerCanRollDiceMessage @ startup. playerTurn = " + gameData.getCurrentPlayerTurnIndex());

        PlayerCanRollDiceMessage message = new PlayerCanRollDiceMessage(gameData.getCurrentPlayerTurnIndex());
        server.sendToAllTCP(message);

        setCurrentState(GameState.WAIT_FOR_DICE_RESULT);
    }

    public void sendPlayerCanRollDice() {
        if (getCurrentState() != GameState.PLAYER_CAN_ROLL_DICE) {
            Log.error("PlayerCanRollDiceMessage", "Trying to send CAN_ROLL_DICE but state is " + getCurrentState());
            return;
        }

        Log.info("PlayerCanRollDiceMessage", "Sending a PlayerCanRollDiceMessage. playerTurn = " + gameData.getCurrentPlayerTurnIndex());

        PlayerCanRollDiceMessage message = new PlayerCanRollDiceMessage(gameData.getCurrentPlayerTurnIndex());
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

        // save moves left and clear movePath
        currentPlayerMovesLeft = diceResultMessage.getDiceResult();
        currentPlayerMoves.clear();
        // move reaming moves
        movePlayer(false);
    }

    public void movePlayer(boolean useOptional) {
        Player player = gameData.getCurrentPlayer();
        Field currField = gameData.getFields()[player.getCurrentFieldIndex()];

        // check if player is currently on intersection and send IntersectionSelectedMessage to let the client pick the direction
        if (currField.isIntersection() && !useOptional) {
            setCurrentState(GameState.WAIT_INTERSECTION_SELECTION);
            server.sendToTCP(getCurrentPlayerTurnConnectionId(), new IntersectionSelectedMessage());
            return;
        }
        while (currentPlayerMovesLeft > 0) {
            if (useOptional) {
                currField = gameData.getFields()[currField.getOptionalNextField()];
                useOptional = false;
            } else {
                currField = gameData.getFields()[currField.getNextField()];
            }
            player.updateField_S(gameData.getFields()[currField.getFieldIndex()]);
            currentPlayerMoves.add(currField.getFieldIndex());
            currentPlayerMovesLeft--;

            // check if player lands on intersection and if it is not the last move then send intersection selection msg
            if (currField.isIntersection() && currentPlayerMovesLeft > 0) {
                server.sendToAllTCP(new PlayerMoves(currentPlayerMoves));
                setCurrentState(GameState.WAIT_INTERSECTION_SELECTION);
                server.sendToTCP(getCurrentPlayerTurnConnectionId(), new IntersectionSelectedMessage());
                currentPlayerMoves.clear();
                return;
            }

            // check for field action and pause the move
            GameState nextState = checkForFieldAction(player, currField);
            if (nextState != null) {
                server.sendToAllTCP(new PlayerMoves(currentPlayerMoves));
                setCurrentState(nextState);
                handleFieldAction(nextState);
                currentPlayerMoves.clear();
                return;
            }
        }
        //send moves to clients
        setCurrentState(GameState.WAIT_FOR_TURN_FINISHED);
        server.sendToAllTCP(new PlayerMoves(currentPlayerMoves));
    }

    /**
     * check for custom action on special fields
     * by returning a GameState the moving will pause
     * use handleFieldAction to send data to player that requiers a paused move like trigger a screen/minigame
     *
     * @param player    current player
     * @param currField current field of player
     * @return State to switch to if specified (can be null if no action)
     */
    private GameState checkForFieldAction(Player player, Field currField) {
        Log.info("checkForFieldAction", "fieldtype: " + currField.getClass().getSimpleName());
        // buy lottery tickets when moving by and moving onto LotteryField only win the lottery when jumpField sends you to lottery
        if (currField instanceof LotterieField) {
            int ticketPrice = ((LotterieField) currField).getTicketPrice();
            gameData.buyLotteryTickets(player.getPlayerIndex(), ticketPrice);
            server.sendToAllExceptTCP(player.getConnectionId(), new Notification("Player " + (player.getPlayerIndex() + 1) + " bought lottery tickets for: " + ticketPrice + "$"));
        } else if (currField instanceof MinigameField) {
            MinigameField minigameField = (MinigameField) currField;

            // SampleMinigame #101
            //sample for minigame -- add your own minigame by type maybe even create a own Class for it
            Log.info("checkForFieldAction", "Minigamefield " + minigameField.getClass().getSimpleName());
            server.sendToAllExceptTCP(player.getConnectionId(), new Notification("P: " + (player.getPlayerIndex() + 1) + " is on minigame: " + minigameField.getMinigameType()));
            server.sendToTCP(player.getConnectionId(), new Notification("You are on minigame: " + minigameField.getMinigameType()));
            return GameState.DO_ACTION;
        }
        return null;
    }

    /**
     * @param nextState handle action according to state (can be of type for a specific minigame)
     */
    private void handleFieldAction(GameState nextState) {
        switch (nextState) {
            case DO_ACTION: {
                Log.info("SampleMinigame");
                // SampleMinigame #101
                // could trigger screen
                // make sure to send the correct message back to resume the playing loop
                server.sendToAllTCP(new SampleMinigame());
                break;
            }
            default: {
                Log.info("handleFieldAction", "there was no action specified for that state " + nextState.toString());
                break;
            }
        }
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

        Player player = gameData.getCurrentPlayer();
        Field currField = gameData.getFields()[player.getCurrentFieldIndex()];
        int nextField = currField.getNextField();
        int optNextField = currField.getOptionalNextField();

        if (nextField == message.getFieldIndex()) {
            movePlayer(false);
        } else if (optNextField == message.getFieldIndex()) {
            movePlayer(true);
        } else {
            Log.error("error getting intersection");
        }
    }

    public void turnFinished() {
        if (currentPlayerMovesLeft > 0) {
            Log.info("current player is not finished yet");
            return;
        }
        if (currentState == GameState.WAIT_FOR_TURN_FINISHED) {
            Player player = gameData.getPlayers().get(gameData.getCurrentPlayerTurnIndex());

            int fieldIndex = player.getCurrentFieldIndex();
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

            server.sendToAllTCP(new GameUpdate(gameData.getLotteryAmount(), gameData.getPlayers(), gameData.getHotels(), gameData.getCurrentPlayerTurnIndex()));

            setNextPlayerTurn();
            setCurrentState(GameState.PLAYER_CAN_ROLL_DICE);
            sendPlayerCanRollDice();
        }
    }
}
