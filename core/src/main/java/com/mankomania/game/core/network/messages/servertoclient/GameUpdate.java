package com.mankomania.game.core.network.messages.servertoclient;

import com.mankomania.game.core.player.Hotel;
import com.mankomania.game.core.player.Player;

import java.util.HashMap;
import java.util.List;

/**
 * Network message that contains all the relevant gamedata to be synced to all clients
 */
public class GameUpdate {
    private int lotteryAmount;
    private List<Player> players;
    private HashMap<Hotel, Integer> hotels;
    private int currentPlayerTurn;

    public GameUpdate() {
        // empty for kryo
    }

    public GameUpdate(int lotteryAmount, List<Player> players, HashMap<Hotel, Integer> hotels, int currentPlayerTurn) {
        this.lotteryAmount = lotteryAmount;
        this.players = players;
        this.hotels = hotels;
        this.currentPlayerTurn = currentPlayerTurn;
    }

    public int getLotteryAmount() {
        return lotteryAmount;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public HashMap<Hotel, Integer> getHotels() {
        return hotels;
    }

    public int getCurrentPlayerTurn() {
        return currentPlayerTurn;
    }
}
