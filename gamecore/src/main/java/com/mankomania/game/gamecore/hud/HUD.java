package com.mankomania.game.gamecore.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;
import com.esotericsoftware.minlog.Log;
import com.mankomania.game.gamecore.MankomaniaGame;
import com.mankomania.game.gamecore.fieldoverlay.FieldOverlay;
import com.mankomania.game.gamecore.util.Screen;
import com.mankomania.game.gamecore.util.ScreenManager;

public class HUD {

    public Stage create(FieldOverlay fieldOverlay) {
        final String styleName = "black";
        DiceOverlay d = new DiceOverlay();

        Skin skin = new Skin(Gdx.files.internal("skin/terra-mother-ui.json"));
        Stage stage = new Stage();

        Table table = new Table();
        Label l1 = new Label("       0      0       0", skin, styleName);

        Texture chat_texture = new Texture(Gdx.files.internal("hud/chat.png"));
        Image chat_image = new Image(chat_texture);

        Texture dice_texture = new Texture(Gdx.files.internal("hud/dice.png"));
        Image dice_image = new Image(dice_texture);

        Texture field_texture = new Texture(Gdx.files.internal("hud/overlay.png"));
        Image field_image = new Image(field_texture);

        Table players = new Table();
        Label p1 = new Label("           P1:  1.000.000 \n           P2: 1.000.000\n           P3: 1.000.000\n           P4: 1.000.000", skin, styleName);
        Label p2 = new Label("\nP2: \n", skin, styleName);
        Label p3 = new Label("\nP3: \n", skin, styleName);
        Label p4 = new Label("\nP4: \n", skin, styleName);

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
        Label p5 = new Label("\nYour are Player " + (localPlayerID + 1), skin, c);
        p5.setPosition(250, 50);
        players.add(p1, p2, p3, p4);
        stage.addActor(p5);

        Texture aktien = new Texture(Gdx.files.internal("aktien.png"));
        Image aktien_img = new Image(aktien);

        Texture spieler = new Texture(Gdx.files.internal("spieler.png"));
        Image spieler_img = new Image(spieler);

        table.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        players.setColor(1, 1, 1, 1);
        table.setFillParent(true);
        skin.getFont("font").getData().setScale(3, 3);
        chat_image.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //ScreenManager.getInstance().switchScreen(Screen.CHAT, Screen.MAIN_GAME);
                ScreenManager.getInstance().switchScreen(Screen.AKTIEN_BOERSE);
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


        Texture hud_button_texture = new Texture(Gdx.files.internal("hud/options.png"));
        Image hud_button_image = new Image(hud_button_texture);

        Texture back_button_texture = new Texture(Gdx.files.internal("hud/back.png"));
        Image back_button_image = new Image(back_button_texture);

        Table t1 = new Table();

        t1.add(chat_image).pad(10).fillY().align(Align.top).width(150).height(150);
        t1.row();
        t1.add(field_image).pad(10).fillY().align(Align.top).width(150).height(150);
        t1.row();
        t1.add(dice_image).pad(10).align(Align.top).width(150).height(150);

        Table t2 = new Table();
        Stack s = new Stack();

        s.add(aktien_img);
        s.add(l1);
        t2.add(s).size(400, 100);

        Table t3 = new Table();
        Stack s2 = new Stack();
        s2.add(spieler_img);
        s2.add(p1);

        t3.add(s2);
        t3.add(back_button_image).align(Align.top).width(150).height(150);

        hud_button_image.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                table.clear();
                table.setFillParent(true);
                table.add(t1).padRight(300).padTop(585);
                table.add(t2).padTop(785).padRight(100);
                table.add(t3).padTop(785);
            }
        });

        dice_image.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                int max = 12;
                int min = 1;
                int range = max - min + 1;
                int rand_int1 = (int) (Math.random() * range) + min;

                table.clear();
                table.add(d.setDice(rand_int1)).padRight(1300).padTop(300);

                float delayInSeconds = 2f;
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        table.clear();
                        table.add(hud_button_image).padLeft(1600).padTop(800).width(200).height(200);
                        Log.info("[DiceScreen] Done rolling the dice (rolled a " + rand_int1 + "). Calling the MessageHandlers'");
                        MankomaniaGame.getMankomaniaGame().getClient().getMessageHandler().sendDiceResultMessage(rand_int1);
                    }
                }, delayInSeconds);
            }
        });

        table.add(hud_button_image).padLeft(1600).padTop(800).width(200).height(200);

        back_button_image.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                table.clear();
                table.add(hud_button_image).padLeft(1600).padTop(800).width(200).height(200);
                stage.addActor(table);
            }

        });

        stage.addActor(table);

        return stage;
    }
}
