package com.mankomania.game.gamecore.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Timer;
import com.esotericsoftware.minlog.Log;
import com.mankomania.game.core.network.messages.servertoclient.Notification;
import com.mankomania.game.core.player.Stock;
import com.mankomania.game.gamecore.MankomaniaGame;
import com.mankomania.game.gamecore.fieldoverlay.FieldOverlay;
import com.mankomania.game.gamecore.util.AssetPaths;
import com.mankomania.game.gamecore.util.Screen;
import com.mankomania.game.gamecore.util.ScreenManager;

import java.util.ArrayList;
import java.util.Locale;

public class HUD {
    Image diceImage;
    Texture diceTexture;
    Table table;
    DiceOverlay diceOverlay;
    Image hudButtonImage;
    Texture hudButtonTexture;
    float lastRoll;
    Stage stageHUD;
    Stage playerInfoStage;
    Image backButtonImage;
    Texture backButtonTexture;
    Texture chatTexture;
    Image chatImage;

    private Skin skin;
    private Skin skin2;
    public static final String STYLE_NAME = "black";
    private ArrayList<Label> moneyLabels;
    private Image fieldImage;
    private Image spielerImg;
    private Image aktienImg;
    private Label youArePlayer;
    private TextButton cheatButton;
    private Label bruchstahlLabel;
    private Label kurzschlussLabel;
    private Label trockenoelLabel;
    private Label playerAtTurn;
    private Label playerNotAtTurn;
    private ShapeRenderer renderer;
    private ShapeRenderer border;
    private FieldOverlay fieldOverlay;

