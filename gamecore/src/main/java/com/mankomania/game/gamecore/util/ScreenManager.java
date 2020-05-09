package com.mankomania.game.gamecore.util;

import com.mankomania.game.gamecore.MankomaniaGame;
import com.mankomania.game.gamecore.screens.AbstractScreen;

/*********************************
 Created by Fabian Oraze on 27.04.20
 *********************************/

public class ScreenManager {

    /**
     * Manager Class for all actions related with screens
     * Uses singleton pattern
     * <p>
     * For switching screens use:
     * ScreenManager.getInstance().switchScreen()
     */

    private static ScreenManager instance;
    private MankomaniaGame game;

    private ScreenManager() {
        super();
    }

    /**
     * method to get instance from Singleton pattern
     *
     * @return instance of ScreenManager
     */
    public static ScreenManager getInstance() {
        if (instance == null) {
            instance = new ScreenManager();
        }
        return instance;
    }

    public void initialize(MankomaniaGame game) {
        this.game = game;
    }

    public MankomaniaGame getGame() {
        return game;
    }

    /**
     * @param screen enum which represents a screen
     * @param params     any parameter that might be necessary, look Screen
     */
    public void switchScreen(Screen screen, Object... params) {

        //Get current Screen to dispose later
        com.badlogic.gdx.Screen currentScreen = game.getScreen();

        //Set new Screen
        AbstractScreen newScreen = screen.getScreen(params);
        game.setScreen(newScreen);

        //dispose old Screen
        if (currentScreen != null) {
            currentScreen.dispose();
        }
    }



}
