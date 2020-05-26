package com.mankomania.game.gamecore;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.mankomania.game.core.data.GameData;
import com.mankomania.game.core.player.Player;
import com.mankomania.game.gamecore.client.NetworkClient;
import com.mankomania.game.gamecore.notificationsystem.Notifier;
import com.mankomania.game.gamecore.util.Screen;
import com.mankomania.game.gamecore.util.ScreenManager;

public class MankomaniaGame extends Game {

    /**
     * Singleton of the game
     */
    private static MankomaniaGame mankomaniaGame;

    private NetworkClient client;


    /**
     * {@link GameData}
     */
    private GameData gameData;

    /**
     * this is the player owned by this gameclient
     */
    private Player localClientPlayer;

    /**
     * playerIndex from players array in gamedata tha is currently at turn
     */
    private int currentPlayerTurn;

    /**
     * Notifier that can display notifications {@link Notifier}
     */
    private Notifier notifier;
    private AssetManager manager;

    private MankomaniaGame() {
        super();
        currentPlayerTurn = -1;
    }

    public static MankomaniaGame getMankomaniaGame() {
        if (mankomaniaGame == null) {
            mankomaniaGame = new MankomaniaGame();
        }
        return mankomaniaGame;
    }

    public AssetManager getManager() {
        return manager;
    }

    public int getCurrentPlayerTurn() {
        return currentPlayerTurn;
    }

    public void setCurrentPlayerTurn(int currentPlayerTurn) {
        this.currentPlayerTurn = currentPlayerTurn;
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

    public NetworkClient getClient() {
        return getMankomaniaGame().client;
    }

    public GameData getGameData() {
        return getMankomaniaGame().gameData;
    }

    @Override
    public void create() {

        manager = new AssetManager();
        //Initialize game in screenManager and switch to first screen
        notifier = new Notifier();

        gameData = new GameData();
        client = new NetworkClient();

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

