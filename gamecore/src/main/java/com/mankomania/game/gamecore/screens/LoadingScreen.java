package com.mankomania.game.gamecore.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.mankomania.game.gamecore.MankomaniaGame;
import com.mankomania.game.gamecore.util.AssetDescriptors;
import com.mankomania.game.gamecore.util.AssetPaths;
import com.mankomania.game.gamecore.util.Screen;
import com.mankomania.game.gamecore.util.ScreenManager;

public class LoadingScreen extends AbstractScreen {

    private Label loadingBar;
    private Table back;
    private Stage stage;

    public LoadingScreen() {
        loadSkin();//important to load first
        loadBoard();
        loadPlayer();
        loadFieldOverlay();
        loadFonts();
        loadHud();
        loadAktienBoerse();
        loadHotelRenderer();
        loadSlotMinigame();
        loadTrickyOneMiniGame();
    }

    public void loadSkin() {
        MankomaniaGame.getMankomaniaGame().getManager().load(AssetDescriptors.SKIN);
        MankomaniaGame.getMankomaniaGame().getManager().finishLoading();
        createLoadingBar();
        MankomaniaGame.getMankomaniaGame().getManager().load(AssetDescriptors.SKIN_2);
    }

    public void createLoadingBar() {
        Skin skin = MankomaniaGame.getMankomaniaGame().getManager().get(AssetPaths.SKIN);
        skin.getFont("font").getData().setScale(4, 4);
        stage = new Stage();
        back = new Table();
        back.setFillParent(true);
        back.setBackground(new TiledDrawable(skin.getTiledDrawable("tile-a")));
        loadingBar = new Label("Loading", skin, "chat");
        loadingBar.setPosition(Gdx.graphics.getWidth() / 2f - 200, Gdx.graphics.getHeight() / 2f - 50);
        stage.addActor(back);
        stage.addActor(loadingBar);
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
        MankomaniaGame.getMankomaniaGame().getManager().load(AssetDescriptors.AKTIENTABLE);
        MankomaniaGame.getMankomaniaGame().getManager().load(AssetDescriptors.MONEYTABLE);
        MankomaniaGame.getMankomaniaGame().getManager().load(AssetDescriptors.HUD_BUTTON_IMAGE);
        MankomaniaGame.getMankomaniaGame().getManager().load(AssetDescriptors.BACK_BUTTON_IMAGE);
        MankomaniaGame.getMankomaniaGame().getManager().load(AssetDescriptors.FIELD_IMAGE);
        MankomaniaGame.getMankomaniaGame().getManager().load(AssetDescriptors.DICE_IMAGE);
        MankomaniaGame.getMankomaniaGame().getManager().load(AssetDescriptors.CHAT_IMAGE);
        MankomaniaGame.getMankomaniaGame().getManager().load(AssetDescriptors.DICE_OVERLAY);
        MankomaniaGame.getMankomaniaGame().getManager().load(AssetDescriptors.ARROW_RIGHT);
        MankomaniaGame.getMankomaniaGame().getManager().load(AssetDescriptors.ARROW_LEFT);
    }

    public void loadPlayer() {
        MankomaniaGame.getMankomaniaGame().getManager().load(AssetDescriptors.PLAYER_BLUE);
        MankomaniaGame.getMankomaniaGame().getManager().load(AssetDescriptors.PLAYER_GREEN);
        MankomaniaGame.getMankomaniaGame().getManager().load(AssetDescriptors.PLAYER_RED);
        MankomaniaGame.getMankomaniaGame().getManager().load(AssetDescriptors.PLAYER_YELLOW);
    }

    public void loadAktienBoerse() {
        MankomaniaGame.getMankomaniaGame().getManager().load(AssetDescriptors.GEWONNEN);
        MankomaniaGame.getMankomaniaGame().getManager().load(AssetDescriptors.GEWONNENT);
        MankomaniaGame.getMankomaniaGame().getManager().load(AssetDescriptors.GEWONNENK);
        MankomaniaGame.getMankomaniaGame().getManager().load(AssetDescriptors.GEWONNENB);
        MankomaniaGame.getMankomaniaGame().getManager().load(AssetDescriptors.VERLORENB);
        MankomaniaGame.getMankomaniaGame().getManager().load(AssetDescriptors.VERLORENK);
        MankomaniaGame.getMankomaniaGame().getManager().load(AssetDescriptors.VERLORENT);
    }

    public void loadHotelRenderer() {
        MankomaniaGame.getMankomaniaGame().getManager().load(AssetDescriptors.HOTEL_MODEL);
        MankomaniaGame.getMankomaniaGame().getManager().load(AssetDescriptors.HOTEL_FLAG_BLUE);
        MankomaniaGame.getMankomaniaGame().getManager().load(AssetDescriptors.HOTEL_FLAG_GREEN);
        MankomaniaGame.getMankomaniaGame().getManager().load(AssetDescriptors.HOTEL_FLAG_RED);
        MankomaniaGame.getMankomaniaGame().getManager().load(AssetDescriptors.HOTEL_FLAG_YELLOW);
    }

    public void loadSlotMinigame() {
        MankomaniaGame.getMankomaniaGame().getManager().load(AssetDescriptors.SLOT_ICONS);
        MankomaniaGame.getMankomaniaGame().getManager().load(AssetDescriptors.SLOT_BACKGROUND);
    }

    public void loadTrickyOneMiniGame() {
        MankomaniaGame.getMankomaniaGame().getManager().load(AssetDescriptors.DICE_ONE);
        MankomaniaGame.getMankomaniaGame().getManager().load(AssetDescriptors.DICE_TWO);
        MankomaniaGame.getMankomaniaGame().getManager().load(AssetDescriptors.DICE_THREE);
        MankomaniaGame.getMankomaniaGame().getManager().load(AssetDescriptors.DICE_FOUR);
        MankomaniaGame.getMankomaniaGame().getManager().load(AssetDescriptors.DICE_FIVE);
        MankomaniaGame.getMankomaniaGame().getManager().load(AssetDescriptors.DICE_SIX);
        MankomaniaGame.getMankomaniaGame().getManager().load(AssetDescriptors.TRICKY_ONE_BACK);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();

        if (MankomaniaGame.getMankomaniaGame().getManager().update()) {
            ScreenManager.getInstance().switchScreen(Screen.LAUNCH);
        }
        float progress = MankomaniaGame.getMankomaniaGame().getManager().getProgress() * 100;
        loadingBar.setText((int) progress + " %");
    }
}
