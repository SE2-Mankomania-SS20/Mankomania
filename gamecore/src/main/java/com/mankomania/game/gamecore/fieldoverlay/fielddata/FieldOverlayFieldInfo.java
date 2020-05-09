package com.mankomania.game.gamecore.fieldoverlay.fielddata;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

/**
 * This class is used as "blueprint" for deserialize JSON objects containing information on how
 * to position the fields in the fieldoverlay.
 */
public class FieldOverlayFieldInfo implements Json.Serializable {
    private int id;
    private int column;
    private FieldOverlayRowPosition rowPosition;

    @Override
    public void write(Json json) {
        // this method will never be used, since we dont export objects to JSON whatsoever
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        // this method gets called by the JSON reader. we process and store the json values here the way we want them.
        this.id = jsonData.get("id").asInt() - 1; // subtract one to compensate for the numbering we started with... (start indexing with 1), I want it to be the same everythere, thats why I kept this
        this.column = jsonData.get("column").asInt();

        switch (jsonData.get("rowpos").asString().toLowerCase()) {
            case "top": this.rowPosition = FieldOverlayRowPosition.TOP; break;
            case "mid": this.rowPosition = FieldOverlayRowPosition.MIDDLE; break;
            case "bot": this.rowPosition = FieldOverlayRowPosition.BOTTOM; break;
            default: throw new IllegalArgumentException("rowpos could not be found/parsed while loading the FieldOverlay field information");
        }
    }

    public int getId() {
        return id;
    }

    public int getColumn() {
        return column;
    }

    public FieldOverlayRowPosition getRowPosition() {
        return rowPosition;
    }

    @Override
    public String toString() {
        return "FieldOverlayFieldInfo{" +
                "id=" + id +
                ", column=" + column +
                ", rowPosition=" + rowPosition +
                '}';
    }
}
