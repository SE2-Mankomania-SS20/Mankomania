package com.mankomania.game.core.fields.types;

import com.mankomania.game.core.fields.FieldColor;
import com.mankomania.game.core.fields.Position3;
import com.mankomania.game.core.player.Hotel;

public class HotelField extends Field {
    private int buy;
    private int rent;
    private Hotel hotelType;

    public HotelField(Position3[] positions, int nextField, int optionalNextField, int previousField, String text, FieldColor color, int buy, int rent, Hotel hotelType) {
        super(positions, nextField, optionalNextField, previousField, text, color);
        this.buy = buy;
        this.rent = rent;
        this.hotelType = hotelType;
    }
}
