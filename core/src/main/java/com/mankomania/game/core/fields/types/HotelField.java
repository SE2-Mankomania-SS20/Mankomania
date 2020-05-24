package com.mankomania.game.core.fields.types;

import com.badlogic.gdx.math.Vector3;
import com.mankomania.game.core.fields.FieldColor;
import com.mankomania.game.core.player.Hotel;

public class HotelField extends Field {
    private final int buy;
    private final int rent;
    private final Hotel hotelType;
    // the player index of the player that owns the hotel on this hotel field
    private int ownerPlayerIndex;

    public HotelField(Vector3[] positions, int nextField, int optionalNextField, int previousField, String text, FieldColor color, int buy, int rent, Hotel hotelType, int fieldIndex) {
        super(positions, nextField, optionalNextField, previousField, text, color, fieldIndex);
        this.buy = buy;
        this.rent = rent;
        this.hotelType = hotelType;

        this.ownerPlayerIndex = -1;
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

    public int getOwnerPlayerIndex() {
        return ownerPlayerIndex;
    }

    public void setOwnerPlayerIndex(int ownerPlayerIndex) {
        this.ownerPlayerIndex = ownerPlayerIndex;
    }
}
