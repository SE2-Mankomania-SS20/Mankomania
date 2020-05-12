package com.mankomania.game.gamecore.util;

/*********************************
 Created by Fabian Oraze on 10.05.20
 *********************************/

public class GameController {

    private static GameController instance;
    private boolean playerMoving = false;
    private int amountToMove;
    private boolean gameOnGoing = false;

    public static GameController getInstance() {
        if (instance == null) {
            instance = new GameController();
        }
        return instance;
    }

    public boolean isPlayerMoving() {
        return playerMoving;
    }

    public void setPlayerMoving(boolean playerMoving) {
        this.playerMoving = playerMoving;
    }

    public int getAmountToMove() {
        return amountToMove;
    }

    public void setAmountToMove(int amountToMove) {
        this.amountToMove = amountToMove;
        playerMoving = true;
    }

    public void movedOneTile() {
        if (amountToMove > 0) {
            this.amountToMove -= 1;
        } else if (amountToMove == 1) {
            this.amountToMove -= 1;
            playerMoving = false;
        }
    }

    public boolean isGameOnGoing() {
        return gameOnGoing;
    }

    public void setGameOnGoing(boolean gameOnGoing) {
        this.gameOnGoing = gameOnGoing;
    }
}
