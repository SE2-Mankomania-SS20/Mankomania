package com.mankomania.game.core.data.horserace;

import com.mankomania.game.core.network.messages.clienttoserver.horserace.HorseRaceSelection;

import java.util.ArrayList;
import java.util.List;

public class HorseRaceData {
    private final List<HorseRacePlayerInfo> horseRacePlayerInfo;

    private int currentPlayerIndex;

    private boolean hasUpdate;

    private int winner;

    public HorseRaceData() {
        horseRacePlayerInfo = new ArrayList<>();
        hasUpdate = false;
        winner = -1;
        currentPlayerIndex = -1;
    }

    public int getWinner() {
        return winner;
    }

    public void setWinner(int winner) {
        this.winner = winner;
    }

    public boolean isHasUpdate() {
        return hasUpdate;
    }

    public void setHasUpdate(boolean hasUpdate) {
        this.hasUpdate = hasUpdate;
    }

    public void reset() {
        horseRacePlayerInfo.clear();
        winner = -1;
        hasUpdate = false;
        currentPlayerIndex = -1;
    }

    public List<HorseRacePlayerInfo> getHorseRacePlayerInfo() {
        return horseRacePlayerInfo;
    }

    public void updateHorseRacePlayerInfo(List<HorseRacePlayerInfo> horseRacePlayerInfo){
        this.horseRacePlayerInfo.clear();
        this.horseRacePlayerInfo.addAll(horseRacePlayerInfo);
    }

    public void setCurrentPlayerIndex(int currentPlayerIndex) {
        this.currentPlayerIndex = currentPlayerIndex;
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public void update(HorseRaceSelection hrs) {
        horseRacePlayerInfo.add(hrs.getHorseRacePlayerInfo());
    }
}
