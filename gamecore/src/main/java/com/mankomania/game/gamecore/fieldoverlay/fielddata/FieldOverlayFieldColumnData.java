package com.mankomania.game.gamecore.fieldoverlay.fielddata;

import com.mankomania.game.gamecore.fieldoverlay.FieldOverlayField;

import java.util.ArrayList;
import java.util.Map;

/**
 * Class that loads and holds all of the columns.
 */
public class FieldOverlayFieldColumnData {
    private FieldOverlayFieldColumn[] columns;

    public void create(Map<Integer, ArrayList<FieldOverlayField>> fieldMap) {
        this.columns = new FieldOverlayFieldColumn[132];

        // iterate over all columns existing (field count * 2)
        for (int i = 0; i < 132; i++) {
            FieldOverlayFieldColumn columnToAdd;
            if (fieldMap.containsKey(i)) {
                ArrayList<FieldOverlayField> fieldsInColumn = fieldMap.get(i);

                if (fieldsInColumn.size() == 1) {
                    columnToAdd = new FieldOverlayFieldColumn(i, fieldsInColumn.get(0));
                } else {
                    columnToAdd = new FieldOverlayFieldColumn(i, fieldsInColumn.get(0), fieldsInColumn.get(1));
                }
            } else {
                columnToAdd = new FieldOverlayFieldColumn(i);
            }
            this.columns[i] = columnToAdd;
        }
    }

    public FieldOverlayFieldColumn getColumnById(int id) {
        return this.columns[id];
    }
}
