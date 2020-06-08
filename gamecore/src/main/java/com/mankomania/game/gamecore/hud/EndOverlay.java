package com.mankomania.game.gamecore.hud;

/*
 Created by Fabian Oraze on 08.06.20
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mankomania.game.gamecore.MankomaniaGame;
import com.mankomania.game.gamecore.util.AssetPaths;
import com.mankomania.game.gamecore.util.Screen;
import com.mankomania.game.gamecore.util.ScreenManager;


public class EndOverlay {

    private Stage stage;
    private Dialog dialogWaitingResult;
    private Skin dialog;
    private boolean isShowing;


    public void create() {
        stage = new Stage();
        dialog = MankomaniaGame.getMankomaniaGame().getManager().get(AssetPaths.SKIN_2);
        dialogWaitingResult = new Dialog("", dialog, "dialog") {
        };
        dialogWaitingResult.button("Back to Lobby").addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ScreenManager.getInstance().switchScreen(Screen.LOBBY);
                MankomaniaGame.getMankomaniaGame().setGameOver(false);
            }
        });
        isShowing = false;
    }

    public void render() {
        if (isShowing) {
            stage.act();
            stage.draw();
        } else {
            if (MankomaniaGame.getMankomaniaGame().isGameOver()) {
                Gdx.input.setInputProcessor(stage);
                dialogWaitingResult.text("Player " + MankomaniaGame.getMankomaniaGame().getWinnerIndex() + 1 + " has won the Game!");
                dialogWaitingResult.show(stage);
                isShowing = true;
            }
        }
    }
}
