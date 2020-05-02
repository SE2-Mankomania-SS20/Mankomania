package com.mankomania.game.gamecore.util;

import com.badlogic.gdx.Screen;
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
     * @param screenEnum enum which represents a screen
     * @param params     any parameter that might be necessary, look ScreenEnum
     */
    public void switchScreen(ScreenEnum screenEnum, Object... params) {

        //Get current Screen to dispose later
        Screen currentScreen = game.getScreen();

        //Set new Screen
        AbstractScreen newScreen = screenEnum.getScreen(params);
        game.setScreen(newScreen);

        //dispose old Screen
        if (currentScreen != null) {
           // currentScreen.dispose();
        }
    }


}
