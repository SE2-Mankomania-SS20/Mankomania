package com.mankomania.game.gamecore.screens.trickyone;

/*
 Created by Fabian Oraze on 12.06.20
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class InfoOverlay {

    private Stage overlay;
    private boolean show;

    //info that should be displayed in a label to give player the rules of the miniGame
    private static final String GAME_INFO = "Willkommen bei der Verflixten 1!\n\n" +
            "Regeln:\n" +
            "Du kannst so lange wuerfeln, wie du willst!\n" +
            "Wenn du 'Stop' drueckst, wird die gesammelte Augenanzahl\n" +
            "mit 5.000 multipliziert und von deinem Konto abgezogen!\n" +
            "Aber Achtung! Wenn du eine 1 wuerfelst, gewinnst du auf\n" +
            "der Stelle 100.000, falls du 2 1-en wuerfelst\n" +
            "gewinnst du sogar 300.000!\n\n" +
            "Beruehre um zu starten!";
    private ShapeRenderer background;


    public void create(Skin skin, InputMultiplexer inputMultiplexer) {
        overlay = new Stage();
        skin.getFont("info").getData().setScale(3.5f, 3.5f);
        show = true;

        background = new ShapeRenderer();

        Label gameInfoLabel = new Label(GAME_INFO, skin, "info");
        gameInfoLabel.setPosition(100f, 100f);
        gameInfoLabel.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                show = false;
                inputMultiplexer.removeProcessor(overlay);
            }
        });

        overlay.addActor(gameInfoLabel);
        inputMultiplexer.addProcessor(overlay);
    }

    public void render() {
        if (show) {
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            background.begin(ShapeRenderer.ShapeType.Filled);
            background.setColor(0, 0, 0, 0.5f);
            background.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            background.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);
            overlay.act();
            overlay.draw();
        }
    }

}
