package com.mankomania.game.gamecore.screens.slots;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mankomania.game.gamecore.screens.AbstractScreen;

/**
 * Screen that displays the whole Slots minigame.
 */
public class SlotsScreen extends AbstractScreen {
    private Stage mainStage;

    public SlotsScreen() {
        this.mainStage = new Stage();

        SlotGameActor slotActor = new SlotGameActor();
        slotActor.setPosition(300f, 300f);
        slotActor.setSize(600f, 600);

        this.mainStage.addActor(slotActor);
    }

    @Override
    public void render(float delta) {
        this.mainStage.act(delta);
        this.mainStage.draw();
    }

    @Override
    public void dispose() {
        this.mainStage.dispose();
    }
}
