package com.mankomania.game.gamecore.client;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Timer;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;
import com.mankomania.game.core.network.messages.ChatMessage;
import com.mankomania.game.core.network.messages.servertoclient.Notification;
import com.mankomania.game.core.network.messages.servertoclient.PlayerConnected;
import com.mankomania.game.core.network.messages.servertoclient.StartGame;
import com.mankomania.game.core.network.messages.servertoclient.baseturn.MovePlayerToFieldAfterIntersectionMessage;
import com.mankomania.game.core.network.messages.servertoclient.baseturn.MovePlayerToFieldMessage;
import com.mankomania.game.core.network.messages.servertoclient.baseturn.MovePlayerToIntersectionMessage;
import com.mankomania.game.core.network.messages.servertoclient.baseturn.PlayerCanRollDiceMessage;
import com.mankomania.game.core.network.messages.servertoclient.minigames.EndRouletteResultMessage;
import com.mankomania.game.core.network.messages.servertoclient.minigames.EndStockMessage;
import com.mankomania.game.core.network.messages.servertoclient.minigames.RouletteResultAllPlayer;
import com.mankomania.game.core.network.messages.servertoclient.stock.EndStockMessage;
import com.mankomania.game.core.network.messages.servertoclient.trickyone.CanRollDiceTrickyOne;
import com.mankomania.game.core.network.messages.servertoclient.trickyone.EndTrickyOne;
import com.mankomania.game.core.network.messages.servertoclient.trickyone.StartTrickyOne;
import com.mankomania.game.core.player.Player;
import com.mankomania.game.gamecore.MankomaniaGame;
import com.mankomania.game.gamecore.util.Screen;
import com.mankomania.game.gamecore.util.ScreenManager;
import com.mankomania.game.core.network.messages.servertoclient.minigames.StartRouletteServer;

/**
 * The listener class that handles all onReceived events of the network client.
 * Events are sorted and processed and redirected to the MessageHandler, that actually
 * manages the consequences of the messages on the gamestate and GameData.
 */
public class ClientListener extends Listener {
    private final MessageHandler messageHandler;
    private final RouletteClient rouletteClient = RouletteClient.getInstance(); // roulette minigame client

