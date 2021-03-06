package com.mankomania.game.gamecore.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.badlogic.gdx.utils.Align;
import com.mankomania.game.core.network.messages.clienttoserver.PlayerReady;
import com.mankomania.game.core.network.messages.servertoclient.Notification;
import com.mankomania.game.gamecore.MankomaniaGame;
import com.mankomania.game.gamecore.util.Screen;
import com.mankomania.game.gamecore.util.ScreenManager;
import com.mankomania.game.gamecore.util.AssetDescriptors;

public class LobbyScreen extends AbstractScreen {

    public static final String DEFAULT = "default";
    private final Stage stage;
    private final Table table;

    public LobbyScreen() {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        table = new Table();
        table.setFillParent(true);
        table.setWidth(stage.getWidth());
        table.align(Align.center | Align.top);
    }

    @Override
    public void show() {

        Skin skin = MankomaniaGame.getMankomaniaGame().getManager().get(AssetDescriptors.SKIN);
        table.setBackground(new TiledDrawable(skin.getTiledDrawable("tile-a")));
        skin.getFont("font").getData().setScale(5, 5);

        TextButton play = new TextButton("READY", skin, DEFAULT);
        TextButton back = new TextButton("BACK", skin, DEFAULT);
        TextButton chat = new TextButton("CHAT", skin, DEFAULT);

        table.padTop(300);
        table.add(play).padBottom(50).width(Gdx.graphics.getWidth() / 2f).height(100f);
        table.row().pad(10, 0, 10, 0);
        table.add(back).padBottom(50).width(Gdx.graphics.getWidth() / 2f).height(100f);
        table.row();
        table.add(chat).padBottom(50).width(Gdx.graphics.getWidth() / 2f).height(100f);

        play.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //send state to server
                MankomaniaGame.getMankomaniaGame().getNetworkClient().sendClientState(new PlayerReady());
            }
        });

        back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                MankomaniaGame.getMankomaniaGame().getNotifier().add(new Notification("client disconnected"));
                MankomaniaGame.getMankomaniaGame().getNetworkClient().disconnect();
                ScreenManager.getInstance().switchScreen(Screen.LAUNCH);
            }
        });
        chat.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                com.mankomania.game.gamecore.util.ScreenManager.getInstance().switchScreen(Screen.CHAT, Screen.LOBBY);
            }
        });

        stage.addActor(table);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        stage.act(delta);
        stage.draw();
        super.renderNotifications(delta);
    }
}
