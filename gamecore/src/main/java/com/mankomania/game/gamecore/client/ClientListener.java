package com.mankomania.game.gamecore.client;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Timer;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.FrameworkMessage;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;
import com.mankomania.game.core.network.messages.ChatMessage;
import com.mankomania.game.core.network.messages.clienttoserver.baseturn.IntersectionSelection;
import com.mankomania.game.core.network.messages.servertoclient.GameUpdate;
import com.mankomania.game.core.network.messages.servertoclient.Notification;
import com.mankomania.game.core.network.messages.servertoclient.PlayerConnected;
import com.mankomania.game.core.network.messages.servertoclient.StartGame;
import com.mankomania.game.core.network.messages.servertoclient.baseturn.PlayerCanRollDiceMessage;
import com.mankomania.game.core.network.messages.servertoclient.roulette.StartRouletteServer;
import com.mankomania.game.core.network.messages.servertoclient.stock.EndStockMessage;
import com.mankomania.game.core.network.messages.servertoclient.baseturn.PlayerMoves;
import com.mankomania.game.core.network.messages.servertoclient.roulette.RouletteResultAllPlayer;
import com.mankomania.game.core.network.messages.servertoclient.trickyone.CanRollDiceTrickyOne;
import com.mankomania.game.core.network.messages.servertoclient.trickyone.EndTrickyOne;
import com.mankomania.game.core.network.messages.servertoclient.trickyone.StartTrickyOne;
import com.mankomania.game.core.network.messages.servertoclient.hotel.PlayerBoughtHotelMessage;
import com.mankomania.game.core.network.messages.servertoclient.hotel.PlayerCanBuyHotelMessage;
import com.mankomania.game.core.network.messages.servertoclient.hotel.PlayerPaysHotelRentMessage;
import com.mankomania.game.core.player.Player;
import com.mankomania.game.gamecore.MankomaniaGame;
import com.mankomania.game.gamecore.screens.RouletteMiniGameScreen;
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
        if (!(object instanceof FrameworkMessage)) {
            Log.info(object.getClass().getSimpleName());
        }

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
            MankomaniaGame.getMankomaniaGame().setCamNeedsUpdate(true);
        } else if (object instanceof PlayerMoves) {
            PlayerMoves playerMoves = (PlayerMoves) object;
            Log.info("PlayerMoves received :: " + playerMoves.getMoves().toString());
            MankomaniaGame.getMankomaniaGame().setTurnFinishSend(false);
            messageHandler.playerMoves(playerMoves);
        } else if (object instanceof GameUpdate) {
            GameUpdate gameUpdate = (GameUpdate) object;
            Log.info("GameUpdate received");
            messageHandler.gameUpdate(gameUpdate);
        } else if (object instanceof IntersectionSelection) {
            MankomaniaGame.getMankomaniaGame().getNotifier().add(new Notification("Choose direction: PRESS I / O"));
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
        } else if (object instanceof PlayerCanBuyHotelMessage) {
            PlayerCanBuyHotelMessage canBuyHotelMessage = (PlayerCanBuyHotelMessage) object;

            messageHandler.gotPlayerCanBuyHotelMessage(canBuyHotelMessage);
        } else if (object instanceof PlayerBoughtHotelMessage) {
            PlayerBoughtHotelMessage boughtHotelMessage = (PlayerBoughtHotelMessage) object;

            messageHandler.gotPlayerBoughtHotelMessage(boughtHotelMessage);
        } else if (object instanceof PlayerPaysHotelRentMessage) {
            PlayerPaysHotelRentMessage paysHotelRentMessage = (PlayerPaysHotelRentMessage) object;

            messageHandler.gotPlayerPayHotelRentMessage(paysHotelRentMessage);
        }

        //Roulette Minigame
        else if (object instanceof StartRouletteServer) {
            //client get message from server, that roulette has started
            StartRouletteServer startRouletteServer = (StartRouletteServer) object;
            Log.info("[StartRouletteServer] Roulette-Minigame: has started from " + startRouletteServer.getPlayerIndex());
            messageHandler.gotStartRouletteServer(startRouletteServer);

        } else if (object instanceof RouletteResultAllPlayer) {
            RouletteResultAllPlayer rouletteResultAllPlayer = (RouletteResultAllPlayer) object;
            Log.info("Received RouletteResultAllPlayerMessage Size = " + rouletteResultAllPlayer.getResults().size());
            MankomaniaGame.getMankomaniaGame().getGameData().setArrayPlayerInformation(rouletteResultAllPlayer.getResults());
            RouletteMiniGameScreen.getInstance().updateUI();
        }


    }

    @Override
    public void disconnected(Connection connection) {
        Gdx.app.postRunnable(() -> ScreenManager.getInstance().switchScreen(Screen.LAUNCH));
    }
}
