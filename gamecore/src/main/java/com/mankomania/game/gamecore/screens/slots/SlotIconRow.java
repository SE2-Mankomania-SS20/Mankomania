package com.mankomania.game.gamecore.screens.slots;

import com.badlogic.gdx.graphics.g2d.Batch;

/**
 * This class handles rendering and animating a single "walze". It implements easy-to-use functions
 * to let the "walze" stop at a specific icon, all being smoothly animated.
 * The walze icons get repeated and can spin therefore forever.
 */
public class SlotIconRow {
    // basic properties
    // the static positions of this row
    private float xPosition;
    private float yPosition;
    private final float width = 100f;
    private final float height = 3 * width;
    // the offset position to let the roll spin
    private float offsetHeight = 0f;

    // TODO: extract some of this config variables out into a static config class
    private float spinSpeed = 8f;

    // references to all the needed textures and texture regions
    private SlotTextures slotTextures;

    public SlotIconRow(SlotTextures slotTextures) {
        this.slotTextures = slotTextures;
    }

    /**
     * Renders (and updates) this roll/"walze".
     * @param batch The (Sprite)Batch that should be used for rendering. batch.begin() must have been called.
     */
    public void render(Batch batch) {
        // draw the three regions, each containing three icons
        batch.draw(this.slotTextures.getSlotTextureFirstRow(), this.xPosition, this.yPosition + this.offsetHeight, this.width, this.height);
        batch.draw(this.slotTextures.getSlotTextureSecondRow(), this.xPosition, this.yPosition + this.height + this.offsetHeight, this.width, this.height);
        batch.draw(this.slotTextures.getSlotTextureThirdRow(), this.xPosition, this.yPosition + this.height * 2 + this.offsetHeight, this.width, this.height);
        batch.draw(this.slotTextures.getSlotTextureDuplicatIcon(), this.xPosition, this.yPosition + this.height * 3 + this.offsetHeight, this.width, this.width);

        this.offsetHeight -= this.spinSpeed;
        this.checkOffsetOutOfBounds();
    }

    /**
     * Checks if the current offset is out of bounds. If the duplicate icon is reached, the whole icons get positioned back
     * to the position they had in the beginning. That way we achieve smooth, seamless scrolling.
     */
    private void checkOffsetOutOfBounds() {
        if (this.offsetHeight <= -(this.height * 3)) {
            this.offsetHeight = this.offsetHeight + this.height * 3;
        }
    }

}
