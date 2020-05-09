package com.mankomania.game.core.fields.types;

import com.mankomania.game.core.fields.FieldColor;
import com.mankomania.game.core.fields.Position3;

public class JumpField extends Field {
    public JumpField(Position3[] positions, int nextField, int optionalNextField, int previousField, String text, FieldColor color, int jumpToField) {
        super(positions, nextField, optionalNextField, previousField, text, color);
        this.jumpToField = jumpToField;
    }

    private int jumpToField;

}
