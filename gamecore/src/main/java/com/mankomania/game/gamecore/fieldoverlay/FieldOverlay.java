package com.mankomania.game.gamecore.fieldoverlay;

import com.badlogic.gdx.Gdx;
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
public class FieldOverlay {
    // for loading and holding information and textures
    FieldData fieldData;
    FieldOverlayData fieldOverlayData;

    BitmapFont debugFont;

    private int currentCenterId = 27;
    private float scrollPosition = 0;
    private float totalScrollPosition = 0;

    public FieldOverlay() {
        fieldData = new FieldData();
        fieldOverlayData = new FieldOverlayData();
    }

    /**
     * Creates and loads all the necessary data (must be called before any other function of this class)
     */
    public void create() {
        this.fieldData.load();
        this.fieldOverlayData.create(fieldData.getFieldData());

        this.debugFont = new BitmapFont(Gdx.files.internal("fonts/beleren.fnt"));
    }

    /**
     * Renders the overlay on given SpriteBatch
     * @param batch the SpriteBatch to render with. The SpriteBatch.begin() and end() methods must be called before/after calling this render call (!)
     */
    public void render(SpriteBatch batch) {
        int tempCenterId = currentCenterId; // temp value, where < 0 and >= MAXSIZE is not permitted
        if (tempCenterId < 0) tempCenterId = this.fieldOverlayData.getSize() + tempCenterId;
        if (tempCenterId >= this.fieldOverlayData.getSize()) tempCenterId = tempCenterId % this.fieldOverlayData.getSize();

        FieldOverlayField currentField = fieldOverlayData.getById(tempCenterId);

        // draw current center field
        float posX = -BOX_WIDTH / 2 - PADDING_LEFT + scrollPosition;
        float posY = getFieldPosY(currentField.getSplitPosition());

        currentField.draw(batch, (int)posX, (int)posY, BOX_WIDTH, BOX_WIDTH);
        this.debugFont.draw(batch, "id = " + currentField.getId(), posX + 25, posY);

//         draw right of center
        int fieldNumberRight = 1; // how many fields is the current box shifted to the right of the center box
        for (int i = 1; i < FieldOverlayConfig.RENDER_DISTANCE_RIGHT; i++) {
            int nextId = currentCenterId - i; // handle < 0 and >= MAXSIZE
            if (nextId < 0) nextId = this.fieldOverlayData.getSize() + nextId;
            if (nextId >= this.fieldOverlayData.getSize()) nextId = nextId % this.fieldOverlayData.getSize();
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

            currentField.draw(batch, (int)posX, (int)posY, BOX_WIDTH, BOX_WIDTH);

            this.debugFont.draw(batch, "id = " + currentField.getId(), posX + 25, posY);
        }
    }


    /**
     * Frees all used resources
     */
    public void dispose() {
        this.debugFont.dispose();

        this.fieldOverlayData.dispose();
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
            if (this.currentCenterId == 18) {
                this.currentCenterId += 4;
            }
            Gdx.app.log("center changed", "currentCenter changed from " + (currentCenterId - 1) + " to " + currentCenterId);
        }

        // di the same while scrolling the other direction (refactor duplicate code needed)
        if (this.scrollPosition <= -(BOX_WIDTH + MARGIN_BETWEEN)) {
            this.scrollPosition = this.scrollPosition + (BOX_WIDTH + MARGIN_BETWEEN);
            this.currentCenterId -= 1;
            if (this.currentCenterId == 18) {
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
}
