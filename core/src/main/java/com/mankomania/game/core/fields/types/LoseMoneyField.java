package com.mankomania.game.core.fields.types;

import com.badlogic.gdx.math.Vector3;
import com.mankomania.game.core.fields.FieldColor;

public class LoseMoneyField extends Field {
    private final int amountMoney;

    public LoseMoneyField(Vector3[] positions, int nextField, int optionalNextField, int previousField, String text, FieldColor color, int amountMoney) {
        super(positions, nextField, optionalNextField, previousField, text, color);
        this.amountMoney = amountMoney;
    }

    public int getAmountMoney() {
        return amountMoney;
    }
}
