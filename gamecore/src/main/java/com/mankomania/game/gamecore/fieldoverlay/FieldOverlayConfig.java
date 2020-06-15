package com.mankomania.game.gamecore.fieldoverlay;

/**
 * Holds the configuration of the positions in design of the overlay fields
 */
public final class FieldOverlayConfig {
    // TODO: implement an extern config that gets loaded
    public static final int BOX_WIDTH = 120;
    public static final int MARGIN_TOP = 120;
    public static final int MARGIN_BETWEEN = 20;
    public static final int SPLIT_HEIGHT = 20;
    public static final int COLUMN_WIDTH = BOX_WIDTH / 2 + MARGIN_BETWEEN / 2;

    // if the values used are constant, we dont actually need an extra function to calculate it
    // may not forget to change in change back to the function if those values above will change
    public static final int SPLIT_MARGIN_TOP = MARGIN_TOP - BOX_WIDTH / 2 - SPLIT_HEIGHT; // the upper row one
    /*public static int getSplitMarginTop() {
        return MARGIN_TOP - BOX_WIDTH / 2 - SPLIT_HEIGHT;
    }*/

    public static final int SPLIT_MARGIN_TOP_ALTERNATE = MARGIN_TOP + BOX_WIDTH / 2 + SPLIT_HEIGHT; // the bottom row one
   /*public static int getSplitMarginTopSecondRow() {
        return MARGIN_TOP + BOX_WIDTH / 2 + SPLIT_HEIGHT;
    }*/

    /* BEGIN CONFIG FOR BORDER */
    public static final int BORDER_SIZE = 9; // only counts the size the border is bigger than the field (!)
    public static final float BORDER_BLINK_SPEED = 0.016f;
    public static final float BORDER_MIN_ALPHA = 0.35f;
    public static final float BORDER_MAX_ALPHA = 1f;
}
