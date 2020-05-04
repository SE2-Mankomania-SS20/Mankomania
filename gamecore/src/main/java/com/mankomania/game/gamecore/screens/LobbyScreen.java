package com.mankomania.game.gamecore.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.badlogic.gdx.utils.Align;
import com.mankomania.game.core.network.GameState;
import com.mankomania.game.gamecore.util.ScreenEnum;
import com.mankomania.game.gamecore.util.ScreenManager;


public class LobbyScreen extends AbstractScreen {

    private Stage stage;
    private Table table;
    private SpriteBatch batch;
    private Sprite sprite;

    public LobbyScreen() {


        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        table = new Table();
        table.setFillParent(true);
        table.setWidth(stage.getWidth());
        table.align(Align.center | Align.top);

    }

    @Override
    public void show() {
        Skin skin = new Skin(Gdx.files.internal("skin/terra-mother-ui.json"));
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
                //send state to server
                GameState state = new GameState(true);
                ScreenManager.getInstance().getGame().getClient().sendClientState(state);
            }
        });

        back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                com.mankomania.game.gamecore.util.ScreenManager.getInstance().switchScreen(com.mankomania.game.gamecore.util.ScreenEnum.LAUNCH);
            }
        });
        chat.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                com.mankomania.game.gamecore.util.ScreenManager.getInstance().switchScreen(ScreenEnum.CHAT,
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

    }
}
