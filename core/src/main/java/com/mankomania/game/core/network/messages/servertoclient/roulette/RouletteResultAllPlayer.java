package com.mankomania.game.core.network.messages.servertoclient.roulette;

import java.util.List;

public class RouletteResultAllPlayer {
    private List<RouletteResultMessage> results;

    public RouletteResultAllPlayer(List<RouletteResultMessage> results) {
        this.results = results;
    }

    public RouletteResultAllPlayer() {

    }

    public List<RouletteResultMessage> getResults() {
        return results;
    }

    public void setResults(List<RouletteResultMessage> results) {
        this.results = results;
    }
}
