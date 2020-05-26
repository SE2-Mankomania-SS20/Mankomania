package com.mankomania.game.gamecore.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.mankomania.game.gamecore.MankomaniaGame;
import com.mankomania.game.gamecore.util.AssetDescriptors;
import com.mankomania.game.gamecore.util.Screen;
import com.mankomania.game.gamecore.util.ScreenManager;

public class LoadingScreen extends AbstractScreen {

    public LoadingScreen() {
        loadBoard();
        loadPlayer();
        loadFieldOverlay();
        loadFonts();
        loadHud();
        loadSkin();

    }

    public void loadBoard() {
        MankomaniaGame.getMankomaniaGame().getManager().load(AssetDescriptors.BOARD);
    }

    public void loadFieldOverlay() {
        MankomaniaGame.getMankomaniaGame().getManager().load(AssetDescriptors.BLUE);
        MankomaniaGame.getMankomaniaGame().getManager().load(AssetDescriptors.MAGENTA);
        MankomaniaGame.getMankomaniaGame().getManager().load(AssetDescriptors.ORANGE);
        MankomaniaGame.getMankomaniaGame().getManager().load(AssetDescriptors.WHITE);
        MankomaniaGame.getMankomaniaGame().getManager().load(AssetDescriptors.YELLOW);
        MankomaniaGame.getMankomaniaGame().getManager().load(AssetDescriptors.BORDER);
        MankomaniaGame.getMankomaniaGame().getManager().load(AssetDescriptors.BORDER_v2);
        MankomaniaGame.getMankomaniaGame().getManager().load(AssetDescriptors.FILLING);
        MankomaniaGame.getMankomaniaGame().getManager().load(AssetDescriptors.FILLING_v2);
        MankomaniaGame.getMankomaniaGame().getManager().load(AssetDescriptors.SELECTED_BORDER);
    }

    public void loadFonts() {
        MankomaniaGame.getMankomaniaGame().getManager().load(AssetDescriptors.BELEREN);
        MankomaniaGame.getMankomaniaGame().getManager().load(AssetDescriptors.BELEREN_SMALL);
    }

    public void loadHud() {
        MankomaniaGame.getMankomaniaGame().getManager().load(AssetDescriptors.BACK);
        MankomaniaGame.getMankomaniaGame().getManager().load(AssetDescriptors.CHAT);
        MankomaniaGame.getMankomaniaGame().getManager().load(AssetDescriptors.DICE);
        MankomaniaGame.getMankomaniaGame().getManager().load(AssetDescriptors.OPTIONS);
        MankomaniaGame.getMankomaniaGame().getManager().load(AssetDescriptors.OVERLAY);
    }

    public void loadPlayer() {
        MankomaniaGame.getMankomaniaGame().getManager().load(AssetDescriptors.PLAYER_BLUE);
        MankomaniaGame.getMankomaniaGame().getManager().load(AssetDescriptors.PLAYER_GREEN);
        MankomaniaGame.getMankomaniaGame().getManager().load(AssetDescriptors.PLAYER_RED);
        MankomaniaGame.getMankomaniaGame().getManager().load(AssetDescriptors.PLAYER_YELLOW);
    }

    public void loadSkin() {
        MankomaniaGame.getMankomaniaGame().getManager().load(AssetDescriptors.SKIN);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (MankomaniaGame.getMankomaniaGame().getManager().update()) {
            ScreenManager.getInstance().switchScreen(Screen.LAUNCH);
        }
        System.out.println(MankomaniaGame.getMankomaniaGame().getManager().getProgress() + "\n");

    }
}
