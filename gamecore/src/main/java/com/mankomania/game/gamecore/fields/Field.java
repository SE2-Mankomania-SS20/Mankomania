package com.mankomania.game.gamecore.fields;

public class Field {
    private int id;
    private int nextId;
    private int optionalNextId;

    private FieldColor color;

    private String text;

    // TODO: implement these field properties and add position on the field and player positions
//    private Field next;
//    private Field optionalNext;
//    private Field previous;

    public Field() { }

    public Field(int id, int nextId, int optNextId, String color, String text) {
        this.id = id;
        this.nextId = nextId;
        this.optionalNextId = optNextId;

        this.text = text;

        // load color through a string
        switch (color.toLowerCase()) {
            case "orange": this.color = FieldColor.ORANGE; break;
            case "yellow": this.color = FieldColor.YELLOW; break;
            case "blue": this.color = FieldColor.BLUE; break;
            case "white": this.color = FieldColor.WHITE; break;
            case "magenta": this.color = FieldColor.MAGENTA; break;
            default: throw new IllegalArgumentException("the color given was empty or not parsable");
        }
    }
}
