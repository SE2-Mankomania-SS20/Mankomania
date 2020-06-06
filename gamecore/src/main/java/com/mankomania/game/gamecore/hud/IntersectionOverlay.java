package com.mankomania.game.gamecore.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.mankomania.game.gamecore.MankomaniaGame;
import com.mankomania.game.gamecore.util.AssetPaths;

/*
 Created by Fabian Oraze on 06.06.20
 */

public class IntersectionOverlay {

    private Texture arrow_left;
    private Texture arrow_right;
    private ShapeRenderer shape;
    private ShapeRenderer border;
    private SpriteBatch spriteBatch;
    private Label text;
    private boolean showOverlay;

    public void create() {
        Skin skin = MankomaniaGame.getMankomaniaGame().getManager().get(AssetPaths.SKIN_2);
        skin.getFont("default-font").getData().setScale(3f, 3f);
        arrow_left = MankomaniaGame.getMankomaniaGame().getManager().get(AssetPaths.ARROW_LEFT);
        arrow_right = MankomaniaGame.getMankomaniaGame().getManager().get(AssetPaths.ARROW_RIGHT);
        spriteBatch = new SpriteBatch();

        text = new Label("Choose a Path!", skin);
        text.setAlignment(Align.center);
        text.setPosition(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 3f);

        shape = new ShapeRenderer();
        border = new ShapeRenderer();

        showOverlay = false;
    }

    public void render(float delta) {
        if (showOverlay){
            shape.setColor(Color.WHITE);
            shape.begin(ShapeRenderer.ShapeType.Filled);
            shape.rect(text.getX() - text.getPrefWidth() / 2f - 10f, text.getY() - text.getPrefHeight() / 2f,
                    text.getPrefWidth() + 20f, text.getPrefHeight());
            shape.end();

            border.setColor(Color.BLACK);
            border.begin(ShapeRenderer.ShapeType.Line);
            border.rect(text.getX() - text.getPrefWidth() / 2f - 10f, text.getY() - text.getPrefHeight() / 2f,
                    text.getPrefWidth() + 20f, text.getPrefHeight());
            border.end();

        }
    }

}
