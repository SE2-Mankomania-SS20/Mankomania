package com.mankomania.game.server.game;

import com.badlogic.gdx.math.Vector3;
import com.esotericsoftware.kryonet.Server;
import com.mankomania.game.core.data.GameData;
import com.mankomania.game.core.data.horserace.HorseRaceData;
import com.mankomania.game.core.network.messages.servertoclient.horserace.HorseRaceStart;
import com.mankomania.game.core.player.Player;
import com.mankomania.game.server.data.ServerData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HorseRaceHandlerTest {
    private Server mockedServer;
    private ServerData mockedServerData;
    private GameData mockedGameData;

    private HorseRaceHandler handler;
    private HorseRaceData horseRaceData;

    @BeforeEach
    public void before() {
        //mock server and serverData
        horseRaceData = new HorseRaceData();
        mockedServer = mock(Server.class);
        mockedServerData = mock(ServerData.class);
        mockedGameData = mock(GameData.class);

        List<Player> playersList = new ArrayList<>();
        playersList.add(new Player(78, 1, new Vector3(), 0));
        playersList.add(new Player(79, 2, new Vector3(), 1));
        playersList.add(new Player(80, 3, new Vector3(), 2));
        when(mockedGameData.getPlayers()).thenReturn(playersList);
        when(mockedGameData.getCurrentPlayerTurnIndex()).thenReturn(0);

        when(mockedServerData.getGameData()).thenReturn(mockedGameData);
        when(mockedGameData.getHorseRaceData()).thenReturn(horseRaceData);
        when(mockedGameData.getCurrentPlayer()).thenReturn(playersList.get(0));
        handler = new HorseRaceHandler(mockedServer, mockedServerData);
    }

    @AfterEach
    public void after() {
        mockedServer = null;
        mockedServerData = null;
        handler = null;
        horseRaceData = null;
    }

    @Test
    public void test() {
        handler.start();
        verify(mockedServer, times(1)).sendToAllTCP(new HorseRaceStart(0));
        assertEquals(0, horseRaceData.getCurrentPlayerIndex());
    }
}