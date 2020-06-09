package com.mankomania.game.gamecore.screens.slots;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;

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
    private static final float WIDTH = 310f;
    private static final float HEIGHT = 3 * WIDTH;
    // the offset position to let the roll spin
    private float offsetHeight = 0f;

    // TODO: extract some of this config variables out into a static config class
    private float spinSpeed = 32f;

    // properties used for making the roll stop at a given icon, making it look as smooth as possible
    // the position at which the roll should start to slow down and stop slowly
    private float stopAtYPosition;
    // this property stores where we want to land when the roll comes eventually to a stop
    private float stopYPositionGoal;
    // if reached the point where the roll should stop, this variable gets set to the distance needed to reach the given icon
    private float stopDistance;
    // shouldStop gets set to true if the "stopAt" function gets called. the roll moves further until we reached a certain distance to the icon we want to stop at
    private boolean shouldStop = false;
    // if we reached the point, where we actually are stopping and slowing down, this variable gets set to true
    private boolean isStopping = false;
    // this property stores at which position we started stopping, that way we can use this point as reference, interpolating from this point to the wanted icon position
    private float startedStoppingAt;

    // the interpolation method we want to use to make the stopping and slowing down as smooth as possible
    private Interpolation stoppingInterpolation = com.badlogic.gdx.math.Interpolation.fastSlow;
    // just a counter that counts up (in seconds) and a max value. used to make the interpolation smooth
    private float interpolationCurrent = 0f;
    private static final float INTERPOLATION_MAX = 2.5f;

    // variable that only is set to true if we fully stopped
    private boolean isStopped = false;

    // callback that gets called when a slot stops
    private SlotStoppedTask slotStoppedTask;

    // references to all the needed textures and texture regions
    private SlotTextures slotTextures;

    public SlotIconRow(SlotTextures slotTextures, float xPosition, float yPosition) {
        this.slotTextures = slotTextures;

        this.xPosition = xPosition;
        this.yPosition = yPosition;
    }

    /**
     * Renders (and updates) this roll/"walze".
     *
     * @param batch The (Sprite)Batch that should be used for rendering. batch.begin() must have been called.
     */
    public void render(Batch batch) {
        // draw the three regions, each containing three icons
        batch.draw(this.slotTextures.getSlotTextureFirstRow(), this.xPosition, this.yPosition + this.offsetHeight, this.WIDTH, this.HEIGHT);
        batch.draw(this.slotTextures.getSlotTextureSecondRow(), this.xPosition, this.yPosition + this.HEIGHT + this.offsetHeight, this.WIDTH, this.HEIGHT);
        batch.draw(this.slotTextures.getSlotTextureThirdRow(), this.xPosition, this.yPosition + this.HEIGHT * 2 + this.offsetHeight, this.WIDTH, this.HEIGHT);
        batch.draw(this.slotTextures.getSlotTextureDuplicatIcon(), this.xPosition, this.yPosition + this.HEIGHT * 3 + this.offsetHeight, this.WIDTH, this.WIDTH);
    }

    /**
     * Updates this row, animating and hande stopping of it.
     *
     * @param delta delta time that elapsed since last frame
     */
    public void update(float delta) {
        // move down the textures to animate the "rolling"
        // if we are not currently stopping, move downwards just with a fixed speed
        if (!this.isStopping) {
            this.offsetHeight -= this.spinSpeed;
        } else {
            // if we are currently stopping, calculate the percentage how close we are before stopping using our interpolation
            float percentage = Math.min(1f, this.interpolationCurrent / this.INTERPOLATION_MAX); // 0 -> 1, 0 = beginning, 1 = stopped
            float interpolatedPercentage = this.stoppingInterpolation.apply(percentage);

            // set the offset to the position where we started stopping substracted with a percentage of the distance left to reach the wanted icon
            this.offsetHeight = this.startedStoppingAt - (this.stopDistance * interpolatedPercentage);

            this.interpolationCurrent += delta;

            // check if we actually stopped
            if (interpolatedPercentage >= 0.9999f && !this.isStopped) {
                this.isStopped = true;
                Gdx.app.debug("slot", "Slot stopped!");
                // call callback here
                if (this.slotStoppedTask != null) {
                    this.slotStoppedTask.run();
                }
            }
        }

        this.checkOffsetOutOfBounds();
        this.checkForStopping();
    }

    /**
     * Lets this roll stop the spinning, halting on the given icon. This may take a variable amount of time,
     * since it depends on where the roll was when this method gets called.
     *
     * @param iconIndex the icon that this roll should halt on
     */
    public void stopAt(int iconIndex) {
        // check if we are not already stopping
        if (!this.isStopping && !this.shouldStop) {
            // calculate stop yPosition: icon 0 -> position = 0, icon 1 -> position = 1 x width, icon 2 -> position = 2 x width
            this.stopYPositionGoal = -(iconIndex * this.WIDTH);
            this.stopAtYPosition = this.stopYPositionGoal + this.WIDTH * 3;

            // if we are positive (meaning we scrolled out of bounds in our case), loop over
            if (this.stopAtYPosition > 0) {
                this.stopAtYPosition = -(this.WIDTH * 8) + this.stopAtYPosition;
            }
            // set isStopping to true and take over calculating position with interpolation
            this.shouldStop = true;
        }
    }

    /**
     * Checks if the current offset is out of bounds. If the duplicate icon is reached, the whole icons get positioned back
     * to the position they had in the beginning. That way we achieve smooth, seamless scrolling.
     */
    private void checkOffsetOutOfBounds() {
        if (this.offsetHeight <= -(this.HEIGHT * 3)) {
            this.offsetHeight = this.offsetHeight + this.HEIGHT * 3;
        }
    }

    private void checkForStopping() {
        // if the current offset is vaguely in the area we want to stop, set the stopping properties, which will make the rolls slowly stop from now on
        if (this.offsetHeight < this.stopAtYPosition + this.WIDTH / 2 && this.offsetHeight > this.stopAtYPosition - this.WIDTH / 2 && this.shouldStop) {
            this.isStopping = true;
            this.shouldStop = false;
            this.startedStoppingAt = this.offsetHeight;

            // calculate difference from the current offset to the goal
            if (this.stopYPositionGoal < this.offsetHeight) {
                // the offset is "deeper"
                this.stopDistance = this.offsetHeight - this.stopYPositionGoal;
            } else {
                // the offset has to loop one time around to reach the goal
                this.stopDistance = (this.HEIGHT * 3 + this.offsetHeight) - this.stopYPositionGoal;
            }
        }
    }

    public void setStoppedTask(SlotStoppedTask slotStoppedTask) {
        this.slotStoppedTask = slotStoppedTask;
    }
}
