package com.mankomania.game.gamecore.fieldoverlay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;
import com.mankomania.game.gamecore.fieldoverlay.fielddata.FieldOverlayFieldInfo;
import com.mankomania.game.gamecore.fieldoverlay.fielddata.FieldOverlayFieldInfoData;
import com.mankomania.game.core.fields.types.Field;
import com.mankomania.game.gamecore.util.ScreenManager;

import java.util.ArrayList;
import java.util.List;

/**
 * this class loads and holds the array of FieldOverlayFields
 */
public class FieldOverlayData {
    ArrayList<FieldOverlayField> fields;

    public FieldOverlayData() {
        this.fields = new ArrayList<>();
    }

    /**
     * Creates and maintains a list of FieldOverlayFields, constructed with the given base field list.
     *
     */
    public void create(FieldOverlayTextures fieldOverlayTextures) {
        // load positionings for fields from json
        Json json = new Json();
        FieldOverlayFieldInfoData fieldInfoData = json.fromJson(FieldOverlayFieldInfoData.class,
                Gdx.files.internal("fieldoverlay/overlay_fieldinfos.json").read());


        Field[] baseFieldData = ScreenManager.getInstance().getGame().getGameData().getFields();

        // TODO: iterate over these fields and merge it with the field information by id
        // parse the textures with these new fields
        // make function to calculate the fields position @FieldoverlayField


        FieldOverlayFieldInfo[] fieldInfos = fieldInfoData.getData();

        for (int i = 0; i < 78; i++) {
            FieldOverlayField newField = new FieldOverlayField(baseFieldData[i], fieldInfos[i], i);
            newField.create(fieldOverlayTextures);
            this.fields.add(newField);
            System.out.println("i = " + i + "; " + newField.toString());
        }
    }

    /**
     * returns a FieldOverlayField instance with a given id
     *
     * @param id
     * @return
     */
    public FieldOverlayField getById(int id) {
        // TODO: improve implementation
        for (FieldOverlayField field : this.fields) {
            if (field.getId() == id) {
                return field;
            }
        }
        return null;
    }


    /**
     * Starts showing the border of the field with given id.
     *
     * @param id the field which should be selected (border shown)
     */
    public void showBorderById(int id) {
        this.getById(id).showBorder();
    }

    /**
     * Hides the border of the field with given id.
     *
     * @param id the field which should be unselected (border not shown)
     */
    public void hideBorderById(int id) {
        this.getById(id).hideBorder();
    }

    /**
     * Hides all field's borders (unselects them).
     */
    public void hideBorderAll() {
        for (FieldOverlayField overlayField : this.fields) {
            overlayField.hideBorder();
        }
    }


    /**
     * Gets the number of fields in the list.
     *
     * @return the number of fields in the list
     */
    public int getSize() {
        return this.fields.size();
    }

    public void dispose() {
    }
}