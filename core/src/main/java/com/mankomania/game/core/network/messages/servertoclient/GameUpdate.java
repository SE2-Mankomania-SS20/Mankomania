package com.mankomania.game.core.network.messages.servertoclient;

import com.mankomania.game.core.data.GameData;
import com.mankomania.game.core.player.Hotel;
import com.mankomania.game.core.player.Player;

import java.util.List;
import java.util.Map;

/**
 * Network message that contains all the relevant gamedata to be synced to all clients
 */
public class GameUpdate {
    private int lotteryAmount;
    private List<Player> players;
    private Map<Hotel, Integer> hotels;
    private int currentPlayerTurn;

    public GameUpdate() {
        // empty for kryo
    }

    public GameUpdate(int lotteryAmount, List<Player> players, Map<Hotel, Integer> hotels, int currentPlayerTurn) {
        this.lotteryAmount = lotteryAmount;
        this.players = players;
        this.hotels = hotels;
        this.currentPlayerTurn = currentPlayerTurn;
    }

    public GameUpdate(GameData gameData){
        this.currentPlayerTurn = gameData.getCurrentPlayerTurnIndex();
        this.players = gameData.getPlayers();
        this.lotteryAmount = gameData.getLotteryAmount();
    }

    public int getLotteryAmount() {
        return lotteryAmount;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Map<Hotel, Integer> getHotels() {
        return hotels;
    }

    public int getCurrentPlayerTurn() {
        return currentPlayerTurn;
    }
}
