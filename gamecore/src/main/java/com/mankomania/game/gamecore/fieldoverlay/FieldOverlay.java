package com.mankomania.game.gamecore.fieldoverlay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import static com.mankomania.game.gamecore.fieldoverlay.FieldOverlayConfig.BOX_WIDTH;
import static com.mankomania.game.gamecore.fieldoverlay.FieldOverlayConfig.SPLIT_MARGIN_TOP;
import static com.mankomania.game.gamecore.fieldoverlay.FieldOverlayConfig.SPLIT_MARGIN_TOP_ALTERNATE;

/**
 * FieldOverlay is used to render the overlay for the board's fields.
 * Only this class should be used when communicating with the field overlay.
 */
public class FieldOverlay implements InputProcessor {
    // for loading and holding information and textures
    private FieldOverlayData fieldOverlayData;
    private FieldOverlayTextures fieldOverlayTextures;
    private FieldOverlayTextBox fieldOverlayTextBox;

    private boolean isShowing = false;
    private float visbility = 1f;
    private boolean isFadingIn = false;
    private boolean isFadingOut = false;

    private int dragStartX = -1;
    private float dragScrollStartX;
    private boolean hasDragged = false; // needed to check on touchUp if there is dragf

    public FieldOverlay() {
        this.fieldOverlayData = new FieldOverlayData();
        this.fieldOverlayTextures = new FieldOverlayTextures();
        this.fieldOverlayTextBox = new FieldOverlayTextBox();
    }

    /**
     * Creates and loads all the necessary data (must be called before any other function of this class)
     */
    public void create() {
        this.fieldOverlayTextures.create();

        this.fieldOverlayData.create(this.fieldOverlayTextures);

        this.fieldOverlayTextBox.create(this.fieldOverlayTextures);
    }

    /**
     * Renders the overlay on given SpriteBatch
     *
     * @param batch the SpriteBatch to render with. The SpriteBatch.begin() and end() methods must be called before/after calling this render call (!)
     */
    public void render(SpriteBatch batch) {
        // TODO: function urgently needs refactoring

        // calculate the current alpha value
        // this.calculateVisibility();
        // Color oldColor = this.setAlphaToSpriteBatch(batch, this.visbility); // use old color alter to restore original color

        if (this.isShowing) {
            // draw the textbox
            this.fieldOverlayTextBox.update();
            this.fieldOverlayTextBox.render(batch);

            this.fieldOverlayData.renderColumns(batch);

            // reset to the old alpha value
            // batch.setColor(oldColor);
        }
    }


    /**
     * Frees all used resources
     */
    public void dispose() {
        this.fieldOverlayData.dispose();
    }

    /**
     * Scrolls the field overlay to the left or right
     *
     * @param value the value how far should be scrolled. if value is positive it scrolls to the right, otherwise to the left
     */
    public void scroll(float value) {
        this.fieldOverlayData.moveColumns(value);
    }


//    public void update(float delta) {
//        // TODO: implement delta
//    }


    /**
     * Starts fading in the whole overlay. TODO: maybe add parameter that specifies how fast to fade
     */
    public void show() {
//        if (!this.isFadingIn && !this.isFadingOut) {
//            this.isShowing = true;
//            this.isFadingIn = true;
//            this.fieldOverlayTextBox.show();
//        }

        this.isShowing = true;
    }

    /**
     * Starts fading out the whole overlay.
     */
    public void hide() {
//        if (!this.isFadingIn && !this.isFadingOut) {
//            this.isFadingOut = true;
//            this.fieldOverlayTextBox.hide();
//        }
        this.isShowing = false;
    }


    /* === HANDLING SELECTED FIELD BORDER */

    /**
     * Starts showing the border of the field with given id (selected).
     *
     * @param id the field which should be selected (border shown)
     */
    public void selectField(int id) {
        this.fieldOverlayData.showBorderById(id);
    }

    /**
     * Hides the border of the field with given id (unselected).
     *
     * @param id the field which should be unselected (border not shown)
     */
    public void unselectField(int id) {
        this.fieldOverlayData.hideBorderById(id);
    }

    /**
     * Hides all field's borders (unselects them).
     */
    public void unselectAll() {
        this.fieldOverlayData.hideBorderAll();
    }

