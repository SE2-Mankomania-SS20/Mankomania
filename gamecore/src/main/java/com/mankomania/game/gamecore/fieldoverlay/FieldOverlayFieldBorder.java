package com.mankomania.game.gamecore.fieldoverlay;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import static com.mankomania.game.gamecore.fieldoverlay.FieldOverlayConfig.BORDER_SIZE;

public class FieldOverlayFieldBorder {
    private Texture borderTexture;
    private boolean isShowing;


    public void create(FieldOverlayTextures textures) {
        this.borderTexture = textures.getFieldBorder();
        this.isShowing = false;
    }

    /**
     * Calling this method will start showing the border. The border flashing gets reset.
     */
    public void show() {
        this.isShowing = true;
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

    }

    /**
     * Used to render the border. The coordinates and size must reflect the position and size of the selected field,
     * not the one of the actual border. The border's position and size gets calculated here.
     */
    public void render(SpriteBatch batch, int x, int y, int w, int h) {
        if (isShowing) {
            int borderX = x - BORDER_SIZE;
            int borderY = y - BORDER_SIZE;
            int borderW = w + 2 * BORDER_SIZE;
            int borderH = h + 2 * BORDER_SIZE;

            batch.draw(this.borderTexture, borderX, borderY, borderW, borderH); // TODO: implement actual border size and position
        }
    }
}
