package com.mankomania.game.gamecore.StockExchange;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.badlogic.gdx.utils.Align;
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
