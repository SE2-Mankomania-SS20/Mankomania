package com.mankomania.game.gamecore.fieldoverlay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mankomania.game.gamecore.fields.FieldData;

import static com.mankomania.game.gamecore.fieldoverlay.FieldOverlayConfig.BOX_WIDTH;
import static com.mankomania.game.gamecore.fieldoverlay.FieldOverlayConfig.MARGIN_BETWEEN;
import static com.mankomania.game.gamecore.fieldoverlay.FieldOverlayConfig.MARGIN_TOP;
import static com.mankomania.game.gamecore.fieldoverlay.FieldOverlayConfig.PADDING_LEFT;
import static com.mankomania.game.gamecore.fieldoverlay.FieldOverlayConfig.SPLIT_MARGIN_TOP;
import static com.mankomania.game.gamecore.fieldoverlay.FieldOverlayConfig.SPLIT_MARGIN_TOP_ALTERNATE;

/**
 * FieldOverlay is used to render the overlay for the board's fields.
 * Only this class should be used when communicating with the field overlay.
 */
public class FieldOverlay implements InputProcessor {
    // for loading and holding information and textures
    private FieldData fieldData;
    private FieldOverlayData fieldOverlayData;
    private FieldOverlayTextures fieldOverlayTextures;
    private FieldOverlayTextBox fieldOverlayTextBox;

    private BitmapFont debugFont;

    private boolean isShowing = true;
    private float visbility = 1f;
    private boolean isFadingIn = false;
    private boolean isFadingOut = false;

    private int currentCenterId = 27;
    private float scrollPosition = 0;
    private float totalScrollPosition = 0;

    public FieldOverlay() {
        this.fieldData = new FieldData();
        this.fieldOverlayData = new FieldOverlayData();
        this.fieldOverlayTextures = new FieldOverlayTextures();
        this.fieldOverlayTextBox = new FieldOverlayTextBox();
    }

    /**
     * Creates and loads all the necessary data (must be called before any other function of this class)
     */
    public void create() {
        this.fieldOverlayTextures.create();

        this.fieldData.load();
        this.fieldOverlayData.create(fieldData.getFieldData(), this.fieldOverlayTextures);

        this.fieldOverlayTextBox.create(this.fieldOverlayTextures);

        this.debugFont = new BitmapFont(Gdx.files.internal("fonts/beleren.fnt"));
        this.debugFont.getData().markupEnabled = true; // enable color markup in font rendering strings
    }

    /**
     * Renders the overlay on given SpriteBatch
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

            int tempCenterId = currentCenterId; // temp value, where < 0 and >= MAXSIZE is not permitted
            if (tempCenterId < 0) tempCenterId = this.fieldOverlayData.getSize() + tempCenterId;
            if (tempCenterId >= this.fieldOverlayData.getSize())
                tempCenterId = tempCenterId % this.fieldOverlayData.getSize();

            FieldOverlayField currentField = fieldOverlayData.getById(tempCenterId);

            // draw current center field
            float posX = -BOX_WIDTH / 2 - PADDING_LEFT + scrollPosition;
            float posY = getFieldPosY(currentField.getSplitPosition());

            currentField.draw(batch, (int) posX, (int) posY, BOX_WIDTH, BOX_WIDTH);
            this.debugFont.draw(batch, "[BLACK]" + currentField.getId(), posX + 32, posY + (2 * BOX_WIDTH / 3));

//         draw right of center
            int fieldNumberRight = 1; // how many fields is the current box shifted to the right of the center box
            for (int i = 1; i < FieldOverlayConfig.RENDER_DISTANCE_RIGHT; i++) {
                int nextId = currentCenterId - i; // handle < 0 and >= MAXSIZE
                if (nextId < 0) nextId = this.fieldOverlayData.getSize() + nextId;
                if (nextId >= this.fieldOverlayData.getSize())
                    nextId = nextId % this.fieldOverlayData.getSize();
                currentField = fieldOverlayData.getById(nextId);

                // calculate the x position of the current field with the information whether the way is split
                if (currentField.getSplitPosition() >= 0 && currentField.getSplitPosition() <= 3) {
                    posX = -BOX_WIDTH / 2 + ((fieldNumberRight - 4) * BOX_WIDTH) + ((fieldNumberRight + 1 - 4) * MARGIN_BETWEEN);
                    fieldNumberRight++;
                } else { // if (currentField.getSplitPosition() >= 4 && currentField.getSplitPosition() <= 7) {
                    posX = -BOX_WIDTH / 2 + (fieldNumberRight * BOX_WIDTH) + ((fieldNumberRight + 1) * MARGIN_BETWEEN);
                    fieldNumberRight++;
                }

                // TODO: refactor this fix (links at scroll function)
                // need to substract 4 from the number that counts the X position, since we have 4 fields that are beneath the other one since they are split
                if (currentField.getSplitPosition() == 0) {
                    fieldNumberRight = fieldNumberRight - 4;
                }

                posX = posX - PADDING_LEFT + scrollPosition;
                posY = getFieldPosY(currentField.getSplitPosition());

                // this.setAlphaToSpriteBatch(batch, this.visbility); // TODO: check why setting the alpha value here it nececsary, even tho it should get reset while rendering the border
                currentField.draw(batch, (int) posX, (int) posY, BOX_WIDTH, BOX_WIDTH);

                this.debugFont.draw(batch, "[BLACK]" + currentField.getId(), posX + 32, posY + (2 * BOX_WIDTH / 3));
            }

//            // draw the textbox
//            this.fieldOverlayTextBox.update();
//            this.fieldOverlayTextBox.render(batch);

            // reset to the old alpha value
            // batch.setColor(oldColor);
        }
    }

    /**
     * Frees all used resources
     */
    public void dispose() {
        this.debugFont.dispose();

        this.fieldOverlayData.dispose();
        this.fieldOverlayTextures.dispose();
    }

