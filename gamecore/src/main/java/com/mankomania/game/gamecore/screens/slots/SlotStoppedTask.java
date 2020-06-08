package com.mankomania.game.gamecore.screens.slots;

/**
 * Class to hold a delegate that gets called when a roll ("walze") stops.
 * That way the three rolls can be stopped "asynchronously" after each other.
 */
public interface SlotStoppedTask {
    /**
     * This method gets called when a roll ("walze") stops.
     */
    void run();
}
