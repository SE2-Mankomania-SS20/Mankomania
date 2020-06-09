package com.mankomania.game.core.data.horserace;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HorseRacePlayerInfoTest {
    private HorseRacePlayerInfo horseRacePlayerInfo;
    private HorseRacePlayerInfo horseRacePlayerInfo2;

    @BeforeEach
    public void before() {
        horseRacePlayerInfo = new HorseRacePlayerInfo(0,0,5000);
        horseRacePlayerInfo2 = new HorseRacePlayerInfo(1,1,10000);
    }

    @AfterEach
    public void after() {
        horseRacePlayerInfo = null;
        horseRacePlayerInfo2 = null;
    }

    @Test
    public void test1(){
        assertEquals(0,horseRacePlayerInfo.getPlayerIndex());
        assertEquals(0,horseRacePlayerInfo.getHorseIndex());
        assertEquals(5000,horseRacePlayerInfo.getBetAmount());
    }

    @Test
    public void test2(){
        assertEquals(1,horseRacePlayerInfo2.getPlayerIndex());
        assertEquals(1,horseRacePlayerInfo2.getHorseIndex());
        assertEquals(10000,horseRacePlayerInfo2.getBetAmount());
    }
}