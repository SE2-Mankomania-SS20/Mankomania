package com.mankomania.game.gamecore;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mankomania.game.core.data.GameData;
import com.mankomania.game.core.player.Player;
import com.mankomania.game.gamecore.client.NetworkClient;
import com.mankomania.game.gamecore.notificationsystem.Notifier;
import com.mankomania.game.gamecore.util.Screen;
import com.mankomania.game.gamecore.util.ScreenManager;

public class MankomaniaGame extends Game {

    private static MankomaniaGame mankomaniaGame;

    private SpriteBatch batch;
    private NetworkClient client;
    private GameData gameData;

    private Player localClientPlayer;

    private int currentPlayerTurn;

    private Notifier notifier;

    private MankomaniaGame(){
        super();
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

    public static MankomaniaGame getMankomaniaGame() {
        if(mankomaniaGame == null){
            mankomaniaGame = new MankomaniaGame();
        }
        return mankomaniaGame;
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
        //Initialize game in screenManager and switch to first screen
        notifier = new Notifier();

        batch = new SpriteBatch();
        gameData = new GameData();
        client = new NetworkClient();

        // load field data from json file
       gameData.loadData(Gdx.files.internal("data.json").read());

        //Initialize game in screenManager and switch to first screen
        ScreenManager.getInstance().initialize(this);
        ScreenManager.getInstance().switchScreen(Screen.LAUNCH, "");
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}

