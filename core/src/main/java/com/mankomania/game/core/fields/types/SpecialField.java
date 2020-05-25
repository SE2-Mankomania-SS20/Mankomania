package com.mankomania.game.core.fields.types;

import com.badlogic.gdx.math.Vector3;
import com.mankomania.game.core.fields.FieldColor;

public class SpecialField extends Field {

    public SpecialField(Vector3[] positions, int nextField, int optionalNextField, int previousField, String text, FieldColor color, int fieldIndex) {
        super(positions, nextField, optionalNextField, previousField, text, color, fieldIndex);
    }
}
