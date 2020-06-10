package com.mankomania.game.gamecore;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.mankomania.game.core.data.GameData;
import com.mankomania.game.core.player.Player;
import com.mankomania.game.gamecore.client.NetworkClient;
import com.mankomania.game.gamecore.notificationsystem.Notifier;
import com.mankomania.game.gamecore.notificationsystem.SpecialNotifier;
import com.mankomania.game.gamecore.util.Screen;
import com.mankomania.game.gamecore.util.ScreenManager;

public class MankomaniaGame extends Game {

    /**
     * Singleton of the game
     */
    private static MankomaniaGame mankomaniaGame;

    private NetworkClient networkClient;

    /**
     * this is the playerIndex of player that won the game
     */
    private int winnerIndex;

    /**
     * if someone won the game it is true
     */
    private boolean gameOver;

    /**
     * {@link GameData}
     */
    private GameData gameData;

    /**
     * this is the player owned by this gameclient
     */
    private Player localClientPlayer;

    /**
     * Notifier that can display notifications {@link Notifier}
     */
    private Notifier notifier;
    /**
     * Special notifier for displaying important messages
     */
    private SpecialNotifier specialNotifier;

    private AssetManager manager;

    private boolean camNeedsUpdate;
    private boolean turnFinishSend;

    private MankomaniaGame() {
        super();
    }

    public static MankomaniaGame getMankomaniaGame() {
        if (mankomaniaGame == null) {
            mankomaniaGame = new MankomaniaGame();
        }
        return mankomaniaGame;
    }

    public boolean isCamNeedsUpdate() {
        return camNeedsUpdate;
    }

    public void setCamNeedsUpdate(boolean camNeedsUpdate) {
        this.camNeedsUpdate = camNeedsUpdate;
    }

    public AssetManager getManager() {
        return manager;
    }

    public Player getLocalClientPlayer() {
        return localClientPlayer;
    }

    public void setLocalClientPlayer(Player localClientPlayer) {
        this.localClientPlayer = localClientPlayer;
    }

    public Notifier getNotifier() {
        return getMankomaniaGame().notifier;
    }

    public SpecialNotifier getSpecialNotifier() {
        return specialNotifier;
    }

    public NetworkClient getNetworkClient() {
        return getMankomaniaGame().networkClient;
    }

    public GameData getGameData() {
        return getMankomaniaGame().gameData;
    }

    public int getWinnerIndex() {
        return winnerIndex;
    }

    public void setWinnerIndex(int winnerIndex) {
        this.winnerIndex = winnerIndex;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public boolean isLocalPlayerTurn() {
        return localClientPlayer.getPlayerIndex() == gameData.getCurrentPlayerTurnIndex();
    }

    public boolean isTurnFinishSend() {
        return turnFinishSend;
    }

    public void setTurnFinishSend(boolean turnFinishSend) {
        this.turnFinishSend = turnFinishSend;
    }

    @Override
    public void create() {
        camNeedsUpdate = false;
        turnFinishSend = false;
        manager = new AssetManager();
        //Initialize game in screenManager and switch to first screen
        notifier = new Notifier();
        specialNotifier = new SpecialNotifier();
        specialNotifier.create();

        gameData = new GameData();
        networkClient = new NetworkClient();

        // load field data from json file
        gameData.loadData(Gdx.files.internal("data.json").read());

        //Initialize game in screenManager and switch to first screen
        ScreenManager.getInstance().initialize(this);
        ScreenManager.getInstance().switchScreen(Screen.LOADING);
    }

    @Override
    public void dispose() {
        notifier.dispose();
    }
}

