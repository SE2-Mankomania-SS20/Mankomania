package com.mankomania.game.core.fields;

import com.esotericsoftware.jsonbeans.JsonReader;
import com.esotericsoftware.jsonbeans.JsonValue;
import com.mankomania.game.core.fields.types.*;

import java.io.InputStreamReader;

/**
 * Load and parse json data to be used by the game
 */
public class FieldDataLoader {
    private JsonValue jsondata;

    /**
     * load and parse json into jsondata
     *
     * @param path path to the json to load
     */
    public Field[] parse(String path) {
        JsonReader json = new JsonReader();
        jsondata = json.parse(new InputStreamReader(FieldDataLoader.class.getResourceAsStream(path)));
        final int readAmount = 78;
        Field[] fields = new Field[readAmount];

        JsonValue fieldsJson = jsondata.get("fields");
        JsonValue fieldsDataJson = fieldsJson.get("data");

        for (int i = 0; i < readAmount; i++) {
            JsonValue fieldJson = fieldsDataJson.get(i);
            int num = fieldJson.get("number").asInt();
            String type = fieldJson.get("type").asString();
            int nextField = fieldJson.get("nextField").asInt();
            int optionNextField = fieldJson.get("optionNextField").asInt();
            int prevField = fieldJson.get("prevField").asInt();
            String text = fieldJson.get("text").asString();
            FieldColor color = FieldColor.WHITE;
            String colorString = fieldJson.get("color").asString();
            switch (colorString) {
                case "white": {
                    color = FieldColor.WHITE;
                    break;
                }
                case "red": {
                    color = FieldColor.RED;
                    break;
                }
                case "orange": {
                    color = FieldColor.ORANGE;
                    break;
                }
                case "yellow": {
                    color = FieldColor.YELLOW;
                    break;
                }
                case "blue": {
                    color = FieldColor.BLUE;
                    break;
                }
                default: {
                    System.out.println("error parsing color!!");
                    break;
                }
            }

            JsonValue posJson = fieldJson.get("position");
            Position3 position = new Position3(posJson.get("x").asFloat(), posJson.get("x").asFloat(), posJson.get("z").asFloat());
            Position3[] positions = new Position3[0];

            JsonValue rotJson = fieldJson.get("rotation");
            Position3 rotation = new Position3(rotJson.get("x").asFloat(), rotJson.get("x").asFloat(), rotJson.get("z").asFloat());
            Field field = null;
            switch (type) {
                case "Lotterie": {
                    int pay = fieldJson.get("pay").asInt();
                    field = new LotterieField(positions,nextField,optionNextField,prevField,text,color,pay);
                    break;
                }
                case "Special": {
                    switch (num) {
                        case 2: {
                            field = new SpecialField(positions,nextField,optionNextField,prevField,text,color) {
                                @Override
                                public void action() {
                                    super.action();
                                }
                            };
                        }
                        case 7: {
                            field = new SpecialField(positions,nextField,optionNextField,prevField,text,color) {
                                @Override
                                public void action() {
                                    super.action();
                                }
                            };
                        }
                        case 9: {
                            field = new SpecialField(positions,nextField,optionNextField,prevField,text,color) {
                                @Override
                                public void action() {
                                    super.action();
                                }
                            };
                        }
                        case 52: {
                            field = new SpecialField(positions,nextField,optionNextField,prevField,text,color) {
                                @Override
                                public void action() {
                                    super.action();
                                }
                            };
                        }
                        case 68: {
                            field = new SpecialField(positions,nextField,optionNextField,prevField,text,color) {
                                @Override
                                public void action() {
                                    super.action();
                                }
                            };
                        }
                        case 64: {
                            field = new SpecialField(positions,nextField,optionNextField,prevField,text,color) {
                                @Override
                                public void action() {
                                    super.action();
                                }
                            };
                        }
                    }
                    break;
                }
                case "Jump": {
                    int jumpTo = fieldJson.get("jumpTo").asInt();
                    field = new JumpField(positions,nextField,optionNextField,prevField,text,color,jumpTo);
                    break;
                }
                case "Hotel": {
                    int buy = fieldJson.get("buy").asInt();
                    int rent = fieldJson.get("rent").asInt();
                    String name = fieldJson.get("name").asString();
                    field = new HotelField(positions,nextField,optionNextField,prevField,text,color,buy,rent,name);
                    break;
                }
                case "StockField": {
                    String stock = fieldJson.get("stock").asString();
                    int buy = fieldJson.get("buy").asInt();
                    field = new StockField(positions,nextField,optionNextField,prevField,text,color,stock,buy);
                    break;
                }
                case "GainMoney": {
                    int amount = fieldJson.get("amount").asInt();
                    field = new GainMoneyField(positions,nextField,optionNextField,prevField,text,color,amount);
                    break;
                }
                case "LoseMoney": {
                    int amount = fieldJson.get("amount").asInt();
                    field = new LoseMoneyField(positions,nextField,optionNextField,prevField,text,color,amount);
                    break;
                }
            }
            fields[i] = field;
        }
        return fields;
    }


}
