package com.mankomania.game.gamecore.fieldoverlay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mankomania.game.core.fields.types.Field;
import com.mankomania.game.gamecore.fieldoverlay.fielddata.FieldOverlayFieldInfo;
import com.mankomania.game.gamecore.fieldoverlay.fielddata.FieldOverlayRowPosition;


import static com.mankomania.game.gamecore.fieldoverlay.FieldOverlayConfig.*;

/**
 * a class which stores among the base field (Field property) the textures to render in the overlay
 */
public class FieldOverlayField {
    private Field baseField;
    private FieldOverlayFieldInfo fieldInfo;
    private int fieldId;
    private Texture texture;
    private BitmapFont debugFont;

    private float currentPosX;
    private float currentPosY;

    private FieldOverlayFieldBorder fieldBorder;

    public  FieldOverlayField(Field baseField, FieldOverlayFieldInfo fieldInfo, int id) {
        this.baseField = baseField;
        this.fieldInfo = fieldInfo;
        this.fieldId = id; // TODO: refactor so that id is property of Field...

        this.fieldBorder = new FieldOverlayFieldBorder();

        this.currentPosY = calculateYPosition();
    }

    public void create(FieldOverlayTextures fieldTextures) {
        switch (baseField.getColor()) {
            case GREY:
            case WHITE:
                this.texture = fieldTextures.getFieldWhite(); break;
            case ORANGE:   this.texture = fieldTextures.getFieldOrange(); break;
            case YELLOW:   this.texture = fieldTextures.getFieldYellow(); break;
            case BLUE:     this.texture = fieldTextures.getFieldBlue(); break;
            case RED:  this.texture = fieldTextures.getFieldMagenta(); break;
        }

        this.fieldBorder.create(fieldTextures);

        this.debugFont = new BitmapFont(Gdx.files.internal("fonts/beleren.fnt"));
        this.debugFont.getData().markupEnabled = true; // enable color markup in font rendering strings
    }

    public void draw(SpriteBatch batch) {
        this.fieldBorder.update();
        this.fieldBorder.render(batch, (int)this.currentPosX, (int)this.currentPosY, BOX_WIDTH, BOX_WIDTH);

        batch.draw(this.texture, this.currentPosX, this.currentPosY, BOX_WIDTH, BOX_WIDTH);
        this.debugFont.draw(batch, "[BLACK]" + this.fieldId, this.currentPosX + 30, this.currentPosY + 80);
    }

    public boolean isOverField(int x, int y, float scrollPosition) {
        // TODO: implement the "new way"
//        int fittedX = x + (int)scrollPosition; // add the current scroll position
//        int fittedY = Gdx.graphics.getHeight() - y;
//
//
//        if ((fittedX >= this.calculateXPosition() && fittedX <= this.calculateXPosition() + BOX_WIDTH) &&
//                fittedY >= this.calculateYPosition() && fittedY <= this.calculateYPosition() + BOX_WIDTH) {
//            return true;
//        }
        return false;
    }


    /* === BORDER STUFF */
    public void showBorder() {
        this.fieldBorder.show();
    }

    public void hideBorder() {
        this.fieldBorder.hide();
    }


    /* === GETTER === */
    public int getId() {
        return this.fieldId;
    }

    public int getColumn() {
        return this.fieldInfo.getColumn();
    }

    public Field getBaseField() {
        return this.baseField;
    }

    public void setCurrentPosX(float currentPosX) {
        this.currentPosX = currentPosX;
    }

    @Override
    public String toString() {
        return "FieldOverlayField{" +
                "baseField=" + baseField +
                ", fieldInfo=" + fieldInfo +
                ", fieldInfoId = " + fieldInfo.getId() +
                ", fieldId=" + fieldId + "}";
    }


    /**
     * Calculates the Y position of the current field, depending on which row it is in
     * @return the base Y position of this field
     */
    private int calculateYPosition() {
        int yPos = 0;

        if (this.fieldInfo.getRowPosition() == FieldOverlayRowPosition.TOP) {
            yPos = (Gdx.graphics.getHeight() - BOX_WIDTH) - SPLIT_MARGIN_TOP;
        } else if (this.fieldInfo.getRowPosition() == FieldOverlayRowPosition.MIDDLE) {
            yPos = (Gdx.graphics.getHeight() - BOX_WIDTH) - MARGIN_TOP;;
        } else {
            yPos = (Gdx.graphics.getHeight() - BOX_WIDTH) - SPLIT_MARGIN_TOP_ALTERNATE;;
        }

        return yPos;
    }
}
