package com.mankomania.game.gamecore.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.mankomania.game.gamecore.MankomaniaGame;


public class LobbyScreen extends ScreenAdapter {

    private MankomaniaGame game;
    private Stage stage;
    private Table table;
    private SpriteBatch batch;
    private Sprite sprite;

    public LobbyScreen(MankomaniaGame game){

        this.game = game;

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        table = new Table();
        table.setFillParent(true);
        table.setWidth(stage.getWidth());
        table.align(Align.center| Align.top);

    }
    @Override
    public void show() {
        Skin skin=new Skin(Gdx.files.internal("skin/terra-mother-ui.json"));
        skin.getFont("font").getData().setScale(5, 5);

        TextButton play = new TextButton("PLAY",skin,"default");
        TextButton back = new TextButton("BACK",skin,"default");
        TextButton chat = new TextButton("CHAT",skin,"default");

        table.padTop(300);
        table.add(play).padBottom(50).width(Gdx.graphics.getWidth()/2).height(100);
        table.row().pad(10,0,10,0);
        table.add(back).padBottom(50).width(Gdx.graphics.getWidth()/2).height(100);
        table.row();
        table.add(chat).padBottom(50).width(Gdx.graphics.getWidth()/2).height(100);

        play.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                game.setScreen(new MainGameScreen((game)));
            }
        });

        back.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                game.setScreen(new LaunchScreen((game)));
            }
        });
        chat.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event,float x,float y){
                // game.setScreen(new ChatScreen((game)));
            }
        });

        stage.addActor(table);
    }


    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 0.5f, 05.f, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
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
