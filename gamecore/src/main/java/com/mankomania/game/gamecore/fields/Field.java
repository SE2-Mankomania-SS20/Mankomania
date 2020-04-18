package com.mankomania.game.gamecore.fields;

public class Field {
    private int id;
    private int nextId;
    private int optionalNextId;

    private String text;

    // TODO: implement these field properties and add position on the field and player positions
//    private Field next;
//    private Field optionalNext;
//    private Field previous;

    public Field() { }

    public Field(int id, int nextId, int optNextId, String text) {
        this.id = id;
        this.nextId = nextId;
        this.optionalNextId = optNextId;

        this.text = text;
    }
}
