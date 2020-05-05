package com.mankomania.game.gamecore.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.badlogic.gdx.utils.Align;
import com.mankomania.game.gamecore.util.ScreenEnum;
import com.mankomania.game.gamecore.util.ScreenManager;
import com.mankomania.game.gamecore.util.AssetDescriptors;


public class LobbyScreen extends AbstractScreen {
    private AssetManager manager;
    private Stage stage;
    private Table table;
    private SpriteBatch batch;
    private Sprite sprite;

    public LobbyScreen() {

        manager = new AssetManager();

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        table = new Table();
        table.setFillParent(true);
        table.setWidth(stage.getWidth());
        table.align(Align.center | Align.top);

    }

    @Override
    public void show() {

        manager.load(AssetDescriptors.BACKGROUND);
        manager.finishLoading();

        Skin skin = manager.get(AssetDescriptors.BACKGROUND);
        table.setBackground(new TiledDrawable(skin.getTiledDrawable("tile-a")));
        skin.getFont("font").getData().setScale(5, 5);

        TextButton play = new TextButton("PLAY", skin, "default");
        TextButton back = new TextButton("BACK", skin, "default");
        TextButton chat = new TextButton("CHAT", skin, "default");

        table.padTop(300);
        table.add(play).padBottom(50).width(Gdx.graphics.getWidth() / 2).height(100);
        table.row().pad(10, 0, 10, 0);
        table.add(back).padBottom(50).width(Gdx.graphics.getWidth() / 2).height(100);
        table.row();
        table.add(chat).padBottom(50).width(Gdx.graphics.getWidth() / 2).height(100);

        play.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ScreenManager.getInstance().switchScreen(ScreenEnum.MAIN_GAME);
            }
        });

        back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ScreenManager.getInstance().switchScreen(ScreenEnum.LAUNCH);
            }
        });
        chat.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ScreenManager.getInstance().switchScreen(ScreenEnum.CHAT,
                        ScreenManager.getInstance().getGame().getClient());
            }
        });

        stage.addActor(table);
    }


    @Override
    public void render(float delta) {
        super.render(delta);
        stage.act(delta);
        stage.draw();
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
        manager.dispose();

    }
}
