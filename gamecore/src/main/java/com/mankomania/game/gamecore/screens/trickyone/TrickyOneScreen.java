package com.mankomania.game.gamecore.screens.trickyone;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.mankomania.game.gamecore.MankomaniaGame;
import com.mankomania.game.gamecore.screens.AbstractScreen;
import com.mankomania.game.gamecore.util.AssetPaths;

import java.util.ArrayList;
import java.util.Locale;

/*
 Created by Fabian Oraze on 22.05.20
 */

public class TrickyOneScreen extends AbstractScreen {

    private Stage stage;
    private Skin skin;
    private TextButton rollButton;
    private TextButton stopButton;
    private Label infoSmallLabel;
    private Label potLabel;
    private Table back;
    private int moneyChangeAmount;
    private int totalRolled;
    private ArrayList<Image> diceListFirst;
    private ArrayList<Image> diceListSecond;
    private InfoOverlay infoOverlay;

    private static final float DICE_SIZE = 150f;

    private static final String SKIN_TYPE = "black";

    private static final String POT = "Pot:\n";

    private static final String CURRENT_ROLLED = "Gewuerfelte \nAugenanzahl:\n";


    public TrickyOneScreen() {

        infoOverlay = new InfoOverlay();

        diceListFirst = new ArrayList<>();
        diceListSecond = new ArrayList<>();
        initDiceCombinations();

        moneyChangeAmount = 0;
        totalRolled = 0;

        stage = new Stage();
        skin = MankomaniaGame.getMankomaniaGame().getManager().get(AssetPaths.SKIN);
        skin.getFont("font").getData().setScale(3, 3);

        Texture board = MankomaniaGame.getMankomaniaGame().getManager().get(AssetPaths.TRICKY_ONE_BACK);
        TextureRegionDrawable drawable = new TextureRegionDrawable(board);

        back = new Table();
        back.setFillParent(true);
        back.setBackground(drawable);

        rollButton = new TextButton("Wuerfeln", skin);
        rollButton.setPosition(Gdx.graphics.getWidth() / 2f + Gdx.graphics.getWidth() / 7f, Gdx.graphics.getHeight() / 8f);
        rollButton.setSize(220, 140);

        stopButton = new TextButton("Stop", skin);
        stopButton.setPosition(Gdx.graphics.getWidth() / 2f + Gdx.graphics.getWidth() / 3f, Gdx.graphics.getHeight() / 8f);
        stopButton.setSize(220, 140);

        infoSmallLabel = new Label(CURRENT_ROLLED + totalRolled, skin, SKIN_TYPE);
        infoSmallLabel.setPosition(Gdx.graphics.getWidth() / 2f + Gdx.graphics.getWidth() / 5f, Gdx.graphics.getHeight() / 2f + 120);

        potLabel = new Label(POT + moneyChangeAmount, skin, SKIN_TYPE);
        potLabel.setPosition(Gdx.graphics.getWidth() / 2f + Gdx.graphics.getWidth() / 5f, Gdx.graphics.getHeight() / 2.5f);

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

        InputMultiplexer multiplexer = new InputMultiplexer();

        infoOverlay.create(skin, multiplexer);

        stage.addActor(back);
        stage.addActor(rollButton);
        stage.addActor(stopButton);
        stage.addActor(infoSmallLabel);
        stage.addActor(potLabel);

        for (Image image : diceListFirst) {
            stage.addActor(image);
        }
        for (Image image : diceListSecond) {
            stage.addActor(image);
        }

        multiplexer.addProcessor(this.stage);
        Gdx.input.setInputProcessor(multiplexer);

    }

    @Override
    public void render(float delta) {

        super.render(delta);
        update();
        checkForInputAble();
        stage.act(delta);
        stage.draw();
        infoOverlay.render();
        super.renderNotifications(delta);

    }

    private void update() {
        totalRolled = MankomaniaGame.getMankomaniaGame().getGameData().getTrickyOneData().getRolledAmount();
        moneyChangeAmount = MankomaniaGame.getMankomaniaGame().getGameData().getTrickyOneData().getPot();

        //update text of labels
        String money = String.format(Locale.GERMAN, "%,d", moneyChangeAmount);
        potLabel.setText(POT + money);
        infoSmallLabel.setText(CURRENT_ROLLED + totalRolled);

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
                image.setBounds(Gdx.graphics.getWidth() / 4f, Gdx.graphics.getHeight() / 2f, DICE_SIZE, DICE_SIZE);
            } else {
                image.setVisible(false);
            }
        }

        for (Image image : diceListSecond) {
            if ((diceListSecond.indexOf(image) + 1) == second) {
                image.setVisible(true);
                image.setBounds(Gdx.graphics.getWidth() / 3f + DICE_SIZE, Gdx.graphics.getHeight() / 3f, DICE_SIZE, DICE_SIZE);
            } else {
                image.setVisible(false);
            }
        }
    }

    private void initDiceCombinations() {

        Texture one = MankomaniaGame.getMankomaniaGame().getManager().get(AssetPaths.DICE_ONE);
        Texture two = MankomaniaGame.getMankomaniaGame().getManager().get(AssetPaths.DICE_TWO);
        Texture three = MankomaniaGame.getMankomaniaGame().getManager().get(AssetPaths.DICE_THREE);
        Texture four = MankomaniaGame.getMankomaniaGame().getManager().get(AssetPaths.DICE_FOUR);
        Texture five = MankomaniaGame.getMankomaniaGame().getManager().get(AssetPaths.DICE_FIVE);
        Texture six = MankomaniaGame.getMankomaniaGame().getManager().get(AssetPaths.DICE_SIX);

        Image diceOneFirst = new Image(one);
        Image diceOneSecond = new Image(one);
        Image diceTwoFirst = new Image(two);
        Image diceTwoSecond = new Image(two);
        Image diceThreeFirst = new Image(three);
        Image diceThreeSecond = new Image(three);
        Image diceFourFirst = new Image(four);
        Image diceFourSecond = new Image(four);
        Image diceFiveFirst = new Image(five);
        Image diceFiveSecond = new Image(five);
        Image diceSixFirst = new Image(six);
        Image diceSixSecond = new Image(six);

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

    }

}
