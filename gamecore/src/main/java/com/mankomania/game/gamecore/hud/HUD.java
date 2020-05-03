package com.mankomania.game.gamecore.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.mankomania.game.gamecore.util.Screen;
import com.mankomania.game.gamecore.util.ScreenManager;

public class HUD {

public HUD(){
    }
    public Stage create(){
        Skin skin = new Skin(Gdx.files.internal("skin/terra-mother-ui.json"));
        Texture texture = new Texture(Gdx.files.internal("options.png"));
        Image image = new Image(texture);
        image.setSize(100, 100);
        Stage stage = new Stage();

        Table table = new Table();
        Label l1 = new Label("Notifications:", skin,"black");
        Label l2 = new Label("Own stats:", skin,"black");


        TextButton chat = new TextButton("Chat", skin, "default");
        TextButton felder = new TextButton("Felder overlay", skin, "default");
        TextButton wurf = new TextButton("Wuerfeln", skin, "default");
        Table players=new Table();
        Label p1 = new Label("P1: \n", skin,"black");
        Label p2 = new Label("P2: \n", skin,"black");
        Label p3 = new Label("P3: \n", skin,"black");
        Label p4 = new Label("P4: \n", skin,"black");
        players.add(p1,p2,p3,p4);
        table.setSize(200,200);
        players.setColor(1,1,1,1);

        skin.getFont("font").getData().setScale(3, 3);
        chat.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ScreenManager.getInstance().switchScreen(Screen.CHAT,ScreenManager.getInstance().getGame().getClient(),Screen.MAIN_GAME);
            }

        });

        wurf.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ScreenManager.getInstance().switchScreen(Screen.DICE,ScreenManager.getInstance().getGame().getClient(),Screen.MAIN_GAME);
            }

        });

                table.setFillParent(true);
                table.setWidth(400);
                table.setHeight(400);
                table.add(l1).pad(10).fillY().align(Align.top).padRight(100).padBottom(500);
                table.add(l2).pad(10).fillY().align(Align.top).padLeft(600).padBottom(500);
                table.row();
                table.add(chat).pad(10).fillY().align(Align.top).width(340).height(100).padRight(100);; //180
                table.row();
        table.add(felder).pad(10).fillY().align(Align.top).width(340).height(100).padRight(100);;
            table.row();
        table.add(wurf).pad(10).fillY().align(Align.top).width(340).height(100).padRight(100);; //220
        table.add(players).pad(10).fillY().align(Align.top).width(720).height(120).padLeft(600);
        table.setHeight(100);
        table.setWidth(100);

        stage.addActor(table);

        return stage;
    }

}
