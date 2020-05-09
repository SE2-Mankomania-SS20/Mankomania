package com.mankomania.game.core.fields.types;

import com.mankomania.game.core.fields.FieldColor;
import com.mankomania.game.core.fields.Position3;

public class LotterieField extends Field {
    private final int pay;

    public LotterieField(Position3[] positions, int nextField, int optionalNextField, int previousField, String text, FieldColor color, int pay) {
        super(positions, nextField, optionalNextField, previousField, text, color);
        this.pay = pay;
    }

    public int getPay() {
        return pay;
    }
}
