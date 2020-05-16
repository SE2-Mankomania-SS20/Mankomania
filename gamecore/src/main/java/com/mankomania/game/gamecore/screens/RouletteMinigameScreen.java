package com.mankomania.game.gamecore.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class RouletteMinigameScreen extends AbstractScreen {
    private Stage stage;
    private Skin skin, skin1, skin2;



    public RouletteMinigameScreen () {
        //set skin
        skin = new Skin(Gdx.files.internal("skin/terra-mother-ui.json"));
        skin1 = new Skin(Gdx.files.internal("skin/uiskin.json"));
        skin2 = new Skin(Gdx.files.internal("skin/terra-mother-ui.json"));

        //set font size of skin
        skin.getFont("font").getData().setScale(5, 5);
        skin1.getFont("default-font").getData().setScale(3, 3);
        skin2.getFont("font").getData().setScale(3, 3);



    }

    @Override
    public void render(float delta) {
        super.render(delta);
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
    }
}
