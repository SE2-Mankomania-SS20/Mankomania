package com.mankomania.game.gamecore.screens.slots;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mankomania.game.gamecore.MankomaniaGame;

/**
 * This actor will handle and render the three rolls ("Walzen") of the slots minigame.
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

        // set the handler for stopping the first row using lambda function syntax.

    }

    @Override
    public void act(float delta) {
        this.iconRow1.update(delta);
        this.iconRow2.update(delta);
        this.iconRow3.update(delta);

        if (Gdx.input.justTouched()) {
            MankomaniaGame.getMankomaniaGame().getNetworkClient().getMessageHandler().sendSpinRollsMessage();
            Gdx.app.log("slots", "Stopped the slots!");
        }

        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        this.iconRow1.render(batch);
        this.iconRow2.render(batch);
        this.iconRow3.render(batch);

        batch.draw(this.slotTextures.getSlotBackgroundTexture(), 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public void stopRolls(int[] rollValues) {
        this.iconRow1.stopAt(rollValues[0]);

        this.iconRow1.setStoppedTask(() -> this.iconRow2.stopAt(rollValues[1]));

        this.iconRow2.setStoppedTask(() -> this.iconRow3.stopAt(rollValues[2]));

        this.iconRow3.setStoppedTask(() -> Gdx.app.debug("slots", "SLOT 3 (last one) stopped as well."));
    }
}
