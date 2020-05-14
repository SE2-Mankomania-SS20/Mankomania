package com.mankomania.game.core.fields.types;

import com.mankomania.game.core.fields.FieldColor;
import com.mankomania.game.core.fields.Position3;

public class PayLotterieField extends Field {
    private final int amountToPay;

    public PayLotterieField(Position3[] positions, int nextField, int optionalNextField, int previousField, String text, FieldColor color, int amount) {
        super(positions, nextField, optionalNextField, previousField, text, color);

        this.amountToPay = amount;
    }

    public int getAmountToPay() {
        return amountToPay;
    }
}
