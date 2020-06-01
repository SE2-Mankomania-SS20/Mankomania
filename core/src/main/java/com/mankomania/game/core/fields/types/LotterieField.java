package com.mankomania.game.core.fields.types;

import com.badlogic.gdx.math.Vector3;
import com.mankomania.game.core.fields.FieldColor;

public class LotterieField extends Field {
    private final int ticketPrice;

    public LotterieField(Vector3[] positions, int nextField, int optionalNextField, int previousField, String text, FieldColor color, int ticketPrice, int fieldIndex) {
        super(positions, nextField, optionalNextField, previousField, text, color, fieldIndex);
        this.ticketPrice = ticketPrice;
    }

    public int getTicketPrice() {
        return ticketPrice;
    }
}
