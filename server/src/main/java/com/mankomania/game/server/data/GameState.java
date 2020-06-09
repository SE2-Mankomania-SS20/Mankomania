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
    TRICKY_ONE_WROS(null),

    PLAYER_CAN_ROLL_DICE(null),
    WAIT_FOR_DICE_RESULT(null),
    WAIT_FOR_TURN_FINISHED(null),
    WAIT_INTERSECTION_SELECTION(null),
    WAIT_FOR_ALL_ROULETTE_BET(null),
    WAIT_HOTELBUY_DECISION(null);

    private final GameState next;

    GameState(GameState next) {
        this.next = next;
    }

    public GameState next() {
        return next;
    }
}
