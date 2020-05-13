package com.mankomania.game.core.fields.types;

import com.mankomania.game.core.fields.FieldColor;
import com.mankomania.game.core.fields.IField;
import com.mankomania.game.core.fields.Position3;

public abstract class Field implements IField {
    private final Position3[] positions;

    private final int nextField;
    private final int optionalNextField;
    private final int previousField;
    private final String text;
    private final FieldColor color;

    public Field(Position3[] positions, int nextField, int optionalNextField, int previousField, String text, FieldColor color) {
        this.positions = positions;
        this.nextField = nextField;
        this.optionalNextField = optionalNextField;
        this.previousField = previousField;
        this.text = text;
        this.color = color;
    }

    @Override
    public void action() {

    }

    public Position3[] getPositions() {
        return positions;
    }

    public int getNextField() {
        return nextField;
    }

    public int getOptionalNextField() {
        return optionalNextField;
    }

    public int getPreviousField() {
        return previousField;
    }

    public String getText() {
        return text;
    }

    public FieldColor getColor() {
        return color;
    }
}
