package com.mankomania.game.gamecore.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.badlogic.gdx.utils.Align;
import com.mankomania.game.gamecore.MankomaniaGame;

public class LaunchScreen implements Screen {
    private Game game;
    private Stage stage;
    private Table table;
    private SpriteBatch batch;
    private Sprite sprite;
    public LaunchScreen(Game g){
        Texture texture=new Texture(Gdx.files.internal("mankomania.png"));
        Image image=new Image(texture);
        image.setSize(400,400);
        game=g;
        stage= new Stage ();
        table = new Table();
        table.setWidth(stage.getWidth());
        table.align(Align.center|Align.top);
        table.setPosition(0,Gdx.graphics.getHeight());

        Gdx.input.setInputProcessor(stage);
        Skin skin=new Skin(Gdx.files.internal("skin/terra-mother-ui.json"));
        skin.getFont("font").getData().setScale(5, 5);
        table.setBackground(new TiledDrawable(skin.getTiledDrawable("tile-a")));

        TextButton btn1=new TextButton("JOIN LOBBY",skin,"default");
        TextButton btn2=new TextButton("QUIT",skin,"default");

        btn1.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event,float x,float y){
                // game.setScreen(new LobbyScreen((MainGame) game));
            }

        });

        btn2.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event,float x,float y){
                Gdx.app.exit();
            }

        });

        table.setColor(1,0,0,1);
        table.padTop(50);
        table.add(image).width(Gdx.graphics.getWidth()-150).height(376);
        table.row();
        table.add(btn1).padBottom(50).width(Gdx.graphics.getWidth()/2).height(100);
        table.row();
        table.add(btn2).padBottom(50).width(Gdx.graphics.getWidth()/2).height(100);

        stage.addActor(table);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 05.f, 0.555f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Texture t=new Texture(Gdx.files.internal("mankomania.png"));
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
