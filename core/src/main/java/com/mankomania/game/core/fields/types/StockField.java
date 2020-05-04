package com.mankomania.game.core.fields.types;

import com.mankomania.game.core.fields.FieldColor;
import com.mankomania.game.core.fields.Position3;
import com.mankomania.game.core.player.Stock;

public class StockField extends Field {
    private Stock stockType;
    private int price;

    public StockField(Position3[] positions, int nextField, int optionalNextField, int previousField, String text, FieldColor color, Stock stockType, int price) {
        super(positions, nextField, optionalNextField, previousField, text, color);
        this.stockType = stockType;
        this.price = price;
    }
}