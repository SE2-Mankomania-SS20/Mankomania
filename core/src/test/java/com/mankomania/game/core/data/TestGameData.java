package com.mankomania.game.core.data;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector3;
import com.mankomania.game.core.fields.types.Field;
import com.mankomania.game.core.player.Player;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/*
 Created by Fabian Oraze on 13.05.20
 */

public class TestGameData {

    public static final int CONNECTION_ID_P1 = 111;
    public static final int CONNECTION_ID_P2 = 222;
    public static final int CONNECTION_ID_P3 = 333;
    public static final int CONNECTION_ID_P4 = 444;
    public static final int PLAYER_INDEX_P1 = 0;
    public static final int PLAYER_INDEX_P2 = 1;
    public static final int PLAYER_INDEX_P3 = 2;
    public static final int PLAYER_INDEX_P4 = 3;
    private static GameData gameData;
    private ArrayList<Player> players;
    private int[] startPositions;

    @BeforeAll
    public static void beforeClass() {
        gameData = new GameData();
        gameData.loadData(TestGameData.class.getResourceAsStream("/resources/data.json"));
    }

    @AfterAll
    public static void afterClass() {
        gameData = null;
    }

    @BeforeEach
    public void setUp() {
        players = new ArrayList<>();
        players.add(new Player(78, CONNECTION_ID_P1, gameData.getFields()[78].getPositions()[0], PLAYER_INDEX_P1));//connection Id
        players.add(new Player(79, CONNECTION_ID_P2, gameData.getFields()[79].getPositions()[0], PLAYER_INDEX_P2));//connection Id
        players.add(new Player(80, CONNECTION_ID_P3, gameData.getFields()[80].getPositions()[0], PLAYER_INDEX_P3));//connection Id
        players.add(new Player(81, CONNECTION_ID_P4, gameData.getFields()[81].getPositions()[0], PLAYER_INDEX_P4));//connection Id
        startPositions = new int[]{78, 79, 80, 81};
    }

    @AfterEach
    public void tearDown() {
        players = null;
        startPositions = null;

    }

    @Test
    public void testPlayerInitializationSize() {
        gameData.intPlayers(players);
        assertEquals(4, gameData.getPlayers().size());
    }

    @Test
    public void testStartPositionPlayer1And2() {
        gameData.intPlayers(players);
        int start1 = gameData.getPlayers().get(0).getCurrentField();
        int start2 = gameData.getPlayers().get(1).getCurrentField();
        int start3 = gameData.getPlayers().get(2).getCurrentField();
        assertEquals(startPositions[0], start1);
        assertEquals(startPositions[1], start2);
        assertEquals(startPositions[2], start3);
    }

    @Test
    public void testGetPosition() {
        gameData.intPlayers(players);
        float startPosX = -94;
        assertEquals(startPosX, gameData.getPlayerPosition(0).x, 1);

    }

    @Test
    public void testGetStartPositionInvalid() {
        gameData.intPlayers(players);
        assertNull(gameData.getStartPosition(4));
        assertNull(gameData.getStartPosition(-1));
    }

    @Test
    public void testGetValidStartPos() {
        gameData.intPlayers(players);
        Vector3 pos1 = gameData.getPlayerPosition(0);
        Vector3 pos2 = gameData.getPlayerPosition(2);

        assertEquals(pos1.x, gameData.getStartPosition(0).x, 0.01);
        assertEquals(pos2.x, gameData.getStartPosition(2).x, 0.01);
    }

    @Test
    public void testSetPlayerToNewField() {
        gameData.intPlayers(players);
        gameData.setPlayerToField(PLAYER_INDEX_P1, 12);
        int expID = 12;
        assertEquals(expID, gameData.getPlayers().get(0).getCurrentField());
    }

    @Test
    public void testSetToNewPosThenGetPos() {
        gameData.intPlayers(players);
        gameData.setPlayerToField(PLAYER_INDEX_P1, 12);
        float posX = -41;
        assertEquals(posX, gameData.getPlayerPosition(0).x, 1);
    }

    @Test
    public void testGetAllFieldsAmount() {
        gameData.intPlayers(players);
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

    @Test
    public void testAddToLottery() {
        gameData.intPlayers(players);
        int amountToPay = 5000;
        gameData.addToLotteryFromPlayer(CONNECTION_ID_P1, amountToPay);
        gameData.addToLotteryFromPlayer(CONNECTION_ID_P2, amountToPay);
        assertEquals(10000, gameData.getLotteryAmount());
    }

    @Test
    public void testPlayerMoneyAmountAfterLotteryWin() {
        gameData.intPlayers(players);
        int amountToPay = 5000;
        gameData.setLotteryAmount(amountToPay * 2);
        gameData.addFromLotteryAmountToPlayer(CONNECTION_ID_P2);
        int expAmount = 1000000 + amountToPay * 2;
        assertEquals(expAmount, gameData.getPlayers().get(1).getMoney());
    }

    @Test
    public void testPlayerMoneyAmountAfterPayToLottery() {
        gameData.intPlayers(players);
        int amountToPay = 5000;
        gameData.addToLotteryFromPlayer(CONNECTION_ID_P1, amountToPay);
        //initial moneyAmount is 1 000 000 after init
        int expAmount = 1000000 - amountToPay;
        assertEquals(expAmount, gameData.getPlayers().get(0).getMoney());
    }

}
