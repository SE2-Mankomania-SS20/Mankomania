package com.mankomania.game.gamecore.fieldoverlay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.mankomania.game.gamecore.MankomaniaGame;
import com.mankomania.game.gamecore.util.AssetDescriptors;

/**
 * this class is used to load and hold the various textures required for drawing the overlay
 */
public class FieldOverlayTextures {
    private Texture fieldOrange, fieldBlue, fieldYellow, fieldWhite, fieldMagenta;


    public FieldOverlayTextures() {

    }

    public void create() {
        fieldWhite = MankomaniaGame.manager.get(AssetDescriptors.WHITE);
        fieldOrange = MankomaniaGame.manager.get(AssetDescriptors.ORANGE);
        fieldBlue = MankomaniaGame.manager.get(AssetDescriptors.BLUE);
        fieldYellow = MankomaniaGame.manager.get(AssetDescriptors.YELLOW);
        fieldMagenta = MankomaniaGame.manager.get(AssetDescriptors.MAGENTA);

    }

    public Texture getFieldOrange() {
        return fieldOrange;
    }

    public Texture getFieldBlue() {
        return fieldBlue;
    }

    public Texture getFieldYellow() {
        return fieldYellow;
    }

    public Texture getFieldWhite() {
        return fieldWhite;
    }

    public Texture getFieldMagenta() {
        return fieldMagenta;
    }

    public void dispose() {

    }
}