    public ClientListener(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    @Override
    public void received(Connection connection, Object object) {
        Log.info(object.getClass().getSimpleName());

        if (object instanceof PlayerConnected) {
            Log.info("player connected");
            Gdx.app.postRunnable(() -> ScreenManager.getInstance().switchScreen(Screen.LOBBY));

        } else if (object instanceof ChatMessage) {
            ChatMessage response = (ChatMessage) object;
            //chat will be updated if message received
            ClientChat.addText(response.text);

            Log.info("ChatMessage", "Received chat message (connection id: " + connection.getID() + "), text: '" + response.text + "'");
        } else if (object instanceof Notification) {
            Notification notification = (Notification) object;
            MankomaniaGame.getMankomaniaGame().getNotifier().add(notification);
            Log.info("Notification", "Received notification message (connection id: " + connection.getID() + "), text: '" + notification.getText() + "'");

        } else if (object instanceof StartGame) {
            /*
             * post a Runnable from networking thread to the libgdx rendering thread
             */
            Gdx.app.postRunnable(() -> ScreenManager.getInstance().switchScreen(Screen.MAIN_GAME));
            // once game starts each player gets a list from server
            // and creates a hashMap with the IDs and player objects
            StartGame gameStartedMessage = (StartGame) object;
            MankomaniaGame.getMankomaniaGame().getGameData().intPlayers(gameStartedMessage.getPlayers());
            Player player = null;
            for (Player pl : gameStartedMessage.getPlayers()) {
                if (pl.getConnectionId() == connection.getID()) {
                    player = pl;
                    break;
                }
            }
            MankomaniaGame.getMankomaniaGame().setLocalClientPlayer(player);

            Log.info("GameStartedMessage", "got GameStartedMessage, player array size: " + gameStartedMessage.getPlayers().size());
            Log.info("GameStartedMessage", "Initialized GameData with player id's");
        } else if (object instanceof PlayerCanRollDiceMessage) {
            PlayerCanRollDiceMessage playerCanRollDiceMessage = (PlayerCanRollDiceMessage) object;

            Log.info("PlayerCanRollDiceMessage", "Player " + playerCanRollDiceMessage.getPlayerIndex() + " can roll the dice now!");
            MankomaniaGame.getMankomaniaGame().setCurrentPlayerTurn(playerCanRollDiceMessage.getPlayerIndex());
            messageHandler.gotPlayerCanRollDiceMessage(playerCanRollDiceMessage);
        } else if (object instanceof MovePlayerToFieldMessage) {
            MovePlayerToFieldMessage movePlayerToFieldMessage = (MovePlayerToFieldMessage) object;

            Log.info("MovePlayerToFieldMessage", "Player " + movePlayerToFieldMessage.getPlayerIndex() + " got move to " + movePlayerToFieldMessage.getFieldToMoveTo() + " message");

            messageHandler.gotMoveToFieldMessage(movePlayerToFieldMessage);
        } else if (object instanceof MovePlayerToIntersectionMessage) {
            MovePlayerToIntersectionMessage mptim = (MovePlayerToIntersectionMessage) object;

            Log.info("MovePlayerToIntersectionMessage", "Player " + mptim.getPlayerIndex() + " got to move to field " +
                    mptim.getFieldIndex() + " and has to choose between path 1 = (" + mptim.getSelectionOption1() +
                    ") and path 2 = (" + mptim.getSelectionOption2() + ")");

            messageHandler.gotMoveToIntersectionMessage(mptim);
        } else if (object instanceof MovePlayerToFieldAfterIntersectionMessage) {
            MovePlayerToFieldAfterIntersectionMessage movePlayerAfterIntersectionMsg = (MovePlayerToFieldAfterIntersectionMessage) object;

            Log.info("MovePlayerToFieldAfterIntersectionMessage", "Player " + movePlayerAfterIntersectionMsg.getPlayerIndex() + " got to move on the field " +
                    movePlayerAfterIntersectionMsg.getFieldIndex() + " directly after the intersection.");

            messageHandler.gotMoveAfterIntersectionMessage(movePlayerAfterIntersectionMsg);
        } else if (object instanceof EndStockMessage) {
            EndStockMessage endStockMessage = (EndStockMessage) object;

            //TODO: startStock msg and switch Screen
            Log.info("[EndStockMessage] Player's money amount updated");
            //messageHandler.setMoneyAmountMessage(endStockMessage.setPlayerProfit(stockResultMessage.getPlayerId(),));
            messageHandler.gotEndStockMessage(endStockMessage);
        } else if (object instanceof StartTrickyOne) {
            StartTrickyOne startTrickyOne = (StartTrickyOne) object;
            Log.info("MiniGame TrickyOne", "Player " + startTrickyOne.getPlayerIndex() + " started TrickyOne");
            messageHandler.gotStartOfTrickyOne();
            Gdx.app.postRunnable(() -> ScreenManager.getInstance().switchScreen(Screen.TRICKY_ONE));

        } else if (object instanceof CanRollDiceTrickyOne) {
            CanRollDiceTrickyOne message = (CanRollDiceTrickyOne) object;
            Log.info("MiniGame TrickyOne", "Player " + message.getPlayerIndex() + " rolled: " + message.getFirstDice() + " " + message.getSecondDice() +
                    " Currently in Pot: " + message.getPot());
            messageHandler.gotTrickyOneCanRollDiceMessage(message);

        } else if (object instanceof EndTrickyOne) {
            EndTrickyOne endTrickyOne = (EndTrickyOne) object;
            Log.info("MiniGame TrickyOne", "Player " + endTrickyOne.getPlayerIndex() + " ended TrickyOne");
            messageHandler.gotEndTrickyOneMessage(endTrickyOne);
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    Gdx.app.postRunnable(() -> ScreenManager.getInstance().switchScreen(Screen.MAIN_GAME));
                }
            }, 3f);
        }

        //Roulette Minigame
        else if (object instanceof StartRouletteServer) {
            //client get message from server, that roulette has started
            StartRouletteServer startRouletteServer = (StartRouletteServer) object;
            Log.info("[StartRouletteServer] Roulette-Minigame: has started from " + startRouletteServer.getPlayerId());
            messageHandler.gotStartRouletteServer(startRouletteServer);

        } else if (object instanceof RouletteResultAllPlayer) {
            RouletteResultAllPlayer rouletteResultAllPlayer = (RouletteResultAllPlayer) object;
            Log.info("Received RouletteResultAllPlayerMessage Size = " + rouletteResultAllPlayer.getResults().size());
            MankomaniaGame.getMankomaniaGame().getGameData().setArrayPlayerInformation(rouletteResultAllPlayer.getResults());
            rouletteClient.receivedResults();
        }else if (object instanceof EndRouletteResultMessage){
            Log.info("[EndRouletteResultMessage] Player's money amount updated");
            messageHandler.endRouletteMessage((EndRouletteResultMessage) object);
        }


    }
}
