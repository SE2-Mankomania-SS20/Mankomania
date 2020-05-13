package com.mankomania.game.core.data;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;


/*********************************
 Created by Fabian Oraze on 13.05.20
 *********************************/

public class TestIDConverter {

    private IDConverter converter;
    private ArrayList<Integer> conIDs;

    @BeforeEach
    public void init() {
        conIDs = new ArrayList<>();
        conIDs.add(111);
        conIDs.add(222);
        conIDs.add(333);
        converter = new IDConverter(conIDs);
    }

    @AfterEach
    public void tearDown() {
        conIDs = null;
        converter = null;
    }

    @Test
    public void testArrayIndicesOfPlayerConIDs() {
        int pl1 = converter.getArrayIndexOfPlayer(111);
        int pl2 = converter.getArrayIndexOfPlayer(222);
        int pl3 = converter.getArrayIndexOfPlayer(333);

        assertEquals(0, pl1);
        assertEquals(1, pl2);
        assertEquals(2, pl3);
    }

    @Test
    public void testGetIndicesArray(){
        ArrayList<Integer> indices = new ArrayList<>();
        indices.add(0);
        indices.add(1);
        indices.add(2);

        assertEquals(indices,converter.getArrayIndices());
    }

}
