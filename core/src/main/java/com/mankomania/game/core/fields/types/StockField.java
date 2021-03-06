package com.mankomania.game.core.fields.types;

import com.badlogic.gdx.math.Vector3;
import com.mankomania.game.core.fields.FieldColor;
import com.mankomania.game.core.player.Stock;

public class StockField extends Field {
    private final Stock stockType;
    private final int price;

    public StockField(Vector3[] positions, int nextField, int optionalNextField, int previousField, String text, FieldColor color, Stock stockType, int price, int fieldIndex) {
        super(positions, nextField, optionalNextField, previousField, text, color, fieldIndex);
        this.stockType = stockType;
        this.price = price;
    }

    public Stock getStockType() {
        return stockType;
    }

    public int getPrice() {
        return price;
    }
}
