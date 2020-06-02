package com.mankomania.game.server.data;

import com.badlogic.gdx.utils.IntArray;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;
import com.mankomania.game.core.data.GameData;
import com.mankomania.game.core.fields.types.*;
import com.mankomania.game.core.network.messages.clienttoserver.baseturn.DiceResultMessage;
import com.mankomania.game.core.network.messages.clienttoserver.baseturn.IntersectionSelection;
import com.mankomania.game.core.network.messages.servertoclient.GameUpdate;
import com.mankomania.game.core.network.messages.servertoclient.Notification;
import com.mankomania.game.core.network.messages.servertoclient.baseturn.PlayerCanRollDiceMessage;
import com.mankomania.game.core.network.messages.servertoclient.baseturn.PlayerMoves;
import com.mankomania.game.core.player.Player;
import com.mankomania.game.server.game.CheatHandler;
import com.mankomania.game.server.game.StockHandler;
import com.mankomania.game.server.game.TrickyOneHandler;

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

    //mini game handlers
    private final TrickyOneHandler trickyOneHandler;
    private final StockHandler stockHandler;

    //cheat handler
    private final CheatHandler cheatHandler;


    public ServerData(Server server) {
        playersReady = new ArrayList<>();
        gameData = new GameData();
        currentState = GameState.PLAYER_CAN_ROLL_DICE;
        trickyOneHandler = new TrickyOneHandler(server, this);
        gameOpen = true;
        this.server = server;
        currentPlayerMoves = new IntArray();
        stockHandler = new StockHandler(server, this);
        cheatHandler = new CheatHandler(server, this);
    }

    public StockHandler getStockHandler() {
        return stockHandler;
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

    public TrickyOneHandler getTrickyOneHandler() {
        return trickyOneHandler;
    }

    public synchronized boolean connectPlayer(Connection con) {
        if (gameOpen && gameData.getPlayers().size() < MAX_PLAYERS) {
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
        movePlayer(false, true);
    }

    public void movePlayer(boolean useOptional, boolean isFirstMove) {
        Player player = gameData.getCurrentPlayer();
        Field currField = gameData.getFields()[player.getCurrentFieldIndex()];

        // check if player is currently on intersection and send IntersectionSelectedMessage to let the client pick the direction
        if (currField.isIntersection() && isFirstMove) {
            setCurrentState(GameState.WAIT_INTERSECTION_SELECTION);
            server.sendToTCP(getCurrentPlayerTurnConnectionId(), new IntersectionSelection());
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
                server.sendToTCP(getCurrentPlayerTurnConnectionId(), new IntersectionSelection());
                currentPlayerMoves.clear();
                return;
            }

            if (currField instanceof JumpField && currentPlayerMovesLeft == 0) {
                JumpField jumpField = (JumpField) currField;
                currField = gameData.getFields()[jumpField.getJumpToField()];
                Log.info("movePlayer", "found jumpfield: " + jumpField.getJumpToField());
                player.updateField_S(gameData.getFields()[jumpField.getJumpToField()]);

                currentPlayerMoves.add(jumpField.getJumpToField());

                Field jumpedToField = gameData.getFields()[player.getCurrentFieldIndex()];
                if (jumpedToField instanceof LotterieField) {
                    //win the lottery
                    handleLotteryWin();
                }
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
        if (currentPlayerMoves.isEmpty()) {
            Log.info("movePlayer", "empty finish turn");
            setCurrentState(GameState.WAIT_FOR_TURN_FINISHED);
            turnFinished();
        } else {
            Log.info("movePlayer", "finsh move");
            server.sendToAllTCP(new PlayerMoves(currentPlayerMoves));
            setCurrentState(GameState.WAIT_FOR_TURN_FINISHED);
            currentPlayerMoves.clear();
        }
    }

    private void handleLotteryWin() {
        Player player = getGameData().getCurrentPlayer();
        int win = gameData.winLottery(player.getPlayerIndex());
        if (win > 0) {
            // you earned win amount
            server.sendToAllExceptTCP(player.getConnectionId(), new Notification("Player " + (player.getPlayerIndex() + 1) + " won at lottery: " + win + "$"));
            server.sendToTCP(player.getConnectionId(), new Notification("You won the lottery: " + win + "$"));
        } else {
            // you lost win amount
            server.sendToAllExceptTCP(player.getConnectionId(), new Notification("Player " + (player.getPlayerIndex() + 1) + " lost lottery: " + win + "$"));
            server.sendToTCP(player.getConnectionId(), new Notification("You lost at the lottery: " + win + "$"));
        }
        server.sendToAllTCP(new GameUpdate(gameData));
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
            server.sendToTCP(player.getConnectionId(), new Notification("You bought lottery tickets for: " + ticketPrice + "$"));
            sendGameData();
        } else if (currField instanceof MinigameField) {
            MinigameField minigameField = (MinigameField) currField;
            switch (minigameField.getMinigameType()) {
                case CASINO: {

                    break;
                }
                case BOESE1: {
                    return GameState.TRICKY_ONE_WROS;
                }
                case AKTIEN_BOERSE: {

                    break;
                }
                case PFERDERENNEN: {

                    break;
                }
            }
        }
        return null;
    }

    /**
     * @param nextState handle action according to state (can be of type for a specific minigame)
     */
    private void handleFieldAction(GameState nextState) {
        switch (nextState) {
            case TRICKY_ONE_WROS: {
                trickyOneHandler.startGame();
                break;
            }
            default: {
                Log.info("handleFieldAction", "there was no action specified for that state " + nextState.toString());
                break;
            }
        }
    }


    public void gotIntersectionSelectionMessage(IntersectionSelection message, int connectionId) {
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
            movePlayer(false, false);
        } else if (optNextField == message.getFieldIndex()) {
            movePlayer(true, false);
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

            } else if (field instanceof LoseMoneyField) {
                LoseMoneyField loseMoneyField = (LoseMoneyField) field;
                player.loseMoney(loseMoneyField.getAmountMoney());
            } else if (field instanceof PayLotterieField) {
                PayLotterieField payLotterieField = (PayLotterieField) field;
                player.loseMoney(payLotterieField.getAmountToPay());
            } else if (field instanceof SpecialField) {
                SpecialField specialField = (SpecialField) field;

            } else if (field instanceof StockField) {
                StockField stockField = (StockField) field;
                player.buyStock(stockField.getStockType(), 1);
            }

            for (Player otherPlayer : gameData.getPlayers()) {
                if (otherPlayer.getPlayerIndex() != player.getPlayerIndex()) {
                    if (otherPlayer.getCurrentFieldIndex() == player.getCurrentFieldIndex()) {
                        player.payToPlayer(otherPlayer, 10000);
                        server.sendToAllExceptTCP(player.getConnectionId(), new Notification("Player " + (player.getPlayerIndex() + 1) + " paid Player" + (otherPlayer.getPlayerIndex() + 1) + " compensation"));
                        server.sendToTCP(player.getConnectionId(), new Notification("You paid Player " + (otherPlayer.getPlayerIndex() + 1) + " compensation"));
                    }
                }
            }

            // TODO rm : only for debug to see actual game state
            for (Player pl : gameData.getPlayers()) {
                Log.info(pl.toString());
            }

            sendGameData();

            setNextPlayerTurn();
            setCurrentState(GameState.PLAYER_CAN_ROLL_DICE);
            sendPlayerCanRollDice();
        }
    }

    public void sendGameData() {
        server.sendToAllTCP(new GameUpdate(gameData));
    }
}
