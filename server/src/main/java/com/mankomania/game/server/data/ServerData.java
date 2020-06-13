package com.mankomania.game.server.data;

import com.badlogic.gdx.utils.IntArray;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;
import com.mankomania.game.core.data.GameData;
import com.mankomania.game.core.fields.types.*;
import com.mankomania.game.core.network.messages.clienttoserver.baseturn.DiceResultMessage;
import com.mankomania.game.core.network.messages.clienttoserver.baseturn.IntersectionSelection;
import com.mankomania.game.core.network.messages.servertoclient.GameUpdate;
import com.mankomania.game.core.network.messages.servertoclient.Notification;
import com.mankomania.game.core.network.messages.servertoclient.PlayerWon;
import com.mankomania.game.core.network.messages.servertoclient.baseturn.PlayerCanRollDiceMessage;
import com.mankomania.game.core.network.messages.servertoclient.baseturn.PlayerMoves;
import com.mankomania.game.core.player.Player;

import com.mankomania.game.server.game.*;
import com.mankomania.game.server.minigames.RouletteHandler;

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

    private final IntArray currentPlayerMoves;

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

    /**
     * List that holds winners, is checked every end of round
     */
    private List<Player> winners;


    private final Server server;

    //mini game handlers
    private final TrickyOneHandler trickyOneHandler;
    private final StockHandler stockHandler;
    private final HotelHandler hotelHandler;
    private final RouletteHandler rouletteHandler;
    private final SlotHandler slotHandler;
    private final HorseRaceHandler horseRaceHandler;

    //cheat handler
    private final CheatHandler cheatHandler;

    public ServerData(Server server) {
        this.server = server;
        playersReady = new ArrayList<>();
        gameData = new GameData();
        gameOpen = true;
        currentState = GameState.PLAYER_CAN_ROLL_DICE;
        currentPlayerMoves = new IntArray();
        trickyOneHandler = new TrickyOneHandler(server, this);
        slotHandler = new SlotHandler(server, this);
        horseRaceHandler = new HorseRaceHandler(server, this);
        stockHandler = new StockHandler(server, this);
        hotelHandler = new HotelHandler(server, this);
        rouletteHandler = new RouletteHandler(this, server);
        cheatHandler = new CheatHandler(server, this);
    }

    public HorseRaceHandler getHorseRaceHandler() {
        return horseRaceHandler;
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

    public RouletteHandler getRouletteHandler() {
        return rouletteHandler;
    }

    public HotelHandler getHotelHandler() {
        return hotelHandler;
    }

    public SlotHandler getSlotHandler() {
        return slotHandler;
    }

    public CheatHandler getCheatHandler() {
        return cheatHandler;
    }

    public synchronized boolean connectPlayer(int conId) {
        if (gameOpen && gameData.getPlayers().size() < MAX_PLAYERS) {
            int playerIndex = gameData.getPlayers().size();
            int fieldIndex = gameData.getStartFieldsIndices()[playerIndex];
            gameData.getPlayers().add(new Player(fieldIndex, conId, gameData.getFieldByIndex(fieldIndex).getPositions()[0], playerIndex));
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
        int index = 0;
        for (Player player : gameData.getPlayers()) {
            if (player.getPlayerIndex() != index) {
                player.setPlayerIndex(index);
                player.setFieldIndex(gameData.getStartFieldsIndices()[index]);
            }
            index++;
        }
        if (playersReady.isEmpty()) {
            gameOpen = true;
        }
    }

    public void playerReady(int playerIndex) {
        if (!playersReady.contains(playerIndex)) {
            playersReady.add(playerIndex);
        }
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
     * Checks if one of the players has less than 0 money if so that player wins and the game should end
     * and reset itself to the lobbyScreen
     *
     * @return true if we have a winner and false otherwise
     */
    public boolean checkForWinner() {
        winners = new ArrayList<>();
        for (Player player : gameData.getPlayers()) {
            if (player.getMoney() < 0) {
                winners.add(player);
            }
        }

        if (winners.isEmpty()) {
            return false;
        } else {
            Player tempWinner = winners.get(0);
            if (winners.size() > 1) {
                //more than one player less than 0 money
                for (int i = 1; i < winners.size(); i++) {
                    if (winners.get(i).getMoney() < tempWinner.getMoney()) {
                        tempWinner = winners.get(i);
                    }
                }
            }
            server.sendToAllTCP(new PlayerWon(tempWinner.getPlayerIndex()));
            Log.info("Player " + (tempWinner.getPlayerIndex() + 1) + " has won!");
            return true;
        }
    }

    /**
     * Reset whole gameData and send its clients to the default state
     */
    public void resetGame() {
        this.winners.clear();
        this.currentPlayerMoves.clear();
        this.playersReady.clear();
        this.gameOpen = true;
        this.cheatHandler.clearHistory();
        gameData.setCurrentPlayerTurn(0);
        gameData.setLotteryAmount(0);
        int[] connections = new int[gameData.getPlayers().size()];
        //save old connections
        for (int i = 0; i < connections.length; i++) {
            connections[i] = gameData.getPlayers().get(i).getConnectionId();
        }
        //clear old player stats from gameData
        gameData.getPlayers().clear();
        //add old players to gameData again with fresh data
        for (int connection : connections) {
            connectPlayer(connection);
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
        sendGameData();

        Log.info("PlayerCanRollDiceMessage", "Sending a PlayerCanRollDiceMessage @ startup. playerTurn = " + gameData.getCurrentPlayerTurnIndex());

        PlayerCanRollDiceMessage message = new PlayerCanRollDiceMessage(gameData.getCurrentPlayerTurnIndex());
        server.sendToAllTCP(message);

        setCurrentState(GameState.WAIT_FOR_DICE_RESULT);
    }

    public void sendPlayerCanRollDice() {
        //check if someone won
        if (checkForWinner()) {
            currentState = GameState.PLAYER_WON;
            resetGame();
        } else {
            if (getCurrentState() != GameState.PLAYER_CAN_ROLL_DICE) {
                Log.error("PlayerCanRollDiceMessage", "Trying to send CAN_ROLL_DICE but state is " + getCurrentState());
                return;
            }
            //clear old cheat history
            cheatHandler.clearHistory();
            Log.info("PlayerCanRollDiceMessage", "Sending a PlayerCanRollDiceMessage. playerTurn = " + gameData.getCurrentPlayerTurnIndex());

            PlayerCanRollDiceMessage message = new PlayerCanRollDiceMessage(gameData.getCurrentPlayerTurnIndex());
            server.sendToAllTCP(message);

            setCurrentState(GameState.WAIT_FOR_DICE_RESULT);
        }
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
            player.updateFieldServer(gameData.getFields()[currField.getFieldIndex()]);
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
                Log.info("movePlayer", "found jumpfield fieldIndex: " + jumpField.getJumpToField());
                player.updateFieldServer(gameData.getFields()[jumpField.getJumpToField()]);

                currentPlayerMoves.add(jumpField.getJumpToField());
            }

            // check for field action and pause the move
            GameState nextState = checkForFieldAction(player, currField);
            if (nextState != null) {
                server.sendToAllTCP(new PlayerMoves(currentPlayerMoves));
                setCurrentState(nextState);
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
        // buy lottery tickets when moving over LotteryField
        if (currField instanceof LotterieField && currentPlayerMovesLeft > 0) {
            int ticketPrice = ((LotterieField) currField).getTicketPrice();
            gameData.buyLotteryTickets(player.getPlayerIndex(), ticketPrice);
            server.sendToAllExceptTCP(player.getConnectionId(), new Notification("Player " + (player.getPlayerIndex() + 1) + " bought lottery tickets for: " + ticketPrice + "$"));
            server.sendToTCP(player.getConnectionId(), new Notification("You bought lottery tickets for: " + ticketPrice + "$"));
            sendGameData();
        } else if (currField instanceof MinigameField) {
            MinigameField minigameField = (MinigameField) currField;
            switch (minigameField.getMinigameType()) {
                case CASINO: {
                    return GameState.WAIT_SLOTS_INPUT;
                }
                case BOESE1: {
                    return GameState.TRICKY_ONE_WROS;
                }
                case AKTIEN_BOERSE: {

                    return GameState.WAIT_STOCK_ROLL;
                }
                case PFERDERENNEN: {
                    return GameState.HORSE_RACE;
                }
            }
        }
        return null;
    }

    /**
     * handle action according to state (can be of type for a specific minigame)
     */
    private void handleFieldAction() {
        switch (currentState) {
            case TRICKY_ONE_WROS: {
                trickyOneHandler.startGame();
                break;
            }
            case WAIT_SLOTS_INPUT: {
                slotHandler.startSlotsGame();
                break;
            }
            case HORSE_RACE: {
                //start horse race
                horseRaceHandler.start();
                break;
            }
            case WAIT_STOCK_ROLL: {
                stockHandler.startGame();
                break;
            }
            default: {
                Log.info("handleFieldAction", "there was no action specified for that state " + currentState.toString());
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
            Log.error("error getting intersection next: " + nextField + " opt: " + optNextField + " selcted: " + message.getFieldIndex());
        }
    }

    public void turnFinished() {

        handleFieldAction();
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
                // call the hotel handler and check if we need to wait for a decision by the player (buy hotel or not)
                boolean gotHandled = hotelHandler.handleHotelFieldAction(gameData.getCurrentPlayerTurnIndex(), hotelField.getFieldIndex());
                // if we have to wait for a decision, don't end the turn now
                if (gotHandled) {
                    return;
                }

            } else if (field instanceof LoseMoneyField) {
                LoseMoneyField loseMoneyField = (LoseMoneyField) field;
                player.loseMoney(loseMoneyField.getAmountMoney());
            } else if (field instanceof PayLotterieField) {
                PayLotterieField payLotterieField = (PayLotterieField) field;
                player.loseMoney(payLotterieField.getAmountToPay());
            } else if (field instanceof SpecialField) {
                SpecialField specialField = (SpecialField) field;
                handleSpecialField(specialField, player);
            } else if (field instanceof StockField) {
                StockField stockField = (StockField) field;
                player.buyStock(stockField.getStockType(), 1);
                player.loseMoney(stockField.getPrice());
            } else if (field instanceof LotterieField) {
                handleLotteryWin();
            }

            for (Player otherPlayer : gameData.getPlayers()) {
                if (otherPlayer.getPlayerIndex() != player.getPlayerIndex() && otherPlayer.getCurrentFieldIndex() == player.getCurrentFieldIndex()) {
                    player.payToPlayer(otherPlayer, 10000);
                    server.sendToAllExceptTCP(player.getConnectionId(), new Notification("Player " + (player.getPlayerIndex() + 1) + " paid Player" + (otherPlayer.getPlayerIndex() + 1) + " compensation"));
                    server.sendToTCP(player.getConnectionId(), new Notification("You paid Player " + (otherPlayer.getPlayerIndex() + 1) + " compensation"));
                }
            }

            // TODO rm : only for debug to see actual game state
            Log.info("turnFinished", "Field text: " + field.getText());
            for (Player pl : gameData.getPlayers()) {
                Log.info(pl.toString());
            }
            sendGameData();
            setNextPlayerTurn();
            setCurrentState(GameState.PLAYER_CAN_ROLL_DICE);
            sendPlayerCanRollDice();
        }
    }

    private void handleSpecialField(SpecialField specialField, Player player) {
        switch (specialField.getFieldIndex()) {
            case 1: { // Du würfelst einmal mit einem Würfel: Für eine 6 gibts 10,000
                Random r = new Random();
                if ((r.nextInt(6) + 1) == 6) {
                    player.addMoney(10000);
                    server.sendToAllExceptTCP(player.getConnectionId(), new Notification("Player " + (player.getPlayerIndex() + 1) + " rolled a 6 adding 10,000$"));
                    server.sendToTCP(player.getConnectionId(), new Notification("You rolled a 6 adding 10,000$"));
                }
                break;
            }
            case 6: { // Verwöhne einen Mitspieler mit 5,000
                // TODO: maybe add a dialog to let the current player choose the recipient instead of random
                List<Player> players = gameData.getPlayers();
                if (players.size() > 1) {
                    IntArray playerIndices = new IntArray();
                    for (int i = 0; i < players.size(); i++) {
                        if (players.get(i).getPlayerIndex() != player.getPlayerIndex()) {
                            playerIndices.add(i);
                        }
                    }
                    Player otherPlayer = players.get(playerIndices.random());
                    player.payToPlayer(otherPlayer, 5000);
                    server.sendToTCP(otherPlayer.getConnectionId(), new Notification("Player " + (player.getPlayerIndex() + 1) + " gifted you 5,000$"));
                    server.sendToTCP(player.getConnectionId(), new Notification("You gifted player " + (otherPlayer.getPlayerIndex() + 1) + " 5,000$"));
                }
                break;
            }
            case 8: { // Gib jedem Mitspieler 5,000 der etwas Blaues trägt
                for (Player pl : gameData.getPlayers()) {
                    if (player.getPlayerIndex() != pl.getPlayerIndex() && (Math.random()) < 0.33d) {// 1/3 chance for this one
                        player.payToPlayer(pl, 5000);
                        server.sendToTCP(pl.getConnectionId(), new Notification("Player " + (player.getPlayerIndex() + 1) + " gifted you 5,000$."));
                        server.sendToTCP(player.getConnectionId(), new Notification("You gifted 5,000$ to player " + (pl.getPlayerIndex() + 1) + "."));
                    }
                }
                break;
            }
            case 51: { // Du und ein Mitspieler würfeln je 1mal. Der höhere Wurf bekommt 50.000€ vom Anderen
                // TODO: maybe add a dialog to let the current player choose the recipient instead of random
                List<Player> players = gameData.getPlayers();
                if (players.size() > 1) {
                    IntArray playerIndices = new IntArray();
                    for (int i = 0; i < players.size(); i++) {
                        if (players.get(i).getPlayerIndex() != player.getPlayerIndex()) {
                            playerIndices.add(i);
                        }
                    }
                    if ((Math.random()) < 0.5d) { // 1/2 chance for this one
                        Player otherPlayer = players.get(playerIndices.random());
                        otherPlayer.payToPlayer(player, 50000);
                        server.sendToTCP(player.getConnectionId(), new Notification("Player " + (otherPlayer.getPlayerIndex() + 1) + " gifted you 50,000$"));
                        server.sendToTCP(otherPlayer.getConnectionId(), new Notification("You gifted player " + (player.getPlayerIndex() + 1) + " 50,000$"));
                    } else {
                        Player otherPlayer = players.get(playerIndices.random());
                        player.payToPlayer(otherPlayer, 50000);
                        server.sendToTCP(otherPlayer.getConnectionId(), new Notification("Player " + (player.getPlayerIndex() + 1) + " gifted you 50,000$"));
                        server.sendToTCP(player.getConnectionId(), new Notification("You gifted player " + (otherPlayer.getPlayerIndex() + 1) + " 50,000$"));
                    }
                }
                break;
            }
            case 67: { // Deine Freunde legen zusammen, damit du dich ordentlich einkleiden kannst. Jeder gibt 5.000€
                int amount = 0;
                for (Player pl : gameData.getPlayers()) {
                    if (player.getPlayerIndex() != pl.getPlayerIndex()) {
                        pl.payToPlayer(player, 5000);
                        amount += 5000;
                    }
                }
                server.sendToAllExceptTCP(player.getConnectionId(), new Notification("Player " + (player.getPlayerIndex() + 1) + " got " + amount + "$. You gave 5,000$"));
                server.sendToTCP(player.getConnectionId(), new Notification("You  got " + amount + "$ from other players."));
                break;
            }
            case 73: { // Gib alle Aktien and den Bankhalter zurück
                player.resetStocks();
                server.sendToAllExceptTCP(player.getConnectionId(), new Notification("Player " + (player.getPlayerIndex() + 1) + " had to give back all stocks to the bank."));
                server.sendToTCP(player.getConnectionId(), new Notification("You had to give back all stocks to the bank."));
                break;
            }
            default:
                throw new IllegalStateException("Unexpected value: " + specialField.getFieldIndex());
        }
    }

    public void sendGameData() {
        server.sendToAllTCP(new GameUpdate(gameData));
    }
}
