package com.mankomania.game.core.fields.types;

import com.badlogic.gdx.math.Vector3;
import com.mankomania.game.core.fields.FieldColor;
import com.mankomania.game.core.fields.IField;


public abstract class Field implements IField {
    private final Vector3[] positions;

    private final int nextField;
    private final int optionalNextField;
    private final int previousField;
    private final String text;
    private final FieldColor color;
    private final int fieldIndex;

    public Field(Vector3[] positions, int nextField, int optionalNextField, int previousField, String text, FieldColor color, int fieldIndex) {
        this.positions = positions;
        this.nextField = nextField;
        this.optionalNextField = optionalNextField;
        this.previousField = previousField;
        this.text = text;
        this.color = color;
        this.fieldIndex = fieldIndex;
    }

    @Override
    public void action() {

    }

    public boolean isIntersection(){
        return nextField > -1 && optionalNextField > -1;
    }

    public int getFieldIndex() {
        return fieldIndex;
    }

    public Vector3[] getPositions() {
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
