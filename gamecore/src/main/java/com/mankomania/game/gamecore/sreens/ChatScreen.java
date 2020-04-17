package com.mankomania.game.gamecore.sreens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mankomania.game.gamecore.MankomaniaGame;

/*********************************
 Created by Fabian Oraze on 17.04.20
 *********************************/

public class ChatScreen extends ScreenAdapter {

    MankomaniaGame game;
    OrthographicCamera guiCam;
    Stage stage;
    TextField textField;
    TextButton sendButton;
    Skin skin;


    public ChatScreen(MankomaniaGame game) {
        this.game = game;
        stage = new Stage();
        skin = new Skin(Gdx.files.internal("skin/terra-mother-ui.json"));
        Gdx.input.setInputProcessor(stage);

        textField = new TextField("Chat", skin);
        textField.setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
        textField.setSize(200, 200);
        stage.addActor(textField);
        textField.addListener(new ClickListener() {
            public void touchUp(InputEvent e, float x, float y, int point, int button) {
                textField.setText("");
            }
        });

        sendButton = new TextButton("Send", skin);
        textField.setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2 - 40);
        textField.setSize(200, 200);
        stage.addActor(sendButton);
        sendButton.addListener(new ClickListener() {
            public void touchUp(InputEvent e, float x, float y, int point, int button) {
                //TODO: send message to server
                textField.setText("");
            }
        });


    }

    public void draw() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();

    }

}
