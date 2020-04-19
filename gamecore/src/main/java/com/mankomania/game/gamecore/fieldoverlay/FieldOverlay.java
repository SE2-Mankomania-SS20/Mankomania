package com.mankomania.game.gamecore.fieldoverlay;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mankomania.game.gamecore.fields.FieldData;

/**
 * FieldOverlay is used to render the overlay for the board's fields.
 * Only this class should be used when communicating with the field overlay.
 */
public class FieldOverlay {
    // for loading and holding information and textures
    FieldData fieldData;
    FieldOverlayData fieldOverlayData;

    BitmapFont debugFont;

    public FieldOverlay() {
        fieldData = new FieldData();
        fieldOverlayData = new FieldOverlayData();
    }

    /**
     * Creates and loads all the necessary data (must be called before any other function of this class)
     */
    public void create() {
        this.fieldData.load();
        this.fieldOverlayData.create(fieldData.getFieldData());

        this.debugFont = new BitmapFont();
    }

    /**
     * Renders the overlay on given SpriteBatch
     * @param batch the SpriteBatch to render with. The SpriteBatch.begin() and end() methods must be called before/after calling this render call (!)
     */
    public void render(SpriteBatch batch) {

    }

//    public void update(float delta) {
//        // TODO: implement delta
//    }
}
