package com.mankomania.game.gamecore.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;

import java.awt.*;

/*********************************
 Created by Fabian Oraze on 27.04.20
 *********************************/


/**
 * Super class from which every Screen will extend
 */
public abstract class AbstractScreen extends ScreenAdapter {


    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    }

}
