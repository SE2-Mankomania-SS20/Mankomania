package com.mankomania.game.gamecore.util;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;


public class AssetDescriptors{

    //Field Overlay
    public static final AssetDescriptor<Texture> EMPTY =
            new AssetDescriptor<Texture>(AssetPaths.EMPTY, Texture.class);

    public static final AssetDescriptor<Texture> BLUE =
            new AssetDescriptor<Texture>(AssetPaths.BLUE, Texture.class);

    public static final AssetDescriptor<Texture> MAGENTA =
            new AssetDescriptor<Texture>(AssetPaths.MAGENTA, Texture.class);

    public static final AssetDescriptor<Texture> ORANGE =
            new AssetDescriptor<Texture>(AssetPaths.ORANGE, Texture.class);

    public static final AssetDescriptor<Texture> WHITE =
            new AssetDescriptor<Texture>(AssetPaths.WHITE, Texture.class);

    public static final AssetDescriptor<Texture> YELLOW =
            new AssetDescriptor<Texture>(AssetPaths.YELLOW, Texture.class);

    public static final AssetDescriptor<Texture> BORDER =
            new AssetDescriptor<Texture>(AssetPaths.BORDER, Texture.class);

    public static final AssetDescriptor<Texture> BORDER_v2 =
            new AssetDescriptor<Texture>(AssetPaths.BORDER_V2, Texture.class);

    public static final AssetDescriptor<Texture> FILLING =
            new AssetDescriptor<Texture>(AssetPaths.FILLING, Texture.class);

    public static final AssetDescriptor<Texture> FILLING_v2 =
            new AssetDescriptor<Texture>(AssetPaths.FILLING_V2, Texture.class);

    public static final AssetDescriptor<Texture> SELECTED_BORDER =
            new AssetDescriptor<Texture>(AssetPaths.SELECTED_BORDER, Texture.class);

    //HUD
    public static final AssetDescriptor<Texture> BACK =
            new AssetDescriptor<Texture>(AssetPaths.BACK, Texture.class);

    public static final AssetDescriptor<Texture> CHAT =
            new AssetDescriptor<Texture>(AssetPaths.CHAT, Texture.class);

    public static final AssetDescriptor<Texture> DICE =
            new AssetDescriptor<Texture>(AssetPaths.DICE, Texture.class);

    public static final AssetDescriptor<Texture> OPTIONS =
            new AssetDescriptor<Texture>(AssetPaths.OPTIONS, Texture.class);

    public static final AssetDescriptor<Texture> OVERLAY =
            new AssetDescriptor<Texture>(AssetPaths.OVERLAY, Texture.class);

    //Player
    public static final AssetDescriptor<Texture> PLAYER_BLUE =
            new AssetDescriptor<Texture>(AssetPaths.PLAYER_BLUE, Texture.class);

    public static final AssetDescriptor<Texture> PLAYER_GREEN =
            new AssetDescriptor<Texture>(AssetPaths.PLAYER_GREEN, Texture.class);

    public static final AssetDescriptor<Texture> PLAYER_RED =
            new AssetDescriptor<Texture>(AssetPaths.PLAYER_RED, Texture.class);

    public static final AssetDescriptor<Texture> PLAYER_YELLOW =
            new AssetDescriptor<Texture>(AssetPaths.PLAYER_YELLOW, Texture.class);

    //Board
    public static final AssetDescriptor<Texture> BOARD =
            new AssetDescriptor<Texture>(AssetPaths.BOARD, Texture.class);


    //Fonts
    public static final AssetDescriptor<BitmapFont> BELEREN =
            new AssetDescriptor<BitmapFont>(AssetPaths.BELEREN, BitmapFont.class);

    public static final AssetDescriptor<BitmapFont> BELEREN_SMALL =
            new AssetDescriptor<BitmapFont>(AssetPaths.BELEREN_SMALL, BitmapFont.class);

    //skin
    public static final AssetDescriptor<Texture> MANKOMANIA =
            new AssetDescriptor<Texture>(AssetPaths.MANKOMANIA, Texture.class);

    public static final AssetDescriptor<Texture> BUTTON1 =
            new AssetDescriptor<Texture>(AssetPaths.BUTTON1, Texture.class);


    //Assets die nicht mehr im Ordner sind?
    public static final AssetDescriptor<BitmapFont> FONT =
            new AssetDescriptor<BitmapFont>(AssetPaths.FONT, BitmapFont.class);

    public static final AssetDescriptor<BitmapFont> FONTEXPORT =
            new AssetDescriptor<BitmapFont>(AssetPaths.FONTEXPORT, BitmapFont.class);

    public static final AssetDescriptor<BitmapFont> GIYGAS =
            new AssetDescriptor<BitmapFont>(AssetPaths.GIYGAS, BitmapFont.class);

    public static final AssetDescriptor<BitmapFont> SATURN =
            new AssetDescriptor<BitmapFont>(AssetPaths.SATURN, BitmapFont.class);

    public static final AssetDescriptor<Texture> TERRAMOTHER =
            new AssetDescriptor<Texture>(AssetPaths.TERRAMOTHER, Texture.class);

    //Asset für den Hintergrund der Screens
    public static final AssetDescriptor<Skin> BACKGROUND =
            new AssetDescriptor<Skin>(AssetPaths.BACKGROUND, Skin.class);






}
