package com.mankomania.game.gamecore.StockExchange;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.badlogic.gdx.utils.Align;
import com.esotericsoftware.minlog.Log;
import com.mankomania.game.gamecore.screens.AbstractScreen;

public class AktienBörse extends AbstractScreen {
    private Stage stage;
    private Table table;

    public AktienBörse(){create();}

    public void create(){
        Skin skin = new Skin(Gdx.files.internal("skin/terra-mother-ui.json"));
        stage = new Stage();
        table = new Table();
        table.setFillParent(true);
        table.setBackground(new TiledDrawable(skin.getTiledDrawable("tile-a")));
        table.setSize(stage.getWidth(),stage.getHeight());
        table.align(Align.center | Align.top);

        Gdx.input.setInputProcessor(stage);
        skin.getFont("font").getData().setScale(5, 5);

        Texture walze= new Texture(Gdx.files.internal("geld.png"));
        Image walze_image = new Image(walze);
        Label roll_text = new Label("Roll it!", skin, "default");

        table.add(roll_text).padTop(150);
        table.row();
        table.add(walze_image).padTop(50).width(Gdx.graphics.getWidth()/2);
        walze_image.setPosition(0,0);
        stage.addActor(table);
    }

    @Override
    public void render(float delta){
        super.render(delta);
        stage.act(delta);
        stage.draw();
        super.renderNotifications(delta);
    }

}
