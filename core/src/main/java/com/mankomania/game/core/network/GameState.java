package com.mankomania.game.core.network;

/*********************************
 Created by Fabian Oraze on 03.05.20
 *********************************/

public class GameState {

    private boolean playerReady;
    private boolean gameReady;

    public GameState() {
    }

    public GameState(boolean playerReady) {
        this.playerReady = playerReady;
    }

    public void setPlayerReady(boolean playerReady) {
        this.playerReady = playerReady;
    }

    public boolean getPlayerReady() {
        return playerReady;
    }

    public void setGameReady(boolean gameReady) {
        this.gameReady = gameReady;
    }

    public boolean getGameReady() {
        return gameReady;
    }


}
