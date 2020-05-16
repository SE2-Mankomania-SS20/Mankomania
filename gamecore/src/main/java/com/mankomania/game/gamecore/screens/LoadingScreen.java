package com.mankomania.game.gamecore.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.mankomania.game.gamecore.MankomaniaGame;
import com.mankomania.game.gamecore.util.AssetDescriptors;
import com.mankomania.game.gamecore.util.Screen;
import com.mankomania.game.gamecore.util.ScreenManager;

public class LoadingScreen extends AbstractScreen {

    public LoadingScreen(){
        loadBoard();
        loadFieldOverlay();
        loadFonts();
        loadHud();
        loadPlayer();
        loadSkin();

    }
    public void loadBoard(){
        MankomaniaGame.manager.load(AssetDescriptors.BOARD);
    }
    public void loadFieldOverlay(){
        MankomaniaGame.manager.load(AssetDescriptors.EMPTY);
        MankomaniaGame.manager.load(AssetDescriptors.BLUE);
        MankomaniaGame.manager.load(AssetDescriptors.MAGENTA);
        MankomaniaGame.manager.load(AssetDescriptors.ORANGE);
        MankomaniaGame.manager.load(AssetDescriptors.WHITE);
        MankomaniaGame.manager.load(AssetDescriptors.YELLOW);
        MankomaniaGame.manager.load(AssetDescriptors.BORDER);
        MankomaniaGame.manager.load(AssetDescriptors.BORDER_v2);
        MankomaniaGame.manager.load(AssetDescriptors.FILLING);
        MankomaniaGame.manager.load(AssetDescriptors.FILLING_v2);
        MankomaniaGame.manager.load(AssetDescriptors.SELECTED_BORDER);
    }
    public void loadFonts() {
        MankomaniaGame.manager.load(AssetDescriptors.BELEREN);
        MankomaniaGame.manager.load(AssetDescriptors.BELEREN_SMALL);
    }
    public void loadHud() {
        MankomaniaGame.manager.load(AssetDescriptors.BACK);
        MankomaniaGame.manager.load(AssetDescriptors.CHAT);
        MankomaniaGame.manager.load(AssetDescriptors.DICE);
        MankomaniaGame.manager.load(AssetDescriptors.OPTIONS);
        MankomaniaGame.manager.load(AssetDescriptors.OVERLAY);
    }
    public void loadPlayer(){
        MankomaniaGame.manager.load(AssetDescriptors.PLAYER_BLUE);
        MankomaniaGame.manager.load(AssetDescriptors.PLAYER_GREEN);
        MankomaniaGame.manager.load(AssetDescriptors.PLAYER_RED);
        MankomaniaGame.manager.load(AssetDescriptors.PLAYER_YELLOW);
    }
    public void loadSkin(){
        MankomaniaGame.manager.load(AssetDescriptors.BUTTON1);
        MankomaniaGame.manager.load(AssetDescriptors.MANKOMANIA);
        MankomaniaGame.manager.load(AssetDescriptors.BACKGROUND);
    }

    @Override
    public void show() {


    }
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1,1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if(MankomaniaGame.manager.update()) {
            ScreenManager.getInstance().switchScreen(Screen.LAUNCH);
        }

    }

    @Override
    public void dispose() {

    }
}
