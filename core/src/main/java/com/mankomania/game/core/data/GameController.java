package com.mankomania.game.core.data;

/*********************************
 Created by Fabian Oraze on 10.05.20
 *********************************/

public class GameController {

    private static GameController instance;

    public static GameController getInstance() {
        if (instance == null) {
            instance = new GameController();
        }
        return instance;
    }
}
