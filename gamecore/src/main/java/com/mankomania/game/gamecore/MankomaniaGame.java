package com.mankomania.game.gamecore;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mankomania.game.gamecore.client.NetworkClient;
import com.mankomania.game.gamecore.screens.MainGameScreen;

public class MankomaniaGame extends Game {
	SpriteBatch batch;
	Texture img;
	NetworkClient client;
	Game game;

	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
		client = new NetworkClient();
		setScreen(new MainGameScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
