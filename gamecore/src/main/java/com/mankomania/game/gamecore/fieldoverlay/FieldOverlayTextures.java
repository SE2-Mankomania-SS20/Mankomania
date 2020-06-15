package com.mankomania.game.gamecore.fieldoverlay;

import com.badlogic.gdx.graphics.Texture;
import com.mankomania.game.gamecore.MankomaniaGame;
import com.mankomania.game.gamecore.util.AssetDescriptors;

/**
 * this class is used to load and hold the various textures required for drawing the overlay
 */
public class FieldOverlayTextures {
    private Texture fieldOrange;
    private Texture fieldBlue;
    private Texture fieldYellow;
    private Texture fieldWhite;
    private Texture fieldMagenta;
    private Texture fieldBorder;
    private Texture textBoxBorder;
    private Texture textBoxInner;

    private PlayerDotTextures playerDotTextures;


    public void create() {
        fieldWhite = MankomaniaGame.getMankomaniaGame().getManager().get(AssetDescriptors.WHITE);
        fieldOrange = MankomaniaGame.getMankomaniaGame().getManager().get(AssetDescriptors.ORANGE);
        fieldBlue = MankomaniaGame.getMankomaniaGame().getManager().get(AssetDescriptors.BLUE);
        fieldYellow = MankomaniaGame.getMankomaniaGame().getManager().get(AssetDescriptors.YELLOW);
        fieldMagenta = MankomaniaGame.getMankomaniaGame().getManager().get(AssetDescriptors.MAGENTA);


        fieldBorder = MankomaniaGame.getMankomaniaGame().getManager().get(AssetDescriptors.SELECTED_BORDER);
        textBoxBorder = MankomaniaGame.getMankomaniaGame().getManager().get(AssetDescriptors.BORDER);
        textBoxInner = MankomaniaGame.getMankomaniaGame().getManager().get(AssetDescriptors.FILLING);

        playerDotTextures = new PlayerDotTextures();
        playerDotTextures.create();
    }

    public Texture getFieldBorder() {
        return fieldBorder;
    }

    public Texture getTextBoxBorder() {
        return textBoxBorder;
    }

    public Texture getTextBoxInner() {
        return textBoxInner;
    }

    public Texture getFieldOrange() {
        return fieldOrange;
    }

    public Texture getFieldBlue() {
        return fieldBlue;
    }

    public Texture getFieldYellow() {
        return fieldYellow;
    }

    public Texture getFieldWhite() {
        return fieldWhite;
    }

    public Texture getFieldMagenta() {
        return fieldMagenta;
    }

    public PlayerDotTextures getPlayerDotTextures() {
        return playerDotTextures;
    }

    public class PlayerDotTextures {
        private Texture playerDotBlue;
        private Texture playerDotGreen;
        private Texture playerDotRed;
        private Texture playerDotYellow;

        public void create() {
            playerDotBlue = MankomaniaGame.getMankomaniaGame().getManager().get(AssetDescriptors.PLAYER_DOT_BLUE);
            playerDotGreen = MankomaniaGame.getMankomaniaGame().getManager().get(AssetDescriptors.PLAYER_DOT_GREEN);
            playerDotRed = MankomaniaGame.getMankomaniaGame().getManager().get(AssetDescriptors.PLAYER_DOT_RED);
            playerDotYellow = MankomaniaGame.getMankomaniaGame().getManager().get(AssetDescriptors.PLAYER_DOT_YELLOW);
        }

        public Texture getPlayerDotBlue() {
            return playerDotBlue;
        }

        public Texture getPlayerDotGreen() {
            return playerDotGreen;
        }

        public Texture getPlayerDotRed() {
            return playerDotRed;
        }

        public Texture getPlayerDotYellow() {
            return playerDotYellow;
        }
    }
}
