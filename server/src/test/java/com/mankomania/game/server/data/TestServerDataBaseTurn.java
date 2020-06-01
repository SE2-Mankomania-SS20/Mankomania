package com.mankomania.game.server.data;

import com.badlogic.gdx.utils.IntArray;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import com.mankomania.game.core.network.messages.clienttoserver.baseturn.DiceResultMessage;
import com.mankomania.game.core.network.messages.clienttoserver.baseturn.IntersectionSelection;
import com.mankomania.game.core.network.messages.servertoclient.baseturn.PlayerCanRollDiceMessage;
import com.mankomania.game.core.network.messages.servertoclient.baseturn.PlayerMoves;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

/**
 * This class is used for testing the base turn functionality.
 * I.e.: start game, rolling the dice, moving player, taking intersections, end turn
 */
public class TestServerDataBaseTurn {
    private ServerData serverData;

    private Server mockedServer;

    @BeforeEach
    public void initEach() {
        // mock server before each, so the verify count will get reset after each test, which would not be the case if it is static
        this.mockedServer = mock(Server.class);

        // gamedata gets loaded in the ctor of server data in this new version
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
        Assertions.assertEquals(1, this.serverData.getGameData().getPlayers().size(), "the player list should have exactly one entry");
        Assertions.assertEquals(7, this.serverData.getGameData().getPlayers().get(0).getConnectionId(), "the connection id of the first player in the list should be 7");
        Assertions.assertEquals(0, this.serverData.getGameData().getPlayers().get(0).getPlayerIndex(), "the player index of the first player in the list should be 0");
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

        // check if the length matches and test if connection id and index are right
        Assertions.assertEquals(4, this.serverData.getGameData().getPlayers().size());
        Assertions.assertEquals(42, this.serverData.getGameData().getPlayers().get(1).getConnectionId());
        Assertions.assertEquals(1, this.serverData.getGameData().getPlayers().get(1).getPlayerIndex());
    }

    //
    @Test
    public void testDisconnectPlayer() {
        // the connection ids used for testing
        int firstConnectionId = 9;
        int secondConnectionId = 7;
        this.serverData.connectPlayer(this.mockConnection(firstConnectionId));
        this.serverData.connectPlayer(this.mockConnection(secondConnectionId));

        Assertions.assertEquals(2, this.serverData.getGameData().getPlayers().size());

        // disconnect the first player
        this.serverData.disconnectPlayer(secondConnectionId);

        // check if we now only have one player left
        Assertions.assertEquals(1, this.serverData.getGameData().getPlayers().size());

        this.serverData.disconnectPlayer(firstConnectionId);

        // check if we now have none player left
        Assertions.assertEquals(0, this.serverData.getGameData().getPlayers().size());
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

        this.serverData.playerReady(connection1.getID());

        // even though we now have two player, not all of them are ready, therefor the game should not start
        Assertions.assertFalse(this.serverData.checkForStart());
    }

    @Test
    public void testCheckForStart_playerReady() {
        Connection connection1 = this.mockConnection(3);
        Connection connection2 = this.mockConnection(5);
        this.serverData.connectPlayer(connection1);
        this.serverData.connectPlayer(connection2);

        // ready up players
        this.serverData.playerReady(connection1.getID());
        this.serverData.playerReady(connection2.getID());

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
        PlayerCanRollDiceMessage expectedMessage = new PlayerCanRollDiceMessage(42);

        // check if server.sendToAllTCP got called with the exact right message
         verify(this.mockedServer, times(1)).sendToAllTCP(new PlayerCanRollDiceMessage(0));

        // check if gamestate is okay
        Assertions.assertEquals(GameState.WAIT_FOR_DICE_RESULT, this.serverData.getCurrentState());
    }

    @Test
    public void testStartGameLoopAndTryingToConnectAfterwards() {
        // this tests if the game gets closed after starting a game
        this.serverData.connectPlayer(this.mockConnection(42));
        // ready up the player
        this.serverData.playerReady(42);
        // start game and check if the game actually started
        boolean isGameStarted = this.serverData.checkForStart();
        Assertions.assertTrue(isGameStarted);

        // check if another player can connect now
        this.serverData.connectPlayer(this.mockConnection(21));
        // there should still be only one player connected
        Assertions.assertEquals(1, this.serverData.getGameData().getPlayers().size());
    }

    @Test
    public void testSendPlayerCanRollDice_wrongState() {
        // set gamestate to something that shpould not work
        this.serverData.setCurrentState(GameState.WAIT_INTERSECTION_SELECTION);
        // call method while in wrong state
        this.serverData.sendPlayerCanRollDice();

        // check if none of the send methods of the server were called
        verify(this.mockedServer, times(0)).sendToAllTCP(Mockito.any());

        // check if gamestate did not change
        Assertions.assertEquals(GameState.WAIT_INTERSECTION_SELECTION, this.serverData.getCurrentState());
    }

