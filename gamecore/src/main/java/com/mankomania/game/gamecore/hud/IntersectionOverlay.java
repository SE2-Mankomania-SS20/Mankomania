package com.mankomania.game.gamecore.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.mankomania.game.gamecore.MankomaniaGame;
import com.mankomania.game.gamecore.util.AssetPaths;

/*
 Created by Fabian Oraze on 06.06.20
 */

public class IntersectionOverlay {

    private static final float HEIGHT_ARROW = 250f;
    private static final float WEIGHT_ARROW = 250f;

    private Texture arrow_left;
    private Texture arrow_right;
    private Image left;
    private Image right;
    private ShapeRenderer shape;
    private ShapeRenderer border;
    private SpriteBatch spriteBatch;
    private Label text;
    private Stage stage;
    private boolean showOverlay;

    public void create() {
        stage = new Stage();

        Skin skin = MankomaniaGame.getMankomaniaGame().getManager().get(AssetPaths.SKIN_2);
        skin.getFont("default-font").getData().setScale(4f, 4f);
        arrow_left = MankomaniaGame.getMankomaniaGame().getManager().get(AssetPaths.ARROW_LEFT);
        arrow_right = MankomaniaGame.getMankomaniaGame().getManager().get(AssetPaths.ARROW_RIGHT);
        left = new Image(arrow_left);
        right = new Image(arrow_right);

        left.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                MankomaniaGame.getMankomaniaGame().getNetworkClient().getMessageHandler().sendIntersectionSelectionMessage(
                        MankomaniaGame.getMankomaniaGame().getGameData().getCurrentPlayerTurnField().getNextField());
                showOverlay = false;
                stage.clear();
            }
        });

        right.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                MankomaniaGame.getMankomaniaGame().getNetworkClient().getMessageHandler().sendIntersectionSelectionMessage(
                        MankomaniaGame.getMankomaniaGame().getGameData().getCurrentPlayerTurnField().getOptionalNextField());
                showOverlay = false;
                stage.clear();
            }
        });

        spriteBatch = new SpriteBatch();
        shape = new ShapeRenderer();
        border = new ShapeRenderer();
        text = new Label(null, skin);

        showOverlay = false;
    }

    public void render(float delta) {
        if (showOverlay) {
            draw();
        } else {
            if (MankomaniaGame.getMankomaniaGame().getGameData().isOnIntersection())
                draw();
        }
    }

    public void draw() {
        text.setAlignment(Align.center);
        text.setColor(Color.BLACK);
        text.setPosition(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2.5f);
        text.setText("Choose a path");

        left.setBounds(text.getX() - text.getPrefWidth() / 2f - WEIGHT_ARROW - 100, text.getY() - 125, WEIGHT_ARROW, HEIGHT_ARROW);
        right.setBounds(text.getX() + text.getPrefWidth() / 2f + 100, text.getY() - 125, WEIGHT_ARROW, HEIGHT_ARROW);
        stage.addActor(left);
        stage.addActor(right);

        border.setColor(Color.BLACK);
        border.begin(ShapeRenderer.ShapeType.Filled);
        border.rect(text.getX() - text.getPrefWidth() / 2f - 15f, text.getY() - text.getPrefHeight() / 2f-5f,
                text.getPrefWidth() + 30f, text.getPrefHeight() + 10f);
        border.end();

        shape.setColor(new Color(107f / 255, 250f / 255f, 205f / 255f, 1));
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.rect(text.getX() - text.getPrefWidth() / 2f - 10f, text.getY() - text.getPrefHeight() / 2f,
                text.getPrefWidth() + 20f, text.getPrefHeight());
        shape.end();

        spriteBatch.begin();
        text.draw(spriteBatch, 1f);
        spriteBatch.end();

        stage.act();
        stage.draw();
    }

    public void addMultiplexer(InputMultiplexer multiplexer) {
        multiplexer.addProcessor(this.stage);
    }
}
