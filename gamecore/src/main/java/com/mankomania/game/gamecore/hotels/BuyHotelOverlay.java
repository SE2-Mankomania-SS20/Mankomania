package com.mankomania.game.gamecore.hotels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mankomania.game.gamecore.MankomaniaGame;
import com.mankomania.game.gamecore.util.AssetDescriptors;

/**
 * A simple overlay for having the player to chose whether to buy a hotel or not after landing on an not owned hotel field.
 */
public class BuyHotelOverlay {
    private Stage stage;
    private Label lblHeading;
    private TextButton btnBuy;
    private TextButton btnNotBuy;

    private boolean isShowing = true;


    public void create() {
        Skin skin = MankomaniaGame.getMankomaniaGame().getManager().get(AssetDescriptors.SKIN);
        // scale up standard skin font for new, maybe create a new skin with bigger size font later on
        skin.getFont("font").getData().setScale(3, 3);

        this.stage = new Stage(new ScreenViewport());

        // header label initialization
        String headingText = "Do you want to buy hotel\n'" + MankomaniaGame.getMankomaniaGame().getGameData().getFieldByIndex(34).getText() + "' for 40.000$?";
        this.lblHeading = new Label(headingText, skin, "chat");
        this.lblHeading.setAlignment(Align.center);
        this.lblHeading.setPosition(60f, Gdx.graphics.getHeight() - 480f);
        this.lblHeading.setSize(Gdx.graphics.getWidth() - 120f, 240f);

        // not buy button initialization
        this.btnNotBuy = new TextButton("Do not buy it!", skin);
        this.btnNotBuy.setSize(480f, 140f);
        this.btnNotBuy.setPosition((Gdx.graphics.getWidth() / 4f) - 240f, Gdx.graphics.getHeight() - 720f);

        // buy button initialization
        this.btnBuy = new TextButton("Buy it!", skin);
//        this.btnBuy.getLabel().setAlignment(Align.center);
        this.btnBuy.setSize(480f, 140f);
        this.btnBuy.setPosition((Gdx.graphics.getWidth() / 4f) * 3 - 240f, Gdx.graphics.getHeight() - 720f);


        this.stage.addActor(this.lblHeading);
        this.stage.addActor(this.btnBuy);
        this.stage.addActor(this.btnNotBuy);
    }

    public void render(float delta) {
        if (this.isShowing) {
            this.stage.act(delta);
            this.stage.draw();
        }
    }

    public void dispose() {
        this.stage.dispose();
    }

    public void show() {
        this.isShowing = true;
    }

    public void hide() {
        this.isShowing = false;
    }

    public void setHeadingText(String headingText) {
        this.lblHeading.setText(headingText);
    }
}
