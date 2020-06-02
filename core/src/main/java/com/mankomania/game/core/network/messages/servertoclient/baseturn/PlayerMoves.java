package com.mankomania.game.core.network.messages.servertoclient.baseturn;

import com.badlogic.gdx.utils.IntArray;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlayerMoves)) return false;
        PlayerMoves that = (PlayerMoves) o;
        if(that.getMoves().items.length != getMoves().items.length) return false;
        for (int i = 0; i < getMoves().items.length; i++) {
            if(getMoves().items[i] != that.getMoves().items[i]) return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMoves());
    }
}
