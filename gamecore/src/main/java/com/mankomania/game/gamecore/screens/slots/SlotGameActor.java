package com.mankomania.game.gamecore.screens.slots;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * This actor will handle and render the three "Walzen" of the slots minigame.
 */
public class SlotGameActor extends Actor {
    private final SlotTextures slotTextures;
    private final SlotIconRow iconRow1;
    private final SlotIconRow iconRow2;
    private final SlotIconRow iconRow3;

    public SlotGameActor() {
        this.slotTextures = new SlotTextures();

        this.iconRow1 = new SlotIconRow(this.slotTextures, 276, 372);
        this.iconRow2 = new SlotIconRow(this.slotTextures, 640, 372);
        this.iconRow3 = new SlotIconRow(this.slotTextures, 1004, 372);
    }

    @Override
    public void act(float delta) {
        this.iconRow1.update(delta);
        this.iconRow2.update(delta);
        this.iconRow3.update(delta);
        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        this.iconRow1.render(batch);
        this.iconRow2.render(batch);
        this.iconRow3.render(batch);

        batch.draw(this.slotTextures.getSlotBackgroundTexture(), 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }
}
