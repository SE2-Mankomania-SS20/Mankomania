package com.mankomania.game.core.fields;

import com.badlogic.gdx.math.Vector3;
import com.esotericsoftware.jsonbeans.JsonReader;
import com.esotericsoftware.jsonbeans.JsonValue;
import com.esotericsoftware.minlog.Log;
import com.mankomania.game.core.fields.types.*;
import com.mankomania.game.core.player.Hotel;
import com.mankomania.game.core.player.Stock;

import java.io.InputStream;

/**
 * Load and parse json data to be used by the game
 */
public class FieldDataLoader {
    public static final String FIELDS = "fields";
    public static final String POSITION = "position";
    public static final String AMOUNT = "amount";
    private JsonValue jsonData;
    private int[] startFieldIndex;


    /**
     * load the json file
     *
     * @param inStream InputStream to read a json
     */
    public void loadJson(InputStream inStream) {
        JsonReader json = new JsonReader();
        jsonData = json.parse(inStream);
    }

    /**
     * load the fields.data from json into their Classes
     *
     * @return returns the loaded data as array where the index is used to get a specific field (nextField, optionalNextField, previousField)
     */
    public Field[] parseFields() {
        Field[] fields;

        if (jsonData != null) {
            JsonValue fieldsJson = jsonData.get(FIELDS);
            JsonValue fieldsDataJson = fieldsJson.get("data");
            final int readAmount = 78;
            Field[] startFields = parseStart();
            fields = new Field[readAmount + startFields.length];
            Vector3[] offPos = parsePlayerPosOffsets();

            for (int i = 0; i < readAmount; i++) {
                JsonValue fieldJson = fieldsDataJson.get(i);
                int num = fieldJson.get("number").asInt() - 1;
                String type = fieldJson.get("type").asString();
                int nextField = fieldJson.get("nextField").asInt() - 1;
                int optionNextField = fieldJson.get("optionNextField").asInt() - 1;
                int prevField = fieldJson.get("prevField").asInt();
                String text = fieldJson.get("text").asString();
                FieldColor color = getColor(fieldJson.get("color").asString());

                Vector3[] positions = new Vector3[offPos.length];

                JsonValue posJson = fieldJson.get(POSITION);
                Vector3 position = new Vector3(posJson.get("x").asFloat(), posJson.get("y").asFloat(), posJson.get("z").asFloat());

                JsonValue rotJson = fieldJson.get("rotation");
                Vector3 rotation = new Vector3(rotJson.get("x").asFloat(), rotJson.get("y").asFloat(), rotJson.get("z").asFloat());

                for (int j = 0; j < offPos.length; j++) {
                    Vector3 temp = calcPosition(position, offPos[j], rotation.z);
                    // swap y and z since libGdx uses them like that
                    // * 100 to convert from m to cm
                    temp.x = temp.x * 100;
                    float tempF = temp.y * 100;
                    temp.y = temp.z * 100;
                    temp.z = -tempF;
                    positions[j] = temp;
                }

                Field field = null;
                switch (type) {
                    case "Lotterie": {
                        int pay = fieldJson.get("pay").asInt();
                        field = new LotterieField(positions, nextField, optionNextField, prevField, text, color, pay, num);
                        break;
                    }
                    case "Special": {
                        field = new SpecialField(positions, nextField, optionNextField, prevField, text, color, num);
                        break;
                    }
                    case "Jump": {
                        int jumpTo = fieldJson.get("jumpTo").asInt() - 1;
                        field = new JumpField(positions, nextField, optionNextField, prevField, text, color, jumpTo, num);
                        break;
                    }
                    case "Hotel": {
                        int buy = fieldJson.get("buy").asInt();
                        int rent = fieldJson.get("rent").asInt();
                        Hotel hotelType = getHotel(fieldJson.get("name").asString());
                        field = new HotelField(positions, nextField, optionNextField, prevField, text, color, buy, rent, hotelType, num);
                        break;
                    }
                    case "StockField": {
                        Stock stockType = getStock(fieldJson.get("stock").asString());
                        int buy = fieldJson.get("buy").asInt();
                        field = new StockField(positions, nextField, optionNextField, prevField, text, color, stockType, buy, num);
                        break;
                    }
                    case "GainMoney": {
                        int amount = fieldJson.get(AMOUNT).asInt();
                        field = new GainMoneyField(positions, nextField, optionNextField, prevField, text, color, amount, num);
                        break;
                    }
                    case "LoseMoney": {
                        int amount = fieldJson.get(AMOUNT).asInt();
                        field = new LoseMoneyField(positions, nextField, optionNextField, prevField, text, color, amount, num);
                        break;
                    }
                    case "PayLotterie": {
                        int amount = fieldJson.get(AMOUNT).asInt();
                        field = new PayLotterieField(positions, nextField, optionNextField, prevField, text, color, amount, num);
                        break;
                    }
                    case "minigame":
                    case "Minigame": {
                        field = parseMinigameField(positions, nextField, optionNextField, prevField, text, color, num);
                        break;
                    }
                    default:
                        break;
                }
                fields[i] = field;
            }
            startFieldIndex = new int[startFields.length];

            for (int i = 0; i < startFields.length; i++) {
                fields[i + readAmount] = startFields[i];
                startFieldIndex[i] = i + readAmount;
            }
            return fields;
        } else {
            Log.error("Could not parse Field: json not loaded");
            return new Field[0];
        }
    }

    /**
     * convert stockstring to enum
     *
     * @param stock stock as string to be parsed into Stock enum
     * @return Stock enum
     */
    private Stock getStock(String stock) {
        switch (stock) {
            case "Bruchstahl-AG": {
                return Stock.BRUCHSTAHLAG;
            }
            case "Trockenöl-AG": {
                return Stock.TROCKENOEL;
            }
            case "Kurzschluss-Versorgungs-AG": {
                return Stock.KURZSCHLUSSAG;
            }
            default: {
                Log.error("error parsing stock");
                return null;
            }
        }
    }

