package com.mankomania.game.server.data;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import com.mankomania.game.core.data.GameData;
import com.mankomania.game.core.network.messages.clienttoserver.baseturn.DiceResultMessage;
import com.mankomania.game.core.network.messages.servertoclient.baseturn.PlayerCanRollDiceMessage;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Map;

import static org.mockito.Mockito.*;

/**
 * This class is used for testing the base turn functionality.
 * I.e.: start game, rolling the dice, moving player, taking intersections, end turn
 */
public class TestServerDataBaseTurn {
    private static GameData gameData;
    private ServerData serverData;

    private Server mockedServer;
    private Connection mockedConnection;

    @BeforeAll
    public static void initAll() {
        gameData = new GameData();
//        gameData.loadData(TestServerDataBaseTurn.class.getResourceAsStream("/resources/data.json"));
    }

    @BeforeEach
    public void initEach() {
        // mock server before each, so the verify count will get reset after each test, which would not be the case if it is static
        this.mockedServer = mock(Server.class);

//        this.mockedConnection = mock(Connection.class);
//        when(this.mockedConnection.getID()).thenReturn(42);

        this.serverData = new ServerData(mockedServer);
    }

    @Test
    public void testConnectPlayer_connectOnePlayer() {
        Connection mockedConnectionPlayer1 = mock(Connection.class);
        when(mockedConnectionPlayer1.getID()).thenReturn(7); // mock a connection with id 7

        // connect the mocked connection
        boolean connectPlayerReturns = this.serverData.connectPlayer(mockedConnectionPlayer1);

        // test if we got the expected return value and if the usermap got set accordingly
        Assertions.assertTrue(connectPlayerReturns, "connectPlayer should be returning true when adding the first player");
        Assertions.assertEquals(1, this.serverData.getPlayerList().size(), "the player list should have exactly one entry");
        Assertions.assertEquals(7, this.serverData.getPlayerList().get(0), "the connection id of the first player in the list should be 7");

        Map<Integer, Connection> userMap = this.serverData.getUserMap();
        Assertions.assertEquals(1, userMap.size(), "the usermap should have size 1");
        Assertions.assertEquals(mockedConnectionPlayer1, userMap.get(7), "usermap should return the given connection when fetching with connection id 7");
    }

    @Test
    public void testConnectPlayer_connectFivePlaers() {
        Connection firstPlayerConnection = this.mockConnection(7);
        Connection secondPlayerConnection = this.mockConnection(42);

        boolean returnsAfterFirstPlayer = this.serverData.connectPlayer(firstPlayerConnection);
        boolean returnsAfterSecondPlayer = this.serverData.connectPlayer(secondPlayerConnection);
        boolean returnsAfterThirdPlayer = this.serverData.connectPlayer(this.mockConnection(666));
        boolean returnsAfterFourthPlayer = this.serverData.connectPlayer(this.mockConnection(1337));
        boolean returnsAfterFifthPlayer = this.serverData.connectPlayer(this.mockConnection(13));

        // check if the first four player connected successfully and the last one should be rejected
        Assertions.assertTrue(returnsAfterFirstPlayer);
        Assertions.assertTrue(returnsAfterSecondPlayer);
        Assertions.assertTrue(returnsAfterThirdPlayer);
        Assertions.assertTrue(returnsAfterFourthPlayer);
        Assertions.assertFalse(returnsAfterFifthPlayer);

        Assertions.assertEquals(4, this.serverData.getPlayerList().size());
        Assertions.assertEquals(42, this.serverData.getPlayerList().get(1));

        // check if we get the right connection through usermap
        Assertions.assertEquals(firstPlayerConnection, this.serverData.getUserMap().get(7));
        Assertions.assertEquals(secondPlayerConnection, this.serverData.getUserMap().get(42));
    }

    @Test
    public void testDisconnectPlayer() {
        Connection firstConnection = this.mockConnection(9);
        Connection secondConnection = this.mockConnection(7);
        this.serverData.connectPlayer(firstConnection);
        this.serverData.connectPlayer(secondConnection);

        Assertions.assertEquals(2, this.serverData.getPlayerList().size());

        this.serverData.disconnectPlayer(secondConnection);

        // check if we now only have one player left
        Assertions.assertEquals(1, this.serverData.getPlayerList().size());
        Assertions.assertEquals(1, this.serverData.getUserMap().size());

        this.serverData.disconnectPlayer(firstConnection);

        // check if we now have none player left
        Assertions.assertEquals(0, this.serverData.getPlayerList().size());
        Assertions.assertEquals(0, this.serverData.getUserMap().size());
    }

    @Test
    public void testCheckForStart_noPlayer() {
        // check for start should return false with no player added
        Assertions.assertFalse(this.serverData.checkForStart());
    }