    public Stage create(FieldOverlay fieldOverlay) {
        this.fieldOverlay = fieldOverlay;
        Texture fieldTexture;
        Texture spieler;
        Texture aktien;
        diceOverlay = new DiceOverlay();
        moneyLabels = new ArrayList<>();
        lastRoll = 0;

        skin = new Skin(Gdx.files.internal("skin/terra-mother-ui.skin"));
        skin2 = MankomaniaGame.getMankomaniaGame().getManager().get(AssetPaths.SKIN_2);
        skin2.getFont("default-font").getData().setScale(3, 3);

        stageHUD = new Stage();

        cheatButton = new TextButton("Assume cheating", skin2, "default");
        cheatButton.setPosition(50f, Gdx.graphics.getHeight() - 100f);
        cheatButton.setSize(530f, 90f);

        table = new Table();

        chatTexture = MankomaniaGame.getMankomaniaGame().getManager().get(AssetPaths.CHAT_IMAGE);
        chatImage = new Image(chatTexture);
        chatImage.setPosition(Gdx.graphics.getWidth() - 1600f, Gdx.graphics.getHeight() - 1050f);

        diceTexture = MankomaniaGame.getMankomaniaGame().getManager().get(AssetPaths.DICE_IMAGE);
        diceImage = new Image(diceTexture);
        diceImage.setPosition(Gdx.graphics.getWidth() - 900f, Gdx.graphics.getHeight() - 1050f);

        fieldTexture = MankomaniaGame.getMankomaniaGame().getManager().get(AssetPaths.FIELD_IMAGE);
        fieldImage = new Image(fieldTexture);
        fieldImage.setPosition(300f, 300f);

        initializePlayers();

        aktien = MankomaniaGame.getMankomaniaGame().getManager().get(AssetPaths.AKTIENTABLE);
        aktienImg = new Image(aktien);
        aktienImg.setPosition(0f, 0f);
        aktienImg.setSize(400, 100);


        spieler = MankomaniaGame.getMankomaniaGame().getManager().get(AssetPaths.MONEYTABLE);
        spielerImg = new Image(spieler);
        spielerImg.setPosition(Gdx.graphics.getWidth() - 800f, Gdx.graphics.getHeight() - 1050f);


        table.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        table.setFillParent(true);
        skin.getFont("font").getData().setScale(3, 3);
        chatImage.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ScreenManager.getInstance().switchScreen(Screen.CHAT, Screen.MAIN_GAME);
            }
        });
        fieldImage.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                if (fieldOverlay.isShowing()) {
                    fieldOverlay.hide();
                    showPlayerInfoStage();
                } else {
                    playerInfoStage.clear();
                    fieldOverlay.show();
                }
            }
        });

        backButtonTexture = MankomaniaGame.getMankomaniaGame().getManager().get(AssetPaths.BACK_BUTTON_IMAGE);
        backButtonImage = new Image(backButtonTexture);
        backButtonImage.setPosition(Gdx.graphics.getWidth() - 225f, Gdx.graphics.getHeight() - 1050f);
        backButtonImage.setSize(200, 200);

        chatImage.setPosition(0f, 0f);
        chatImage.setSize(150, 150);

        fieldImage.setPosition(0f, 0f);
        fieldImage.setSize(150, 150);

        diceImage.setPosition(0f, 0f);
        diceImage.setSize(150, 150);

        backButtonImage.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                stageHUD.clear();
                hudButtonImage.setPosition(Gdx.graphics.getWidth() - 225f, Gdx.graphics.getHeight() - 1050f);
                hudButtonImage.setSize(200, 200);
                stageHUD.addActor(hudButtonImage);

            }
        });

        diceImage.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                rollTheDice();
            }
        });

        cheatButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                MankomaniaGame.getMankomaniaGame().getNetworkClient().getMessageHandler().sendCheated();
            }
        });

        bruchstahlLabel = new Label("0", skin, STYLE_NAME);
        kurzschlussLabel = new Label("0", skin, STYLE_NAME);
        trockenoelLabel = new Label("0", skin, STYLE_NAME);

        hudButtonTexture = MankomaniaGame.getMankomaniaGame().getManager().get(AssetPaths.HUD_BUTTON_IMAGE);
        hudButtonImage = new Image(hudButtonTexture);
        hudButtonImage.setPosition(Gdx.graphics.getWidth() - 250f, Gdx.graphics.getHeight() - 1050f);
        hudButtonImage.setSize(200, 200);

        hudButtonImage.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showExtended();
            }
        });

        stageHUD.addActor(hudButtonImage);
        stageHUD.addActor(table);

        Gdx.input.setInputProcessor(stageHUD);
        return stageHUD;
    }

    public Stage createPlayerInfo() {

        playerInfoStage = new Stage();
        showPlayerInfoStage();
        return playerInfoStage;
    }

    public void showPlayerInfoStage() {
        int localPlayerID = MankomaniaGame.getMankomaniaGame().getLocalClientPlayer().getPlayerIndex();
        String myColor;
        switch (localPlayerID) {
            case 0: {
                myColor = "blue";
                break;
            }
            case 1: {
                myColor = "green";
                break;
            }
            case 2: {
                myColor = "red";
                break;
            }
            case 3: {
                myColor = "yellow";
                break;
            }
            default: {
                myColor = "black";
                break;
            }
        }

        youArePlayer = new Label("\nYour are Player " + (localPlayerID + 1), skin, myColor);
        youArePlayer.setPosition(Gdx.graphics.getWidth() - 1000f, Gdx.graphics.getHeight() - 100f);
        playerAtTurn = new Label("You are at turn!", skin, myColor);
        playerAtTurn.setPosition(Gdx.graphics.getWidth() - 500f, Gdx.graphics.getHeight() - 100f);
        playerNotAtTurn = new Label("You are not at turn", skin, STYLE_NAME);
        playerNotAtTurn.setPosition(Gdx.graphics.getWidth() - 500f, Gdx.graphics.getHeight() - 100f);

        renderer = new ShapeRenderer();
        border = new ShapeRenderer();
        renderer.setColor(Color.WHITE);
        border.setColor(Color.BLACK);

        playerInfoStage.addActor(youArePlayer);
        playerInfoStage.addActor(playerAtTurn);
        playerInfoStage.addActor(playerNotAtTurn);
        playerInfoStage.addActor(cheatButton);

    }


    public void render(float delta) {

        final float GRAVITY_EARTH = 9.81f;

        setButtonVisibility();
        if (!fieldOverlay.isShowing()) {
            showBoxes();
        }
        float xGrav = Gdx.input.getAccelerometerX() / GRAVITY_EARTH;
        float yGrav = Gdx.input.getAccelerometerY() / GRAVITY_EARTH;
        float zGrav = Gdx.input.getAccelerometerZ() / GRAVITY_EARTH;
        double gForce = Math.sqrt((xGrav * xGrav) + (yGrav * yGrav) + (zGrav * zGrav));
        if(lastRoll < 1){
            lastRoll += delta;
        }
        if ((gForce > 1.50d || gForce < 0.40d) && lastRoll > 1) {
            lastRoll = 0;
            rollTheDice();
        }
        getMoneyFromData();
        getStockFromData();
    }

    public void showBoxes() {
        float relativeWidth;
        if (MankomaniaGame.getMankomaniaGame().getLocalClientPlayer().getPlayerIndex() == MankomaniaGame.getMankomaniaGame().getGameData().getCurrentPlayerTurnIndex()) {
            relativeWidth = playerAtTurn.getPrefWidth() + 130f;
        } else {
            relativeWidth = playerNotAtTurn.getPrefWidth() + 120f;
        }
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.rect(youArePlayer.getX() - 10f, youArePlayer.getY() - 20f,
                youArePlayer.getPrefWidth() + relativeWidth, youArePlayer.getPrefHeight() + 20f);
        renderer.end();

        border.begin(ShapeRenderer.ShapeType.Line);
        border.rect(youArePlayer.getX() - 10f, youArePlayer.getY() - 20f,
                youArePlayer.getPrefWidth() + relativeWidth, youArePlayer.getPrefHeight() + 20f);
        border.end();
    }

    public void setButtonVisibility() {
        //check if local player is at turn, if so then change text of cheatButton
        if (MankomaniaGame.getMankomaniaGame().getLocalClientPlayer().getPlayerIndex() == MankomaniaGame.getMankomaniaGame().getGameData().getCurrentPlayerTurnIndex()) {
            cheatButton.setText("Cheat");
            playerAtTurn.setVisible(true);
            playerNotAtTurn.setVisible(false);
        } else {
            cheatButton.setText("Assume Cheating");
            playerAtTurn.setVisible(false);
            playerNotAtTurn.setVisible(true);
        }
    }

    public void rollTheDice() {
        if (MankomaniaGame.getMankomaniaGame().isCanRollTheDice()) {
            MankomaniaGame.getMankomaniaGame().setCanRollTheDice(false);
            int max = 12;
            int min = 1;
            int range = max - min + 1;
            int randInt1 = (int) (Math.random() * range) + min;

            diceOverlay.number = String.valueOf(randInt1);
            stageHUD.addActor(diceOverlay.setDice()); // .padRight(1800).padTop(300); //1300 , 300
            float delayInSeconds = 2f;
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    showExtended();
                    Log.info("[DiceScreen] Done rolling the dice (rolled a " + randInt1 + "). Calling the MessageHandlers'");
                    MankomaniaGame.getMankomaniaGame().getNetworkClient().getMessageHandler().sendDiceResultMessage(randInt1);
                }
            }, delayInSeconds);
        } else {
            MankomaniaGame.getMankomaniaGame().getNotifier().add(new Notification("You are not at turn!"));
        }
    }

    public void initializePlayers() {
        Label p1;
        Label p2;
        Label p3;
        Label p4;

        ArrayList<Label> temp = new ArrayList<>();
        p1 = new Label("P1:", skin, STYLE_NAME);
        p1.setPosition(1155f, 245f);
        p2 = new Label("P2:", skin, STYLE_NAME);
        p2.setPosition(1155f, 175f);
        p3 = new Label("P3:", skin, STYLE_NAME);
        p3.setPosition(1155f, 110f);
        p4 = new Label("P4:", skin, STYLE_NAME);
        p4.setPosition(1155f, 45f);

        temp.add(p1);
        temp.add(p2);
        temp.add(p3);
        temp.add(p4);

        for (int i = 0; i < MankomaniaGame.getMankomaniaGame().getGameData().getPlayers().size(); i++) {
            moneyLabels.add(temp.get(i));
        }
    }

    public void getMoneyFromData() {
        for (int i = 0; i < moneyLabels.size(); i++) {
            int amount = MankomaniaGame.getMankomaniaGame().getGameData().getPlayers().get(i).getMoney();
            String money = String.format(Locale.GERMAN, "%,d", amount);
            moneyLabels.get(i).setText("P" + (i + 1) + ": " + money);
        }
    }

    public void getStockFromData() {
        int trockenoel;
        int kurzschluss;
        int bruchstahl;

        bruchstahl = MankomaniaGame.getMankomaniaGame().getLocalClientPlayer().getAmountOfStock(Stock.BRUCHSTAHLAG);
        trockenoel = MankomaniaGame.getMankomaniaGame().getLocalClientPlayer().getAmountOfStock(Stock.TROCKENOEL);
        kurzschluss = MankomaniaGame.getMankomaniaGame().getLocalClientPlayer().getAmountOfStock(Stock.KURZSCHLUSSAG);

        bruchstahlLabel.setText("" + bruchstahl);
        trockenoelLabel.setText("" + trockenoel);
        kurzschlussLabel.setText("" + kurzschluss);
    }

    public void showExtended() {
        stageHUD.clear();
        playerInfoStage.clear();
        spielerImg.setPosition(Gdx.graphics.getWidth() - 725f, Gdx.graphics.getHeight() - 1050f);
        stageHUD.addActor(spielerImg);
        aktienImg.setPosition(Gdx.graphics.getWidth() - 1550f, Gdx.graphics.getHeight() - 1050f);
        stageHUD.addActor(aktienImg);

        /* chat,field overlay,dice button's */
        chatImage.setPosition(Gdx.graphics.getWidth() - 1750f, Gdx.graphics.getHeight() - 730f);
        stageHUD.addActor(chatImage);
        fieldImage.setPosition(Gdx.graphics.getWidth() - 1750f, Gdx.graphics.getHeight() - 890f);
        stageHUD.addActor(fieldImage);
        diceImage.setPosition(Gdx.graphics.getWidth() - 1750f, Gdx.graphics.getHeight() - 1050f);
        stageHUD.addActor(diceImage);

        /*cheat button*/
        stageHUD.addActor(cheatButton);

        /* back button */
        stageHUD.addActor(backButtonImage);

        /* Player 1,2,3,4 money */
        for (Label moneyLabel : moneyLabels) {
            stageHUD.addActor(moneyLabel);
        }

        /*Player Stock's */
        bruchstahlLabel.setPosition(Gdx.graphics.getWidth() - 1450f, Gdx.graphics.getHeight() - 1050f);
        stageHUD.addActor(bruchstahlLabel);
        trockenoelLabel.setPosition(Gdx.graphics.getWidth() - 1330f, Gdx.graphics.getHeight() - 1050f);
        stageHUD.addActor(trockenoelLabel);
        kurzschlussLabel.setPosition(Gdx.graphics.getWidth() - 1200f, Gdx.graphics.getHeight() - 1050f);
        stageHUD.addActor(kurzschlussLabel);

        showPlayerInfoStage();

    }
}
