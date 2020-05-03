package com.mankomania.game.core.fields;

import com.esotericsoftware.jsonbeans.JsonReader;
import com.esotericsoftware.jsonbeans.JsonValue;
import com.mankomania.game.core.fields.types.*;

import java.io.InputStreamReader;

//TODO: remove/replace sysout with logger

/**
 * Load and parse json data to be used by the game
 */
public class FieldDataLoader {
    private JsonValue jsonData;

    /**
     * @param path path to the json to be loaded
     */
    public void loadJson(String path) {
        JsonReader json = new JsonReader();
        jsonData = json.parse(new InputStreamReader(FieldDataLoader.class.getResourceAsStream(path)));
    }

    public Position3 calcPosition(Position3 field, Position3 offset, float rotation) {
        double c = Math.cos(rotation);
        double s = Math.sin(rotation);
        double[][] r = {{c, -s}, {s, c}};

        r[0][0] = r[0][0] * offset.x;
        r[0][1] = r[0][1] * offset.y;

        r[1][0] = r[1][0] * offset.x;
        r[1][1] = r[1][1] * offset.y;

        Position3 tmp = new Position3((float) (r[0][0] + r[0][1]), (float) (r[1][0] + r[1][1]), 0);
        tmp = tmp.add(field);
        return tmp;
    }

    /**
     * load the fields.data from json into their Classes and returns
     *
     * @return returns the loaded data as array where the index is used to get a specific field (nextField, optionalNextField, previousField)
     */
    public Field[] parseFields() {
        Field[] fields = null;

        if (jsonData != null) {
            JsonValue fieldsJson = jsonData.get("fields");
            JsonValue fieldsDataJson = fieldsJson.get("data");
            final int readAmount = 78;
            fields = new Field[readAmount];
            Position3[] offPos = parsePlayerPosOffsets();

            for (int i = 0; i < readAmount; i++) {
                JsonValue fieldJson = fieldsDataJson.get(i);
                int num = fieldJson.get("number").asInt() - 1;
                String type = fieldJson.get("type").asString();
                int nextField = fieldJson.get("nextField").asInt() - 1;
                int optionNextField = fieldJson.get("optionNextField").asInt() - 1;
                int prevField = fieldJson.get("prevField").asInt();
                String text = fieldJson.get("text").asString();
                FieldColor color = getColor(fieldJson.get("color").asString());

                Position3[] positions = new Position3[offPos.length];

                JsonValue posJson = fieldJson.get("position");
                Position3 position = new Position3(posJson.get("x").asFloat(), posJson.get("y").asFloat(), posJson.get("z").asFloat());

                JsonValue rotJson = fieldJson.get("rotation");
                Position3 rotation = new Position3(rotJson.get("x").asFloat(), rotJson.get("y").asFloat(), rotJson.get("z").asFloat());

                for (int j = 0; j < offPos.length; j++) {
                    positions[j] = calcPosition(position, offPos[j], rotation.z);
                }


                Field field = null;
                switch (type) {
                    case "Lotterie": {
                        int pay = fieldJson.get("pay").asInt();
                        field = new LotterieField(positions, nextField, optionNextField, prevField, text, color, pay);
                        break;
                    }
                    case "Special": {
                        field = parseSpecialField(positions, nextField, optionNextField, prevField, text, color, num);
                        break;
                    }
                    case "Jump": {
                        int jumpTo = fieldJson.get("jumpTo").asInt();
                        field = new JumpField(positions, nextField, optionNextField, prevField, text, color, jumpTo);
                        break;
                    }
                    case "Hotel": {
                        int buy = fieldJson.get("buy").asInt();
                        int rent = fieldJson.get("rent").asInt();
                        String name = fieldJson.get("name").asString();
                        field = new HotelField(positions, nextField, optionNextField, prevField, text, color, buy, rent, name);
                        break;
                    }
                    case "StockField": {
                        String stock = fieldJson.get("stock").asString();
                        int buy = fieldJson.get("buy").asInt();
                        field = new StockField(positions, nextField, optionNextField, prevField, text, color, stock, buy);
                        break;
                    }
                    case "GainMoney": {
                        int amount = fieldJson.get("amount").asInt();
                        field = new GainMoneyField(positions, nextField, optionNextField, prevField, text, color, amount);
                        break;
                    }
                    case "LoseMoney": {
                        int amount = fieldJson.get("amount").asInt();
                        field = new LoseMoneyField(positions, nextField, optionNextField, prevField, text, color, amount);
                        break;
                    }
                }
                fields[i] = field;
            }
        } else {
            System.out.println("load json first");
        }
        return fields;
    }


