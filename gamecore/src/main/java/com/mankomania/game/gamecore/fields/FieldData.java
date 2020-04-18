package com.mankomania.game.gamecore.fields;

import java.util.ArrayList;
import java.util.List;

public class FieldData {
    private ArrayList<Field> fields = new ArrayList<>();

    public FieldData() {

    }

    public void load() {
        // TODO: load fields out of JSON file
        addDemoFields();
    }

    private void addDemoFields() {
        fields.add(new Field(0, 1, -1, "orange", "Gehe ins Spielcasino"));
        fields.add(new Field(1, 2, -1, "yellow", "Gehe ins Spielcasino"));
        fields.add(new Field(2, 3, -1, "yellow", "Gehe ins Spielcasino"));
        fields.add(new Field(3, 4, -1, "blue", "Gehe ins Spielcasino"));
        fields.add(new Field(4, 5, -1, "orange", "Gehe ins Spielcasino"));
        fields.add(new Field(5, 6, -1,  "yellow", "Gehe ins Spielcasino"));
        fields.add(new Field(6, 7, -1, "yellow", "Gehe ins Spielcasino"));
        fields.add(new Field(7, 8, -1, "yellow", "Gehe ins Spielcasino"));
        fields.add(new Field(8, 9, -1, "orange", "Gehe ins Spielcasino"));
        fields.add(new Field(9, 10, -1, "yellow", "Gehe ins Spielcasino"));
        fields.add(new Field(10, 11, -1, "yellow", "Gehe ins Spielcasino"));
        fields.add(new Field(11, 12, -1, "orange", "Gehe ins Spielcasino"));
        fields.add(new Field(12, 13, -1, "blue", "Gehe ins Spielcasino"));
        fields.add(new Field(13, 14, -1, "orange", "Gehe ins Spielcasino"));
        fields.add(new Field(14, 15, -1, "yellow", "Gehe ins Spielcasino"));
        fields.add(new Field(15, 16, -1, "yellow", "Gehe ins Spielcasino"));
        fields.add(new Field(16, 17, 21, "orange", "Gehe ins Spielcasino"));
        fields.add(new Field(17, 18, -1, "yellow", "Gehe ins Spielcasino"));
        fields.add(new Field(18, 19, -1, "yellow", "Gehe ins Spielcasino"));
        fields.add(new Field(19, 20, -1, "orange", "Gehe ins Spielcasino"));
        fields.add(new Field(20, 25, -1, "yellow", "Gehe ins Spielcasino"));
        fields.add(new Field(21, 22, -1, "orange", "Gehe ins Spielcasino"));
        fields.add(new Field(22, 23, -1, "magenta", "Gehe ins Spielcasino"));
        fields.add(new Field(23, 24, -1, "yellow", "Gehe ins Spielcasino"));
        fields.add(new Field(24, 25, -1, "yellow", "Gehe ins Spielcasino"));
        fields.add(new Field(25, 26, -1, "yellow", "Gehe ins Spielcasino"));
        fields.add(new Field(26, 27, -1, "blue", "Gehe ins Spielcasino"));
        fields.add(new Field(27, 28, -1, "yellow", "Gehe ins Spielcasino"));
        fields.add(new Field(28, 29, -1, "orange", "Gehe ins Spielcasino"));
    }

    public List<Field> getFieldData() {
        return this.fields;
    }
}
