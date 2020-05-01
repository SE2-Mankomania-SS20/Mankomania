package com.mankomania.game.core.fields.types;

import com.mankomania.game.core.fields.FieldColor;
import com.mankomania.game.core.fields.Position3;

public class HotelField extends Field {
    private int buy;
    private int rent;
    private String name;

    public HotelField(Position3[] positions, int nextField, int optionalNextField, int previousField, String text, FieldColor color, int buy, int rent, String name) {
        super(positions, nextField, optionalNextField, previousField, text, color);
        this.buy = buy;
        this.rent = rent;
        this.name = name;
    }
}
