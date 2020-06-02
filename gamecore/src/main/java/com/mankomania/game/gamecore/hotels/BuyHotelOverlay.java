package com.mankomania.game.gamecore.hotels;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mankomania.game.gamecore.MankomaniaGame;
import com.mankomania.game.gamecore.util.AssetDescriptors;

/**
 * A simple overlay for having the player to chose whether to buy a hotel or not after landing on an not owned hotel field.
 */
public class BuyHotelOverlay {
    private Stage stage;

    public void create() {

    }

    public void render(float delta) {
        this.stage.act(delta);
        this.stage.draw();
    }

    public void dispose() {
        this.stage.dispose();
    }

    public void show() {

    }

    public void hide() {

    }
}
