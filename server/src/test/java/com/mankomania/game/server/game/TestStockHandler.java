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
import org.junit.jupiter.api.Assertions;
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
    private Player player2;
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
        player2 = new Player(0, 1, v, 1);
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
    public void testRollCorrectWithoutAnyStocks() {

        GameData gameData = mock(GameData.class);
        player.setMoney(1000000);
        ArrayList<Player> list = new ArrayList<>();
        list.add(player);

        when(mockedServerData.getGameData()).thenReturn(gameData);
        when(gameData.getPlayers()).thenReturn(list);

        Connection con1 = getMockedConnection(11);
        when(mockedServerData.getCurrentState()).thenReturn(GameState.WAIT_STOCK_ROLL);
        when(mockedServerData.getCurrentPlayerTurnConnectionId()).thenReturn(11);
        HashMap<Integer, Integer> profit = new HashMap<>();
        resultMessage.setPlayerIndex(1);
        int i = 0;
        while (i < 6) {
            resultMessage.setStockResult(i);
            handler.gotStockResult(resultMessage);
            i++;
        }
        Assertions.assertEquals(1000000,player.getMoney());
        verify(mockedServer, times(6)).sendToAllTCP(any());
        verify(mockedServerData, atLeastOnce()).movePlayer(false, false);//should check for end of move state
    }

    @Test
    public void testRollCorrectWithAllStocks() {
        GameData gameData = mock(GameData.class);
        player.buyStock(Stock.BRUCHSTAHLAG, 1);
        player.buyStock(Stock.KURZSCHLUSSAG, 1);
        player.buyStock(Stock.TROCKENOEL, 1);
        player.setMoney(1000000);

        ArrayList<Player> list = new ArrayList<>();
        list.add(player);

        when(mockedServerData.getGameData()).thenReturn(gameData);
        when(gameData.getPlayers()).thenReturn(list);

        Connection con1 = getMockedConnection(11);
        when(mockedServerData.getCurrentState()).thenReturn(GameState.WAIT_STOCK_ROLL);
        when(mockedServerData.getCurrentPlayerTurnConnectionId()).thenReturn(11);
        HashMap<Integer, Integer> profit = new HashMap<>();
        resultMessage.setPlayerIndex(1);
        int i = 0;
        while (i < 6) {
            resultMessage.setStockResult(i);
            handler.gotStockResult(resultMessage);
            i++;
        }
        Assertions.assertEquals( player.getMoney(),1000000); // +30.000 danach -30.000 also es bleibt gleich :)
        verify(mockedServer, times(6)).sendToAllTCP(any());
        verify(mockedServerData, atLeastOnce()).movePlayer(false, false);//should check for end of move state
    }

    @Test
    public void testRollCorrectWithSomeStocks() {
        GameData gameData = mock(GameData.class);
        player.buyStock(Stock.BRUCHSTAHLAG, 1);
        player.buyStock(Stock.KURZSCHLUSSAG, 1);
        player.buyStock(Stock.TROCKENOEL, 1);
        player.setMoney(1000000);

        ArrayList<Player> list = new ArrayList<>();
        list.add(player);

        when(mockedServerData.getGameData()).thenReturn(gameData);
        when(gameData.getPlayers()).thenReturn(list);

        Connection con1 = getMockedConnection(11);
        when(mockedServerData.getCurrentState()).thenReturn(GameState.WAIT_STOCK_ROLL);
        when(mockedServerData.getCurrentPlayerTurnConnectionId()).thenReturn(11);
        HashMap<Integer, Integer> profit = new HashMap<>();
        resultMessage.setPlayerIndex(1);

        resultMessage.setStockResult(1); // get  +10.000
        resultMessage.setStockResult(2);//  lose -10.000
        resultMessage.setStockResult(3);//  get +10.000, also haben wir am ende 1.010.000...

        handler.gotStockResult(resultMessage);

        Assertions.assertEquals(player.getMoney(),1010000 ); // +30.000 danach -30.000 also es bleibt gleich :)
        verify(mockedServer, times(1)).sendToAllTCP(any());
        verify(mockedServerData, atLeastOnce()).movePlayer(false, false);//should check for end of move state
    }

    @Test
    public void testRollCorrectWithSomeStocksTwoPlayers() {
        GameData gameData = mock(GameData.class);
        player.buyStock(Stock.BRUCHSTAHLAG, 1);
        player.buyStock(Stock.KURZSCHLUSSAG, 1);
        player.buyStock(Stock.TROCKENOEL, 1);
        player.setMoney(1000000);

        player2.buyStock(Stock.BRUCHSTAHLAG, 3);
        player2.buyStock(Stock.KURZSCHLUSSAG, 3);
        player2.buyStock(Stock.TROCKENOEL, 3);
        player2.setMoney(1000000);

        ArrayList<Player> list = new ArrayList<>();
        list.add(player);
        list.add(player2);

        when(mockedServerData.getGameData()).thenReturn(gameData);
        when(gameData.getPlayers()).thenReturn(list);

        Connection con1 = getMockedConnection(11);
        when(mockedServerData.getCurrentState()).thenReturn(GameState.WAIT_STOCK_ROLL);
        when(mockedServerData.getCurrentPlayerTurnConnectionId()).thenReturn(11);
        HashMap<Integer, Integer> profit = new HashMap<>();
        resultMessage.setPlayerIndex(1);

        resultMessage.setStockResult(1); // get  +30.000
        handler.gotStockResult(resultMessage);

        Assertions.assertEquals(1010000,player.getMoney()); //player 1
        Assertions.assertEquals( 1030000,player2.getMoney());//player 2
        verify(mockedServer, times(1)).sendToAllTCP(any());
        verify(mockedServerData, atLeastOnce()).movePlayer(false, false);//should check for end of move state
    }

    private Connection getMockedConnection(int connectionId) {
        Connection mockedConnection = mock(Connection.class);
        when(mockedConnection.getID()).thenReturn(connectionId); // mock a connection with id connectionId

        return mockedConnection;
    }
}

