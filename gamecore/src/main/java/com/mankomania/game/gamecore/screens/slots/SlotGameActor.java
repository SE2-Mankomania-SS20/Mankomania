package com.mankomania.game.gamecore.screens.slots;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Align;
import com.mankomania.game.gamecore.MankomaniaGame;
import com.mankomania.game.gamecore.util.AssetDescriptors;

/**
 * This actor will handle and render the three rolls ("Walzen") of the slots minigame.
 */
public class SlotGameActor extends Actor {
    private final SlotTextures slotTextures;
    private final SlotIconRow iconRow1;
    private final SlotIconRow iconRow2;
    private final SlotIconRow iconRow3;
    private boolean stopped;

    private final Texture textBoxBorderTexture;
    private final Texture textBoxInnerTexture;
    private final BitmapFont bannerFont;
    private String bannerText;
    private boolean isBannerShowing = false;


    public SlotGameActor() {
        this.slotTextures = new SlotTextures();
        stopped = false;

        this.iconRow1 = new SlotIconRow(this.slotTextures, 276, 372);
        this.iconRow2 = new SlotIconRow(this.slotTextures, 640, 372);
        this.iconRow3 = new SlotIconRow(this.slotTextures, 1004, 372);

        // get textures
        this.textBoxBorderTexture = MankomaniaGame.getMankomaniaGame().getManager().get(AssetDescriptors.BORDER);
        this.textBoxInnerTexture = MankomaniaGame.getMankomaniaGame().getManager().get(AssetDescriptors.FILLING);

        this.bannerFont = new BitmapFont(Gdx.files.internal("fonts/beleren_78.fnt"));
        this.bannerFont.getData().markupEnabled = true; // enable color markup in font rendering strings
    }

    @Override
    public void act(float delta) {
        this.iconRow1.update(delta);
        this.iconRow2.update(delta);
        this.iconRow3.update(delta);

        if (Gdx.input.justTouched() && !stopped) {
            stopped = true;
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

        if (this.isBannerShowing) {
            batch.draw(this.textBoxInnerTexture, 0, 730, 1920, 250);
            batch.draw(this.textBoxBorderTexture, 0, 730, 1920, 250);

            this.bannerFont.draw(batch, "[BLACK]" + this.bannerText, 0f, 890f, 1920, Align.center, false);
        }
    }

    public void stopRolls(int[] rollValues, int winAmount) {
        this.iconRow1.stopAt(rollValues[0]);

        this.iconRow1.setStoppedTask(() -> this.iconRow2.stopAt(rollValues[1]));

        this.iconRow2.setStoppedTask(() -> this.iconRow3.stopAt(rollValues[2]));

        this.iconRow3.setStoppedTask(() -> {
            Gdx.app.debug("slots", "SLOT 3 (last one) stopped as well.");
            this.showBanner(winAmount);
            // use a timer so the user can see the results for a bit
            if (MankomaniaGame.getMankomaniaGame().isLocalPlayerTurn()) {
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        MankomaniaGame.getMankomaniaGame().getNetworkClient().getMessageHandler().sendSlotsFiinished();
                    }
                }, 5f);
            }
        });
    }

    private void showBanner(int winAmount) {
        this.isBannerShowing = true;
        // switch over the win amount, since there are only three possible values...
        switch (winAmount) {
            case 50000: {
                this.bannerText = "Du hast 50.000€ gewonnen!";
                break;
            }
            case 150000: {
                this.bannerText = "Du hast 150.000€ gewonnen!";
                break;
            }
            case 250000: {
                this.bannerText = "Du hast 250.000€ gewonnen!";
                break;
            }
            default: {
                this.bannerText = "Du hast nicht gewonnen (this should not be seen kek)";
                // reset the banner to not showing
                this.isBannerShowing = false;
            }
        }
    }
}
