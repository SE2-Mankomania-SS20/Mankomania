package com.mankomania.game.gamecore.client;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Timer;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;
import com.mankomania.game.core.network.messages.ChatMessage;
import com.mankomania.game.core.network.messages.clienttoserver.baseturn.IntersectionSelectedMessage;
import com.mankomania.game.core.network.messages.clienttoserver.baseturn.SampleMinigame;
import com.mankomania.game.core.network.messages.servertoclient.GameUpdate;
import com.mankomania.game.core.network.messages.servertoclient.Notification;
import com.mankomania.game.core.network.messages.servertoclient.PlayerConnected;
import com.mankomania.game.core.network.messages.servertoclient.StartGame;
import com.mankomania.game.core.network.messages.servertoclient.baseturn.PlayerCanRollDiceMessage;
import com.mankomania.game.core.network.messages.servertoclient.stock.EndStockMessage;
import com.mankomania.game.core.network.messages.servertoclient.baseturn.PlayerMoves;
import com.mankomania.game.core.network.messages.servertoclient.trickyone.CanRollDiceTrickyOne;
import com.mankomania.game.core.network.messages.servertoclient.trickyone.EndTrickyOne;
import com.mankomania.game.core.network.messages.servertoclient.trickyone.StartTrickyOne;
import com.mankomania.game.core.player.Player;
import com.mankomania.game.gamecore.MankomaniaGame;
import com.mankomania.game.gamecore.util.Screen;
import com.mankomania.game.gamecore.util.ScreenManager;

/**
 * The listener class that handles all onReceived events of the network client.
 * Events are sorted and processed and redirected to the MessageHandler, that actually
 * manages the consequences of the messages on the gamestate and GameData.
 */
public class ClientListener extends Listener {
    private final MessageHandler messageHandler;

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
            Gdx.app.postRunnable(() -> ScreenManager.getInstance().switchScreen(Screen.MAIN_GAME));
            // once game starts each player gets a list from server
            // and creates a hashMap with the IDs and player objects
            StartGame gameStartedMessage = (StartGame) object;
            MankomaniaGame.getMankomaniaGame().getGameData().intPlayers(gameStartedMessage.getPlayers());
            for (Player pl : gameStartedMessage.getPlayers()) {
                if (pl.getConnectionId() == connection.getID()) {
                    MankomaniaGame.getMankomaniaGame().setLocalClientPlayer(pl);
                    break;
                }
            }

            Log.info("GameStartedMessage", "got GameStartedMessage, player array size: " + gameStartedMessage.getPlayers().size());

        } else if (object instanceof PlayerCanRollDiceMessage) {
            PlayerCanRollDiceMessage playerCanRollDiceMessage = (PlayerCanRollDiceMessage) object;

            Log.info("PlayerCanRollDiceMessage", "Player " + playerCanRollDiceMessage.getPlayerIndex() + " can roll the dice now!");
            MankomaniaGame.getMankomaniaGame().getGameData().setCurrentPlayerTurn(playerCanRollDiceMessage.getPlayerIndex());
            messageHandler.gotPlayerCanRollDiceMessage(playerCanRollDiceMessage);

        } else if (object instanceof PlayerMoves) {
            PlayerMoves playerMoves = (PlayerMoves) object;
            Log.info("PlayerMoves received :: " + playerMoves.getMoves().toString());
            messageHandler.playerMoves(playerMoves);
        } else if (object instanceof GameUpdate) {
            GameUpdate gameUpdate = (GameUpdate) object;
            Log.info("GameUpdate received");
            messageHandler.gameUpdate(gameUpdate);
        } else if (object instanceof IntersectionSelectedMessage) {
            MankomaniaGame.getMankomaniaGame().getNotifier().add(new Notification("Choose direction: PRESS I / O"));
        } else if (object instanceof SampleMinigame) {
            // SampleMinigame #101
            if (MankomaniaGame.getMankomaniaGame().isLocalPlayerTurn()) {
                // start minigame or so
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        messageHandler.sendSampleMinigame();
                        Log.info("SampleMinigame", "sending SampleMinigame back to server");
                    }
                }, 3f);
            }
        } else if(object instanceof EndStockMessage){
            EndStockMessage endStockMessage=(EndStockMessage) object;

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
    }

    @Override
    public void disconnected(Connection connection) {
        Gdx.app.postRunnable(() -> ScreenManager.getInstance().switchScreen(Screen.LAUNCH));
    }
}
