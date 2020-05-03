package com.mankomania.game.gamecore.screens;

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
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.Timer;
import com.mankomania.game.gamecore.util.Screen;
import com.mankomania.game.gamecore.util.ScreenManager;

import java.util.Random;

public class DiceScreen extends AbstractScreen {
    private Stage stage;
    private Table table;
    private float timeSeconds = 0f;
    private float period = 1f;

    public DiceScreen(){
        Skin skin = new Skin(Gdx.files.internal("skin/terra-mother-ui.json"));
        Texture texture = new Texture(Gdx.files.internal("button1.png"));
        Image image = new Image(texture);
        image.setSize(400, 400);
        stage = new Stage();
        table = new Table();
        table.setFillParent(true);
        table.setBackground(new TiledDrawable(skin.getTiledDrawable("tile-a")));
        table.setWidth(stage.getWidth());
        table.align(Align.center | Align.top);
        Label dice=new Label("You rolled: ",skin);
        Gdx.input.setInputProcessor(stage);
        skin.getFont("font").getData().setScale(5, 5);
        Random rand = new Random();
        int rand_int1 = rand.nextInt(11)+1;
        String roll=String.valueOf(rand_int1);
        Label value=new Label(roll,skin);
        table.add(image).padTop(30);
        table.row();
        table.add(dice);
        table.row();
        table.add(value);
        stage.addActor(table);

        float delayInSeconds = 4;

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {

                ScreenManager.getInstance().switchScreen(Screen.MAIN_GAME);
            }
        }, delayInSeconds);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        super.render(delta);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }


    }

