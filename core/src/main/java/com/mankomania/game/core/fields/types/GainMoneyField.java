package com.mankomania.game.core.fields.types;

import com.badlogic.gdx.math.Vector3;
import com.mankomania.game.core.fields.FieldColor;


public class GainMoneyField extends Field {

    private final int amountMoney;

    public GainMoneyField(Vector3[] positions, int nextField, int optionalNextField, int previousField, String text, FieldColor color, int amountMoney, int fieldIndex) {
        super(positions, nextField, optionalNextField, previousField, text, color, fieldIndex);
        this.amountMoney = amountMoney;
    }

    public int getAmountMoney() {
        return amountMoney;
    }
}
