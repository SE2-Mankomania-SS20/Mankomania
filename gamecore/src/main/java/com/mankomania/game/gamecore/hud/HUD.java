package com.mankomania.game.gamecore.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Timer;
import com.esotericsoftware.minlog.Log;
import com.mankomania.game.gamecore.MankomaniaGame;
import com.mankomania.game.gamecore.fieldoverlay.FieldOverlay;
import com.mankomania.game.gamecore.screens.AbstractScreen;
import com.mankomania.game.gamecore.util.Screen;
import com.mankomania.game.gamecore.util.ScreenManager;

import java.util.ArrayList;
import java.util.Locale;

public class HUD extends AbstractScreen {
    Image dice_image;
    Texture dice_texture;
    Table table;
    DiceOverlay diceOverlay;
    Image hud_button_image;
    Texture hud_button_texture;
    Boolean canRollTheDice;
    int count = 0;
    Stage stage;
    Image back_button_image;
    Texture back_button_texture;
    Texture chat_texture;
    Image chat_image;
    private Label p1;
    private Label p2;
    private Label p3;
    private Label p4;
    private Skin skin;
    public static final String STYLE_NAME = "black";
    private ArrayList<Label> moneyLabels;
    private Texture field_texture;
    private Image field_image;
    private Texture spieler;
    private Image spieler_img;
    private Label stock1;
    private Label stock2;
    private Label stock3;
    private Texture aktien;
    private Image aktien_img;
    private Label yourArePlayer;

