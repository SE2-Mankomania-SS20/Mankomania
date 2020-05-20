package com.mankomania.game.core.fields.types;

import com.badlogic.gdx.math.Vector3;
import com.mankomania.game.core.fields.FieldColor;

public class LotterieField extends Field {
    private final int pay;

    public LotterieField(Vector3[] positions, int nextField, int optionalNextField, int previousField, String text, FieldColor color, int pay) {
        super(positions, nextField, optionalNextField, previousField, text, color);
        this.pay = pay;
    }

    public int getPay() {
        return pay;
    }
}
