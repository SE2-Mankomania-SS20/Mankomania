package com.mankomania.game.core.fields.types;

import com.mankomania.game.core.fields.FieldColor;
import com.mankomania.game.core.fields.MinigameType;
import com.mankomania.game.core.fields.Position3;

public class MinigameField extends Field {
    private MinigameType minigameType;
    public MinigameField(Position3[] positions, int nextField, int optionalNextField, int previousField, String text, FieldColor color, MinigameType minigameType) {
        super(positions, nextField, optionalNextField, previousField, text, color);
        this.minigameType = minigameType;
    }
}
