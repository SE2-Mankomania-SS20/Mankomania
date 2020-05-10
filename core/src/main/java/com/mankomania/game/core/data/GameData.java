package com.mankomania.game.core.data;

import com.mankomania.game.core.fields.FieldDataLoader;
import com.mankomania.game.core.fields.Position3;
import com.mankomania.game.core.fields.types.Field;
import com.mankomania.game.core.fields.types.HotelField;
import com.mankomania.game.core.player.Player;

import javax.security.auth.callback.Callback;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

/*********************************
 Created by Fabian Oraze on 04.05.20
 *********************************/

public class GameData {


    private Field[] fields;

    private int[] startFieldsIndices;

    private int lotteryAmount;

    /**
     * key: connection ID from Player
     * value: Player Object that holds all player relevant info
     */
    private PlayerHashMap players;


    /**
     * key: HotelFieldIndex (Index from fields array)
     * value: PlayerID --> key from players HashMap
     */
    private HashMap<Integer, Integer> hotels;


    public GameData() {
    }


    /**
     * Method to load initial data into gameData object
     *
     * @param stream gets data from path gotten by stream
     */
    public void loadData(InputStream stream) {
        FieldDataLoader loader = new FieldDataLoader();
        loader.loadJson(stream);
        fields = loader.parseFields();
        startFieldsIndices = loader.getStartFieldIndex();
        hotels = new HashMap<>();
        for (int i = 0; i < fields.length; i++) {
            if (fields[i] instanceof HotelField) {
                hotels.put(i, null);
            }
        }
    }

    /**
     * Initializes player hashMap object with given parameter
     *
     * @param listIDs connection IDs which are gotten from server
     */
    public void intPlayers(ArrayList<Integer> listIDs) {
        this.players = new PlayerHashMap();
        for (int i = 0; i < listIDs.size(); i++) {
            players.put(listIDs.get(i), new Player());
            //set players start field to one of the 4 starting points beginning at index 78
            players.get(listIDs.get(i)).setCurrentField(78 + i);
        }
        this.lotteryAmount = 0;

    }


    public PlayerHashMap getPlayers() {
        return players;
    }

    /**
     * get start position for a certain player
     *
     * @param player defines which playerStart field will be used 1 to 4 possible
     * @return returns a Position3 object which can be used with helper class to get Vector3
     */
    public Position3 getStartPosition(int player) {
        if (player >= 0 && player < 4) {
            return fields[startFieldsIndices[player]].getPositions()[0];
        } else {
            return null;
        }
    }

    public ArrayList<Field> movePlayer(Field endField) {
        ArrayList<Field> fieldsToMovePast = new ArrayList<>();


        return fieldsToMovePast;
    }

    public void setPlayerToNewField(Integer PlayerID, Field field){

    }

}
