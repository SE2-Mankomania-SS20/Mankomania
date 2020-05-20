package com.mankomania.game.gamecore.fieldoverlay.fielddata;

/**
 * This class is used as a "wrapper" for the "overlay_fieldinfos.json" file.
 * Data from said file gets loaded straight into an instance of this class, hence
 * its simple structure.
 */
public class FieldOverlayFieldInfoData {
    private FieldOverlayFieldInfo[] data;

    public int getLength() {
        if (this.data == null)
            return -1;
        return this.data.length;
    }

    public FieldOverlayFieldInfo getFieldInfoById(int id) {
        for (int i = 0; i < this.data.length; i++) {
            if (this.data[i].getId() == id) {
                return this.data[i];
            }
        }
        return null;
    }

    public FieldOverlayFieldInfo[] getData() {
        return this.data;
    }
}
