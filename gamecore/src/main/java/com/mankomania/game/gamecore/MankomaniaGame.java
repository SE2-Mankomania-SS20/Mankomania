package com.mankomania.game.gamecore;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.esotericsoftware.minlog.Log;
import com.mankomania.game.core.data.GameData;
import com.mankomania.game.gamecore.client.NetworkClient;
import com.mankomania.game.gamecore.notificationsystem.Notifier;
import com.mankomania.game.gamecore.util.Screen;
import com.mankomania.game.gamecore.util.ScreenManager;

import java.io.IOException;

public class MankomaniaGame extends Game {

    private SpriteBatch batch;
    private NetworkClient client;
    private GameData gameData;
    private Notifier notifier;

    public Notifier getNotifier() {
        return notifier;
    }


    public NetworkClient getClient() {
        return client;
    }

    public GameData getGameData() {
        return gameData;
    }

    @Override
    public void create() {

        //Initialize game in screenManager and switch to first screen
        notifier = new Notifier();
        ScreenManager.getInstance().initialize(this);
        ScreenManager.getInstance().switchScreen(Screen.LAUNCH, "");
        batch = new SpriteBatch();
        gameData = new GameData();
        client = new NetworkClient();

       // TODO: load somewhere else (care for double loading, if someone else is using this already)
        gameData.loadData(Gdx.files.internal("data.json").read());

    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
        try {
            client.dispose();
        } catch (IOException e) {
            Log.trace("Client dispose error: ",e);
        }
    }


}

