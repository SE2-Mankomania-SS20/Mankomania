package com.mankomania.game.gamecore.fieldoverlay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import static com.mankomania.game.gamecore.fieldoverlay.FieldOverlayConfig.*;

public class FieldOverlayTextBox {
    private Texture textBoxTextureBorder, textBoxTextureInner;
    private String currentText;
    private boolean isShowing;

    private BitmapFont textBoxFont;

    public void create(FieldOverlayTextures overlayTextures) {
        this.textBoxTextureBorder = overlayTextures.getTextBoxBorder();
        this.textBoxTextureInner = overlayTextures.getTextBoxInner();

        this.textBoxFont = new BitmapFont(Gdx.files.internal("fonts/beleren_small.fnt"));
        this.textBoxFont.getData().markupEnabled = true; // enable color markup in font rendering strings

        this.isShowing = true;
        this.currentText = "Kaufe 1 Aktie \"Kurzschluss-Versorungs-AG\" für 100.000€";
    }

    public void update() {
        // TODO: use delta
        // TODO: handle fading
    }

    public void render(SpriteBatch batch) {
        if (this.isShowing) {

            // set our alpha value in the spritebatch's color
            Color color = batch.getColor();
            float oldAlpha = color.a; // remember old alpha value so we can reset it

            color.a = TEXTBOX_MAX_ALPHA;
            batch.setColor(color);
            batch.draw(this.textBoxTextureInner, TEXTBOX_POS_X, TEXTBOX_POS_Y, TEXTBOX_WIDTH, TEXTBOX_HEIGHT);
            batch.draw(this.textBoxTextureBorder, TEXTBOX_POS_X, TEXTBOX_POS_Y, TEXTBOX_WIDTH, TEXTBOX_HEIGHT);

            // reset to the old alpha value
            color.a = oldAlpha;
            batch.setColor(color);


            // TODO: calculate position
            this.textBoxFont.draw(batch, "[BLACK]" + this.currentText, TEXTBOX_POS_X + 80, TEXTBOX_POS_Y + 130);
        }
    }

    /**
     * Starts fading in the TextBox (durations and other configurations are in FieldOverlayConfig). Maybe will be changed to "scrolling" in from top.
     */
    public void show() {
        // TODO: add fading
        this.isShowing = true;
    }

    /**
     * Starts fading out the TextBox (durations and other configurations are in FieldOverlayConfig). Maybe will be changed to "scrolling" out top.
     */
    public void hide() {
        // TODO: add fading
        this.isShowing = false;
    }

    public void dispose() {
        this.textBoxFont.dispose();
    }


    public String getCurrentText() {
        return currentText;
    }

    public void setCurrentText(String currentText) {
        this.currentText = currentText;
    }
}
