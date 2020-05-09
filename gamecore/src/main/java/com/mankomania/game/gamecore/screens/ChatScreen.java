package com.mankomania.game.gamecore.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.badlogic.gdx.utils.Align;
import com.mankomania.game.core.network.messages.ChatMessage;
import com.mankomania.game.gamecore.client.ClientChat;
import com.mankomania.game.gamecore.client.NetworkClient;
import com.mankomania.game.gamecore.util.ScreenEnum;

import com.mankomania.game.gamecore.util.ScreenManager;


/*********************************
 Created by Fabian Oraze on 17.04.20
 *********************************/

public class ChatScreen extends AbstractScreen {

    Stage stage;
    TextField textField;
    TextButton sendButton;
    TextButton backButton;
    Skin skin;
    NetworkClient client;
    Table table;
    Image back;
    Label chatLabel;
    ScreenEnum screenEnum;
    public ChatScreen(NetworkClient client, ScreenEnum lastscreen) {

        this.client = client;
        stage = new Stage();
        table = new Table();
        table.setFillParent(true);
        back = new Image();
        this.screenEnum =lastscreen;

        skin = new Skin(Gdx.files.internal("skin/terra-mother-ui.json"));
        skin.getFont("font").getData().setScale(3, 3);

        chatLabel = new Label("", skin, "chat");
        chatLabel.setPosition(60, 100);
        chatLabel.setSize(Gdx.graphics.getWidth() - 140, 780);
        chatLabel.setAlignment(Align.topLeft);

        table.setBackground(new TiledDrawable(skin.getTiledDrawable("tile-a")));

        textField = new TextField("Enter chat", skin, "black");
        textField.setColor(Color.BLACK);
        textField.setPosition(60, Gdx.graphics.getHeight() - 180);
        textField.setSize(Gdx.graphics.getWidth() - 600, 140);

        sendButton = new TextButton("Send", skin);
        sendButton.setPosition(Gdx.graphics.getWidth() - 540, Gdx.graphics.getHeight() - 180);
        sendButton.setSize(220, 140);


        backButton = new TextButton("Back", skin);
        backButton.setPosition(Gdx.graphics.getWidth() - 300, Gdx.graphics.getHeight() - 180);
        backButton.setSize(220, 140);

        Gdx.input.setInputProcessor(stage);

        textField.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent e, float x, float y, int point, int button) {
                textField.setText("");
            }
        });

        sendButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                sendMsgToServer(textField.getText());
                textField.setText("");
            }
        });

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                ScreenManager.getInstance().switchScreen(lastscreen);
            }
        });

        stage.addActor(table);
        stage.addActor(chatLabel);
        stage.addActor(textField);
        stage.addActor(backButton);
        stage.addActor(sendButton);

    }


    public void draw() {
        chatLabel.setText(ClientChat.getText());
    }

    public void sendMsgToServer(String msg) {
        client.sendMsgToServer(new ChatMessage(msg));
    }


    @Override
    public void render(float delta) {
        super.render(delta);
        draw();
        stage.act(delta);
        stage.draw();

    }


}
