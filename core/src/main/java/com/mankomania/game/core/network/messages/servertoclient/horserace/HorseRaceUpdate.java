package com.mankomania.game.core.network.messages.servertoclient.horserace;

import com.mankomania.game.core.data.horserace.HorseRaceData;
import com.mankomania.game.core.data.horserace.HorseRacePlayerInfo;

import java.util.List;


public class HorseRaceUpdate {

    private int currentPlayerIndex;

    private List<HorseRacePlayerInfo> horseRacePlayerInfos;

    public HorseRaceUpdate() {
        // empty for kryonet
    }

    public HorseRaceUpdate(HorseRaceData horseRaceData) {
        horseRacePlayerInfos = horseRaceData.getHorseRacePlayerInfo();
        currentPlayerIndex = horseRaceData.getCurrentPlayerIndex();
    }

    public List<HorseRacePlayerInfo> getHorseRacePlayerInfos() {
        return horseRacePlayerInfos;
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }
}
