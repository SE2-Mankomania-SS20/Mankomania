package com.mankomania.game.gamecore.fieldoverlay;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class FieldOverlayFieldBorder {
    private Texture borderTexture;
    private boolean isShowing;


    public void create(FieldOverlayTextures textures) {
        this.borderTexture = textures.getFieldBorder();
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
            batch.draw(this.borderTexture, x, y, w, h); // TODO: implement actual border size and position
        }
    }
}
