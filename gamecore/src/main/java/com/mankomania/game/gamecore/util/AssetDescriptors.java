package com.mankomania.game.gamecore.util;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;


public class AssetDescriptors{

    public static AssetManager manager = new AssetManager();

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

    //Fonts
    public static final AssetDescriptor<BitmapFont> BELEREN =
            new AssetDescriptor<BitmapFont>(AssetPaths.BELEREN, BitmapFont.class);

    //skin fonts
    public static final AssetDescriptor<BitmapFont> FONT =
            new AssetDescriptor<BitmapFont>(AssetPaths.FONT, BitmapFont.class);

    public static final AssetDescriptor<BitmapFont> FONTEXPORT =
            new AssetDescriptor<BitmapFont>(AssetPaths.FONTEXPORT, BitmapFont.class);

    public static final AssetDescriptor<BitmapFont> GIYGAS =
            new AssetDescriptor<BitmapFont>(AssetPaths.GIYGAS, BitmapFont.class);

    public static final AssetDescriptor<BitmapFont> SATURN =
            new AssetDescriptor<BitmapFont>(AssetPaths.SATURN, BitmapFont.class);

    //skin png
    public static final AssetDescriptor<Texture> TERRAMOTHER =
            new AssetDescriptor<Texture>(AssetPaths.TERRAMOTHER, Texture.class);

    //skin
    public static final AssetDescriptor<Skin> BACKGROUND =
            new AssetDescriptor<Skin>(AssetPaths.BACKGROUND, Skin.class);


    //Board
    public static final AssetDescriptor<Texture> LOGO =
            new AssetDescriptor<Texture>(AssetPaths.LOGO, Texture.class);


    public void dispose(){
        manager.dispose();
    }

}
