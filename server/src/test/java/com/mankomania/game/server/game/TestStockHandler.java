package com.mankomania.game.server.game;

import com.badlogic.gdx.math.Vector3;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import com.mankomania.game.core.data.GameData;
import com.mankomania.game.core.network.messages.clienttoserver.stock.StockResultMessage;
import com.mankomania.game.core.network.messages.servertoclient.stock.EndStockMessage;
import com.mankomania.game.core.network.messages.servertoclient.stock.StartStockMessage;
import com.mankomania.game.core.player.Player;
import com.mankomania.game.core.player.Stock;
import com.mankomania.game.server.data.GameState;
import com.mankomania.game.server.data.ServerData;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TestStockHandler {
    private Player player;
    private ServerData mockedServerData;
    private Server mockedServer;
    private StockHandler handler;
    private StockResultMessage resultMessage;
    private EndStockMessage end;

    @BeforeEach
    public void init() {

        this.mockedServer = mock(Server.class);
        this.mockedServerData = mock(ServerData.class);
        this.handler = new StockHandler(mockedServer, mockedServerData);
        Vector3 v = new Vector3();
        player = new Player(0, 1, v, 1);
        player.buyStock(Stock.BRUCHSTAHLAG, 5);
        resultMessage = new StockResultMessage();
    }

    @AfterEach
    public void after() {
        this.mockedServer = null;
        this.mockedServerData = null;
        this.handler = null;
    }

    @Test()
    public void testStartOfGameCorrectState() {
        GameData gameData = mock(GameData.class);
        when(mockedServerData.getGameData()).thenReturn(gameData);
        when(gameData.getCurrentPlayerTurnIndex()).thenReturn(0);
        handler.startGame();
        verify(mockedServer, times(1)).sendToAllTCP(new StartStockMessage(0));
        verify(mockedServerData, times(1)).setCurrentState(GameState.WAIT_STOCK_ROLL);
    }

    @Test
    public void testRollStockCorrect() {

        GameData gameData = mock(GameData.class);
        Player player = mock(Player.class);
        ArrayList<Player> list = new ArrayList<>();
        list.add(player);

        when(mockedServerData.getGameData()).thenReturn(gameData);
        when(gameData.getPlayers()).thenReturn(list);

        Connection con1 = getMockedConnection(11);
        when(mockedServerData.getCurrentState()).thenReturn(GameState.WAIT_STOCK_ROLL);
        when(mockedServerData.getCurrentPlayerTurnConnectionId()).thenReturn(11);
        // this.resultMessage.setPlayerId(mockedServerData.getCurrentPlayerTurnConnectionId());
        HashMap<Integer, Integer> profit = new HashMap<>();
        resultMessage.setPlayerIndex(1);
        int i=0;
        while(i<6) {
            resultMessage.setStockResult(i);
            handler.gotStockResult(resultMessage);
            i++;
        }
        verify(mockedServer, times(6)).sendToAllTCP(any());
        //verify(mockedServerData, atLeastOnce()).setCurrentState(GameState.WAIT_STOCK_ROLL);
        verify(mockedServerData, atLeastOnce()).movePlayer(false, false);//should check for end of move state
    }

    private Connection getMockedConnection(int connectionId) {
        Connection mockedConnection = mock(Connection.class);
        when(mockedConnection.getID()).thenReturn(connectionId); // mock a connection with id connectionId

        return mockedConnection;
    }
}

