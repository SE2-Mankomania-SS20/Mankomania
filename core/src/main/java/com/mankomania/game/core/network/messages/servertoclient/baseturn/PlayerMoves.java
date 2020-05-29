package com.mankomania.game.core.network.messages.servertoclient.baseturn;

import com.badlogic.gdx.utils.IntArray;

/**
 * Just moves the given player to the given field.
 */
public class PlayerMoves {
    private IntArray moves;

    public PlayerMoves() {
        // empty for kryonet
    }

    public PlayerMoves(IntArray moves) {
        this.moves = moves;
    }

    public IntArray getMoves() {
        return moves;
    }

    public void setMoves(IntArray moves) {
        this.moves = moves;
    }

}
