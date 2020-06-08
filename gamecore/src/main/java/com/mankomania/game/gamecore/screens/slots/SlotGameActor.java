package com.mankomania.game.gamecore.screens.slots;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * This actor will handle and render the three "Walzen" of the slots minigame.
 */
public class SlotGameActor extends Actor {
    private final SlotTextures slotTextures;

    public SlotGameActor() {
        this.slotTextures = new SlotTextures();
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
