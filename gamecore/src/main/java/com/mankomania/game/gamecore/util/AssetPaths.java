package com.mankomania.game.gamecore.util;

public class AssetPaths {

    private AssetPaths() {
        throw new IllegalStateException(); //utility class should never be instantiated
    }

    //fieldoverlay
    public static final String EMPTY = "fieldoverlay/field_empty.png";
    public static final String BLUE = "fieldoverlay/field_blue.png";
    public static final String MAGENTA = "fieldoverlay/field_magenta.png";
    public static final String ORANGE = "fieldoverlay/field_orange.png";
    public static final String WHITE = "fieldoverlay/field_white.png";
    public static final String YELLOW = "fieldoverlay/field_yellow.png";
    public static final String BORDER = "fieldoverlay/textbox_border.png";
    public static final String BORDER_V2 = "fieldoverlay/textbox_border_v2.png";
    public static final String FILLING = "fieldoverlay/textbox_filling.png";
    public static final String FILLING_V2 = "fieldoverlay/textbox_filling_v2.png";
    public static final String SELECTED_BORDER = "fieldoverlay/field_selected_border.png";

    //HUD
    public static final String BACK = "hud/back.png";
    public static final String CHAT = "hud/chat.png";
    public static final String DICE = "hud/dice.png";
    public static final String OPTIONS = "hud/options.png";
    public static final String OVERLAY = "hud/overlay.png";

    //Player
    public static final String PLAYER_BLUE = "player/player_blue.g3db";
    public static final String PLAYER_GREEN = "player/player_green.g3db";
    public static final String PLAYER_RED = "player/player_red.g3db";
    public static final String PLAYER_YELLOW = "player/player_yellow.g3db";

    //board
    public static final String BOARD = "board/board.g3db";

    //fonts
    public static final String BELEREN = "fonts/beleren.fnt";
    public static final String BELEREN_SMALL = "fonts/beleren_small.fnt";

    //skin
    public static final String SKIN = "skin/terra-mother-ui.json";

   //AktienBoerse
    public static final String GEWONNEN ="aktien/geld_gewonnen.png";
    public static final String GEWONNENB="aktien/geld_gewonnen_b.png";
    public static final String GEWONNENK ="aktien/geld_gewonnen_k.png";
    public static final String GEWONNENT ="aktien/geld_gewonnen_t.png";
    public static final String VERLORENB ="aktien/geld_verlieren_b.png";
    public static final String VERLORENK ="aktien/geld_verlieren_k.png";
    public static final String VERLORENT ="aktien/geld_verlieren_t.png";

    //HUD

}
