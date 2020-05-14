package com.mankomania.game.gamecore.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.mankomania.game.gamecore.MankomaniaGame;
import com.mankomania.game.gamecore.util.Screen;
import com.mankomania.game.gamecore.util.ScreenManager;

public class LoadingScreen extends AbstractScreen {

    public LoadingScreen(){




    }

    @Override
    public void show() {


    }
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1.0f,0.0f, 1.0f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if(MankomaniaGame.manager.update()) {
            ScreenManager.getInstance().switchScreen(Screen.LAUNCH);
        }

    }
    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
