package com.mankomania.game.server.game;

/*
 Created by Fabian Oraze on 05.06.20
*/

import com.esotericsoftware.kryonet.Server;
import com.mankomania.game.core.data.GameData;
import com.mankomania.game.core.player.Player;
import com.mankomania.game.server.data.GameState;
import com.mankomania.game.server.data.ServerData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.mockito.Mockito.*;

public class TestCheatHandler {

    private GameData gameData;
    private ServerData serverData;
    private Server server;
    private CheatHandler cheatHandler;
    private ArrayList<Player> players;
    private Player playerOne;
    private Player playerTwo;

    @BeforeEach
    public void init() {
        this.gameData = mock(GameData.class);
        this.serverData = mock(ServerData.class);
        this.server = mock(Server.class);
        this.playerOne = mock(Player.class);
        this.playerTwo = mock(Player.class);
        this.players = new ArrayList<>();
        this.players.add(playerOne);
        this.players.add(playerTwo);
        this.cheatHandler = new CheatHandler(server, serverData);

    }

    @AfterEach
    public void tearDown() {
        this.serverData = null;
        this.server = null;
        this.cheatHandler = null;
        this.players = null;
        this.playerOne = null;
        this.playerTwo = null;
    }

    @Test
    public void testGotCheatMsgPlayerOnTurn() {
        when(serverData.getGameData()).thenReturn(gameData);
        when(gameData.getCurrentPlayerTurnIndex()).thenReturn(0);

        cheatHandler.gotCheatedMsg(0);
        verify(serverData, times(1)).getGameData();
        verify(gameData, times(1)).getCurrentPlayerTurnIndex();
        verify(serverData, times(1)).getCurrentState();
    }

    @Test
    public void testGotCheatMsgPlayerNotOnTurn() {
        when(serverData.getGameData()).thenReturn(gameData);
        when(gameData.getCurrentPlayerTurnIndex()).thenReturn(0);

        cheatHandler.gotCheatedMsg(1);
        verify(serverData, times(1)).getGameData();
        verify(gameData, times(1)).getCurrentPlayerTurnIndex();
        verify(serverData, times(4)).getCurrentState();
    }

    @Test
    public void testPlayerTriesToCheatWrongState() {
        when(serverData.getCurrentState()).thenReturn(GameState.WAIT_FOR_ALL_ROULETTE_BET);
        cheatHandler.playerTriesToCheat(0);

    }


}
