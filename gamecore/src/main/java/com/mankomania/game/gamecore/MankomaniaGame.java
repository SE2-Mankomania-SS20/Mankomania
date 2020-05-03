package com.mankomania.game.gamecore;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mankomania.game.gamecore.client.NetworkClient;
import com.mankomania.game.gamecore.util.Screen;
import com.mankomania.game.gamecore.util.ScreenManager;

import java.io.IOException;

public class MankomaniaGame extends Game {

    private SpriteBatch batch;
    private NetworkClient client;

    public NetworkClient getClient() {
        return client;
    }


    @Override
    public void create() {
        batch = new SpriteBatch();
        client = new NetworkClient();

        //Initialize game in screenManager and switch to first screen
        ScreenManager.getInstance().initialize(this);
        ScreenManager.getInstance().switchScreen(Screen.LAUNCH);
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
            e.printStackTrace();
        }
    }


}

