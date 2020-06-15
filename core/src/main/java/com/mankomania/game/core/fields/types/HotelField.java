package com.mankomania.game.core.fields.types;

import com.badlogic.gdx.math.Vector3;
import com.mankomania.game.core.fields.FieldColor;
import com.mankomania.game.core.player.Hotel;

public class HotelField extends Field {
    private final int buy;
    private final int rent;
    private final Hotel hotelType;
    // the position where to place to hotel 3d models on the field
    private final Vector3 hotelPosition;

    public HotelField(Vector3[] positions, int nextField, int optionalNextField, int previousField, String text, FieldColor color, int buy, int rent, Hotel hotelType, int fieldIndex, Vector3 hotelPosition) {
        super(positions, nextField, optionalNextField, previousField, text, color, fieldIndex);
        this.buy = buy;
        this.rent = rent;
        this.hotelType = hotelType;
        this.hotelPosition = hotelPosition;
    }

    public int getBuy() {
        return buy;
    }

    public int getRent() {
        return rent;
    }

    public Hotel getHotelType() {
        return hotelType;
    }

    public Vector3 getHotelPosition() {
        return hotelPosition;
    }
}
