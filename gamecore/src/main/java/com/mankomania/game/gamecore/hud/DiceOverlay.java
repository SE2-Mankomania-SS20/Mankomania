package com.mankomania.game.gamecore.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.badlogic.gdx.utils.Align;
import com.esotericsoftware.minlog.Log;

public class DiceOverlay {

    int rand_int1;
    private Skin skin = new Skin(Gdx.files.internal("skin/terra-mother-ui.json"));

    Table setDice(int rand) {
        Table table;

        Texture texture = new Texture(Gdx.files.internal("button1.png"));
        Image image = new Image(texture);
        image.setSize(400, 400);

        table = new Table();
        table.setFillParent(true);
        table.setBackground(new TiledDrawable(skin.getTiledDrawable("tile-a")));
        table.setWidth(Gdx.graphics.getWidth());
        table.setHeight(Gdx.graphics.getHeight());
        table.align(Align.center | Align.top);
        Label dice = new Label("You rolled: ", skin);

        skin.getFont("font").getData().setScale(5, 5);
        String roll = String.valueOf(rand);
        Label value = new Label(roll, skin);

        table.add(image).padTop(30);
        table.row();
        table.add(dice);
        table.row();
        table.add(value);

        Log.info("[DiceScreen] Done rolling the dice (rolled a " + rand + "). Calling the MessageHandlers'");

        return table;

    }

}

