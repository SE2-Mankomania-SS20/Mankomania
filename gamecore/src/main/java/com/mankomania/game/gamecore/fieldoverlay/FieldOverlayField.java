package com.mankomania.game.gamecore.fieldoverlay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mankomania.game.core.fields.types.Field;
import com.mankomania.game.gamecore.fieldoverlay.fielddata.FieldOverlayFieldInfo;

import java.util.ArrayList;

/**
 * a class which stores among the base field (Field property) the textures to render in the overlay
 */
public class FieldOverlayField {
    private Field baseField;
    private FieldOverlayFieldInfo fieldInfo;
    private int fieldId;
    private Texture texture;

    private FieldOverlayFieldBorder fieldBorder;

    public  FieldOverlayField(Field baseField, FieldOverlayFieldInfo fieldInfo, int id) {
        this.baseField = baseField;
        this.fieldInfo = fieldInfo;
        this.fieldId = id; // TODO: refactor so that id is property of Field...

        this.fieldBorder = new FieldOverlayFieldBorder();


//        System.out.println("ID = " + id + ", INFO = [" + fieldInfo + "], BASEFIELD = " + baseField);

//        Gdx.app.log("splitPos", "added id = " + baseField.getId() + ", splitPos = " + this.splitPosition);

        /*
        TODO:
            add fieldinfo
            add position calculation
            add collision detection
         */
    }

    public void create(FieldOverlayTextures fieldTextures) {
        // TODO: revert
//        switch (baseField.getColor()) {
//            case GREY:    this.texture = fieldTextures.getFieldWhite(); break;
//            case ORANGE:   this.texture = fieldTextures.getFieldOrange(); break;
//            case YELLOW:   this.texture = fieldTextures.getFieldYellow(); break;
//            case BLUE:     this.texture = fieldTextures.getFieldBlue(); break;
//            case RED:  this.texture = fieldTextures.getFieldMagenta(); break;
//        }

        this.fieldBorder.create(fieldTextures);
    }

    public void draw(SpriteBatch batch, int x, int y, int w, int h) {
        this.fieldBorder.update();
        this.fieldBorder.render(batch, x, y, w, h);

        batch.draw(this.texture, x, y, w, h);
    }


    public void showBorder() {
        this.fieldBorder.show();
    }

    public void hideBorder() {
        this.fieldBorder.hide();
    }


    public int getId() {
        return this.fieldId;
    }

    public Field getBaseField() {
        return this.baseField;
    }

    @Override
    public String toString() {
        return "FieldOverlayField{" +
                "baseField=" + baseField +
                ", fieldInfo=" + fieldInfo +
                ", fieldInfoId = " + fieldInfo.getId() +
                ", fieldId=" + fieldId +
                '}';
    }
}
