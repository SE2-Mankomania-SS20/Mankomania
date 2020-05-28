package com.mankomania.game.server.game;

/*
 Created by Fabian Oraze on 26.05.20
 */

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import com.mankomania.game.core.data.GameData;
import com.mankomania.game.core.network.messages.clienttoserver.trickyone.RollDiceTrickyOne;
import com.mankomania.game.core.network.messages.servertoclient.trickyone.CanRollDiceTrickyOne;
import com.mankomania.game.core.network.messages.servertoclient.trickyone.EndTrickyOne;
import com.mankomania.game.core.network.messages.servertoclient.trickyone.StartTrickyOne;
import com.mankomania.game.core.player.Player;
import com.mankomania.game.server.data.GameState;
import com.mankomania.game.server.data.ServerData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.mockito.Mockito.*;


public class TestTrickyOneHandler {

    private ServerData mockedServerData;
    private Server mockedServer;
    private TrickyOneHandler handler;

    @BeforeEach
    public void init() {
        //mock server and data
        this.mockedServer = mock(Server.class);
        this.mockedServerData = mock(ServerData.class);
        this.handler = new TrickyOneHandler(mockedServer, mockedServerData);
    }

    @AfterEach
    public void tearDown() {
        this.mockedServer = null;
        this.mockedServerData = null;
        this.handler = null;
    }

    @Test()
    public void testStartOfGameCorrectState() {
        handler.startGame(0);
        verify(mockedServer, times(1)).sendToAllTCP(new StartTrickyOne(0));
        verify(mockedServer, times(1)).sendToAllTCP(new CanRollDiceTrickyOne(0, 0, 0, 0, 0));
        verify(mockedServerData, times(1)).setCurrentState(GameState.WAIT_FOR_PLAYER_ROLL_OR_STOP);
    }

    @Test
    public void testRollDiceWrongPlayer() {
        Connection con1 = getMockedConnection(10);
        when(mockedServerData.getCurrentPlayerTurnConnectionId()).thenReturn(20);
        handler.rollDice(new RollDiceTrickyOne(0), con1.getID());
        verify(mockedServer, times(0)).sendToAllTCP(any());
    }

    @Test
    public void testRollDiceWrongState() {
        Connection con1 = getMockedConnection(10);
        when(mockedServerData.getCurrentState()).thenReturn(GameState.DO_ACTION);
        handler.rollDice(new RollDiceTrickyOne(0), con1.getID());
        verify(mockedServer, times(0)).sendToAllTCP(any());
    }

    @Test
    public void testRollDiceCorrect() {
        //need to mock gamedata and one player in order to verify if amount has been changed
        GameData gameData = mock(GameData.class);
        Player player = mock(Player.class);
        ArrayList<Player> list = new ArrayList<>();
        list.add(player);

        when(mockedServerData.getGameData()).thenReturn(gameData);
        when(gameData.getPlayers()).thenReturn(list);

        Connection con1 = getMockedConnection(11);
        when(mockedServerData.getCurrentState()).thenReturn(GameState.WAIT_FOR_PLAYER_ROLL_OR_STOP);
        int rollTimes = 24; //number has to be high enough to ensure at least one roll with a 1
        for (int i = 0; i < rollTimes; i++) {
            handler.rollDice(new RollDiceTrickyOne(0), con1.getID());
        }
        verify(mockedServer, times(rollTimes)).sendToAllTCP(any());
        verify(mockedServerData, atLeastOnce()).setCurrentState(GameState.WAIT_FOR_PLAYER_ROLL_OR_STOP);
        verify(mockedServerData, atLeastOnce()).setCurrentState(GameState.PLAYER_CAN_ROLL_DICE);//should check for end of move state
    }

    @Test
    public void testAfterContinueRollingCorrectValues() {
        int[] numbers = {3, 5};
        handler.continueRolling(new RollDiceTrickyOne(0), numbers);
        Assertions.assertEquals(8, handler.getRollAmount());
        Assertions.assertEquals(8 * 5000, handler.getPot());
    }

    @Test
    public void testContinueRollingMultiple() {
        int[] numbers = {4, 6};
        handler.continueRolling(new RollDiceTrickyOne(0), numbers);
        Assertions.assertEquals(10, handler.getRollAmount());
        handler.continueRolling(new RollDiceTrickyOne(0), numbers);
        Assertions.assertEquals(20, handler.getRollAmount());
    }

    @Test
    public void testContinueRollingCorrectCalls() {
        int[] numbers = {4, 6};
        handler.continueRolling(new RollDiceTrickyOne(0), numbers);
        verify(mockedServer, times(1)).sendToAllTCP(new CanRollDiceTrickyOne(0, 4, 6, 10 * 5000, 10));
        verify(mockedServerData, times(1)).setCurrentState(GameState.WAIT_FOR_PLAYER_ROLL_OR_STOP);
    }

    @Test
    public void testEndRollingCorrectInvocationsWinSingle() {
        //need to mock gamedata and one player in order to verify if amount has been changed
        GameData gameData = mock(GameData.class);
        Player player = mock(Player.class);
        ArrayList<Player> list = new ArrayList<>();
        list.add(player);

        when(mockedServerData.getGameData()).thenReturn(gameData);
        when(gameData.getPlayers()).thenReturn(list);

        handler.endRolling(new RollDiceTrickyOne(0), 10, 1);
        verify(gameData, times(1)).getPlayers();
        verify(player, times(1)).addMoney(100000);
        verify(mockedServer, times(1)).sendToAllTCP(new EndTrickyOne(0, 100000));
        verify(mockedServer, times(1)).sendToTCP(eq(10), any());
    }

    @Test
    public void testEndRollingCorrectInvocationsWinDouble() {
        //need to mock gamedata and one player in order to verify if amount has been changed
        GameData gameData = mock(GameData.class);
        Player player = mock(Player.class);
        ArrayList<Player> list = new ArrayList<>();
        list.add(player);

        when(mockedServerData.getGameData()).thenReturn(gameData);
        when(gameData.getPlayers()).thenReturn(list);

        handler.endRolling(new RollDiceTrickyOne(0), 10, 2);
        verify(gameData, times(1)).getPlayers();
        verify(player, times(1)).addMoney(300000);
        verify(mockedServer, times(1)).sendToAllTCP(new EndTrickyOne(0, 300000));
        verify(mockedServer, times(1)).sendToTCP(eq(10), any());
    }


    /**
     * Connects a player using a mocked connection with given id.
     *
     * @param connectionId the id the connection should return
     * @return the mocked connection
     */
    private Connection getMockedConnection(int connectionId) {
        Connection mockedConnection = mock(Connection.class);
        when(mockedConnection.getID()).thenReturn(connectionId); // mock a connection with id connectionId

        return mockedConnection;
    }


}
