package com.mankomania.game.core.fields.types;

import com.mankomania.game.core.fields.FieldColor;
import com.mankomania.game.core.fields.Position3;

public class StockField extends Field {
    private String name;
    private int price;

    public StockField(Position3[] positions, int nextField, int optionalNextField, int previousField, String text, FieldColor color, String name, int prica) {
        super(positions, nextField, optionalNextField, previousField, text, color);
        this.name = name;
        this.price = prica;
    }
}
