package com.mankomania.game.gamecore.util;

import com.badlogic.gdx.Game;
import com.mankomania.game.gamecore.MankomaniaGame;

/*********************************
 Created by Fabian Oraze on 14.05.20
 *********************************/

public class GameController {

    /**
     * Controller class which provides access to the Game object via a singleton pattern
     */

    private static GameController instance;
    private MankomaniaGame game;

    private GameController() {
        super();
    }


    /**
     * should be called once when game starts
     *
     * @param game object of libGdx game type
     */
    public static void initialize(MankomaniaGame game) {
        if (instance == null) {
            instance = new GameController();
        }
        instance.game = game;
    }


    public static MankomaniaGame getGame() {
        return instance.game;
    }


}
