package com.mankomania.game.gamecore.fieldoverlay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.mankomania.game.gamecore.util.AssetDescriptors;

/**
 * this class is used to load and hold the various textures required for drawing the overlay
 */
public class FieldOverlayTextures {
    private Texture fieldOrange, fieldBlue, fieldYellow, fieldWhite, fieldMagenta;
    private AssetManager manager;

    public FieldOverlayTextures() {
        manager = new AssetManager();
        loadAssets();
    }

    private void loadAssets(){
        manager.load(AssetDescriptors.WHITE);
        manager.load(AssetDescriptors.ORANGE);
        manager.load(AssetDescriptors.BLUE);
        manager.load(AssetDescriptors.YELLOW);
        manager.load(AssetDescriptors.MAGENTA);

        manager.finishLoading();
    }
    public void create() {
        fieldWhite = manager.get(AssetDescriptors.WHITE);
        fieldOrange = manager.get(AssetDescriptors.ORANGE);
        fieldBlue = manager.get(AssetDescriptors.BLUE);
        fieldYellow = manager.get(AssetDescriptors.YELLOW);
        fieldMagenta = manager.get(AssetDescriptors.MAGENTA);

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
        manager.dispose();
    }
}
