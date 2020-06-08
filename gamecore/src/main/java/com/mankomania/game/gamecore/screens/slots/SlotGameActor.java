package com.mankomania.game.gamecore.screens.slots;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * This actor will handle and render the three "Walzen" of the slots minigame.
 */
public class SlotGameActor extends Actor {
    private final Texture slotIconTexture;

    // texture regions to specify regions to only render a small rectangle out of the whole texture atlas
    // each row texture region contains three icons
    private final TextureRegion slotTextureFirstRow;
    private final TextureRegion slotTextureSecondRow;
    private final TextureRegion slotTextureThirdRow;
    // need to have one specific icon doubled in order to make a smooth, seemless animation
    private final TextureRegion slotTextureDuplicatIcon;

    public SlotGameActor() {
        // load the slot icon texture containing all icons
        this.slotIconTexture = new Texture(Gdx.files.local("slot_icons.png"));

        // setting up texture regions: each icon is sized 400x400 pixel, so a row is 400x1200 pixel
        this.slotTextureFirstRow = new TextureRegion(this.slotIconTexture, 0, 0, 400, 1200);
        this.slotTextureSecondRow = new TextureRegion(this.slotIconTexture, 400, 0, 400, 1200);
        this.slotTextureThirdRow = new TextureRegion(this.slotIconTexture, 800, 0, 400, 1200);
        this.slotTextureDuplicatIcon = new TextureRegion(this.slotIconTexture, 0, 0, 400, 400);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }
}