    /**
     * convert hotelstring to enum
     *
     * @param name string name to be parsed into Hotel enum
     * @return Hotel enum
     */
    private Hotel getHotel(String name) {
        switch (name) {
            case "Hotel Sehblick": {
                return Hotel.HOTELSEHBLICK;
            }
            case "Hotel Ruhe Sanft": {
                return Hotel.HOTELRUHESANFT;
            }
            case "Hotel Willa Nicht": {
                return Hotel.HOTELWILLANICHT;
            }
            case "Hotel Santa Fu": {
                return Hotel.HOTELSANTAFU;
            }
            case "Schloss Dietrich": {
                return Hotel.SCHLOSSDIETRICH;
            }
            case "Hotel Garnie": {
                return Hotel.HOTELGARNIE;
            }
            default: {
                Log.error("error parsing hotel");
                return null;
            }
        }
    }


    /**
     * parse the start positions from json
     *
     * @return return Startfields there are usually 4, they are linked to the other fileds
     */
    private Field[] parseStart() {
        Field[] fields;
        if (jsonData != null) {
            fields = new Field[4];
            JsonValue startFields = jsonData.get(FIELDS).get("starts");
            for (int i = 0; i < 4; i++) {
                JsonValue startFieldJson = startFields.get(i);
                int nextField = startFieldJson.get("nextField").asInt() - 1;
                FieldColor color = getColor(startFieldJson.get("color").asString());
                Vector3[] positions = new Vector3[1];
                JsonValue posJson = startFieldJson.get(POSITION);
                positions[0] = new Vector3(posJson.get("x").asFloat() * 100, posJson.get("z").asFloat() * 100, -posJson.get("y").asFloat() * 100);
                fields[i] = new StartField(positions, nextField, -1, -1, "", color, 78 + i);
            }
            return fields;
        } else {
            Log.error("Could not parse Startfields: json not loaded");
            return new Field[0];
        }
    }

    /**
     * calculate the positions for one field
     * a field only has one center position
     * the actuall 4 positions that will be used are calculated with this function and the player pos offsets
     *
     * @param field    field position
     * @param offset   offset that will be applied to field position
     * @param rotation rotation that will be applied to the offset+field position
     * @return field + offset rotated by rotation
     */
    private Vector3 calcPosition(Vector3 field, Vector3 offset, float rotation) {
        double c = Math.cos(rotation);
        double s = Math.sin(rotation);
        double[][] r = {{c, -s}, {s, c}};

        r[0][0] = r[0][0] * offset.x;
        r[0][1] = r[0][1] * offset.y;

        r[1][0] = r[1][0] * offset.x;
        r[1][1] = r[1][1] * offset.y;

        Vector3 tmp = new Vector3((float) (r[0][0] + r[0][1]), (float) (r[1][0] + r[1][1]), 0);
        tmp = tmp.add(field);
        return tmp;
    }

    /**
     * get the player offsets from json
     *
     * @return return an array of positions, first element is the center of the next four positions (used to calculate the offsets foreach field)
     */
    private Vector3[] parsePlayerPosOffsets() {
        Vector3[] positions;
        if (jsonData != null) {
            positions = new Vector3[4];
            JsonValue playerPosOffset = jsonData.get(FIELDS).get("playerPosOffset");
            for (int i = 0; i < 4; i++) {
                JsonValue playerPosOffsetEl = playerPosOffset.get(i);
                JsonValue posJson = playerPosOffsetEl.get(POSITION);
                Vector3 pos = new Vector3(posJson.get("x").asFloat(), posJson.get("y").asFloat(), posJson.get("z").asFloat());
                positions[i] = pos;
            }
            return positions;
        } else {
            Log.error("Could not parse Playeroffset: json not loaded");
            return new Vector3[0];
        }
    }

    /**
     * Parse minigame field
     *
     * @param positions       Vector3[] positions on that Field
     * @param nextField       int nextField
     * @param optionNextField int optionNextField
     * @param prevField       int previouseField
     * @param text            Field description
     * @param color           Enum color
     * @param id              int fieldId
     * @return SpecialField
     */
    private MinigameField parseMinigameField(Vector3[] positions, int nextField, int optionNextField, int prevField, String text, FieldColor color, int id) {
        MinigameField field;

        switch (id) {
            // Minigame field: "Aktien Boerse"
            case 15: {
                field = new MinigameField(positions, nextField, optionNextField, prevField, text, color, MinigameType.AKTIEN_BOERSE, id);
                break;
            }
            // Minigame field: "Casino"
            case 26: {
                field = new MinigameField(positions, nextField, optionNextField, prevField, text, color, MinigameType.CASINO, id);
                break;
            }
            // Minigame field: "Böse 1 Minispiel"
            case 55: {
                field = new MinigameField(positions, nextField, optionNextField, prevField, text, color, MinigameType.BOESE1, id);
                break;
            }
            // Minigame field: "Pferderennen"
            case 66: {
                field = new MinigameField(positions, nextField, optionNextField, prevField, text, color, MinigameType.PFERDERENNEN, id);
                break;
            }
            default: {
                throw new IllegalArgumentException("given field with id " + id + " is not parseable as minigame field");
            }
        }

        return field;
    }

    /**
     * convert string to enum color
     *
     * @param color colorstring
     * @return Enum color
     */
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
                Log.error("error parsing color!!");
                return null;
            }
        }
    }

    public int[] getStartFieldIndex() {
        return startFieldIndex;
    }

}
