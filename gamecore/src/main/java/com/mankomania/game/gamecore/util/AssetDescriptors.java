package com.mankomania.game.gamecore.util;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;


public class AssetDescriptors {

    private AssetDescriptors() {
        throw new IllegalStateException(); //utility class should never be instantiated
    }

    //Field Overlay
    public static final AssetDescriptor<Texture> EMPTY =
            new AssetDescriptor<>(AssetPaths.EMPTY, Texture.class);

    public static final AssetDescriptor<Texture> BLUE =
            new AssetDescriptor<>(AssetPaths.BLUE, Texture.class);

    public static final AssetDescriptor<Texture> MAGENTA =
            new AssetDescriptor<>(AssetPaths.MAGENTA, Texture.class);

    public static final AssetDescriptor<Texture> ORANGE =
            new AssetDescriptor<>(AssetPaths.ORANGE, Texture.class);

    public static final AssetDescriptor<Texture> WHITE =
            new AssetDescriptor<>(AssetPaths.WHITE, Texture.class);

    public static final AssetDescriptor<Texture> YELLOW =
            new AssetDescriptor<>(AssetPaths.YELLOW, Texture.class);

    public static final AssetDescriptor<Texture> BORDER =
            new AssetDescriptor<>(AssetPaths.BORDER, Texture.class);

    public static final AssetDescriptor<Texture> BORDER_v2 =
            new AssetDescriptor<>(AssetPaths.BORDER_V2, Texture.class);

    public static final AssetDescriptor<Texture> FILLING =
            new AssetDescriptor<>(AssetPaths.FILLING, Texture.class);

    public static final AssetDescriptor<Texture> FILLING_v2 =
            new AssetDescriptor<>(AssetPaths.FILLING_V2, Texture.class);

    public static final AssetDescriptor<Texture> SELECTED_BORDER =
            new AssetDescriptor<>(AssetPaths.SELECTED_BORDER, Texture.class);

    //HUD
    public static final AssetDescriptor<Texture> BACK =
            new AssetDescriptor<>(AssetPaths.BACK, Texture.class);

    public static final AssetDescriptor<Texture> CHAT =
            new AssetDescriptor<>(AssetPaths.CHAT, Texture.class);

    public static final AssetDescriptor<Texture> DICE =
            new AssetDescriptor<>(AssetPaths.DICE, Texture.class);

    public static final AssetDescriptor<Texture> OPTIONS =
            new AssetDescriptor<>(AssetPaths.OPTIONS, Texture.class);

    public static final AssetDescriptor<Texture> OVERLAY =
            new AssetDescriptor<>(AssetPaths.OVERLAY, Texture.class);

    //Player
    public static final AssetDescriptor<Model> PLAYER_BLUE =
            new AssetDescriptor<>(AssetPaths.PLAYER_BLUE, Model.class);

    public static final AssetDescriptor<Model> PLAYER_GREEN =
            new AssetDescriptor<>(AssetPaths.PLAYER_GREEN, Model.class);

    public static final AssetDescriptor<Model> PLAYER_RED =
            new AssetDescriptor<>(AssetPaths.PLAYER_RED, Model.class);

    public static final AssetDescriptor<Model> PLAYER_YELLOW =
            new AssetDescriptor<>(AssetPaths.PLAYER_YELLOW, Model.class);

    //Board
    public static final AssetDescriptor<Model> BOARD =
            new AssetDescriptor<>(AssetPaths.BOARD, Model.class);


    //Fonts
    public static final AssetDescriptor<BitmapFont> BELEREN =
            new AssetDescriptor<>(AssetPaths.BELEREN, BitmapFont.class);

    public static final AssetDescriptor<BitmapFont> BELEREN_SMALL =
            new AssetDescriptor<>(AssetPaths.BELEREN_SMALL, BitmapFont.class);

    //skin
    public static final AssetDescriptor<Skin> SKIN =
            new AssetDescriptor<>(AssetPaths.SKIN, Skin.class);

    //AktienBoerse
    public static final AssetDescriptor<Texture> GEWONNEN =
            new AssetDescriptor<>(AssetPaths.GEWONNEN, Texture.class);
    public static final AssetDescriptor<Texture> GEWONNENB =
            new AssetDescriptor<>(AssetPaths.GEWONNENB, Texture.class);
    public static final AssetDescriptor<Texture> GEWONNENK =
            new AssetDescriptor<>(AssetPaths.GEWONNENK, Texture.class);
    public static final AssetDescriptor<Texture> GEWONNENT =
            new AssetDescriptor<>(AssetPaths.GEWONNENT, Texture.class);
    public static final AssetDescriptor<Texture> VERLORENB =
            new AssetDescriptor<>(AssetPaths.VERLORENB, Texture.class);
    public static final AssetDescriptor<Texture> VERLORENK =
            new AssetDescriptor<>(AssetPaths.VERLORENK, Texture.class);
    public static final AssetDescriptor<Texture> VERLORENT =
            new AssetDescriptor<>(AssetPaths.VERLORENT, Texture.class);

    // Hotel Renderer
    public static final AssetDescriptor<Model> HOTEL_MODEL =
            new AssetDescriptor<>(AssetPaths.HOTEL_MODEL, Model.class);
    public static final AssetDescriptor<Model> HOTEL_FLAG_BLUE =
            new AssetDescriptor<>(AssetPaths.HOTEL_FLAG_BLUE, Model.class);
    public static final AssetDescriptor<Model> HOTEL_FLAG_GREEN =
            new AssetDescriptor<>(AssetPaths.HOTEL_FLAG_GREEN, Model.class);
    public static final AssetDescriptor<Model> HOTEL_FLAG_RED =
            new AssetDescriptor<>(AssetPaths.HOTEL_FLAG_RED, Model.class);
    public static final AssetDescriptor<Model> HOTEL_FLAG_YELLOW =
            new AssetDescriptor<>(AssetPaths.HOTEL_FLAG_YELLOW, Model.class);

}
