package com.mankomania.game.core.fields.types;

import com.mankomania.game.core.fields.FieldColor;
import com.mankomania.game.core.fields.Position3;

public class LoseMoneyField extends Field {
    private int amountMoney;

    public LoseMoneyField(Position3[] positions, int nextField, int optionalNextField, int previousField, String text, FieldColor color, int amountMoney) {
        super(positions, nextField, optionalNextField, previousField, text, color);
        this.amountMoney = amountMoney;
    }
}
