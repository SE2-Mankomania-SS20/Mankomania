package com.mankomania.game.gamecore.util;

import com.badlogic.gdx.Game;
import com.mankomania.game.core.data.GameData;
import com.mankomania.game.gamecore.MankomaniaGame;
import com.mankomania.game.gamecore.client.NetworkClient;

/*********************************
 Created by Fabian Oraze on 14.05.20
 *********************************/

public class GameController {

    /**
     * Controller class which provides access to the Game object via a singleton pattern
     */

    private static GameController instance;
    private MankomaniaGame game;
    private NetworkClient client;

    private GameController() {
        super();
    }

    /**
     * should be called once when game starts
     * @param game Mankomania game object that is created at the start of the application
     * @param client NetworkClient which can be used to trigger sending methods from client to server, can
     *               not be used to get client specific data such as connection id
     */
    public static void initialize(MankomaniaGame game, NetworkClient client) {
        if (instance == null) {
            instance = new GameController();
        }
        instance.game = game;
        instance.client = client;
    }


    public static MankomaniaGame getGame() {
        return instance.game;
    }

    public static NetworkClient getClient() {
        return instance.client;
    }

    public static GameData getGameData() {
        return instance.game.getGameData();
    }


}
