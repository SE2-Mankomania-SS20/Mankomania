package com.mankomania.game.gamecore.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.badlogic.gdx.utils.Align;
import com.mankomania.game.core.network.messages.servertoclient.Notification;
import com.mankomania.game.gamecore.MankomaniaGame;
import com.mankomania.game.gamecore.util.AssetPaths;

public class HorseRaceScreen extends AbstractScreen {
    private Stage stage;
    private Skin skin;
    private Table background;

    private static final String SKIN_TYPE = "black";

    public HorseRaceScreen() {
        stage = new Stage();

        skin = MankomaniaGame.getMankomaniaGame().getManager().get(AssetPaths.SKIN);
        skin.getFont("font").getData().setScale(3, 3);
        skin.getFont("info").getData().setScale(2.5f, 2.5f);

        background = new Table();
        background.setDebug(true);
        background.setFillParent(true);
        background.setBackground(new TiledDrawable(skin.getTiledDrawable("tile-a")));

        TextButton textButton = new TextButton("Blitz", skin, "default");
        textButton.setPosition(Gdx.graphics.getWidth() * 0.04f, (float) Gdx.graphics.getHeight() * 0.82f);
        textButton.align(Align.center);
        textButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                MankomaniaGame.getMankomaniaGame().getNotifier().add(new Notification("Hello"));
            }
        });
        background.add(textButton);
        stage.addActor(background);
//        stage.addActor(textButton);


        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        stage.draw();

        super.renderNotifications(delta);
    }
}
