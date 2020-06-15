package com.mankomania.game.server.data;

/*
 Created by Fabian Oraze on 03.05.20
 */

/**
 * State Pattern to track and simulate turn based Game behaviour
 * Every State holds the successor state and can be called via the next method
 * This can be used in gameLogic to ensure that stateSwitching is only able in a particular current state
 */
public enum GameState {

    /**
     * Tricky one wait for player to roll or stop
     */
    TRICKY_ONE_WROS,

    /**
     * horse race minigame waiting state
     */
    HORSE_RACE,
    WAIT_STOCK_ROLL,
    PLAYER_CAN_ROLL_DICE,
    WAIT_FOR_DICE_RESULT,
    /**
     * wait for current player to finish his turn
     */
    WAIT_FOR_TURN_FINISHED,
    /**
     * wait for intersection selection from current player
     */
    WAIT_INTERSECTION_SELECTION,
    /**
     * waiting state for waiting for a player to roll the slot machine
     */
    WAIT_SLOTS_INPUT,
    WAIT_SLOTS_END,
    WAIT_FOR_ALL_ROULETTE_BET,
    WAIT_HOTELBUY_DECISION,
    PLAYER_WON,
}
