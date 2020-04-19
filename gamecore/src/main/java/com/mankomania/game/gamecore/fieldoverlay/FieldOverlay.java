package com.mankomania.game.gamecore.fieldoverlay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mankomania.game.gamecore.fields.FieldData;

import static com.mankomania.game.gamecore.fieldoverlay.FieldOverlayConfig.BOX_WIDTH;
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

        this.debugFont = new BitmapFont();
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
        float posX = -BOX_WIDTH / 2 - PADDING_LEFT;;
        float posY = 0;

        int tempCurrentSplitPosition = currentField.getSplitPosition();

        if (tempCurrentSplitPosition == -1) {
            posY = (Gdx.graphics.getHeight() - BOX_WIDTH) - MARGIN_TOP;
        } else if (tempCurrentSplitPosition >= 0 && tempCurrentSplitPosition <= 3) {
            posY = (Gdx.graphics.getHeight() - BOX_WIDTH) - SPLIT_MARGIN_TOP;
        } else if (tempCurrentSplitPosition >= 4 && tempCurrentSplitPosition <= 7) {
            posY = (Gdx.graphics.getHeight() - BOX_WIDTH) - SPLIT_MARGIN_TOP_ALTERNATE;
        } else {
            throw new IllegalArgumentException("split position cant be bigger than 5");
        }

        currentField.draw(batch, (int)posX, (int)posY, BOX_WIDTH, BOX_WIDTH);
        this.debugFont.draw(batch, "id = " + currentField.getId(), posX + 25, posY);


        // draw right of center
        int fieldNumberRight = 1; // how many fields is the current box shifted to the right of the center box
        for (int i = 1; i < FieldOverlayConfig.RENDER_DISTANCE_RIGHT; i++) {
            int nextId = currentCenterId - i; // handle < 0 and >= MAXSIZE
            if (nextId < 0) nextId = this.fieldOverlayData.getSize() + nextId;
            if (nextId >= this.fieldOverlayData.getSize()) nextId = nextId % this.fieldOverlayData.getSize();
            currentField = fieldOverlayData.getById(nextId); // TODO: handle below 0 and above MAXSIZE

            // calculate the x position of the current field with the information whether the way is split
            if (currentField.getSplitPosition() >= 0 && currentField.getSplitPosition() <= 3) {
                posX = -BOX_WIDTH / 2 + ((fieldNumberRight - 4) * BOX_WIDTH) + ((fieldNumberRight + 1 - 4) * FieldOverlayConfig.MARGIN_BETWEEN);
                fieldNumberRight++;
            } else { // if (currentField.getSplitPosition() >= 4 && currentField.getSplitPosition() <= 7) {
                posX = -BOX_WIDTH / 2 + (fieldNumberRight * BOX_WIDTH) + ((fieldNumberRight + 1) * FieldOverlayConfig.MARGIN_BETWEEN);
                fieldNumberRight++;
//            } else { // no split field
//                posX = -BOX_WIDTH / 2 + (fieldNumberRight * BOX_WIDTH) + ((fieldNumberRight + 1) * FieldOverlayConfig.MARGIN_BETWEEN);
//                fieldNumberRight++;
//            }
            }

            // need to substract 4 from the number that counts the X position, since we have 4 fields that are beneath the other one since they are split
            if (currentField.getSplitPosition() == 0) {
                fieldNumberRight = fieldNumberRight - 4;
            }

            posX = posX - PADDING_LEFT;

            if (currentField.getSplitPosition() == -1) {
                posY = (Gdx.graphics.getHeight() - BOX_WIDTH) - MARGIN_TOP;
            } else if (currentField.getSplitPosition() >= 0 && currentField.getSplitPosition() <= 3) {
                posY = (Gdx.graphics.getHeight() - BOX_WIDTH) - SPLIT_MARGIN_TOP;
            } else if (currentField.getSplitPosition() >= 4 && currentField.getSplitPosition() <= 7) {
                posY = (Gdx.graphics.getHeight() - BOX_WIDTH) - SPLIT_MARGIN_TOP_ALTERNATE;
            } else {
                throw new IllegalArgumentException("split position cant be bigger than 5");
            }

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

//    public void update(float delta) {
//        // TODO: implement delta
//    }
}