    @Test
    public void testCheckForStart_playerNotReady() {
        Connection connection1 = this.mockConnection(3);
        Connection connection2 = this.mockConnection(5);
        this.serverData.connectPlayer(connection1);
        this.serverData.connectPlayer(connection2);

        this.serverData.playerReady(connection1);

        // even though we now have to player, not all of them are ready, therefore the game should not start
        Assertions.assertFalse(this.serverData.checkForStart());
    }

    @Test
    public void testCheckForStart_playerReady() {
        Connection connection1 = this.mockConnection(3);
        Connection connection2 = this.mockConnection(5);
        this.serverData.connectPlayer(connection1);
        this.serverData.connectPlayer(connection2);

        // ready up players
        this.serverData.playerReady(connection1);
        this.serverData.playerReady(connection2);

        // now all players are ready
        Assertions.assertTrue(this.serverData.checkForStart());
    }

    @Test
    public void testSetNextPlayerTurn() {
        this.serverData.connectPlayer(this.mockConnection(2));
        this.serverData.connectPlayer(this.mockConnection(7));

        Assertions.assertEquals(2, this.serverData.getCurrentPlayerTurnConnectionId());

        this.serverData.setNextPlayerTurn();
        Assertions.assertEquals(7, this.serverData.getCurrentPlayerTurnConnectionId());

        this.serverData.setNextPlayerTurn();
        Assertions.assertEquals(2, this.serverData.getCurrentPlayerTurnConnectionId());
    }

    @Test
    public void testStartGameLoop() {
        // add a player
        this.serverData.connectPlayer(this.mockConnection(42));

        this.serverData.startGameLoop();

        // expected message sent
        PlayerCanRollDiceMessage expectedMessage = PlayerCanRollDiceMessage.createPlayerCanRollDiceMessage(42);

        // check if server.sendToAllTCP got called with the exact right message
        // TODO: refactor message ctors and implement hash() and equals() methods on each message
        // verify(this.mockedServer, times(1)).sendToAllTCP(expectedMessage);

        // check if gamestate is okay
        Assertions.assertEquals(GameState.WAIT_FOR_DICE_RESULT, this.serverData.getCurrentState());
    }

    @Test
    public void testSendPlayerCanRollDice_wrongState() {
        // set gamestate to something that shpould not work
        this.serverData.setCurrentState(GameState.WAIT_INTERSECTION_SELECTION);
        // call method while in wrong state
        this.serverData.sendPlayerCanRollDice();

        // check if none of the send methods of the server were called
        verify(this.mockedServer, times(0)).sendToAllTCP(Mockito.any());
        verify(this.mockedServer, times(0)).sendToAllExceptTCP(Mockito.anyInt(), Mockito.any());

        // check if gamestate did not change
        Assertions.assertEquals(GameState.WAIT_INTERSECTION_SELECTION, this.serverData.getCurrentState());
    }

    @Test
    public void testSendPlayerCanRollDice_rightState() {
        // add a player
        this.serverData.connectPlayer(this.mockConnection(2));
        // set gamestate for the function to work
        this.serverData.setCurrentState(GameState.PLAYER_CAN_ROLL_DICE);
        // call method
        this.serverData.sendPlayerCanRollDice();

        // implement hash/equals for the exact verification to work
        // check if send methods got called with the right parameters
        verify(this.mockedServer, times(1)).sendToAllTCP(Mockito.any());
        verify(this.mockedServer, times(1)).sendToAllExceptTCP(eq(2), Mockito.any());

        // check if gamestate did not change
        Assertions.assertEquals(GameState.WAIT_FOR_DICE_RESULT, this.serverData.getCurrentState());
    }

    @Test
    public void testGotDiceResult_wrongState() {
        // add a player
        this.serverData.connectPlayer(this.mockConnection(2));
        // set gamestate for the function not to work
        this.serverData.setCurrentState(GameState.PLAYER_CAN_ROLL_DICE);
        // call method
        this.serverData.gotDiceRollResult(new DiceResultMessage());

        // verify that state has not changed and no send call has been made
        verify(this.mockedServer, times(0)).sendToAllTCP(Mockito.any());

        // check if gamestate did not change
        Assertions.assertEquals(GameState.PLAYER_CAN_ROLL_DICE, this.serverData.getCurrentState());
    }

    /**
     * Connects a player using a mocked connection with given id.
     * @param connectionId the id the connection should return
     * @return the mocked connection
     */
    private Connection mockConnection(int connectionId) {
        Connection mockedConnection = mock(Connection.class);
        when(mockedConnection.getID()).thenReturn(connectionId); // mock a connection with id 7

        return mockedConnection;
    }
}
