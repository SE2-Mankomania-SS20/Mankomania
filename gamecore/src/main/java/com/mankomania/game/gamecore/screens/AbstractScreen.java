package com.mankomania.game.gamecore.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mankomania.game.gamecore.MankomaniaGame;

/*
 Created by Fabian Oraze on 27.04.20
 */


/**
 * Super class from which every Screen will extend
 */
public abstract class AbstractScreen extends ScreenAdapter {
    private SpriteBatch specialNotifierBatch;

    public AbstractScreen() {
        specialNotifierBatch = new SpriteBatch();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // enabling blending, so transparency can be used (batch.setAlpha(x))
        specialNotifierBatch.enableBlending();

    }

    public void renderNotifications(float delta) {
        MankomaniaGame.getMankomaniaGame().getNotifier().render(delta);
        specialNotifierBatch.begin();
        MankomaniaGame.getMankomaniaGame().getSpecialNotifier().render(specialNotifierBatch);
        specialNotifierBatch.end();
    }
}
