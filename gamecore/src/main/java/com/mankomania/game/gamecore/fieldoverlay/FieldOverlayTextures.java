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
    private Texture fieldOrange, fieldBlue, fieldYellow, fieldWhite, fieldMagenta, fieldBorder, textBoxBorder, textBoxInner;


    public void create() {
        fieldWhite = MankomaniaGame.getMankomaniaGame().getManager().get(AssetDescriptors.WHITE);
        fieldOrange = MankomaniaGame.getMankomaniaGame().getManager().get(AssetDescriptors.ORANGE);
        fieldBlue = MankomaniaGame.getMankomaniaGame().getManager().get(AssetDescriptors.BLUE);
        fieldYellow = MankomaniaGame.getMankomaniaGame().getManager().get(AssetDescriptors.YELLOW);
        fieldMagenta = MankomaniaGame.getMankomaniaGame().getManager().get(AssetDescriptors.MAGENTA);


        fieldBorder = MankomaniaGame.getMankomaniaGame().getManager().get(AssetDescriptors.SELECTED_BORDER);
        textBoxBorder = MankomaniaGame.getMankomaniaGame().getManager().get(AssetDescriptors.BORDER);
        textBoxInner = MankomaniaGame.getMankomaniaGame().getManager().get(AssetDescriptors.FILLING);
    }

    public Texture getFieldBorder() {
        return fieldBorder;
    }

    public Texture getTextBoxBorder() {
        return textBoxBorder;
    }

    public Texture getTextBoxInner() {
        return textBoxInner;
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

}
