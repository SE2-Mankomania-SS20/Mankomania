package com.mankomania.game.gamecore.fieldoverlay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

/**
 * this class is used to load and hold the various textures required for drawing the overlay
 */
public class FieldOverlayTextures {
    private Texture fieldOrange, fieldBlue, fieldYellow, fieldWhite, fieldMagenta;
    private Texture fieldBorder;
    private Texture textBoxBorder, textBoxInner;

    public void create() {
        fieldWhite = new Texture(Gdx.files.internal("fieldoverlay/field_white.png"));
        fieldOrange = new Texture(Gdx.files.internal("fieldoverlay/field_orange.png"));
        fieldBlue = new Texture(Gdx.files.internal("fieldoverlay/field_blue.png"));
        fieldYellow = new Texture(Gdx.files.internal("fieldoverlay/field_yellow.png"));
        fieldMagenta = new Texture(Gdx.files.internal("fieldoverlay/field_magenta.png"));

        fieldBorder = new Texture(Gdx.files.internal("fieldoverlay/field_selected_border.png"));

        textBoxBorder = new Texture(Gdx.files.internal("fieldoverlay/textbox_border.png"));
        textBoxInner = new Texture(Gdx.files.internal("fieldoverlay/textbox_filling.png"));
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

    public Texture getFieldBorder() {
        return fieldBorder;
    }

    public Texture getTextBoxBorder() {
        return textBoxBorder;
    }

    public Texture getTextBoxInner() {
        return textBoxInner;
    }

    public void dispose() {
        this.fieldOrange.dispose();
        this.fieldWhite.dispose();
        this.fieldBlue.dispose();
        this.fieldYellow.dispose();
        this.fieldMagenta.dispose();

        this.fieldBorder.dispose();

        this.textBoxBorder.dispose();
        this.textBoxInner.dispose();
    }
}
