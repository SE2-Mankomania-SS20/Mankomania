package com.mankomania.game.gamecore.fieldoverlay;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import static com.mankomania.game.gamecore.fieldoverlay.FieldOverlayConfig.BORDER_BLINK_SPEED;
import static com.mankomania.game.gamecore.fieldoverlay.FieldOverlayConfig.BORDER_MAX_ALPHA;
import static com.mankomania.game.gamecore.fieldoverlay.FieldOverlayConfig.BORDER_MIN_ALPHA;
import static com.mankomania.game.gamecore.fieldoverlay.FieldOverlayConfig.BORDER_SIZE;

public class FieldOverlayFieldBorder {
    private Texture borderTexture;
    private boolean isShowing;
    private float visibility;
    private boolean visibilitySinking = true;


    public void create(FieldOverlayTextures textures) {
        this.borderTexture = textures.getFieldBorder();
        this.isShowing = false;
        this.visibility = 0;
    }

    /**
     * Calling this method will start showing the border. The border flashing gets reset.
     */
    public void show() {
        this.isShowing = true;

        this.visibility = 1f;
        this.visibilitySinking = true;
    }

    /**
     * Hides this border instance.
     */
    public void hide() {
        this.isShowing = false;
    }

    /**
     * Used to calculate the flashing of the border
     */
    public void update() {
        // TODO: use delta time
        // if showing calculate the current alpha value (border is "blinking")
        if (isShowing) {
            if (visibilitySinking) {
                this.visibility -= BORDER_BLINK_SPEED;

                if (visibility <= BORDER_MIN_ALPHA) {
                    this.visibilitySinking = false;
                    this.visibility = BORDER_MIN_ALPHA;
                }
            } else {
                this.visibility += BORDER_BLINK_SPEED;

                if (visibility >= BORDER_MAX_ALPHA) {
                    this.visibilitySinking = true;
                    this.visibility = BORDER_MAX_ALPHA;
                }
            }

        }
    }

    /**
     * Used to render the border. The coordinates and size must reflect the position and size of the selected field,
     * not the one of the actual border. The border's position and size gets calculated here.
     * @param batch SpriteBatch
     * @param x position x
     * @param y position y
     * @param w position w
     * @param h position h
     */
    public void render(SpriteBatch batch, int x, int y, int w, int h) {
        if (isShowing) {
            int borderX = x - BORDER_SIZE;
            int borderY = y - BORDER_SIZE;
            int borderW = w + 2 * BORDER_SIZE;
            int borderH = h + 2 * BORDER_SIZE;

            // set our alpha value in the spritebatch's color
            Color color = batch.getColor();
            float oldAlpha = color.a; // remember old alpha value so we can reset it

            color.a = this.visibility;
            batch.setColor(color);
            batch.draw(this.borderTexture, borderX, borderY, borderW, borderH);

            // reset to the old alpha value
            color.a = oldAlpha;
            batch.setColor(color);
        }
    }
}
