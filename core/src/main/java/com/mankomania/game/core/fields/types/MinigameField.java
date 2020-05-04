package com.mankomania.game.core.fields.types;

import com.mankomania.game.core.fields.FieldColor;
import com.mankomania.game.core.fields.Position3;

public abstract class MinigameField extends Field {

    public MinigameField(Position3[] positions, int nextField, int optionalNextField, int previousField, String text, FieldColor color) {
        super(positions, nextField, optionalNextField, previousField, text, color);
    }
}