    /**
     * Scrolls the field overlay to the left or right
     * @param value the value how far should be scrolled. if value is positive it scrolls to the right, otherwise to the left
     */
    public void scroll(float value) {
        this.totalScrollPosition += value;
        this.scrollPosition += value;

        // if we scrolled far enough, we can move up/down one field. this way we only render the textures that are actually shown.
        if (this.scrollPosition >= BOX_WIDTH + MARGIN_BETWEEN) {
            this.scrollPosition = this.scrollPosition - (BOX_WIDTH + MARGIN_BETWEEN);
            this.currentCenterId += 1;
            // TODO: refactor this fix (links to render function)
            if (this.currentCenterId == 51 || this.currentCenterId == 68 || this.currentCenterId == 9 || this.currentCenterId == 26) {
                this.currentCenterId += 4;
            }
            Gdx.app.log("center changed", "currentCenter changed from " + (currentCenterId - 1) + " to " + currentCenterId);
        }

        // do the same while scrolling the other direction (refactor duplicate code needed)
        if (this.scrollPosition <= -(BOX_WIDTH + MARGIN_BETWEEN)) {
            this.scrollPosition = this.scrollPosition + (BOX_WIDTH + MARGIN_BETWEEN);
            this.currentCenterId -= 1;
            if (this.currentCenterId == 51 || this.currentCenterId == 68 || this.currentCenterId == 9 || this.currentCenterId == 26) {
                this.currentCenterId -= 4;
            }
            Gdx.app.log("center changed", "currentCenter changed from " + (currentCenterId + 1) + " to " + currentCenterId);
        }

        // check if currentCenterId is in bounds
        if (this.currentCenterId < 0)
            this.currentCenterId = this.currentCenterId + this.fieldOverlayData.getSize();
        if (this.currentCenterId >= this.fieldOverlayData.getSize())
            this.currentCenterId = this.currentCenterId % this.fieldOverlayData.getSize();
    }

    public float getScrollPosition() {
        return this.totalScrollPosition;
    }

//    public void update(float delta) {
//        // TODO: implement delta
//    }

    /**
     * Calculates the Y position of the current field, paying attention to the field splits
     * @param currentSplitPosition
     * @return
     */
    private int getFieldPosY(int currentSplitPosition) {
        int posY = 0;

        if (currentSplitPosition == -1) {
            posY = (Gdx.graphics.getHeight() - BOX_WIDTH) - MARGIN_TOP;
        } else if (currentSplitPosition >= 0 && currentSplitPosition <= 3) {
            posY = (Gdx.graphics.getHeight() - BOX_WIDTH) - SPLIT_MARGIN_TOP;
        } else if (currentSplitPosition >= 4 && currentSplitPosition <= 7) {
            posY = (Gdx.graphics.getHeight() - BOX_WIDTH) - SPLIT_MARGIN_TOP_ALTERNATE;
        } else {
            throw new IllegalArgumentException("split position cant be bigger than 5");
        }

        return posY;
    }

    /**
     * Starts fading in the whole overlay. TODO: maybe add parameter that specifies how fast to fade
     */
    public void show() {
//        if (!this.isFadingIn && !this.isFadingOut) {
//            this.isShowing = true;
//            this.isFadingIn = true;
//            this.fieldOverlayTextBox.show();
//        }
        if (!this.isShowing) {
            this.fieldOverlayTextBox.show();
        }
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
     * @param id the field which should be selected (border shown)
     */
    public void selectField(int id) {
        this.fieldOverlayData.showBorderById(id);
    }

    /**
     * Hides the border of the field with given id (unselected).
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
        Gdx.app.log("overlay-input-debug", "there was touch down @ ("  + screenX + ", " + screenY + "), pointer = " + pointer + ", button = " + button);

        if (this.isShowing) {
            this.fieldOverlayTextBox.toggleVisibility();
        }
        return false;
    }
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        Gdx.app.log("overlay-input-debug", "there was touch up @ ("  + screenX + ", " + screenY + "), pointer = " + pointer + ", button = " + button);
        return false;
    }
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        Gdx.app.log("overlay-input-debug", "there was touch dragged @ ("  + screenX + ", " + screenY + "), pointer = " + pointer);
        return false;
    }
    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        Gdx.app.log("overlay-input-debug", "there was mouse moved @ ("  + screenX + ", " + screenY + ")");
        return false;
    }
    @Override
    public boolean scrolled(int amount) {
        Gdx.app.log("overlay-input-debug", "there was scrolled: amount = " + amount + ")");
        return false;
    }
    @Override
    public boolean keyDown(int keycode) { return false; }
    @Override
    public boolean keyUp(int keycode) { return false; }


    /* ==== HELPER FUNCTIONS ==== */
    /**
     * Sets the spritebatch's alpha value to the given one. returns the spritebatch's original color,
     * use that to restore the original color!
     * @param value
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
}
