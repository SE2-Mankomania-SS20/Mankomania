package com.mankomania.game.server.game;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import com.mankomania.game.core.network.messages.servertoclient.slots.SlotResultMessage;
import com.mankomania.game.core.network.messages.servertoclient.slots.StartSlotsMessage;
import com.mankomania.game.server.data.GameState;
import com.mankomania.game.server.data.ServerData;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TestSlotHandler {
    private Server mockedServer;
    private ServerData serverData;

    @BeforeEach
    public void initEach() {
        // mock server before each, so the count of calls to "verify" will get reset after each test, which would not be the case if server would be only mocked once
        this.mockedServer = mock(Server.class);

        // we need serverdata in this case unmocked since it contains the reference to the SlotHanlder
        this.serverData = new ServerData(this.mockedServer);
    }

    @Test
    public void testStartSlotsGame() {
        SlotHandler slotHandler = this.serverData.getSlotHandler();
        slotHandler.startSlotsGame();

        // add a player
        this.serverData.connectPlayer(this.mockConnection(7));

        // verify send call, using playerIndex (which is 0 for the first player)
        verify(this.mockedServer, times(1)).sendToAllTCP(new StartSlotsMessage(0));

        // verify state change
        Assertions.assertEquals(GameState.WAIT_SLOTS_INPUT, this.serverData.getCurrentState());
    }

    @Test
    public void testGotSpinRollsMessage_wrongState() {
        // add two players
        this.serverData.connectPlayer(this.mockConnection(42));
        this.serverData.connectPlayer(this.mockConnection(1337));

        // set the state so it is wrong
        this.serverData.setCurrentState(GameState.WAIT_FOR_DICE_RESULT);

        // call the gotSpinRollMessage(connectionId) function
        SlotHandler slotHandler = this.serverData.getSlotHandler();
        slotHandler.gotSpinRollsMessage(123);

        // no send call shouldve been made, since we are in the wrong state
        verify(this.mockedServer, times(0)).sendToAllTCP(Mockito.any());

        // check if gamestate did not change
        Assertions.assertEquals(GameState.WAIT_FOR_DICE_RESULT, this.serverData.getCurrentState());
    }

    @Test
    public void testGotSpinRollsMessage_wrongConnectionId() {
        // add two players
        this.serverData.connectPlayer(this.mockConnection(42));
        this.serverData.connectPlayer(this.mockConnection(1337));

        // set the state so it is right to proceed further
        this.serverData.setCurrentState(GameState.WAIT_SLOTS_INPUT);

        // call the gotSpinRollMessage(connectionId) function with a wrong connection id
        SlotHandler slotHandler = this.serverData.getSlotHandler();
        slotHandler.gotSpinRollsMessage(123);

        // no send call shouldve been made, since we are in the wrong state
        verify(this.mockedServer, times(0)).sendToAllTCP(Mockito.any());

        // check if gamestate did not change
        Assertions.assertEquals(GameState.WAIT_SLOTS_INPUT, this.serverData.getCurrentState());
    }

    @Test
    public void testGotSpinRollsMessage_allOkay() {
        // add two players
        this.serverData.connectPlayer(this.mockConnection(42));
        this.serverData.connectPlayer(this.mockConnection(1337));

        // set the state so it is right to proceed further
        this.serverData.setCurrentState(GameState.WAIT_SLOTS_INPUT);

        // call the gotSpinRollMessage(connectionId) function with a right connection id
        SlotHandler slotHandler = this.serverData.getSlotHandler();
        slotHandler.gotSpinRollsMessage(42);

        // a send should have been made. it can only be tested if the parameter to the send call was of type SlotResultMessage.
        // the exact properties (like randomRollValues) are randomised.
        verify(this.mockedServer, times(1)).sendToAllTCP(Mockito.any(SlotResultMessage.class));
    }


    /**
     * Connects a player using a mocked connection with given id.
     *
     * @param connectionId the id the connection should return if getId() is called
     * @return the connection that was mocked
     */
    private Connection mockConnection(int connectionId) {
        Connection mockedConnection = mock(Connection.class);
        when(mockedConnection.getID()).thenReturn(connectionId); // mock a connection with id connectionId

        return mockedConnection;
    }
}
