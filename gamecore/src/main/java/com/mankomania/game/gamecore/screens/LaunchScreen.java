package com.mankomania.game.gamecore.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.badlogic.gdx.utils.Align;
import com.mankomania.game.gamecore.MankomaniaGame;

public class LaunchScreen extends AbstractScreen {
    private Stage stage;
    private Table table;
    private Label errLabel;

    public LaunchScreen(String errMsg) {
        Skin skin = new Skin(Gdx.files.internal("skin/terra-mother-ui.json"));
        Texture texture = new Texture(Gdx.files.internal("mankomania.png"));
        Image image = new Image(texture);
        image.setSize(400, 400);
        stage = new Stage();
        table = new Table();
        table.setFillParent(true);
        table.setBackground(new TiledDrawable(skin.getTiledDrawable("tile-a")));
        table.setWidth(stage.getWidth());
        table.align(Align.center | Align.top);

        Gdx.input.setInputProcessor(stage);
        skin.getFont("font").getData().setScale(5, 5);

        TextButton btn1 = new TextButton("JOIN LOBBY", skin, "default");
        TextButton btn2 = new TextButton("QUIT", skin, "default");

        errLabel = new Label(errMsg, skin, "black");

        btn1.addListener(new ClickListener() {
            @Override

            public void clicked(InputEvent event, float x, float y) {
                //at this point client should try to connect to server
                MankomaniaGame.getMankomaniaGame().getClient().tryConnectClient();
            }

        });

        btn2.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }

        });

        table.padTop(50);
        table.add(image).width(Gdx.graphics.getWidth() - 150f).height(376f);
        table.row();
        table.add(btn1).padBottom(50).width(Gdx.graphics.getWidth() / 2f).height(100f);
        table.row();
        table.add(btn2).padBottom(50).width(Gdx.graphics.getWidth() / 2f).height(100f);
        table.row();
        table.add(errLabel).padBottom(50).width(Gdx.graphics.getWidth() / 2f).height(100f);

        stage.addActor(table);
    }

    public void setErrorText(String text) {
        errLabel.setText(text);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        stage.act(delta);
        stage.draw();
        super.renderNotifications(delta);
    }
}
