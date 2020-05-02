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
        image.setFillParent(true);
        image.addListener(new ClickListener() {
            @Override

            public void clicked(InputEvent event, float x, float y) {
                ScreenManager.getInstance().switchScreen(ScreenEnum.LOBBY);

            }

        });

        TextButton btn1 = new TextButton("JOIN LOBBY", skin, "default");


        btn1.addListener(new ClickListener() {
            @Override

            public void clicked(InputEvent event, float x, float y) {
                ScreenManager.getInstance().switchScreen(ScreenEnum.LOBBY);

            }

        });

        table.add(btn1);
        //table.setBackground(new TiledDrawable(skin.getTiledDrawable("tile-a")));
        table.add(image);
        table.setHeight(300);
        table.setWidth(300);
        table.align(Align.center | Align.top);
        stage.addActor(table);

        return stage;
    }

}