    public Stage create(FieldOverlay fieldOverlay) {
        canRollTheDice = true;
        diceOverlay = new DiceOverlay();
        moneyLabels = new ArrayList<>();

        skin = new Skin(Gdx.files.internal("skin/terra-mother-ui.json"));
        stage = new Stage();

        stock1 = new Label("0", skin, STYLE_NAME);
        stock2 = new Label("0", skin, STYLE_NAME);
        stock3 = new Label("0", skin, STYLE_NAME);

        table = new Table();

        chat_texture = new Texture(Gdx.files.internal("hud/chat.png"));
        chat_image = new Image(chat_texture);
        chat_image.setPosition(Gdx.graphics.getWidth() - 1600, Gdx.graphics.getHeight() - 1050);

        dice_texture = new Texture(Gdx.files.internal("hud/dice.png"));
        dice_image = new Image(dice_texture);
        dice_image.setPosition(Gdx.graphics.getWidth() - 900, Gdx.graphics.getHeight() - 1050);

        field_texture = new Texture(Gdx.files.internal("hud/overlay.png"));
        field_image = new Image(field_texture);
        field_image.setPosition(300, 300);

        initializePlayers();

        int localPlayerID = MankomaniaGame.getMankomaniaGame().getLocalClientPlayer().getPlayerIndex();
        String c;
        switch (localPlayerID) {
            case 0: {
                c = "blue";
                break;
            }
            case 1: {
                c = "green";
                break;
            }
            case 2: {
                c = "red";
                break;
            }
            case 3: {
                c = "yellow";
                break;
            }
            default: {
                c = "black";
                break;
            }
        }
        yourArePlayer = new Label("\nYour are Player " + (localPlayerID + 1), skin, c);
        yourArePlayer.setPosition(250, 50);

        stage.addActor(yourArePlayer);

        aktien = new Texture(Gdx.files.internal("aktien.png"));
        aktien_img = new Image(aktien);
        aktien_img.setPosition(0, 0);
        aktien_img.setSize(400, 100);
        //stage.addActor(aktien_img);

        spieler = new Texture(Gdx.files.internal("hud/spieler.png"));
        spieler_img = new Image(spieler);
        spieler_img.setPosition(Gdx.graphics.getWidth() - 800, Gdx.graphics.getHeight() - 1050);
        //stage.addActor(spieler_img);

        table.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        table.setFillParent(true);
        skin.getFont("font").getData().setScale(3, 3);
        chat_image.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ScreenManager.getInstance().switchScreen(Screen.CHAT, Screen.MAIN_GAME);
            }
        });
        field_image.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                if (fieldOverlay.isShowing()) {
                    fieldOverlay.hide();
                } else {


                    fieldOverlay.show();
                }
            }
        });

        back_button_texture = new Texture(Gdx.files.internal("hud/back.png"));
        back_button_image = new Image(back_button_texture);
        back_button_image.setPosition(Gdx.graphics.getWidth() - 225, Gdx.graphics.getHeight() - 1050);
        back_button_image.setSize(200, 200);
        //stage.addActor(back_button_image);

        chat_image.setPosition(0, 0);
        chat_image.setSize(150, 150);

        //stage.addActor(chat_image);
        field_image.setPosition(0, 0);
        field_image.setSize(150, 150);

        //stage.addActor(field_image);
        dice_image.setPosition(0, 0);
        dice_image.setSize(150, 150);
        //stage.addActor(dice_image);

        back_button_image.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                stage.clear();
                hud_button_image.setPosition(Gdx.graphics.getWidth() - 225, Gdx.graphics.getHeight() - 1050);
                hud_button_image.setSize(200, 200);
                stage.addActor(hud_button_image);

            }
        });

        dice_image.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                table.clear();
                rollTheDice();
                count++;
            }
        });

        hud_button_texture = new Texture(Gdx.files.internal("hud/options.png"));
        hud_button_image = new Image(hud_button_texture);
        hud_button_image.setPosition(Gdx.graphics.getWidth() - 250, Gdx.graphics.getHeight() - 1050);
        hud_button_image.setSize(200, 200);

        hud_button_image.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showExtented();
            }
        });

        stage.addActor(hud_button_image);
        stage.addActor(table);

        Gdx.input.setInputProcessor(stage);
        return stage;
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        final float GRAVITY_EARTH = 9.81f;

        float xGrav = Gdx.input.getAccelerometerX() / GRAVITY_EARTH;
        float yGrav = Gdx.input.getAccelerometerY() / GRAVITY_EARTH;
        float zGrav = Gdx.input.getAccelerometerZ() / GRAVITY_EARTH;
        double gForce = Math.sqrt((xGrav * xGrav) + (yGrav * yGrav) + (zGrav * zGrav));
        if (gForce > 1.50d || gForce < 0.40d) {
            if (count == 0) {
                rollTheDice();
                count++;
            }
        }
        getMoneyFromData();

    }

    public void rollTheDice() {
        if (canRollTheDice) {

            int max = 12;
            int min = 1;
            int range = max - min + 1;
            int rand_int1 = (int) (Math.random() * range) + min;
            Skin skin = new Skin(Gdx.files.internal("skin/terra-mother-ui.json"));

            diceOverlay.number = String.valueOf(rand_int1);

            stage.addActor(diceOverlay.setDice()); // .padRight(1800).padTop(300); //1300 , 300

            float delayInSeconds = 2f;
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    showExtented();
                    Log.info("[DiceScreen] Done rolling the dice (rolled a " + rand_int1 + "). Calling the MessageHandlers'");
                    MankomaniaGame.getMankomaniaGame().getNetworkClient().getMessageHandler().sendDiceResultMessage(rand_int1);
                    count--;
                }
            }, delayInSeconds);

        }
    }

    public void initializePlayers() {
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

    public void showExtented() {
        stage.clear();

        /* Your stock ammount */
        stage.addActor(stock1);
        stage.addActor(stock2);
        stage.addActor(stock3);

        /* Money table, Stock table */
        spieler_img.setPosition(Gdx.graphics.getWidth() - 725, Gdx.graphics.getHeight() - 1050);
        stage.addActor(spieler_img);
        aktien_img.setPosition(Gdx.graphics.getWidth() - 1550, Gdx.graphics.getHeight() - 1050);
        stage.addActor(aktien_img);

        /* chat,field overlay,dice button's */
        chat_image.setPosition(Gdx.graphics.getWidth() - 1750, Gdx.graphics.getHeight() - 730);
        stage.addActor(chat_image);
        field_image.setPosition(Gdx.graphics.getWidth() - 1750, Gdx.graphics.getHeight() - 890);
        stage.addActor(field_image);
        dice_image.setPosition(Gdx.graphics.getWidth() - 1750, Gdx.graphics.getHeight() - 1050);
        stage.addActor(dice_image);

        /*Player label */
        yourArePlayer.setPosition(650, 100);
        stage.addActor(yourArePlayer);

        /* back button */
        stage.addActor(back_button_image);

        /* Player 1,2,3,4 money */
        for (int i = 0; i < moneyLabels.size(); i++) {
            stage.addActor(moneyLabels.get(i));
        }
    }
}
