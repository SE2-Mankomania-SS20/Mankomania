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
        fields.add(new Field(0, 1, -1, "white", "Gehe ins Spielcasino"));
        fields.add(new Field(1, 2, -1, "yellow", "Gehe ins Spielcasino"));
        fields.add(new Field(2, 3, -1, "orange", "Gehe ins Spielcasino"));
        fields.add(new Field(3, 4, -1, "orange", "Gehe ins Spielcasino"));
        fields.add(new Field(4, 5, -1, "blue", "Gehe ins Spielcasino"));
        fields.add(new Field(5, 6, -1,  "yellow", "Gehe ins Spielcasino"));
        fields.add(new Field(6, 7, -1, "yellow", "Gehe ins Spielcasino"));
        fields.add(new Field(7, 8, 12, "orange", "Gehe ins Spielcasino"));
        fields.add(new Field(8, 9, -1, "yellow", "Gehe ins Spielcasino"));
        fields.add(new Field(9, 10, -1, "yellow", "Gehe ins Spielcasino"));
        fields.add(new Field(10, 11, -1, "orange", "Gehe ins Spielcasino"));
        fields.add(new Field(11, 16, -1, "yellow", "Gehe ins Spielcasino"));
        fields.add(new Field(12, 13, -1, "white", "Gehe ins Spielcasino"));
        fields.add(new Field(13, 14, -1, "magenta", "Gehe ins Spielcasino"));
        fields.add(new Field(14, 15, -1, "yellow", "Gehe ins Spielcasino"));
        fields.add(new Field(15, 16, -1, "yellow", "Gehe ins Spielcasino"));
        fields.add(new Field(16, 17, -1, "yellow", "Gehe ins Spielcasino"));
        fields.add(new Field(17, 18, -1, "orange", "Gehe ins Spielcasino"));
        fields.add(new Field(18, 19, -1, "yellow", "Gehe ins Spielcasino"));
        fields.add(new Field(19, 20, -1, "yellow", "Gehe ins Spielcasino"));
        fields.add(new Field(20, 21, -1, "orange", "Gehe ins Spielcasino"));
        fields.add(new Field(21, 22, -1, "orange", "Gehe ins Spielcasino"));
        fields.add(new Field(22, 23, -1, "yellow", "Gehe ins Spielcasino"));
        fields.add(new Field(23, 24, -1, "blue", "Gehe ins Spielcasino"));
        fields.add(new Field(24, 25, 29, "yellow", "Gehe ins Spielcasino"));
        fields.add(new Field(25, 26, -1, "yellow", "Gehe ins Spielcasino"));
        fields.add(new Field(26, 27, -1, "orange", "Gehe ins Spielcasino"));
        fields.add(new Field(27, 28, -1, "yellow", "Gehe ins Spielcasino"));
        fields.add(new Field(28, 33, -1, "yellow", "Gehe ins Spielcasino"));
        fields.add(new Field(29, 30, -1, "yellow", "Gehe ins Spielcasino"));
        fields.add(new Field(30, 31, -1, "yellow", "Gehe ins Spielcasino"));
        fields.add(new Field(31, 32, -1, "magenta", "Gehe ins Spielcasino"));
        fields.add(new Field(32, 33, -1, "white", "Gehe ins Spielcasino"));
        fields.add(new Field(33, 34, -1, "orange", "Gehe ins Spielcasino"));
        fields.add(new Field(34, 35, -1, "yellow", "Gehe ins Spielcasino"));
        fields.add(new Field(35, 36, -1, "yellow", "Gehe ins Spielcasino"));
        fields.add(new Field(36, 37, -1, "blue", "Gehe ins Spielcasino"));
        fields.add(new Field(37, 38, -1, "orange", "Gehe ins Spielcasino"));
        fields.add(new Field(38, 39, -1, "yellow", "Gehe ins Spielcasino"));
        fields.add(new Field(39, 40, -1, "yellow", "Gehe ins Spielcasino"));
        fields.add(new Field(40, 41, -1, "yellow", "Gehe ins Spielcasino"));
        fields.add(new Field(41, 42, -1, "orange", "Gehe ins Spielcasino"));
        fields.add(new Field(42, 43, -1, "yellow", "Gehe ins Spielcasino"));
        fields.add(new Field(43, 44, -1, "yellow", "Gehe ins Spielcasino"));
        fields.add(new Field(44, 45, -1, "orange", "Gehe ins Spielcasino"));
        fields.add(new Field(45, 46, -1, "blue", "Gehe ins Spielcasino"));
        fields.add(new Field(46, 47, -1, "orange", "Gehe ins Spielcasino"));
        fields.add(new Field(47, 48, -1, "yellow", "Gehe ins Spielcasino"));
        fields.add(new Field(48, 49, -1, "yellow", "Gehe ins Spielcasino"));
        fields.add(new Field(49, 50, 54, "orange", "Gehe ins Spielcasino"));
        fields.add(new Field(50, 51, -1, "yellow", "Gehe ins Spielcasino"));
        fields.add(new Field(51, 52, -1, "yellow", "Gehe ins Spielcasino"));
        fields.add(new Field(52, 53, -1, "orange", "Gehe ins Spielcasino"));
        fields.add(new Field(53, 58, -1, "yellow", "Gehe ins Spielcasino"));
        fields.add(new Field(54, 55, -1, "white", "Gehe ins Spielcasino"));
        fields.add(new Field(55, 56, -1, "magenta", "Gehe ins Spielcasino"));
        fields.add(new Field(56, 57, -1, "yellow", "Gehe ins Spielcasino"));
        fields.add(new Field(57, 58, -1, "yellow", "Gehe ins Spielcasino"));
        fields.add(new Field(58, 59, -1, "yellow", "Gehe ins Spielcasino"));
        fields.add(new Field(59, 60, -1, "blue", "Gehe ins Spielcasino"));
        fields.add(new Field(60, 61, -1, "yellow", "Gehe ins Spielcasino"));
        fields.add(new Field(61, 62, -1, "orange", "Gehe ins Spielcasino"));
        fields.add(new Field(62, 63, -1, "orange", "Gehe ins Spielcasino"));
        fields.add(new Field(63, 64, -1, "yellow", "Gehe ins Spielcasino"));
        fields.add(new Field(64, 65, -1, "yellow", "Gehe ins Spielcasino"));
        fields.add(new Field(65, 66, -1, "blue", "Gehe ins Spielcasino"));
        fields.add(new Field(66, 67, 71, "orange", "Gehe ins Spielcasino"));
        fields.add(new Field(67, 68, -1, "yellow", "Gehe ins Spielcasino"));
        fields.add(new Field(68, 69, -1, "yellow", "Gehe ins Spielcasino"));
        fields.add(new Field(69, 70, -1, "yellow", "Gehe ins Spielcasino"));
        fields.add(new Field(70, 75, -1, "yellow", "Gehe ins Spielcasino"));
        fields.add(new Field(71, 72, -1, "yellow", "Gehe ins Spielcasino"));
        fields.add(new Field(72, 73, -1, "yellow", "Gehe ins Spielcasino"));
        fields.add(new Field(73, 74, -1, "magenta", "Gehe ins Spielcasino"));
        fields.add(new Field(74, 75, -1, "white", "Gehe ins Spielcasino"));
        fields.add(new Field(75, 76, -1, "orange", "Gehe ins Spielcasino"));
        fields.add(new Field(76, 77, -1, "yellow", "Gehe ins Spielcasino"));
        fields.add(new Field(77, 78, -1, "yellow", "Gehe ins Spielcasino"));
        fields.add(new Field(78, 79, -1, "blue", "Gehe ins Spielcasino"));
        fields.add(new Field(79, 80, -1, "orange", "Gehe ins Spielcasino"));
        fields.add(new Field(80, 81, -1, "yellow", "Gehe ins Spielcasino"));
        fields.add(new Field(81, 0, -1, "yellow", "Gehe ins Spielcasino"));
    }

    public List<Field> getFieldData() {
        return this.fields;
    }
}
