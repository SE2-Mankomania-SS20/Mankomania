package com.mankomania.game.gamecore.sreens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.mankomania.game.core.network.ChatMessage;
import com.mankomania.game.gamecore.MankomaniaGame;
import com.mankomania.game.gamecore.client.NetworkClient;


/*********************************
 Created by Fabian Oraze on 17.04.20
 *********************************/

public class ChatScreen extends ScreenAdapter {

    MankomaniaGame game;
    Stage stage;
    TextField textField;
    TextButton sendButton;
    Skin skin;
    NetworkClient client;
    Table table;
    Image back;

    public ChatScreen(MankomaniaGame game, NetworkClient client) {

        this.game = game;
        this.client = client;
        stage = new Stage();
        table = new Table();
        table.setFillParent(true);
        back = new Image();

        skin = new Skin(Gdx.files.internal("skin/terra-mother-ui.json"));
        skin.getFont("font").getData().setScale(5, 5);


        table.setBackground(new TiledDrawable(skin.getTiledDrawable("tile-a")));


        textField = new TextField("Enter chat", skin, "black");
        textField.setColor(Color.BLACK);
        textField.setPosition(60, Gdx.graphics.getHeight() - 180);
        textField.setSize(Gdx.graphics.getWidth() - 100, 160);

        sendButton = new TextButton("Send", skin);
        sendButton.setPosition(60, 100);

        Gdx.input.setInputProcessor(stage);


        textField.addListener(new ClickListener() {
            public void clicked(InputEvent e, float x, float y) {
                textField.setText("");
            }
        });

        sendButton.addListener(new ClickListener() {
            public void clicked(InputEvent e, float x, float y) {
                sendMsgToServer(textField.getText());
            }
        });

        stage.addActor(table);
        stage.addActor(textField);
        stage.addActor(sendButton);

    }

    public void draw() {

    }

    public void sendMsgToServer(String msg) {
        client.sendMsgToServer(new ChatMessage(msg));
    }


    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        draw();
        stage.act(delta);
        stage.draw();

    }

}
