package com.mankomania.game.core.fields.types;

import com.badlogic.gdx.math.Vector3;
import com.mankomania.game.core.fields.FieldColor;
import com.mankomania.game.core.fields.MinigameType;

public class MinigameField extends Field {
    private final MinigameType minigameType;

    public MinigameField(Vector3[] positions, int nextField, int optionalNextField, int previousField, String text, FieldColor color, MinigameType minigameType, int fieldIndex) {
        super(positions, nextField, optionalNextField, previousField, text, color, fieldIndex);
        this.minigameType = minigameType;
    }

    public MinigameType getMinigameType() {
        return minigameType;
    }
}
