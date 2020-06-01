package com.mankomania.game.core.network.messages.servertoclient.roulette;

import java.util.Map;

public class EndRouletteResultMessage {

    private Map<Integer,Integer> money;

    public EndRouletteResultMessage(Map<Integer, Integer> money) {
        this.money = money;
    }

    public EndRouletteResultMessage(){}

    public Map<Integer, Integer> getMoney() {
        return money;
    }

    public void setMoney(Map<Integer, Integer> money) {
        this.money = money;
    }
}
