package com.mankomania.game.gamecore.hotels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.esotericsoftware.minlog.Log;
import com.mankomania.game.core.data.GameData;
import com.mankomania.game.core.fields.types.HotelField;
import com.mankomania.game.gamecore.MankomaniaGame;
import com.mankomania.game.gamecore.util.AssetDescriptors;

/**
 * A simple overlay for having the player to chose whether to buy a hotel or not after landing on an not owned hotel field.
 */
public class BuyHotelOverlay {
    private Stage stage;
    private Label lblHeading;

    private boolean isShowing = false;


    public void create() {
        Skin skin = MankomaniaGame.getMankomaniaGame().getManager().get(AssetDescriptors.SKIN);
        // scale up standard skin font for new, maybe create a new skin with bigger size font later on
        skin.getFont("font").getData().setScale(3, 3);

        this.stage = new Stage(new ScreenViewport());

        // header label initialization
        this.lblHeading = new Label("headingText", skin, "chat");
        this.lblHeading.setAlignment(Align.center);
        this.lblHeading.setPosition(60f, Gdx.graphics.getHeight() - 480f);
        this.lblHeading.setSize(Gdx.graphics.getWidth() - 120f, 240f);

        // not buy button initialization
        TextButton btnNotBuy = new TextButton("Do not buy it!", skin);
        btnNotBuy.setSize(480f, 140f);
        btnNotBuy.setPosition((Gdx.graphics.getWidth() / 4f) - 240f, Gdx.graphics.getHeight() - 720f);

        // buy button initialization
        TextButton btnBuy = new TextButton("Buy it!", skin);
        btnBuy.setSize(480f, 140f);
        btnBuy.setPosition((Gdx.graphics.getWidth() / 4f) * 3 - 240f, Gdx.graphics.getHeight() - 720f);

        // action listeners
        btnNotBuy.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // send the message that the player did not buy the hotel to the server
                MankomaniaGame.getMankomaniaGame().getNetworkClient().getMessageHandler().sendPlayerBuyHotelDecisionMessage(false);
                // reset the buyable hotel (just to be sure)
                MankomaniaGame.getMankomaniaGame().getGameData().setBuyableHotelFieldId(-1);

                // hide the overlay
                isShowing = false;
            }
        });

        btnBuy.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // send the message that the player did not buy the hotel to the server
                MankomaniaGame.getMankomaniaGame().getNetworkClient().getMessageHandler().sendPlayerBuyHotelDecisionMessage(true);
                // reset the currently buyable hotel (just to be sure)
                MankomaniaGame.getMankomaniaGame().getGameData().setBuyableHotelFieldId(-1);
                // hide the overlay
                isShowing = false;
            }
        });

        this.stage.addActor(this.lblHeading);
        this.stage.addActor(btnBuy);
        this.stage.addActor(btnNotBuy);
    }

    public void render(float delta) {
        if (this.isShowing) {
            this.stage.act(delta);
            this.stage.draw();
        } else {
            // if not currently showing, check if currently buyableHotel is not -1
            if (MankomaniaGame.getMankomaniaGame().getGameData().getBuyableHotelFieldId() > 0) {
                // so if there is a buyable hotel, show the overlay
                this.show();
            }
        }
    }

    public void dispose() {
        this.stage.dispose();
    }

    public void show() {
        GameData gameData = MankomaniaGame.getMankomaniaGame().getGameData();
        // check if there is a buyableHotel set, abort with an error if not
        if (gameData.getBuyableHotelFieldId() < 0) {
            Log.error("HotelOverlay", "The BuyHotelOverlay was opened but buyableHotelFieldId was set to -1! Aborting...");
            return;
        }

        // update the heading text field using the currently buyable hotel field
        HotelField buyableHotelField = (HotelField) gameData.getFieldByIndex(gameData.getBuyableHotelFieldId());
        String hotelName = buyableHotelField.getHotelType().getName();
        String headingText = "Do you want to buy hotel\n'" + hotelName + "' for " + buyableHotelField.getBuy() + "$?";

        this.lblHeading.setText(headingText);

        this.isShowing = true;
    }

    /**
     * Adds the stage of the BuyHotelOverlay to the given multiplexer.
     *
     * @param multiplexer the multiplexer the stage should be added to
     */
    public void addStageToMultiplexer(InputMultiplexer multiplexer) {
        multiplexer.addProcessor(new HotelOverlayInputProcessor());
    }

    /**
     * The BuyHotelOverlay's own InputProcessor. It redirects the input events to the underlying stage,
     * but only if the BuyHotelOverlay is currently displayed. That way other event handling is not hindered.
     * Only needed events get redirected. By default false is returned, meaning the event was not captured here.
     */
    private class HotelOverlayInputProcessor implements InputProcessor {
        @Override
        public boolean keyDown(int keycode) {
            return false;
        }

        @Override
        public boolean keyUp(int keycode) {
            return false;
        }

        @Override
        public boolean keyTyped(char character) {
            return false;
        }

        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            if (isShowing) {
                return stage.touchDown(screenX, screenY, pointer, button);
            }
            return false;
        }

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            if (isShowing) {
                return stage.touchUp(screenX, screenY, pointer, button);
            }
            return false;
        }

        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer) {
            if (isShowing) {
                return stage.touchDragged(screenX, screenY, pointer);
            }
            return false;
        }

        @Override
        public boolean mouseMoved(int screenX, int screenY) {
            return false;
        }

        @Override
        public boolean scrolled(int amount) {
            if (isShowing) {
                return stage.scrolled(amount);
            }
            return false;
        }
    }
}
