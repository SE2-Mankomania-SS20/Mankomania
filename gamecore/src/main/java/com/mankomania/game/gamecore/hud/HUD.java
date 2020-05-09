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
import com.badlogic.gdx.utils.Align;
import com.mankomania.game.gamecore.fieldoverlay.FieldOverlay;
import com.mankomania.game.gamecore.util.ScreenEnum;
import com.mankomania.game.gamecore.util.ScreenManager;

public class HUD {
    public HUD() {
    }

    public Stage create(FieldOverlay fieldOverlay) {
        Skin skin = new Skin(Gdx.files.internal("skin/terra-mother-ui.json"));
        Stage stage = new Stage();

        Table table = new Table();
//        table.setDebug(true);

        Label l1 = new Label("Notifications:", skin, "black");
        Label l2 = new Label("Own stats:", skin, "black");

        Texture chat_texture = new Texture(Gdx.files.internal("hud/chat.png"));
        Image chat_image = new Image(chat_texture);

        Texture dice_texture = new Texture(Gdx.files.internal("hud/dice.png"));
        Image dice_image = new Image(dice_texture);

        Texture field_texture = new Texture(Gdx.files.internal("hud/overlay.png"));
        Image field_image = new Image(field_texture);

//        TextButton chat = new TextButton("Chat", skin, "default");
//        TextButton felder = new TextButton("Field overlay", skin, "default");
//        TextButton wurf = new TextButton("Dice", skin, "default");
        Table players = new Table();
        Label p1 = new Label("P1: \n", skin, "black");
        Label p2 = new Label("P2: \n", skin, "black");
        Label p3 = new Label("P3: \n", skin, "black");
        Label p4 = new Label("P4: \n", skin, "black");
        players.add(p1, p2, p3, p4);
        table.setSize(200, 200);
        players.setColor(1, 1, 1, 1);

        skin.getFont("font").getData().setScale(3, 3);
        chat_image.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ScreenManager.getInstance().switchScreen(ScreenEnum.CHAT, ScreenManager.getInstance().getGame().getClient(), ScreenEnum.MAIN_GAME);
            }

        });
        field_image.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //TODO: FELIX :-)
                if (fieldOverlay.isShowing()) {
                    fieldOverlay.hide();
                } else {
                    fieldOverlay.show();
                }
            }

        });
        dice_image.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ScreenManager.getInstance().switchScreen(ScreenEnum.DICE, ScreenManager.getInstance().getGame().getClient(), ScreenEnum.MAIN_GAME);
            }

        });

        Texture hud_button_texture = new Texture(Gdx.files.internal("hud/options.png"));
        Image hud_button_image = new Image(hud_button_texture);

        Texture back_button_texture = new Texture(Gdx.files.internal("hud/back.png"));
        Image back_button_image = new Image(back_button_texture);

        // TODO: extract all values to explicit config file and refactor this method, splitting buttons up in multiple tables
        hud_button_image.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                table.clear();
                table.setFillParent(true);

                table.add(l1).pad(10).fillY().align(Align.top).padRight(100).padBottom(490).padTop(80);
                table.add(l2).pad(10).fillY().align(Align.top).padLeft(600).padBottom(490).padTop(80);
                table.row();
                table.add(chat_image).pad(10).fillY().align(Align.top).width(150).height(150).padRight(100).padBottom(0);
                ; //180
                table.row();
                table.add(field_image).pad(10).fillY().align(Align.top).width(150).height(150).padRight(100).padBottom(0);
                ;
                table.row();
                table.add(dice_image).pad(10).align(Align.top).width(150).height(150).padRight(100).padBottom(0);
                ; //220
                table.add(players).pad(10).align(Align.top).width(620).height(120).padLeft(550);
                table.add(back_button_image).align(Align.top).padBottom(90).width(160).height(160).padLeft(0);
                table.setHeight(100);
                table.setWidth(100);

            }
        });


        table.add(hud_button_image).padLeft(3200).padBottom(40).width(200).height(200);

        back_button_image.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                table.clear();
                table.add(hud_button_image).padLeft(1600).padTop(850).width(200).height(200);
                stage.addActor(table);
            }

        });

        stage.addActor(table);

        return stage;
    }
}
