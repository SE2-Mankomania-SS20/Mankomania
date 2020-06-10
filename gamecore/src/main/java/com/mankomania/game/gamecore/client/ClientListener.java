package com.mankomania.game.gamecore.client;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Timer;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.FrameworkMessage;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;
import com.mankomania.game.core.network.messages.ChatMessage;
import com.mankomania.game.core.network.messages.clienttoserver.baseturn.IntersectionSelection;
import com.mankomania.game.core.network.messages.servertoclient.*;
import com.mankomania.game.core.network.messages.servertoclient.baseturn.PlayerCanRollDiceMessage;
import com.mankomania.game.core.network.messages.servertoclient.horserace.HorseRaceStart;
import com.mankomania.game.core.network.messages.servertoclient.horserace.HorseRaceUpdate;
import com.mankomania.game.core.network.messages.servertoclient.horserace.HorseRaceWinner;
import com.mankomania.game.core.network.messages.servertoclient.roulette.StartRouletteServer;
import com.mankomania.game.core.network.messages.servertoclient.slots.*;
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

import java.util.Arrays;

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
            ClientChat.addText(response.getText());

            Log.info("ChatMessage", "Received chat message (connection id: " + connection.getID() + "), text: '" + response.getText() + "'");
        } else if (object instanceof Notification) {
            Notification notification = (Notification) object;
            MankomaniaGame.getMankomaniaGame().getNotifier().add(notification);
            Log.info("Notification", "Received notification message (connection id: " + connection.getID() + "), text: '" + notification.getText() + "'");
        } else if (object instanceof SpecialNotification) {
            SpecialNotification specialNotification = (SpecialNotification) object;
            Log.info("Notification", "Received special notification, text: " + specialNotification.getTextToShow());

            MankomaniaGame.getMankomaniaGame().getSpecialNotifier().setCurrentText(specialNotification.getTextToShow());
            MankomaniaGame.getMankomaniaGame().getSpecialNotifier().show();
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
            MankomaniaGame.getMankomaniaGame().getGameData().setOnIntersection(true);
        } else if (object instanceof EndStockMessage) {
            EndStockMessage endStockMessage = (EndStockMessage) object;

            //TODO: startStock msg and switch Screen
            Log.info("[EndStockMessage] Player's money amount updated");
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
            messageHandler.gotEndTrickyOneMessage();
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
            Gdx.app.postRunnable(new Timer.Task() {
                @Override
                public void run() {
                    RouletteMiniGameScreen.getInstance().updateUI();
                }
            });
        } else if (object instanceof HorseRaceStart) {
            HorseRaceStart hrs = (HorseRaceStart) object;
            Log.info("HorseRaceStart");
            MankomaniaGame.getMankomaniaGame().getGameData().getHorseRaceData().reset();
            Gdx.app.postRunnable(() -> ScreenManager.getInstance().switchScreen(Screen.HORSE_RACE));
            MankomaniaGame.getMankomaniaGame().getGameData().getHorseRaceData().setCurrentPlayerIndex(hrs.getCurrentPlayerIndex());
            MankomaniaGame.getMankomaniaGame().getGameData().getHorseRaceData().setHasUpdate(true);
        } else if (object instanceof HorseRaceWinner) {
            HorseRaceWinner hrw = (HorseRaceWinner) object;
            Log.info("HorseRaceWinner");
                MankomaniaGame.getMankomaniaGame().getGameData().getHorseRaceData().setWinner(hrw.getHorseIndex());
                MankomaniaGame.getMankomaniaGame().getGameData().getHorseRaceData().setHasUpdate(true);
                Gdx.app.postRunnable(() -> Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        ScreenManager.getInstance().switchScreen(Screen.MAIN_GAME);
                    }
                },5f));

        } else if (object instanceof HorseRaceUpdate) {
            HorseRaceUpdate hru = (HorseRaceUpdate) object;
            Log.info("HorseRaceUpdate");
            MankomaniaGame.getMankomaniaGame().getGameData().getHorseRaceData().updateHorseRacePlayerInfo(hru.getHorseRacePlayerInfos());
            MankomaniaGame.getMankomaniaGame().getGameData().getHorseRaceData().setCurrentPlayerIndex(hru.getCurrentPlayerIndex());
            MankomaniaGame.getMankomaniaGame().getGameData().getHorseRaceData().setHasUpdate(true);
        }
        // slots minigame
        else if (object instanceof StartSlotsMessage) {
            Log.info("StartSlotsMessage", "Got StartSlotsMessage. Switching to SlotsScreen.");
            messageHandler.gotStartSlotsMessage();
        } else if (object instanceof SlotResultMessage) {
            SlotResultMessage slotResultMessage = (SlotResultMessage) object;
            Log.info("SlotResultMessage", "Got SlotResultMessage for player index " + slotResultMessage.getPlayerIndex() +
                    ". Rolled results are: "  + Arrays.toString(slotResultMessage.getRollResult()) + ". Win amount: " + slotResultMessage.getWinAmount());

            messageHandler.gotSlotResultMessage(slotResultMessage);
        }
        //player won game
        else if (object instanceof PlayerWon) {
            PlayerWon playerWon = (PlayerWon) object;
            MankomaniaGame.getMankomaniaGame().setWinnerIndex(playerWon.getPlayerIndex());
            MankomaniaGame.getMankomaniaGame().setGameOver(true);
            Log.info("Received PlayerWon message --> switching to EndOverlay");
        }
    }

    @Override
    public void disconnected(Connection connection) {
        Gdx.app.postRunnable(() -> ScreenManager.getInstance().switchScreen(Screen.LAUNCH));
    }
}
