package com.mankomania.game.gamecore.fieldoverlay;

import com.mankomania.game.gamecore.fields.Field;

import java.util.ArrayList;
import java.util.List;

/**
 * this class loads and holds the array of FieldOverlayFields
 */
public class FieldOverlayData {
    ArrayList<FieldOverlayField> fields;
    FieldOverlayTextures fieldOverlayTextures;

    public FieldOverlayData() {
        this.fields = new ArrayList<>();
        this.fieldOverlayTextures = new FieldOverlayTextures();
    }

    /**
     * Creates and maintains a list of FieldOverlayFields, constructed with the given base field list.
     * @param baseFields a list of Field objects representing the fields on the board
     */
    public void create(List<Field> baseFields) {
        this.fieldOverlayTextures.create();

        for (Field field : baseFields) {
            FieldOverlayField newField = new FieldOverlayField(field);
            newField.create(this.fieldOverlayTextures);
            this.fields.add(newField);
        }
    }

    /**
     * returns a FieldOverlayField instance with a given id
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
     * Gets the number of fields in the list.
     * @return the number of fields in the list
     */
    public int getSize() {
        return this.fields.size();
    }

    public void dispose() {
        this.fieldOverlayTextures.dispose();
    }
}