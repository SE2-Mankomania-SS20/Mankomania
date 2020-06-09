package com.mankomania.game.server.game;

import com.badlogic.gdx.math.Vector3;
import com.esotericsoftware.kryonet.Server;
import com.mankomania.game.core.data.GameData;
import com.mankomania.game.core.data.horserace.HorseRaceData;
import com.mankomania.game.core.data.horserace.HorseRacePlayerInfo;
import com.mankomania.game.core.network.messages.clienttoserver.horserace.HorseRaceSelection;
import com.mankomania.game.core.network.messages.servertoclient.horserace.HorseRaceStart;
import com.mankomania.game.core.network.messages.servertoclient.horserace.HorseRaceUpdate;
import com.mankomania.game.core.network.messages.servertoclient.horserace.HorseRaceWinner;
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
    private List<Player> playersList;

    @BeforeEach
    public void before() {
        //mock server and serverData
        horseRaceData = new HorseRaceData();
        mockedServer = mock(Server.class);
        mockedServerData = mock(ServerData.class);
        mockedGameData = mock(GameData.class);

        playersList = new ArrayList<>();
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
    public void testStart() {
        handler.start();
        verify(mockedServer, times(1)).sendToAllTCP(new HorseRaceStart(0));
        assertEquals(0, horseRaceData.getCurrentPlayerIndex());
    }

    @Test
    public void testUpdate() {
        handler.start();
        int playerIndex = 0;
        int bet = 10000;
        handler.processUpdate(new HorseRaceSelection(new HorseRacePlayerInfo(playerIndex, 1, bet)));
        assertEquals(1, horseRaceData.getHorseRacePlayerInfo().size());
        assertEquals(1000000 - bet, playersList.get(playerIndex).getMoney());
        assertEquals(1, horseRaceData.getCurrentPlayerIndex());
    }

    @Test
    public void testEnd() {
        handler.start();
        int playerIndex = 0;
        int bet = 10000;
        handler.processUpdate(new HorseRaceSelection(new HorseRacePlayerInfo(playerIndex, 1, bet)));
        handler.processUpdate(new HorseRaceSelection(new HorseRacePlayerInfo(1, 2, bet * 2)));
        handler.processUpdate(new HorseRaceSelection(new HorseRacePlayerInfo(2, 0, bet * 3)));

        verify(mockedServer, times(1)).sendToAllTCP(new HorseRaceStart(0));
        verify(mockedServer, times(3)).sendToAllTCP(any(HorseRaceUpdate.class));
        verify(mockedServer, times(1)).sendToAllTCP(new HorseRaceWinner(horseRaceData.getWinner()));

        assertEquals(bet, horseRaceData.getHorseRacePlayerInfo().get(0).getBetAmount());
        assertEquals(bet * 2, horseRaceData.getHorseRacePlayerInfo().get(1).getBetAmount());
        assertEquals(bet * 3, horseRaceData.getHorseRacePlayerInfo().get(2).getBetAmount());
    }
}