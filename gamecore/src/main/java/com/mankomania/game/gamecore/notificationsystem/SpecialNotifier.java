package com.mankomania.game.gamecore.notificationsystem;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.mankomania.game.gamecore.MankomaniaGame;
import com.mankomania.game.gamecore.fieldoverlay.FieldOverlayTextures;
import com.mankomania.game.gamecore.util.AssetDescriptors;
import com.mankomania.game.gamecore.util.AssetPaths;

import static com.mankomania.game.gamecore.fieldoverlay.FieldOverlayConfig.TEXTBOX_FADE_DURATION;
import static com.mankomania.game.gamecore.fieldoverlay.FieldOverlayConfig.TEXTBOX_HEIGHT;
import static com.mankomania.game.gamecore.fieldoverlay.FieldOverlayConfig.TEXTBOX_MARGIN_TOP;
import static com.mankomania.game.gamecore.fieldoverlay.FieldOverlayConfig.TEXTBOX_MAX_ALPHA;
import static com.mankomania.game.gamecore.fieldoverlay.FieldOverlayConfig.TEXTBOX_POS_X;
import static com.mankomania.game.gamecore.fieldoverlay.FieldOverlayConfig.TEXTBOX_POS_Y;
import static com.mankomania.game.gamecore.fieldoverlay.FieldOverlayConfig.TEXTBOX_WIDTH;

public class SpecialNotifier {
    private Texture textBoxTextureBorder, textBoxTextureInner;
    private String currentText;
    private boolean isShowing;

    private Interpolation interpolationIn = Interpolation.smooth2; // change here to change method of fading in/out
    // nice are: smoothX, swingOut, bounceOut
    private float interpolationCurrent = 0;
    private boolean isFadingIn = false;
    private boolean isFadingOut = false;

    private BitmapFont textBoxFont;
    private GlyphLayout glyphLayout; // needed to calculate a strings width and height (for text rendering)

    public void create() {
        this.textBoxTextureBorder = new Texture(Gdx.files.internal(AssetPaths.BORDER));
        this.textBoxTextureInner = new Texture(Gdx.files.internal(AssetPaths.FILLING));

        this.textBoxFont = new BitmapFont(Gdx.files.internal("fonts/beleren_small.fnt"));
        this.textBoxFont.getData().markupEnabled = true; // enable color markup in font rendering strings

        this.glyphLayout = new GlyphLayout(); // needed for calculating string dimensions for rendering, TODO: remove if not needed in future

        this.isShowing = false;
        this.isFadingIn = false;
        this.currentText = "Kaufe 1 Aktie \"Kurzschluss-Versorungs-AG\" für 100.000€";
    }

    public void update() {
        // TODO: use delta
        // TODO: handle fading
    }

    public void render(SpriteBatch batch) {
        if (this.isShowing) {

            if (Gdx.input.justTouched()) {
                int currentYTouch = Gdx.input.getY();
                Gdx.app.log("Notifier", "Handling on touch. Current Y: " + currentYTouch);
                this.handleOnTouchUp(currentYTouch);
            }

            // TODO: refactor interpolation out in its own method
            // INTERPPOLATION BEGIN
            if (this.isFadingIn) {
                this.interpolationCurrent += Gdx.graphics.getDeltaTime();
                if (this.interpolationCurrent >= TEXTBOX_FADE_DURATION) {
                    this.isFadingIn = false;
                }

            }
            if (this.isFadingOut) {
                this.interpolationCurrent -= Gdx.graphics.getDeltaTime();
                if (this.interpolationCurrent <= 0) {
                    this.isFadingOut = false;
                    this.isShowing = false;
                }
            }

            float progress = Math.min(1f, interpolationCurrent / TEXTBOX_FADE_DURATION); // 0 -> 1, 1 = showing, 0 = not showing
            float percentVal = this.interpolationIn.apply(progress);
//            int interpolatedPosX = (int)(TEXTBOX_POS_X + ((Gdx.graphics.getWidth() - TEXTBOX_POS_X) * percentVal));
            int interpolatedPosX = (int) (Gdx.graphics.getWidth() - (Gdx.graphics.getWidth() - TEXTBOX_POS_X) * percentVal);
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
            if (this.textBoxFont != null) {
                this.textBoxFont.draw(batch, "[BLACK]" + this.currentText, interpolatedPosX + 50f, (float) TEXTBOX_POS_Y + 130f, 1820, Align.center, true);
            } else {
                Gdx.app.error("Notification", "textBoxFont is still null, wtf");
            }
        }
    }

    /**
     * Starts fading in the TextBox (durations and other configurations are in FieldOverlayConfig). Maybe will be changed to "scrolling" in from top.
     */
    public void show() {
        if (!this.isFadingOut && !this.isFadingIn) {
            this.isShowing = true;
            this.isFadingIn = true;
        }
    }

    /**
     * Starts fading out the TextBox (durations and other configurations are in FieldOverlayConfig). Maybe will be changed to "scrolling" out top.
     */
    public void hide() {
        if (!this.isFadingOut && !this.isFadingIn) {
            this.isFadingOut = true;
        }
    }

    /**
     * Toggles the visibility of this widget on/off, depending on the current state.
     */
    public void toggleVisibility() {
        if (!this.isShowing) {
            this.show();
        } else {
            if (!this.isFadingIn && !this.isFadingOut) { // check whether widget is not currently scrolling in or out
                this.hide();
            }
        }
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

    public boolean isShowing() {
        return isShowing;
    }

    /**
     * Handles the touch event and shows/hides the textbox if "hit" by a touch event.
     *
     * @param screenY screen touch y position
     * @return true if hit, false otherwise (used for chaining InputProcessors)
     */
    public boolean handleOnTouchUp(int screenY) {
        if (this.isShowing && !this.isFadingIn && !this.isFadingOut) {
            if (screenY >= TEXTBOX_MARGIN_TOP && screenY <= TEXTBOX_MARGIN_TOP + TEXTBOX_HEIGHT) {
                this.hide();
                return true;
            }
        }
        return false;
    }

    /**
     * helper function that calculates a string's width and height with a specific font using the GlyphLayout.
     *
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
