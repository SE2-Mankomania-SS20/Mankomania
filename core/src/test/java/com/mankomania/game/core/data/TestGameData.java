package com.mankomania.game.core.data;

import com.mankomania.game.core.fields.Position3;

import java.util.ArrayList;

import com.mankomania.game.core.fields.types.Field;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/*********************************
 Created by Fabian Oraze on 13.05.20
 *********************************/

public class TestGameData {

    private static GameData gameData;
    private ArrayList<Integer> conIds;
    private int[] startPositions;

    @BeforeAll
    public static void beforeClass() throws Exception {
        gameData = new GameData();
        gameData.loadData(TestGameData.class.getResourceAsStream("/resources/data.json"));
    }

    @AfterAll
    public static void afterClass() throws Exception {
        gameData = null;
    }

    @BeforeEach
    public void setUp() {
        conIds = new ArrayList<>();
        conIds.add(111);//connection Id
        conIds.add(222);//connection Id
        conIds.add(333);//connection Id
        startPositions = new int[]{78, 79, 80, 81};
    }

    @AfterEach
    public void tearDown() {
        conIds = null;
        startPositions = null;
    }

    @Test
    public void testPlayerInitializationSize() {
        gameData.intPlayers(conIds);
        assertEquals(3, gameData.getPlayers().size());
    }

    @Test
    public void testStartPositionPlayer1And2() {
        gameData.intPlayers(conIds);
        int start1 = gameData.getPlayers().get(0).getFieldID();
        int start2 = gameData.getPlayers().get(1).getFieldID();
        int start3 = gameData.getPlayers().get(2).getFieldID();
        assertEquals(startPositions[0], start1);
        assertEquals(startPositions[1], start2);
        assertEquals(startPositions[2], start3);
    }

    @Test
    public void testGetPosition() {
        gameData.intPlayers(conIds);
        float startPosX = -94;
        assertEquals(startPosX, gameData.getPosition3FromField(0).x, 1);

    }

    @Test
    public void testGetStartPositionInvalid() {
        gameData.intPlayers(conIds);
        assertEquals(null, gameData.getStartPosition(4));
        assertEquals(null, gameData.getStartPosition(-1));
    }

    @Test
    public void testGetValidStartPos() {
        gameData.intPlayers(conIds);
        Position3 pos1 = gameData.getPosition3FromField(0);
        Position3 pos2 = gameData.getPosition3FromField(2);

        assertEquals(pos1.x, gameData.getStartPosition(0).x, 0.01);
        assertEquals(pos2.x, gameData.getStartPosition(2).x, 0.01);
    }

    @Test
    public void testSetPlayerToNewField() {
        gameData.intPlayers(conIds);
        gameData.setPlayerToNewField(111, 12, 5);
        int expID = 11;
        assertEquals(expID, gameData.getPlayers().get(0).getFieldID());
    }

    @Test
    public void testSetToNewPosThenGetPos() {
        gameData.intPlayers(conIds);
        gameData.setPlayerToNewField(111, 12, 5);
        float posX = -50;
        assertEquals(posX, gameData.getPosition3FromField(0).x, 1);
    }

    @Test
    public void testGetAllFieldsAmount() {
        gameData.intPlayers(conIds);
        int size = gameData.getFields().length;
        assertEquals(82, size);
    }

    @Test
    public void testGetFieldsPositions() {
        int sizeStart = gameData.getFieldPos(79).length;
        int sizeNormal = gameData.getFieldPos(10).length;
        assertEquals(1, sizeStart);
        assertEquals(4, sizeNormal);

    }

    @Test
    public void testGetProperFieldByIndex() {
        Field field = gameData.getFieldByIndex(10);
        assertEquals("Geh zur Lotterie", field.getText());
    }

}
