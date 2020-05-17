package com.mankomania.game.gamecore.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;
import com.esotericsoftware.minlog.Log;
import com.mankomania.game.gamecore.MankomaniaGame;
import com.mankomania.game.gamecore.util.Screen;
import com.mankomania.game.gamecore.util.ScreenManager;

import java.util.Random;

public class DiceScreen extends AbstractScreen {
    private final Stage stage;
    private final Table table;

    public DiceScreen() {
        Skin skin = new Skin(Gdx.files.internal("skin/terra-mother-ui.json"));
        Texture texture = new Texture(Gdx.files.internal("button1.png"));
        Image image = new Image(texture);
        image.setSize(400, 400);
        stage = new Stage();
        table = new Table();
        table.setFillParent(true);
        table.setBackground(new TiledDrawable(skin.getTiledDrawable("tile-a")));
        table.setWidth(stage.getWidth());
        table.align(Align.center | Align.top);
        Label dice = new Label("You rolled: ", skin);
        Gdx.input.setInputProcessor(stage);
        skin.getFont("font").getData().setScale(5, 5);
        Random rand = new Random();
        int rand_int1 = rand.nextInt(12) + 1;
        String roll = String.valueOf(rand_int1);
        Label value = new Label(roll, skin);
        table.add(image).padTop(30);
        table.row();
        table.add(dice);
        table.row();
        table.add(value);
        stage.addActor(table);

        float delayInSeconds = 3;

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                // TODO: only allow sending this message if we got an actual PlayerCanRollDice message with player id that matches this local player (!)
                // furthermore: grey out the button if not possible to roll the dice, show notification that player is expected to roll the dice, etc.
                ScreenManager.getInstance().switchScreen(Screen.MAIN_GAME);
                Log.info("[DiceScreen] Done rolling the dice (rolled a " + rand_int1 + "). Calling the MessageHandlers'");

                // Game as singleton would shrink that massive call chain a bit down lol
                MankomaniaGame.getMankomaniaGame().getClient().getMessageHandler().sendDiceResultMessage(rand_int1);
            }
        }, delayInSeconds);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        stage.act(delta);
        stage.draw();
        super.renderNotifications(delta);
    }
}

