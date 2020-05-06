package com.mankomania.game.gamecore.fieldoverlay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;

import static com.mankomania.game.gamecore.fieldoverlay.FieldOverlayConfig.*;

public class FieldOverlayTextBox {
    private Texture textBoxTextureBorder, textBoxTextureInner;
    private String currentText;
    private boolean isShowing;

    private Interpolation interpolationIn = Interpolation.linear;
    private float interpolationCurrent = 0;
    private float interpolationDuration = 2; // TODO: move to config

    private boolean isFadingIn = false;
    private boolean isFadingOut = false;

    private BitmapFont textBoxFont;
    private GlyphLayout glyphLayout; // needed to calculate a strings width and height (for text rendering)

    public void create(FieldOverlayTextures overlayTextures) {
        this.textBoxTextureBorder = overlayTextures.getTextBoxBorder();
        this.textBoxTextureInner = overlayTextures.getTextBoxInner();

        this.textBoxFont = new BitmapFont(Gdx.files.internal("fonts/beleren_small.fnt"));
        this.textBoxFont.getData().markupEnabled = true; // enable color markup in font rendering strings

        this.glyphLayout = new GlyphLayout(); // needed for calculating string dimensions for rendering, TODO: remove if not needed in future

        this.isShowing = true;
        this.isFadingIn = true;
        this.currentText = "Kaufe 1 Aktie \"Kurzschluss-Versorungs-AG\" für 100.000€";
    }

    public void update() {
        // TODO: use delta
        // TODO: handle fading
    }

    public void render(SpriteBatch batch) {
        if (this.isShowing) {

            // INTERPPOLATION BEGIN
            if (this.isFadingIn) {
                this.interpolationCurrent += Gdx.graphics.getDeltaTime();
                if (this.interpolationCurrent >= interpolationDuration) {
                    this.isFadingIn = false;
                }

            }
            if (this.isFadingOut) {
                this.interpolationCurrent -= Gdx.graphics.getDeltaTime();
                if (this.interpolationCurrent <= 0) {
                    this.isFadingOut = false;
                }
            }

            float progress = Math.min(1f, interpolationCurrent / interpolationDuration); // 0 -> 1, 1 = showing, 0 = not showing
            float percentVal = this.interpolationIn.apply(progress);
//            int interpolatedPosX = (int)(TEXTBOX_POS_X + ((Gdx.graphics.getWidth() - TEXTBOX_POS_X) * percentVal));
            int interpolatedPosX = (int)(Gdx.graphics.getWidth() - (Gdx.graphics.getWidth() - TEXTBOX_POS_X) * percentVal);
            // == INTERPOLATION END ==

            // set our alpha value in the spritebatch's color
            Color color = batch.getColor();
            float oldAlpha = color.a; // remember old alpha value so we can reset it

            color.a = TEXTBOX_MAX_ALPHA;
            batch.setColor(color);
            batch.draw(this.textBoxTextureInner, interpolatedPosX, TEXTBOX_POS_Y, TEXTBOX_WIDTH, TEXTBOX_HEIGHT);
            batch.draw(this.textBoxTextureBorder, interpolatedPosX, TEXTBOX_POS_Y, TEXTBOX_WIDTH, TEXTBOX_HEIGHT);

            // reset to the old alpha value
            color.a = oldAlpha;
            batch.setColor(color);


            // no need to calculate the string length per hand, it seems. maybe it will be usefull later on tho, so its just commented out
//            Vector2 textDims = getTextDimensions(this.currentText);
//            this.textBoxFont.draw(batch, "[BLACK]" + this.currentText, (Gdx.graphics.getWidth() / 2) - textDims.x / 2, TEXTBOX_POS_Y + 130, 200, Align.center, true);
            this.textBoxFont.draw(batch, "[BLACK]" + this.currentText, 0, TEXTBOX_POS_Y + 130, 1920, Align.center, true);
        }
    }

    /**
     * Starts fading in the TextBox (durations and other configurations are in FieldOverlayConfig). Maybe will be changed to "scrolling" in from top.
     */
    public void show() {
        // TODO: add fading
        this.isShowing = true;
        this.isFadingIn = true;
    }

    /**
     * Starts fading out the TextBox (durations and other configurations are in FieldOverlayConfig). Maybe will be changed to "scrolling" out top.
     */
    public void hide() {
        // TODO: add fading
//        this.isShowing = false;
        this.isFadingOut = true;
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


    /**
     * helper function that calculates a string's width and height with a specific font using the GlyphLayout.
     * @param text the text to calculate the dimensions with
     * @return (for now) a Vector2 where x holds the width and y holds the height
     */
    private Vector2 getTextDimensions(String text) {
        // TODO: create own datatype instead of using Vector2
        this.glyphLayout.setText(this.textBoxFont, text);

        float width = this.glyphLayout.width;
        float height = this.glyphLayout.height;

        return new Vector2(width, height);
    }
}
