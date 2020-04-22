package com.mankomania.game.gamecore.fieldoverlay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mankomania.game.gamecore.fields.Field;

import java.util.ArrayList;

/**
 * a class which stores among the base field (Field property) the textures to render in the overlay
 */
public class FieldOverlayField {
    private Field baseField;
    private Texture texture;
    private int splitPosition;

    public  FieldOverlayField(Field baseField) {
        this.baseField = baseField;

        // TODO: better solution to manage the split of the way (for now it checks where each split happens)
        // split the way into:
        //   16 splits into 17 or 21
        //   17 -> 18 -> 19 -> 20 -> 25 (at 25 both ways merge again)
        //   21 -> 22 -> 23 -> 24 -> 25 (at 25 both ways merge again)
        // so 17 has split pos 0, 18 split pos 1, ..., 21 split pos 4, ..., 24 split pos 7

        // for now, hardcode (since the field will unlikely change) the points where the way splits and merges again...
        this.splitPosition = -1;
        if (baseField.getId() > 49 && baseField.getId() < 58) {
            this.splitPosition = baseField.getId() % 50;
        }
        if (baseField.getId() > 66 && baseField.getId() < 75) {
            this.splitPosition = baseField.getId() % 67;
        }
        if (baseField.getId() > 7 && baseField.getId() < 16) {
            this.splitPosition = baseField.getId() % 8;
        }
        if (baseField.getId() > 24 && baseField.getId() < 33)  {
            this.splitPosition = baseField.getId() % 25;
        }

        Gdx.app.log("splitPos", "added id = " + baseField.getId() + ", splitPos = " + this.splitPosition);
    }

    public void create(FieldOverlayTextures fieldTextures) {
        switch (baseField.getColor()) {
            case WHITE:    this.texture = fieldTextures.getFieldWhite(); break;
            case ORANGE:   this.texture = fieldTextures.getFieldOrange(); break;
            case YELLOW:   this.texture = fieldTextures.getFieldYellow(); break;
            case BLUE:     this.texture = fieldTextures.getFieldBlue(); break;
            case MAGENTA:  this.texture = fieldTextures.getFieldMagenta(); break;
        }
    }

    public void draw(SpriteBatch batch, int x, int y, int w, int h) {
        batch.draw(this.texture, x, y, w, h);
    }


    public int getId() {
        return this.baseField.getId();
    }

    public int getSplitPosition() {
        return this.splitPosition;
    }

    public Field getBaseField() {
        return this.baseField;
    }
}
