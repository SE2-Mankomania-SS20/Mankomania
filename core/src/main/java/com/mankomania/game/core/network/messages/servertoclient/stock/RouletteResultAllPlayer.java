package com.mankomania.game.core.network.messages.servertoclient.minigames;

import java.util.ArrayList;

public class RouletteResultAllPlayer {
    private ArrayList <RouletteResultMessage> results;

    public RouletteResultAllPlayer(ArrayList<RouletteResultMessage> results) {
        this.results = results;
    }

    public RouletteResultAllPlayer() {

    }

    public ArrayList<RouletteResultMessage> getResults() {
        return results;
    }

    public void setResults(ArrayList<RouletteResultMessage> results) {
        this.results = results;
    }
}