    /* ==================================== */
    /* BEGIN INPUT PROCESSOR IMPLEMENTATION */
    /* ==================================== */
    // TODO: remove debug output
    @Override
    public boolean keyTyped(char character) {
        Gdx.app.log("overlay-input-debug", "there was key '" + character + "' typed");
        if (character == 'a') {
            this.show();
        }
        if (character == 'd') {
            this.hide();
        }
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        boolean result = false; // being careful to return "true" if we actually use the event (for chaining it)

        if (this.isShowing) {
            // save touch start position for calculating the position while dragging.
            if (this.isOverFields(screenX, screenY)) {
                this.dragStartX = screenX;
                this.dragScrollStartX = this.fieldOverlayData.getTotalScrollValue();
                result = true;
            } else {
                this.dragStartX = -1;
            }
        }

        return result;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        boolean result = false;

        if (this.isShowing) {
            // test if touched on field, selecting it and showing according text
            int touchYConverted = Gdx.graphics.getHeight() - screenY;

            FieldOverlayField field = this.fieldOverlayData.getTouchedField(screenX, touchYConverted);
            if (field != null && !this.hasDragged) {
                // first hide all borders and then show only the selected one (for now)
                this.fieldOverlayData.hideBorderAll();
                field.showBorder();

                // set current text on the textfield
                this.fieldOverlayTextBox.setCurrentText(field.getBaseField().getText());

                result = true;
            }

            // redirect the event to textbox to hide it. if textbox is not shown, check if touch was on the fields, if yes, show field.
            if (this.fieldOverlayTextBox.isShowing()) {
                result = this.fieldOverlayTextBox.handleOnTouchUp(screenX, screenY, pointer, button);
            } else {
                if (field != null && !this.hasDragged) {
                    this.fieldOverlayTextBox.show();
                }
            }

            // check if we are currently dragging, if yes, take the difference between und scroll the fields by that value
            if (this.dragStartX > -1) {
                this.dragStartX = -1;

                result = true;
            }
        }

        this.hasDragged = false;

        return result;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        boolean result = false;

        // check if we are currently dragging, if yes, return true, so the camInputProcessor doesnt get this event and moves the camera
        if (this.isShowing && this.dragStartX > -1) {
            // TODO: fix calculation with adding new fields, so scrolling can be fast
            float distanceScrolled = (screenX - this.dragStartX) / 2f;
            this.fieldOverlayData.moveColumnsTo(this.dragScrollStartX + distanceScrolled);

            result = true;
        }

        this.hasDragged = true;

        return result;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.L) {
            this.fieldOverlayData.moveColumns(10.32f);
        }
        if (keycode == Input.Keys.J) {
            this.fieldOverlayData.moveColumns(-10.54f);
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }


    /* ==== HELPER FUNCTIONS ==== */

    /**
     * Sets the spritebatch's alpha value to the given one. returns the spritebatch's original color,
     * use that to restore the original color!
     *
     * @param value the alpha value
     * @return the old spritebatch's color to reset the old alpha value (!)
     */
    private Color setAlphaToSpriteBatch(SpriteBatch batch, float value) {
        // set our alpha value in the spritebatch's color
        Color color = batch.getColor();
        float oldAlpha = color.a; // remember old alpha value so we can reset it

        color.a = value;
        batch.setColor(color);
        color.a = oldAlpha;

        return color;
    }

    /**
     * Checks wheter a given point is over the field overlay.
     */
    private boolean isOverFields(int screenX, int screenY) {
        if (screenY >= SPLIT_MARGIN_TOP && screenY <= SPLIT_MARGIN_TOP_ALTERNATE + BOX_WIDTH) {
            return true;
        }
        return false;
    }

    private void calculateVisibility() {
        // TODO: use delta time for calculations
        // TODO: move values to config file
        if (this.isFadingIn) {
            this.visbility += 0.019f;

            if (this.visbility >= 1f) { // TODO: maybe introduce MAX_ALPHA to make field transparent?
                this.visbility = 1;
                this.isFadingIn = false;
            }
        }
        if (this.isFadingOut) {
            this.visbility -= 0.019f;
            if (this.visbility <= 0) {
                this.visbility = 0f;
                this.isFadingOut = false;
                this.isShowing = false;
            }
        }
    }


    public boolean isShowing() {
        return isShowing;
    }
}
