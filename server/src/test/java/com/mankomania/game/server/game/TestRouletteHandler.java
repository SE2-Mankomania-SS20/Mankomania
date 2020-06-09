package com.mankomania.game.server.game;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import com.mankomania.game.core.data.GameData;
import com.mankomania.game.core.network.messages.clienttoserver.roulette.RouletteStakeMessage;
import com.mankomania.game.core.network.messages.servertoclient.roulette.RouletteResultMessage;
import com.mankomania.game.core.network.messages.servertoclient.roulette.StartRouletteServer;
import com.mankomania.game.core.player.Player;
import com.mankomania.game.server.data.GameState;
import com.mankomania.game.server.data.ServerData;
import com.mankomania.game.server.minigames.RouletteHandler;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;


public class TestRouletteHandler {

    private ServerData mockedServerData;
    private Server mockedServer;
    private List <Player> players;
    private RouletteHandler handler;

    @BeforeEach
    public void init() {
        this.mockedServer = mock(Server.class);
        this.mockedServerData = mock(ServerData.class);
        this.players = new ArrayList<>();
        this.handler = new RouletteHandler(mockedServerData,mockedServer);
    }

    @AfterEach
    public void reset() {
        this.mockedServer = null;
        this.mockedServerData = null;
        this.handler = null;
    }

    @Test ()
    public void testStartOfGameCorrectState() {
        GameData gameData = mock(GameData.class);
        when(mockedServerData.getGameData()).thenReturn(gameData);
        when(gameData.getCurrentPlayerTurnIndex()).thenReturn(0);
        handler.startGame();
        verify(mockedServer, times(1)).sendToAllTCP(new StartRouletteServer(0));
        verify(mockedServerData, times(1)).setCurrentState(GameState.WAIT_FOR_ALL_ROULETTE_BET);
    }

    @Test
    public void testRouletteWrongState () {
        Connection con = getMockedConnection(10);
        when(mockedServerData.getCurrentState()).thenReturn(GameState.PLAYER_CAN_ROLL_DICE);
        handler.setInputPlayerBet(con.getID(),new RouletteStakeMessage());
        verify(mockedServer, never()).sendToAllTCP(any());
    }

    @Test ()
    public void testReceivedPlayersBets () {
        mockedServerData.setCurrentState(GameState.WAIT_FOR_ALL_ROULETTE_BET);
        when(mockedServerData.getCurrentState()).thenReturn(GameState.WAIT_FOR_ALL_ROULETTE_BET);

        GameData gameData = mock(GameData.class);
        when(mockedServerData.getGameData()).thenReturn(gameData);

        handler.setInputPlayerBet(0,new RouletteStakeMessage());
        handler.setInputPlayerBet(1,new RouletteStakeMessage());
        handler.setInputPlayerBet(2,new RouletteStakeMessage());
        handler.setInputPlayerBet(3,new RouletteStakeMessage());

        Assertions.assertEquals(4,handler.getInputPlayerBets().size());
    }

    @Test
    public void testGenerateRouletteMessage (){
        int playerId = 10;
        int bet = 100;
        String resultOfWheel = "42";
        boolean win = true;
        int amountWin = 10;

        RouletteResultMessage rouletteResultMessage = new RouletteResultMessage();
        rouletteResultMessage.setPlayerIndex(playerId);
        rouletteResultMessage.setBet(bet);
        rouletteResultMessage.setResultOfRouletteWheel(resultOfWheel);
        rouletteResultMessage.setWinOrLost(win);
        rouletteResultMessage.setAmountWin(amountWin);
        RouletteResultMessage rm = handler.generateRouletteMessage(playerId,bet,resultOfWheel,win,amountWin);

        Assertions.assertEquals(rouletteResultMessage.getBet(), rm.getBet());
        Assertions.assertEquals(rouletteResultMessage.getAmountWin(), rm.getAmountWin());
        Assertions.assertEquals(rouletteResultMessage.getResultOfRouletteWheel(), rm.getResultOfRouletteWheel());
    }

