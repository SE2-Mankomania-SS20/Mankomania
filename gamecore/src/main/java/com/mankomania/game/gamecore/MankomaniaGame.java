package com.mankomania.game.gamecore;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mankomania.game.gamecore.client.NetworkClient;
import com.mankomania.game.gamecore.screens.LaunchScreen;

public class MankomaniaGame extends Game {
	public SpriteBatch batch;
	NetworkClient client;
	Game game;

	@Override
	public void create() {
		batch = new SpriteBatch();
		client = new NetworkClient();
		setScreen(new LaunchScreen(this));
	}

	@Override
	public void render() {

		super.render();
	}

	@Override
	public void dispose() {
		batch.dispose();
	}

}

