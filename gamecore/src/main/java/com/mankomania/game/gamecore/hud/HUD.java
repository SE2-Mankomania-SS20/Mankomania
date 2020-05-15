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
import com.mankomania.game.gamecore.fieldoverlay.FieldOverlay;
import com.mankomania.game.gamecore.util.Screen;
import com.mankomania.game.gamecore.util.ScreenManager;

public class HUD {

    public Stage create(FieldOverlay fieldOverlay) {
        final String styleName = "black";
        Skin skin = new Skin(Gdx.files.internal("skin/terra-mother-ui.json"));
        Stage stage = new Stage();

        Table table = new Table();
//        table.setDebug(true);

        Label l1 = new Label("       0      0       0", skin, styleName);

        Texture chat_texture = new Texture(Gdx.files.internal("hud/chat.png"));
        Image chat_image = new Image(chat_texture);

        Texture dice_texture = new Texture(Gdx.files.internal("hud/dice.png"));
        Image dice_image = new Image(dice_texture);

        Texture field_texture = new Texture(Gdx.files.internal("hud/overlay.png"));
        Image field_image = new Image(field_texture);

        Table players = new Table();
        Label p1 = new Label("P1: \n", skin, styleName);
        Label p2 = new Label("P2: \n", skin, styleName);
        Label p3 = new Label("P3: \n", skin, styleName);
        Label p4 = new Label("P4: \n", skin, styleName);
        players.add(p1, p2, p3, p4);

        table.debug();

        Texture aktien = new Texture(Gdx.files.internal("aktien.png"));
        Image aktien_img = new Image(aktien);

        table.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        players.setColor(1, 1, 1, 1);
        table.setFillParent(true);
        skin.getFont("font").getData().setScale(3, 3);
        chat_image.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ScreenManager.getInstance().switchScreen(Screen.CHAT,ScreenManager.getInstance().getGame().getClient(), Screen.MAIN_GAME);
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
        dice_image.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ScreenManager.getInstance().switchScreen(Screen.DICE,ScreenManager.getInstance().getGame().getClient(), Screen.MAIN_GAME);
            }

        });

        Texture hud_button_texture = new Texture(Gdx.files.internal("hud/options.png"));
        Image hud_button_image = new Image(hud_button_texture);

        Texture back_button_texture = new Texture(Gdx.files.internal("hud/back.png"));
        Image back_button_image = new Image(back_button_texture);

        Table t1=new Table();

        t1.add(chat_image).pad(10).fillY().align(Align.top).width(150).height(150);
        t1.row();
        t1.add(field_image).pad(10).fillY().align(Align.top).width(150).height(150);
        t1.row();
        t1.add(dice_image).pad(10).align(Align.top).width(150).height(150);

        Table t2=new Table();
        Stack s=new Stack();

        s.add(aktien_img);
        s.add(l1);
        t2.add(s).size(400,100);
        //t2.add(aktien_img).fillY().align(Align.top).size(400,100);
        //t2.add(l1).pad(10).fillY().align(Align.top).padRight(200);

        Table t3=new Table();

        t3.add(players).pad(10).align(Align.top).width(620).height(120);
        t3.add(back_button_image).align(Align.top).width(200).height(200);

        hud_button_image.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                table.clear();
                table.setFillParent(true);
                table.add(t1).padRight(300).padTop(585);
                table.add(t2).padTop(785);
                table.add(t3).padTop(785);

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
