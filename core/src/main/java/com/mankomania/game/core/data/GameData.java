package com.mankomania.game.core.data;

import com.mankomania.game.core.fields.types.Field;
import com.mankomania.game.core.player.Player;

import java.util.ArrayList;
import java.util.HashMap;

/*********************************
 Created by Fabian Oraze on 04.05.20
 *********************************/

public class GameData {


    private Field[] fields;

    private Field[] startFields;

    private int lotteryAmount;

    /**
     * key: connection ID from Player
     * value: Player Object that holds all player relevant info
     */
    private HashMap<Integer, Player> players;


    /**
     * key: HotelFieldIndex (Index from fields array)
     * value: PlayerID --> key from players HashMap
     */
    private HashMap<Integer, Integer> hotels;


    public GameData(ArrayList<Integer> listIDs) {
        this.players = new HashMap<>();
        for (Integer id : listIDs) {
            players.put(id, new Player());
            //TODO: Init all fields from parser
        }

    }

    //TODO: field data


}
