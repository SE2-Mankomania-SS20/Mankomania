package com.mankomania.game.gamecore.screens.trickyone;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.mankomania.game.gamecore.MankomaniaGame;
import com.mankomania.game.gamecore.screens.AbstractScreen;
import com.mankomania.game.gamecore.util.AssetPaths;

import java.util.ArrayList;

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
    private ArrayList<Image> diceListFirst;
    private ArrayList<Image> diceListSecond;

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

        diceListFirst = new ArrayList<>();
        diceListSecond = new ArrayList<>();
        initDiceCombinations();

        moneyChangeAmount = 0;
        totalRolled = 0;

        stage = new Stage();
        skin = MankomaniaGame.getMankomaniaGame().getManager().get(AssetPaths.SKIN);
        skin.getFont("font").getData().setScale(3, 3);
        skin.getFont("info").getData().setScale(2.5f, 2.5f);

        /*Texture img = MankomaniaGame.getMankomaniaGame().getManager().get(AssetPaths.SLOT_BACKGROUND);
        Image image = new Image(img);
        TextureRegionDrawable drawable = new TextureRegionDrawable(img);*/

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

        for (Image image : diceListFirst) {
            stage.addActor(image);
        }
        for (Image image : diceListSecond) {
            stage.addActor(image);
        }

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

        if (MankomaniaGame.getMankomaniaGame().getGameData().getTrickyOneData().isGotUpdate()) {
            displayDice();
            MankomaniaGame.getMankomaniaGame().getGameData().getTrickyOneData().setGotUpdate(false);
        }
    }

    @Override
    public void dispose() {

        stage.dispose();
    }

    public void checkForInputAble() {
        boolean inputEnabled = !MankomaniaGame.getMankomaniaGame().getGameData().getTrickyOneData().isInputEnabled();
        boolean onTurn = MankomaniaGame.getMankomaniaGame().getLocalClientPlayer().getPlayerIndex() != MankomaniaGame.getMankomaniaGame().getGameData().getCurrentPlayerTurnIndex();
        if (inputEnabled || onTurn) {
            stopButton.setTouchable(Touchable.disabled);
            stopButton.setDisabled(true);
            rollButton.setTouchable(Touchable.disabled);
            rollButton.setDisabled(true);
        }

    }

    private void displayDice() {
        int first = MankomaniaGame.getMankomaniaGame().getGameData().getTrickyOneData().getFirstDice();
        int second = MankomaniaGame.getMankomaniaGame().getGameData().getTrickyOneData().getSecondDice();

        for (Image image : diceListFirst) {
            if ((diceListFirst.indexOf(image) + 1) == first) {
                image.setVisible(true);
                image.setBounds(Gdx.graphics.getWidth() / 2f + Gdx.graphics.getWidth() / 5f, Gdx.graphics.getHeight() / 4f, 300f, 300f);
            } else {
                image.setVisible(false);
            }
        }

        for (Image image : diceListSecond) {
            if ((diceListSecond.indexOf(image) + 1) == second) {
                image.setVisible(true);
                image.setBounds(Gdx.graphics.getWidth() / 2f + Gdx.graphics.getWidth() / 5f, Gdx.graphics.getHeight() / 4f, 300f, 300f);
            } else {
                image.setVisible(false);
            }
        }
    }

    private void initDiceCombinations() {

        /*Image diceOneFirst = MankomaniaGame.getMankomaniaGame().getManager().get(AssetPaths.DICE_ONE);
        Image diceOneSecond = MankomaniaGame.getMankomaniaGame().getManager().get(AssetPaths.DICE_ONE);
        Image diceTwoFirst = MankomaniaGame.getMankomaniaGame().getManager().get(AssetPaths.DICE_TWO);
        Image diceTwoSecond = MankomaniaGame.getMankomaniaGame().getManager().get(AssetPaths.DICE_TWO);
        Image diceThreeFirst = MankomaniaGame.getMankomaniaGame().getManager().get(AssetPaths.DICE_THREE);
        Image diceThreeSecond = MankomaniaGame.getMankomaniaGame().getManager().get(AssetPaths.DICE_THREE);
        Image diceFourFirst = MankomaniaGame.getMankomaniaGame().getManager().get(AssetPaths.DICE_FOUR);
        Image diceFourSecond = MankomaniaGame.getMankomaniaGame().getManager().get(AssetPaths.DICE_FOUR);
        Image diceFiveFirst = MankomaniaGame.getMankomaniaGame().getManager().get(AssetPaths.DICE_FIVE);
        Image diceFiveSecond = MankomaniaGame.getMankomaniaGame().getManager().get(AssetPaths.DICE_FIVE);
        Image diceSixFirst = MankomaniaGame.getMankomaniaGame().getManager().get(AssetPaths.DICE_SIX);
        Image diceSixSecond = MankomaniaGame.getMankomaniaGame().getManager().get(AssetPaths.DICE_SIX);

        diceListFirst.add(diceOneFirst);
        diceListFirst.add(diceTwoFirst);
        diceListFirst.add(diceThreeFirst);
        diceListFirst.add(diceFourFirst);
        diceListFirst.add(diceFiveFirst);
        diceListFirst.add(diceSixFirst);

        diceListSecond.add(diceOneSecond);
        diceListSecond.add(diceTwoSecond);
        diceListSecond.add(diceThreeSecond);
        diceListSecond.add(diceFourSecond);
        diceListSecond.add(diceFiveSecond);
        diceListSecond.add(diceSixSecond);
        */

    }

}
