package com.mankomania.game.gamecore.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mankomania.game.gamecore.screens.LaunchScreen;

public class MainGame extends Game {
    public SpriteBatch batcher;
    public BitmapFont font;

    @Override
    public void create () {
        batcher = new SpriteBatch();
        font = new BitmapFont();
        font.getData().setScale(4,4);

        this.setScreen(new LaunchScreen(this));
    }

    @Override
    public void render () {
        super.render();
    }

    @Override
    public void dispose () {

    }
}