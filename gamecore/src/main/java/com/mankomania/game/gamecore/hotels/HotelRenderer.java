package com.mankomania.game.gamecore.hotels;

import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.mankomania.game.core.data.GameData;

/**
 * This class is responsible for loading, positioning and drawing the 3d model for the hotels.
 * It is "extracted" out of MainGameScreen, to avoid bloating MainGameScreen and
 * to help keeping the code there clean and readable.
 */
public class HotelRenderer {
    private GameData gameData; // reference to gameData, to get information about the game (which player owns which hotel)

    public HotelRenderer() {

    }

    public void render(ModelBatch modelBatch) {

    }

    public void dispose() {

    }
}
