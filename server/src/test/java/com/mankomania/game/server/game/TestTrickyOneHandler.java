package com.mankomania.game.server.game;

/*
 Created by Fabian Oraze on 26.05.20
 */

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import com.mankomania.game.core.network.messages.servertoclient.trickyone.CanRollDiceTrickyOne;
import com.mankomania.game.core.network.messages.servertoclient.trickyone.StartTrickyOne;
import com.mankomania.game.server.data.GameState;
import com.mankomania.game.server.data.ServerData;
import com.sun.tools.javac.util.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
