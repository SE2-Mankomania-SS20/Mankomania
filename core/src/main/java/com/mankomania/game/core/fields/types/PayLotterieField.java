package com.mankomania.game.core.fields.types;

import com.badlogic.gdx.math.Vector3;
import com.mankomania.game.core.fields.FieldColor;

public class PayLotterieField extends Field {
    private final int amountToPay;

    public PayLotterieField(Vector3[] positions, int nextField, int optionalNextField, int previousField, String text, FieldColor color, int amount, int fieldIndex) {
        super(positions, nextField, optionalNextField, previousField, text, color, fieldIndex);

        this.amountToPay = amount;
    }

    public int getAmountToPay() {
        return amountToPay;
    }
}
