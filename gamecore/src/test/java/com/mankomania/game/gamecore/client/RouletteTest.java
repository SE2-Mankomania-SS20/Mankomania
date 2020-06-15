package com.mankomania.game.gamecore.client;

import com.mankomania.game.gamecore.screens.RouletteMiniGameScreen;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RouletteTest {
    private RouletteMiniGameScreen rouletteMiniGameScreen;

    @BeforeEach
    public void init() {
        rouletteMiniGameScreen = mock(RouletteMiniGameScreen.class);
    }

    @Test
    public void testEnteredRouletteNumber4() {
        when(rouletteMiniGameScreen.isNumeric("2")).thenReturn(true);
        Assertions.assertTrue( rouletteMiniGameScreen.isNumeric("2"));
        verify(rouletteMiniGameScreen, times(1)).isNumeric("2");

    }
    @Test
    public void testEnteredRouletteNumber1() {
        when(rouletteMiniGameScreen.isNumeric("27")).thenReturn(true);
        Assertions.assertTrue( rouletteMiniGameScreen.isNumeric("27"));
        verify(rouletteMiniGameScreen, times(1)).isNumeric("27");

    }
    @Test
    public void testEnteredRouletteNumber7() {
        when(rouletteMiniGameScreen.isNumeric("asdf")).thenReturn(false);
        Assertions.assertFalse( rouletteMiniGameScreen.isNumeric("asdf"));
        verify(rouletteMiniGameScreen, times(1)).isNumeric("asdf");

    }
}
