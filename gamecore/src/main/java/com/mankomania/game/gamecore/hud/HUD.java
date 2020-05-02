package com.mankomania.game.gamecore.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.mankomania.game.gamecore.util.ScreenEnum;
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
        table.setFillParent(false);
        /* image.setFillParent(true);
        image.addListener(new ClickListener() {
            @Override

            public void clicked(InputEvent event, float x, float y) {
                ScreenManager.getInstance().switchScreen(ScreenEnum.LOBBY);

            }

        }); */

        TextButton btn2 = new TextButton("Hide HUD", skin, "default");


        TextButton btn1 = new TextButton("Options", skin, "default");
 btn1.setFillParent(true);

        btn1.addListener(new ClickListener() {
            @Override

            public void clicked(InputEvent event, float x, float y) {
               // ScreenManager.getInstance().switchScreen(ScreenEnum.LOBBY);
                table.clear();
                table.setHeight(stage.getHeight()-220);
                table.setWidth(400);
                table.padLeft(900);
                table.align(Align.center);
                btn2.setFillParent(true);
                table.add(btn2);


            }

        });

        btn2.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                table.clear();
                table.add(btn1);
                table.setHeight(100);
                table.setWidth(100);
                table.padLeft(0);
            }

        });

        table.add(btn1);
        //table.setBackground(new TiledDrawable(skin.getTiledDrawable("tile-a")));
        //table.add(image);
        table.setHeight(100);
        table.setWidth(100);
        //
        stage.addActor(table);

        return stage;
    }

}