    @Test
    public void testSendPlayerCanRollDice_rightState() {
        // add a player
        this.serverData.connectPlayer(this.mockConnection(2));
        this.serverData.connectPlayer(this.mockConnection(10));
        // set gamestate for the function to work
        this.serverData.setCurrentState(GameState.PLAYER_CAN_ROLL_DICE);
        // call method
        this.serverData.sendPlayerCanRollDice();

        // check if send methods got called with the right parameters
        verify(this.mockedServer, times(1)).sendToAllTCP(new PlayerCanRollDiceMessage(0));

        // check if gamestate did change accordingly
        Assertions.assertEquals(GameState.WAIT_FOR_DICE_RESULT, this.serverData.getCurrentState());
    }

    @Test
    public void testGotDiceResult_wrongState() {
        // set gamestate for the function not to work
        this.serverData.setCurrentState(GameState.PLAYER_CAN_ROLL_DICE);
        // call method
        this.serverData.gotDiceRollResult(new DiceResultMessage(), 42);

        // verify that state has not changed and no send call has been made
        verify(this.mockedServer, times(0)).sendToAllTCP(Mockito.any());

        // check if gamestate did not change
        Assertions.assertEquals(GameState.PLAYER_CAN_ROLL_DICE, this.serverData.getCurrentState());
    }

    @Test
    public void testGotDiceResult_differingConnectionId() {
        // add a player that is currently on turn
        this.serverData.connectPlayer(this.mockConnection(7));
        // set gamestate for the function to work
        this.serverData.setCurrentState(GameState.WAIT_FOR_DICE_RESULT);
        // call method, using a differing connection id
        this.serverData.gotDiceRollResult(new DiceResultMessage(1, 12), 8);

        // verify that state has not changed and no send call has been made
        verify(this.mockedServer, times(0)).sendToAllTCP(Mockito.any());

        // check if gamestate did not change
        Assertions.assertEquals(GameState.WAIT_FOR_DICE_RESULT, this.serverData.getCurrentState());
    }

    // TODO: implement another test for getDiceResult where sendMovePlayerMessage gets called

    @Test
    public void testSendMovePlayerMessage_checkingIntersection() {
        // add a player that will get moved
        this.serverData.connectPlayer(this.mockConnection(12));
        // the first player starts on field 78 with intersection immediately on the next  field
        // call sendMovePlayer and let the player move one field
        this.serverData.checkForStart();
        this.serverData.startGameLoop();
        Assertions.assertEquals(GameState.WAIT_FOR_DICE_RESULT, this.serverData.getCurrentState());
        this.serverData.gotDiceRollResult(new DiceResultMessage(0,6),12);
        verify(this.mockedServer, times(1)).sendToAllTCP(any(PlayerCanRollDiceMessage.class));

        // check if we went into the right state, waiting for an intersection selection of the client
        Assertions.assertEquals(GameState.WAIT_INTERSECTION_SELECTION, this.serverData.getCurrentState());

        // since there is an intersection, we can check here if the right message was getting sent
        IntArray moves = new IntArray();
        moves.add(7);
        verify(this.mockedServer, times(1)).sendToAllTCP(new PlayerMoves(moves));

        // check if the player halted on the field before the intersection
        Assertions.assertEquals(7, this.serverData.getGameData().getPlayerByConnectionId(12).getCurrentFieldIndex());
    }

    // TODO: implement tests for field actions and lottery

    @Test
    public void testGotIntersectionSelectionMessage_wrongState() {
        // set game state to something that should not work
        this.serverData.setCurrentState(GameState.WAIT_FOR_DICE_RESULT);
        // call method while in wrong state
        this.serverData.gotIntersectionSelectionMessage(new IntersectionSelection(), 123);

        // check if none of the send methods of the server were called
        verify(this.mockedServer, times(0)).sendToAllTCP(Mockito.any());

        // check if game state did not change
        Assertions.assertEquals(GameState.WAIT_FOR_DICE_RESULT, this.serverData.getCurrentState());
    }

    @Test
    public void testGotIntersectionSelectionMessage_wrongConnectionId() {
        // add a player that is currently on turn
        this.serverData.connectPlayer(this.mockConnection(3));
        // set game state so it works
        this.serverData.setCurrentState(GameState.WAIT_INTERSECTION_SELECTION);
        // create a message that the function should handle, but using a different connection id
        IntersectionSelection intersectionSelection = new IntersectionSelection();
        intersectionSelection.setPlayerIndex(1);
        intersectionSelection.setFieldIndex(13);
        this.serverData.gotIntersectionSelectionMessage(intersectionSelection, 34);

        // check if none of the send methods of the server were called
        verify(this.mockedServer, times(0)).sendToAllTCP(Mockito.any());

        // check if game state did not change
        Assertions.assertEquals(GameState.WAIT_INTERSECTION_SELECTION, this.serverData.getCurrentState());
    }

    // TODO: implement tests for gotIntersectionSelectionMessage as soon as special field actions are implemented


    /**
     * Connects a player using a mocked connection with given id.
     *
     * @param connectionId the id the connection should return
     * @return the mocked connection
     */
    private Connection mockConnection(int connectionId) {
        Connection mockedConnection = mock(Connection.class);
        when(mockedConnection.getID()).thenReturn(connectionId); // mock a connection with id connectionId

        return mockedConnection;
    }
}
