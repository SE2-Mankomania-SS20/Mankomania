package com.mankomania.game.core.network.messages.clienttoserver.horserace;

import com.mankomania.game.core.data.horserace.HorseRacePlayerInfo;

public class HorseRaceSelection {
    HorseRacePlayerInfo horseRacePlayerInfo;

    public HorseRaceSelection() {
    }

    public HorseRaceSelection(HorseRacePlayerInfo horseRacePlayerInfo) {
        this.horseRacePlayerInfo = horseRacePlayerInfo;
    }

    public HorseRacePlayerInfo getHorseRacePlayerInfo() {
        return horseRacePlayerInfo;
    }
}