    /**
     * @return return Startfields there are usually 4, they are linked to the other fileds
     */
    public Field[] parseStart() {
        Field[] fields = null;
        if (jsonData != null) {
            fields = new Field[4];
            JsonValue startFields = jsonData.get("fields").get("starts");
            for (int i = 0; i < 4; i++) {
                JsonValue startFieldJson = startFields.get(i);
                int nextField = startFieldJson.get("nextField").asInt() - 1;
                FieldColor color = getColor(startFieldJson.get("color").asString());
                Position3[] positions = new Position3[1];
                JsonValue posJson = startFieldJson.get("position");
                positions[0] = new Position3(posJson.get("x").asFloat(), posJson.get("y").asFloat(), posJson.get("z").asFloat());

                fields[i] = new StartField(positions, nextField, -1, -1, "", color);
            }

        } else {
            System.out.println("load json first");
        }
        return fields;
    }

    /**
     * @return return an array of positions, first element is the center of the next four positions (used to calculate the offsets foreach field)
     */
    public Position3[] parsePlayerPosOffsets() {
        Position3[] positions = null;
        if (jsonData != null) {
            positions = new Position3[4];
            JsonValue playerPosOffset = jsonData.get("fields").get("playerPosOffset");
            for (int i = 0; i < 4; i++) {
                JsonValue playerPosOffsetEl = playerPosOffset.get(i);
                JsonValue posJson = playerPosOffsetEl.get("position");
                String name = playerPosOffsetEl.get("name").asString();
                Position3 pos = new Position3(posJson.get("x").asFloat(), posJson.get("y").asFloat(), posJson.get("z").asFloat());
                positions[i] = pos;
            }

        } else {
            System.out.println("load json first");
        }
        return positions;
    }

    private Field parseSpecialField(Position3[] positions, int nextField, int optionNextField, int prevField, String text, FieldColor color, int num) {
        Field field = null;
        switch (num) {
            case 2: {
                field = new SpecialField(positions, nextField, optionNextField, prevField, text, color) {
                    @Override
                    public void action() {
                        super.action();
                        // TODO: Implement action
                    }
                };
                break;
            }
            case 7: {
                field = new SpecialField(positions, nextField, optionNextField, prevField, text, color) {
                    @Override
                    public void action() {
                        super.action();
                        // TODO: Implement action
                    }
                };
                break;
            }
            case 9: {
                field = new SpecialField(positions, nextField, optionNextField, prevField, text, color) {
                    @Override
                    public void action() {
                        super.action();
                        // TODO: Implement action
                    }
                };
                break;
            }
            case 52: {
                field = new SpecialField(positions, nextField, optionNextField, prevField, text, color) {
                    @Override
                    public void action() {
                        super.action();
                        // TODO: Implement action
                    }
                };
                break;
            }
            case 68: {
                field = new SpecialField(positions, nextField, optionNextField, prevField, text, color) {
                    @Override
                    public void action() {
                        super.action();
                        // TODO: Implement action
                    }
                };
                break;
            }
            case 64: {
                field = new SpecialField(positions, nextField, optionNextField, prevField, text, color) {
                    @Override
                    public void action() {
                        super.action();
                        // TODO: Implement action
                    }
                };
                break;
            }
        }
        return field;
    }

    private FieldColor getColor(String color) {
        switch (color) {
            case "white": {
                return FieldColor.WHITE;
            }
            case "red": {
                return FieldColor.RED;
            }
            case "orange": {
                return FieldColor.ORANGE;
            }
            case "yellow": {
                return FieldColor.YELLOW;
            }
            case "blue": {
                return FieldColor.BLUE;
            }
            case "gray":
            case "grey": {
                return FieldColor.GREY;
            }
            default: {
                System.out.println("error parsing color!!");
                return null;
            }
        }
    }

}
