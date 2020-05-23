package com.mankomania.game.gamecore.screens.trickyone;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.mankomania.game.gamecore.screens.AbstractScreen;

/*********************************
 Created by Fabian Oraze on 22.05.20
 *********************************/

public class TrickyOneScreen extends AbstractScreen {

    private Stage stage;
    private Skin skin;
    private TextButton rollButton;
    private TextButton stopButton;
    private Label resultLabel;
    private Label infoSmallLabel;
    private Label gameInfoLabel;
    private Label rolledDiceLabel;
    private Table back;
    private Texture diceTexture;
    private Image diceImage;

    //info that should be displayed in a label to give player the rules of the miniGame
    private static final String GAME_INFO = "Willkommen bei der Verflixten 1!\n" +
            "Regeln:\n" +
            "Du kannst so lange wuerfeln, wie du willst!\n" +
            "Wenn du 'Stop' drückst, wird deine gesammelte\n" +
            "Augenanzahl mit 5.000 multipliziert und von\n" +
            "deinem Konto abgezogen!\n" +
            "Aber Achtung! Wenn du eine 1 wuerfelst,\n" +
            "gewinnst du auf der Stelle 100.000\n" +
            "falls du sogar 2 1en wuerfelst gewinnst\n" +
            "du sogar 300.000!";


    public TrickyOneScreen() {

        stage = new Stage();
        skin = new Skin(Gdx.files.internal("skin/terra-mother-ui.json"));
        skin.getFont("font").getData().setScale(3, 3);
        skin.getFont("info").getData().setScale(2.5f, 2.5f);

        back = new Table();
        back.setFillParent(true);
        back.setBackground(new TiledDrawable(skin.getTiledDrawable("tile-a")));

        rollButton = new TextButton("Wuerfeln", skin);
        rollButton.setPosition(Gdx.graphics.getWidth() / 2 + Gdx.graphics.getWidth() / 6, Gdx.graphics.getHeight() / 6);
        rollButton.setSize(220, 140);

        stopButton = new TextButton("Stop", skin);
        stopButton.setPosition(Gdx.graphics.getWidth() / 2 + Gdx.graphics.getWidth() / 3, Gdx.graphics.getHeight() / 6);
        stopButton.setSize(220, 140);

        resultLabel = new Label("Ausgabe", skin, "black");
        resultLabel.setPosition(Gdx.graphics.getWidth() / 2 + Gdx.graphics.getWidth() / 6, Gdx.graphics.getHeight() / 3);

        infoSmallLabel = new Label("Insgesamt Gewuerfelte \nAugenanzahl:", skin, "black");
        infoSmallLabel.setPosition(Gdx.graphics.getWidth() / 2 + 280, Gdx.graphics.getHeight() / 2 + Gdx.graphics.getHeight() / 5);

        rolledDiceLabel = new Label("0", skin, "black");
        rolledDiceLabel.setPosition(Gdx.graphics.getWidth() / 2 + 280, Gdx.graphics.getHeight() / 2 + Gdx.graphics.getHeight() / 8);

        gameInfoLabel = new Label(GAME_INFO, skin, "info");
        gameInfoLabel.setPosition(40, Gdx.graphics.getHeight() / 2);

        diceTexture = new Texture(Gdx.files.internal("hud/dice.png"));
        diceImage = new Image(diceTexture);
        diceImage.setBounds(Gdx.graphics.getWidth() / 2 - 200, Gdx.graphics.getHeight() / 2, 400, 400);

        Gdx.input.setInputProcessor(stage);

        stage.addActor(back);
        stage.addActor(rollButton);
        stage.addActor(stopButton);
        stage.addActor(resultLabel);
        stage.addActor(infoSmallLabel);
        stage.addActor(gameInfoLabel);
        stage.addActor(rolledDiceLabel);
        stage.addActor(diceImage);

    }

    @Override
    public void render(float delta) {

        super.render(delta);
        stage.act(delta);
        stage.draw();
        super.renderNotifications(delta);

    }

    @Override
    public void dispose() {

        stage.dispose();
        skin.dispose();
    }

}
