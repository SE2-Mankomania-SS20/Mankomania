package com.mankomania.game.gamecore.fieldoverlay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Json;
import com.mankomania.game.gamecore.fieldoverlay.fielddata.FieldOverlayFieldColumn;
import com.mankomania.game.gamecore.fieldoverlay.fielddata.FieldOverlayFieldColumnData;
import com.mankomania.game.gamecore.fieldoverlay.fielddata.FieldOverlayFieldInfo;
import com.mankomania.game.gamecore.fieldoverlay.fielddata.FieldOverlayFieldInfoData;
import com.mankomania.game.core.fields.types.Field;
import com.mankomania.game.gamecore.util.ScreenManager;

import java.util.ArrayList;
import java.util.HashMap;

import static com.mankomania.game.gamecore.fieldoverlay.FieldOverlayConfig.COLUMN_WIDTH;

/**
 * this class loads and holds the array of FieldOverlayFields
 */
class FieldOverlayData {
    private ArrayList<FieldOverlayField> fields;
    private FieldOverlayFieldColumnData columnData;

    private static final int columnCountToShow = 30;
    private static final float columnBreakLeft = 0; //0 - 2 * COLUMN_WIDTH; //0 - 2 * COLUMN_WIDTH; // positons where the fields start and stop (a bit outside of the screen, so updating is invisbile)
    private static final float columnBreakRight = columnBreakLeft + columnCountToShow * COLUMN_WIDTH;
    private float totalScrollValue = 0;

    private ArrayList<FieldOverlayFieldColumn> shownColumns;

    public FieldOverlayData() {
        this.fields = new ArrayList<>();
    }

    /**
     * Creates and maintains a list of FieldOverlayFields, constructed with the given base field list.
     */
    public void create(FieldOverlayTextures fieldOverlayTextures) {
        // load positionings for fields from json
        Json json = new Json();
        FieldOverlayFieldInfoData fieldInfoData = json.fromJson(FieldOverlayFieldInfoData.class,
                Gdx.files.internal("fieldoverlay/overlay_fieldinfos.json").read());


        Field[] baseFieldData = ScreenManager.getInstance().getGame().getGameData().getFields();

        // create hash map to map column id to list of fields
        HashMap<Integer, ArrayList<FieldOverlayField>> columnMap = new HashMap<>();
        FieldOverlayFieldInfo[] fieldInfos = fieldInfoData.getData();

        for (int i = 0; i < 78; i++) {
            FieldOverlayField newField = new FieldOverlayField(baseFieldData[i], fieldInfos[i], i);
            newField.create(fieldOverlayTextures);
            this.fields.add(newField);
            if (columnMap.get(newField.getColumn()) == null) {
                columnMap.put(newField.getColumn(), new ArrayList<>());
            }
            columnMap.get(newField.getColumn()).add(newField);
        }

        this.columnData = new FieldOverlayFieldColumnData();
        this.columnData.create(columnMap);

        // show columns is a list that holds references to the currently shown fields
        this.shownColumns = new ArrayList<>();
        FieldOverlayFieldColumn currentColumn;
        for (int i = 0; i < this.columnCountToShow; i++) {
            currentColumn = this.columnData.getColumnById(i);
            currentColumn.setPositionX((float)COLUMN_WIDTH * i);
            this.shownColumns.add(currentColumn);
        }

    }

    /**
     * Renders the columns currently shown.
     * @param batch SpriteBatch used for rendering
     */
    public void renderColumns(SpriteBatch batch) {
        for (FieldOverlayFieldColumn column : this.shownColumns) {
            column.renderFields(batch);
        }
    }

    /**
     * Moves the fields to the left (positive value) or to the right (negative value).
     * @param value how much scrolling you want
     */
    public void moveColumns(float value) {
        // move each column
        for (FieldOverlayFieldColumn column : this.shownColumns) {
            column.movePositionX(value);
        }

        FieldOverlayFieldColumn leftmostColumn = this.shownColumns.get(0);
        FieldOverlayFieldColumn rightmostColumn = this.shownColumns.get(this.shownColumns.size() - 1);

        float leftmostPosition = leftmostColumn.getPositionX();
        float rightmostPosition = rightmostColumn.getPositionX();

        while (leftmostPosition < this.columnBreakLeft || rightmostPosition > this.columnBreakRight) {

            // check if first or last element is over the boundaries
            // if column is over boundaries, remove it from the list and
            // add the following field on the other side
            // TODO: check in a while loop if there are fields needed to be removed (enables faster scrolling)
            if (leftmostPosition < this.columnBreakLeft) {
                this.shownColumns.remove(0);
                int nextFieldRightId = (rightmostColumn.getColumnId() + 1) % 132;
                FieldOverlayFieldColumn nextRightField = this.columnData.getColumnById(nextFieldRightId);
                nextRightField.setPositionX(this.columnBreakRight + leftmostPosition);
                this.shownColumns.add(nextRightField);
            }
            if (rightmostPosition > this.columnBreakRight) {
                this.shownColumns.remove(this.shownColumns.size() - 1);
                int nextFieldLeftId = (leftmostColumn.getColumnId() - 1) % 132;
                if (nextFieldLeftId < 0) nextFieldLeftId = 132 + nextFieldLeftId;
                FieldOverlayFieldColumn nextLeftField = this.columnData.getColumnById(nextFieldLeftId);
                nextLeftField.setPositionX(this.columnBreakLeft + (rightmostPosition - this.columnBreakRight));
                this.shownColumns.add(0, nextLeftField);
            }

            leftmostColumn = this.shownColumns.get(0);
            rightmostColumn = this.shownColumns.get(this.shownColumns.size() - 1);

            leftmostPosition = leftmostColumn.getPositionX();
            rightmostPosition = rightmostColumn.getPositionX();
        }

        this.totalScrollValue += value;
    }

    public void moveColumnsTo(float value) {
        float difference = value - this.totalScrollValue;

        this.moveColumns(difference);
    }


    /**
     * Returns the field, that overlaps with given point.
     *
     * @param touchX point x value
     * @param touchY point y value
     * @return FieldOverlayField that is "hit" by the given point
     */
    public FieldOverlayField getTouchedField(int touchX, int touchY) {
        for (FieldOverlayFieldColumn column : this.shownColumns) {
            FieldOverlayField foundField;
            if ((foundField = column.getTouchedField(touchX, touchY)) != null) {
                return foundField;
            }
        }
        return null;
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

    /**
     * Disposes all of the stuff neede. Still empty, because no data is to be disposed here yet.
     */
    public void dispose() {

    }

    public float getTotalScrollValue() {
        return this.totalScrollValue;
    }
}