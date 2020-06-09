package com.mankomania.game.core.data.horserace;

import com.mankomania.game.core.network.messages.clienttoserver.horserace.HorseRaceSelection;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HorseRaceDataTest {
    private HorseRaceData horseRaceData;

    @BeforeEach
    public void before(){
        horseRaceData = new HorseRaceData();
    }

    @AfterEach
    public void after(){
        horseRaceData = null;
    }

    @Test
    public void testInit(){
        assertEquals(-1, horseRaceData.getWinner());
        assertEquals(-1, horseRaceData.getCurrentPlayerIndex());
        assertFalse( horseRaceData.isHasUpdate());
        assertEquals(0, horseRaceData.getHorseRacePlayerInfo().size());
    }

    @Test
    public void testReset(){
        horseRaceData.setCurrentPlayerIndex(1);
        horseRaceData.setHasUpdate(true);
        horseRaceData.update(new HorseRaceSelection(new HorseRacePlayerInfo(0,0,5000)));
        horseRaceData.reset();
        testInit();
    }

    @Test
    public void testUpdate(){
        horseRaceData.update(new HorseRaceSelection(new HorseRacePlayerInfo(0,0,5000)));
        assertEquals(horseRaceData.getHorseRacePlayerInfo().get(0), new HorseRacePlayerInfo(0, 0, 5000));
    }

    @Test
    public void testUpdateHorseRacePlayerInfo(){
        List<HorseRacePlayerInfo> horseRacePlayerInfo = new ArrayList<>();
        horseRacePlayerInfo.add(new HorseRacePlayerInfo(0,0,5000));
        horseRacePlayerInfo.add(new HorseRacePlayerInfo(1,2,10000));
        horseRacePlayerInfo.add(new HorseRacePlayerInfo(2,1,50000));
        horseRaceData.updateHorseRacePlayerInfo(horseRacePlayerInfo);

        List<HorseRacePlayerInfo> horseRacePlayerInfo2 = new ArrayList<>();
        horseRacePlayerInfo2.add(new HorseRacePlayerInfo(0,0,5000));
        horseRacePlayerInfo2.add(new HorseRacePlayerInfo(1,2,10000));
        horseRacePlayerInfo2.add(new HorseRacePlayerInfo(2,1,50000));
        assertEquals(horseRaceData.getHorseRacePlayerInfo(),horseRacePlayerInfo2);
    }
}