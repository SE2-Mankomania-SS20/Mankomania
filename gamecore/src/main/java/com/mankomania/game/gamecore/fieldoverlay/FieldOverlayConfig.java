package com.mankomania.game.gamecore.fieldoverlay;

/**
 * Holds the configuration of the positions in design of the overlay fields
 */
public final class FieldOverlayConfig {
    // TODO: implement an extern config that gets loaded
    public static int BOX_WIDTH = 120;
    public static int MARGIN_TOP =  120;
    public static int MARGIN_BETWEEN =  20;
    public static int SPLIT_HEIGHT = 20;

    // if the values used are constant, we dont actually need an extra function to calculate it
    // may not forget to change in change back to the function if those values above will change
    public static int SPLIT_MARGIN_TOP = MARGIN_TOP - BOX_WIDTH / 2 - SPLIT_HEIGHT;
    /*public static int getSplitMarginTop() {
        return MARGIN_TOP - BOX_WIDTH / 2 - SPLIT_HEIGHT;
    }*/

    public static int SPLIT_MARGIN_TOP_ALTERNATE = MARGIN_TOP + BOX_WIDTH / 2 + SPLIT_HEIGHT;
   /*public static int getSplitMarginTopSecondRow() {
        return MARGIN_TOP + BOX_WIDTH / 2 + SPLIT_HEIGHT;
    }*/

    public static int RENDER_DISTANCE_LEFT = 12;
    public static int RENDER_DISTANCE_RIGHT = 25;

    public static int PADDING_LEFT = BOX_WIDTH * 2;


    /* BEGIN CONFIG FOR BORDER */
    public static int BORDER_SIZE = 9; // only counts the size the border is bigger than the field (!)
    public static float BORDER_BLINK_SPEED = 0.016f;
    public static float BORDER_MIN_ALPHA = 0.35f;
    public static float BORDER_MAX_ALPHA = 1f;
}
