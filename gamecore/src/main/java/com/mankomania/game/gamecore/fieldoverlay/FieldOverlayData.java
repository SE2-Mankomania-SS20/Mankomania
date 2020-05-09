package com.mankomania.game.gamecore.fieldoverlay;

import com.mankomania.game.gamecore.fields.Field;

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
     * @param baseFields a list of Field objects representing the fields on the board
     */
    public void create(List<Field> baseFields, FieldOverlayTextures fieldOverlayTextures) {
        for (Field field : baseFields) {
            FieldOverlayField newField = new FieldOverlayField(field);
            newField.create(fieldOverlayTextures);
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
     * Starts showing the border of the field with given id.
     * @param id the field which should be selected (border shown)
     */
    public void showBorderById(int id) {
        this.getById(id).showBorder();
    }

    /**
     * Hides the border of the field with given id.
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
     * @return the number of fields in the list
     */
    public int getSize() {
        return this.fields.size();
    }

    public void dispose() { }
}