    @Test
    public void testGenerateResult () {
        mockedServerData.setCurrentState(GameState.WAIT_FOR_ALL_ROULETTE_BET);
        when(mockedServerData.getCurrentState()).thenReturn(GameState.WAIT_FOR_ALL_ROULETTE_BET);

        GameData gameData = mock(GameData.class);

        Player player1 = mock(Player.class);
        Player player2 = mock(Player.class);
        Player player3 = mock(Player.class);
        Player player4 = mock(Player.class);
        ArrayList<Player> list = new ArrayList<>();
        list.add(player1);
        list.add(player2);
        list.add(player3);
        list.add(player4);

        when(mockedServerData.getGameData()).thenReturn(gameData);
        when(gameData.getPlayers()).thenReturn(list);

        handler.setInputPlayerBet(0,new RouletteStakeMessage());
        handler.setInputPlayerBet(1,new RouletteStakeMessage());
        handler.setInputPlayerBet(2,new RouletteStakeMessage());
        handler.setInputPlayerBet(3,new RouletteStakeMessage());
        handler.generateResult();

        verify(mockedServer, times(2)).sendToAllTCP(any());
    }


    @Test
    public void testGenerateAmountWin() {
        Assertions.assertEquals(155000, handler.generateAmountWin(true, 5000));
        Assertions.assertEquals(120000, handler.generateAmountWin(true, 20000));
        Assertions.assertEquals(130000, handler.generateAmountWin(true, 50000));
        Assertions.assertEquals(-5000, handler.generateAmountWin(false, 5000));
        Assertions.assertEquals(-20000, handler.generateAmountWin(false, 20000));
        Assertions.assertEquals(-50000, handler.generateAmountWin(false, 50000));
    }

    @Test
    public void testResultRoulette() {
        //player win bet
        Assertions.assertTrue(handler.resultRoulette(37, 1));
        Assertions.assertTrue(handler.resultRoulette(38, 14));
        Assertions.assertTrue(handler.resultRoulette(39, 26));
        Assertions.assertTrue(handler.resultRoulette(40, 1)); //1 = red
        Assertions.assertTrue(handler.resultRoulette(41, 2)); //2 = black
        Assertions.assertTrue(handler.resultRoulette(5, 5));

        //player lose bet
        Assertions.assertFalse( handler.resultRoulette(37, 36));
        Assertions.assertFalse( handler.resultRoulette(38, 26));
        Assertions.assertFalse( handler.resultRoulette(39, 14));
        Assertions.assertFalse( handler.resultRoulette(40, 2)); //2 = black
        Assertions.assertFalse( handler.resultRoulette(41, 1)); //1 = red
        Assertions.assertFalse( handler.resultRoulette(5, 3));
    }

    @Test
    public void testFindColor () {
        Assertions.assertEquals("red", handler.findColor(1));
        Assertions.assertEquals("black", handler.findColor(2));
        Assertions.assertEquals("red", handler.findColor(36));
        Assertions.assertEquals("black", handler.findColor(35));
    }

    @Test
    public void testClearInputs () {
        ArrayList<RouletteStakeMessage> inputPlayerBets = new ArrayList<>();

        RouletteStakeMessage rouletteStakeMessage1 = new RouletteStakeMessage(0,5000,5);
        RouletteStakeMessage rouletteStakeMessage2 = new RouletteStakeMessage(1,20000,37);
        RouletteStakeMessage rouletteStakeMessage3 = new RouletteStakeMessage(2,20000,38);
        RouletteStakeMessage rouletteStakeMessage4 = new RouletteStakeMessage(3,50000,41);

        inputPlayerBets.add(rouletteStakeMessage1);
        inputPlayerBets.add(rouletteStakeMessage2);
        inputPlayerBets.add(rouletteStakeMessage3);
        inputPlayerBets.add(rouletteStakeMessage4);
        handler.setInputPlayerBetsList(inputPlayerBets);

        Assertions.assertEquals(4,handler.getInputPlayerBets().size());
        handler.clearInputs();
        Assertions.assertEquals(0,handler.getInputPlayerBets().size());

    }

    private Connection getMockedConnection(int connectionId) {
        Connection mockedConnection = mock(Connection.class);
        when(mockedConnection.getID()).thenReturn(connectionId); // mock a connection with id connectionId
        return mockedConnection;
    }


}
