package com.mankomania.game.gamecore.fieldoverlay;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class FieldOverlayFieldBorder {
    private Texture borderTexture;

    public void create(FieldOverlayTextures textures) {
        this.borderTexture = textures.getFieldBorder();
    }

    public void render(SpriteBatch batch, int x, int y, int w, int h) {

    }
}
