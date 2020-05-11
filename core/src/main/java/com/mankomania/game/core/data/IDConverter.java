package com.mankomania.game.core.data;

import java.util.ArrayList;
import java.util.HashMap;

/*********************************
 Created by Fabian Oraze on 10.05.20
 *********************************/

public class IDConverter {

    /**
     * Important look up table to get index applicable to arrays from Connection ID
     *
     * @key Connection ID of Player
     * @value array index starting from 0
     */
    private HashMap<Integer, Integer> lookUpTable;

    public IDConverter(ArrayList<Integer> conIds) {
        lookUpTable = new HashMap<>();
        for (int i = 0; i < conIds.size(); i++) {
            lookUpTable.put(conIds.get(i), i);
        }
    }

    public ArrayList<Integer> getArrayIndices() {
        ArrayList<Integer> list = new ArrayList<>();
        for (Integer index : lookUpTable.values()) {
            list.add(index);
        }
        return list;
    }

    public Integer getArrayIndexOfPlayer(Integer conn) {
        return lookUpTable.get(conn);
    }
}
