package com.mankomania.game.core.data;

/*********************************
 Created by Fabian Oraze on 10.05.20
 *********************************/

public class GameController {

    private static GameController instance;
    private boolean playerMoving = false;
    private int amountToMove;

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


}
