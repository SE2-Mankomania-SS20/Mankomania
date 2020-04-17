package com.mankomania.game.gamecore.sreens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
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


    public ChatScreen(MankomaniaGame game, NetworkClient client) {
        this.game = game;
        this.client = client;
        stage = new Stage();
        skin = new Skin(Gdx.files.internal("skin/terra-mother-ui.json"));
        skin.getFont("font").getData().setScale(3, 3);

        textField = new TextField("Enter chat", skin, "black");
        textField.setColor(Color.BLACK);
        textField.setPosition(40, Gdx.graphics.getHeight() - 100);
        textField.setSize(Gdx.graphics.getWidth() - 20, 50);

        sendButton = new TextButton("Send", skin, "black");
        sendButton.setPosition(40, 100);
        sendButton.background(skin.getDrawable("window-player-c"));

        Gdx.input.setInputProcessor(stage);


        stage.addActor(textField);
        stage.addActor(sendButton);

    }

    public void draw() {

        textField.addListener(new ClickListener() {
            public void touchUp(InputEvent e, float x, float y, int point, int button) {
                textField.setText("");
            }
        });

        sendButton.addListener(new ClickListener() {
            public void clicked(InputEvent e, float x, float y) {
                sendMsgToServer(textField.getText());
            }
        });

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
