package com.mankomania.game.gamecore.screens.trickyone;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.mankomania.game.gamecore.MankomaniaGame;
import com.mankomania.game.gamecore.screens.AbstractScreen;
import com.mankomania.game.gamecore.util.AssetPaths;

/*
 Created by Fabian Oraze on 22.05.20
 */

public class TrickyOneScreen extends AbstractScreen {

    private Stage stage;
    private Skin skin;
    private TextButton rollButton;
    private TextButton stopButton;
    private Label resultLabel;
    private Label infoSmallLabel;
    private Label gameInfoLabel;
    private Label potLabel;
    private Label firstDice;
    private Label secondDice;
    private Table back;
    private Texture diceTexture;
    private Image diceImage;
    private int moneyChangeAmount;
    private int totalRolled;

    private static final String SKIN_TYPE = "black";

    private static final String POT = "Pot: ";

    private static final String CURRENT_ROLLED = "Insgesamt Gewuerfelte \nAugenanzahl: ";

    //info that should be displayed in a label to give player the rules of the miniGame
    private static final String GAME_INFO = "Willkommen bei der Verflixten 1!\n\n" +
            "Regeln:\n\n" +
            "Du kannst so lange wuerfeln, wie du willst!\n\n" +
            "Wenn du 'Stop' drueckst, wird deine\n\n" +
            "gesammelte Augenanzahl mit 5.000 multipliziert\n\n" +
            "und von deinem Konto abgezogen!\n\n" +
            "Aber Achtung! Wenn du eine 1 wuerfelst,\n\n" +
            "gewinnst du auf der Stelle 100.000\n\n" +
            "falls du sogar 2 1en wuerfelst gewinnst\n\n" +
            "du sogar 300.000!";


    public TrickyOneScreen() {

        moneyChangeAmount = 0;
        totalRolled = 0;

        stage = new Stage();
        skin = MankomaniaGame.getMankomaniaGame().getManager().get(AssetPaths.SKIN);
        skin.getFont("font").getData().setScale(3, 3);
        skin.getFont("info").getData().setScale(2.5f, 2.5f);

        back = new Table();
        back.setFillParent(true);
        back.setBackground(new TiledDrawable(skin.getTiledDrawable("tile-a")));

        rollButton = new TextButton("Wuerfeln", skin);
        rollButton.setPosition(Gdx.graphics.getWidth() / 2f + Gdx.graphics.getWidth() / 6f, Gdx.graphics.getHeight() / 6f);
        rollButton.setSize(220, 140);

        stopButton = new TextButton("Stop", skin);
        stopButton.setPosition(Gdx.graphics.getWidth() / 2f + Gdx.graphics.getWidth() / 3f, Gdx.graphics.getHeight() / 6f);
        stopButton.setSize(220, 140);

        resultLabel = new Label("Ausgabe", skin, SKIN_TYPE);
        resultLabel.setPosition(Gdx.graphics.getWidth() / 2f + Gdx.graphics.getWidth() / 6f, Gdx.graphics.getHeight() / 3f);

        infoSmallLabel = new Label(CURRENT_ROLLED + totalRolled, skin, SKIN_TYPE);
        infoSmallLabel.setPosition(Gdx.graphics.getWidth() / 2f + 280, Gdx.graphics.getHeight() / 2f + 120);

        potLabel = new Label(POT + moneyChangeAmount, skin, SKIN_TYPE);
        potLabel.setPosition(Gdx.graphics.getWidth() / 2f + 280, Gdx.graphics.getHeight() / 2f - 40);

        gameInfoLabel = new Label(GAME_INFO, skin, "info");
        gameInfoLabel.setPosition(40, 40);

        firstDice = new Label("0", skin, SKIN_TYPE);
        firstDice.setPosition(Gdx.graphics.getWidth() / 2f + Gdx.graphics.getWidth() / 3f, Gdx.graphics.getHeight() / 3f);

        secondDice = new Label("0", skin, SKIN_TYPE);
        secondDice.setPosition(Gdx.graphics.getWidth() / 2f + Gdx.graphics.getWidth() / 2.6f, Gdx.graphics.getHeight() / 3f);

        diceTexture = new Texture(Gdx.files.internal("hud/dice.png"));
        diceImage = new Image(diceTexture);
        diceImage.setBounds(Gdx.graphics.getWidth() / 2f + 280, Gdx.graphics.getHeight() - 280f, 280, 280);

        rollButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                MankomaniaGame.getMankomaniaGame().getNetworkClient().getMessageHandler().sendRollTrickyOneMessage();
            }
        });

        stopButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                MankomaniaGame.getMankomaniaGame().getNetworkClient().getMessageHandler().sendStopTrickyOneMessage();
            }
        });

        Gdx.input.setInputProcessor(stage);

        stage.addActor(back);
        stage.addActor(rollButton);
        stage.addActor(stopButton);
        stage.addActor(resultLabel);
        stage.addActor(infoSmallLabel);
        stage.addActor(gameInfoLabel);
        stage.addActor(firstDice);
        stage.addActor(secondDice);
        stage.addActor(potLabel);
        stage.addActor(diceImage);

    }

    @Override
    public void render(float delta) {

        super.render(delta);
        update();
        checkForInputAble();
        stage.act(delta);
        stage.draw();
        super.renderNotifications(delta);

    }


    private void update() {
        totalRolled = MankomaniaGame.getMankomaniaGame().getGameData().getTrickyOneData().getRolledAmount();
        moneyChangeAmount = MankomaniaGame.getMankomaniaGame().getGameData().getTrickyOneData().getPot();

        //update text of labels
        potLabel.setText(POT + moneyChangeAmount);
        infoSmallLabel.setText(CURRENT_ROLLED + totalRolled);
        firstDice.setText(MankomaniaGame.getMankomaniaGame().getGameData().getTrickyOneData().getFirstDice());
        secondDice.setText(MankomaniaGame.getMankomaniaGame().getGameData().getTrickyOneData().getSecondDice());
    }

    @Override
    public void dispose() {

        stage.dispose();
    }

    public void checkForInputAble() {
        boolean enabled = MankomaniaGame.getMankomaniaGame().getGameData().getTrickyOneData().isInputEnabled();
        stopButton.setVisible(enabled);
        rollButton.setVisible(enabled);
    }

}
