package com.mankomania.game.core.fields.types;

import com.badlogic.gdx.math.Vector3;
import com.mankomania.game.core.fields.FieldColor;

public class JumpField extends Field {
    private final int jumpToField;

    public JumpField(Vector3[] positions, int nextField, int optionalNextField, int previousField, String text, FieldColor color, int jumpToField, int fieldIndex) {
        super(positions, nextField, optionalNextField, previousField, text, color, fieldIndex);
        this.jumpToField = jumpToField;
    }

    public int getJumpToField() {
        return jumpToField;
    }
}
