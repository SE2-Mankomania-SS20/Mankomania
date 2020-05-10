package com.mankomania.game.gamecore.fieldoverlay.fielddata;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mankomania.game.gamecore.fieldoverlay.FieldOverlayField;

/**
 * Class that represents a column in the field overlay and stores it's id and the fields that are part of it.
 */
public class FieldOverlayFieldColumn {
    private int columnId;
    private boolean hasDoubleRow, isEmpty;
    private FieldOverlayField fieldTop;
    private FieldOverlayField fieldMid;
    private FieldOverlayField fieldBot;

    private float positionX;

    public FieldOverlayFieldColumn(int columnId) {
        this.columnId = columnId;
        this.isEmpty = true;
    }

    public FieldOverlayFieldColumn(int columnId, FieldOverlayField middleField) {
        this.columnId = columnId;
        this.fieldMid = middleField;

        this.hasDoubleRow = false;
        this.isEmpty = false;
    }

    public FieldOverlayFieldColumn(int columnId, FieldOverlayField topField, FieldOverlayField bottomField) {
        this.columnId = columnId;
        this.fieldTop = topField;
        this.fieldBot = bottomField;

        this.hasDoubleRow = true;
        this.isEmpty = false;
    }

    /**
     * Renders all fields of this column.
     *
     * @param batch SpriteBatch used for rendering
     */
    public void renderFields(SpriteBatch batch) {
        if (!this.isEmpty) {
            if (this.hasDoubleRow) {
                this.fieldTop.draw(batch);
                this.fieldBot.draw(batch);
            } else {
                this.fieldMid.draw(batch);
            }
        }
    }

    /**
     * Move the column this amount.
     *
     * @param value how much it should move
     */
    public void movePositionX(float value) {
        float newPosition = this.positionX + value;

        this.setPositionX(newPosition);
    }

    /**
     * Returns the field if given points lies on one of the fields in this column.
     *
     * @param touchX x coord
     * @param touchY y coord
     * @return the field if point lies on one field, otherwhise null
     */
    public FieldOverlayField getTouchedField(int touchX, int touchY) {
        if (!this.isEmpty) {
            if (this.hasDoubleRow) {
                if (this.fieldTop.isOverField(touchX, touchY)) {
                    return this.fieldTop;
                }
                if (this.fieldBot.isOverField(touchX, touchY)) {
                    return this.fieldBot;
                }
            } else {
                if (this.fieldMid.isOverField(touchX, touchY)) {
                    return this.fieldMid;
                }
            }
        }

        return null;
    }

    public float getPositionX() {
        return positionX;
    }

    public void setPositionX(float positionX) {
        this.positionX = positionX;

        if (!this.isEmpty) {
            if (this.hasDoubleRow) {
                this.fieldTop.setCurrentPosX(positionX);
                this.fieldBot.setCurrentPosX(positionX);
            } else {
                this.fieldMid.setCurrentPosX(positionX);
            }
        }
    }

    public int getColumnId() {
        return columnId;
    }

    @Override
    public String toString() {
        return "Column (" + this.columnId + "): fields count = " +
                (isEmpty ? "0" : (hasDoubleRow ? "2" : "1"));
    }
}
