package com.mankomania.game.server.game;

/*
 Created by Fabian Oraze on 05.06.20
*/

import com.esotericsoftware.kryonet.Server;
import com.mankomania.game.core.data.GameData;
import com.mankomania.game.core.network.messages.servertoclient.Notification;
import com.mankomania.game.core.player.Player;
import com.mankomania.game.server.data.GameState;
import com.mankomania.game.server.data.ServerData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
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

        startTurn();

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

    private void startTurn() {
        when(serverData.getCurrentState()).thenReturn(GameState.WAIT_FOR_DICE_RESULT);
        when(serverData.getGameData()).thenReturn(gameData);
        when(gameData.getPlayers()).thenReturn(players);
        when(gameData.getCurrentPlayer()).thenReturn(playerOne);
        when(gameData.getCurrentPlayerTurnIndex()).thenReturn(0);
    }

    @Test
    public void testGotCheatMsgPlayerOnTurn() {
        //check if certain methods are invoked the correct amount when player is on turn
        cheatHandler.gotCheatedMsg(0);
        verify(serverData, times(8)).getGameData();
        verify(gameData, times(1)).getCurrentPlayerTurnIndex();
        verify(serverData, times(1)).getCurrentState();
    }

    @Test
    public void testGotCheatMsgPlayerNotOnTurn() {
        //check if certain methods are invoked the correct amount when player is not on turn
        cheatHandler.gotCheatedMsg(1);
        verify(serverData, times(3)).getGameData();
        verify(gameData, times(1)).getCurrentPlayerTurnIndex();
        verify(serverData, times(1)).getCurrentState();
    }

    @Test
    public void testPlayerTriesToCheatWrongState() {
        when(serverData.getCurrentState()).thenReturn(GameState.WAIT_FOR_ALL_ROULETTE_BET);
        cheatHandler.playerTriesToCheat(0);
        //check for some methods that should never be invoked when in wrong state
        verify(serverData, times(1)).getCurrentState();
        verify(gameData, never()).getCurrentPlayer();
        verify(server, never()).sendToTCP(anyInt(), any(Notification.class));
    }


}
