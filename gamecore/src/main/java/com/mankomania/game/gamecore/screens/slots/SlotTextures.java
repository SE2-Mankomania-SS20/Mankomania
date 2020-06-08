package com.mankomania.game.gamecore.screens.slots;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mankomania.game.gamecore.MankomaniaGame;
import com.mankomania.game.gamecore.util.AssetDescriptors;

/**
 * This class holds all textures and texture regions needed by the slots minigame.
 */
public class SlotTextures {
    private final Texture slotIconTexture;

    // texture regions to specify regions to only render a small rectangle out of the whole texture atlas
    // each row texture region contains three icons
    private final TextureRegion slotTextureFirstRow;
    private final TextureRegion slotTextureSecondRow;
    private final TextureRegion slotTextureThirdRow;
    // need to have one specific icon doubled in order to make a smooth, seemless animation
    private final TextureRegion slotTextureDuplicatIcon;

    public SlotTextures() {
        // load the slot icon texture containing all icons
        this.slotIconTexture = MankomaniaGame.getMankomaniaGame().getManager().get(AssetDescriptors.SLOT_ICONS);

        // setting up texture regions: each icon is sized 400x400 pixel, so a row is 400x1200 pixel
        this.slotTextureFirstRow = new TextureRegion(this.slotIconTexture, 0, 0, 400, 1200);
        this.slotTextureSecondRow = new TextureRegion(this.slotIconTexture, 400, 0, 400, 1200);
        this.slotTextureThirdRow = new TextureRegion(this.slotIconTexture, 800, 0, 400, 1200);
        this.slotTextureDuplicatIcon = new TextureRegion(this.slotIconTexture, 0, 800, 400, 400);
    }

    public TextureRegion getSlotTextureFirstRow() {
        return slotTextureFirstRow;
    }

    public TextureRegion getSlotTextureSecondRow() {
        return slotTextureSecondRow;
    }

    public TextureRegion getSlotTextureThirdRow() {
        return slotTextureThirdRow;
    }

    public TextureRegion getSlotTextureDuplicatIcon() {
        return slotTextureDuplicatIcon;
    }
}
