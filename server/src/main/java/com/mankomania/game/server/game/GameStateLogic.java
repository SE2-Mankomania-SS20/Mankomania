package com.mankomania.game.server.game;

import com.mankomania.game.core.data.GameData;
import com.mankomania.game.server.data.GameState;
import com.mankomania.game.server.data.ServerData;

/*********************************
 Created by Fabian Oraze on 05.05.20
 *********************************/

public class GameStateLogic {
    private GameState currentState;
    private ServerData serverData;
    private GameData gameData;

    public GameStateLogic(ServerData serverData, GameData gameData) {
        this.serverData = serverData;
        this.gameData = gameData;


    }

    public void receivedDiceResult() {

    }

}
