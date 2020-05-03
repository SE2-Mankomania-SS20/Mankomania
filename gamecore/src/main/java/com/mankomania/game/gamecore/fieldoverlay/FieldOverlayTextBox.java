package com.mankomania.game.gamecore.fieldoverlay;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class FieldOverlayTextBox {
    private Texture textBoxTextureBorder, textBoxTextureInner;
    private String currentText;

    public void create(FieldOverlayTextures overlayTextures) {
        this.textBoxTextureBorder = overlayTextures.getTextBoxBorder();
        this.textBoxTextureInner = overlayTextures.getTextBoxInner();
    }

    public void update() {
        // TODO: use delta
    }

    public void render(SpriteBatch batch) {

    }

    /**
     * Starts fading in the TextBox (durations and other configurations are in FieldOverlayConfig). Maybe will be changed to "scrolling" in from top.
     */
    public void show() {

    }

    /**
     * Starts fading out the TextBox (durations and other configurations are in FieldOverlayConfig). Maybe will be changed to "scrolling" out top.
     */
    public void hide() {

    }



    public String getCurrentText() {
        return currentText;
    }

    public void setCurrentText(String currentText) {
        this.currentText = currentText;
    }
}
