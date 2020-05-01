package com.mankomania.game.core.fields.types;

import com.mankomania.game.core.fields.FieldColor;
import com.mankomania.game.core.fields.IField;
import com.mankomania.game.core.fields.Position3;

public abstract class Field implements IField {
    private Position3 positions[];

    private int nextField;
    private int optionalNextField;
    private int previousField;
    private String text;
    private FieldColor color;

}
