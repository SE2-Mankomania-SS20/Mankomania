package com.mankomania.game.gamecore.notificationsystem;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.mankomania.game.core.network.messages.servertoclient.Notification;

public class Notifier implements Disposable {
    private final ShapeRenderer renderer;
    private final Array<Notification> notifications;
    private final SpriteBatch spriteBatch;
    private final Label label;
    private final Skin skin;

    public Notifier() {
        renderer = new ShapeRenderer();
        notifications = new Array<>();
        spriteBatch = new SpriteBatch();
        skin = new Skin(Gdx.files.internal("skin/terra-mother-ui.json"));
        skin.getFont("font").getData().setScale(3, 3);

        label = new Label(null, skin);
        label.setAlignment(Align.center);
        label.setPosition(Gdx.graphics.getWidth() / 2.8f, Gdx.graphics.getHeight() / 4f);
    }

    public void add(Notification notification) {
        //adapt string to insert new lines if necessary
        notification.setText(checkForNewLines(notification.getText()));
        notifications.add(notification);
    }

    public void render(float delta) {
        if (notifications.size > 0) {
            Notification notification = notifications.get(0);
            notification.updateTime(delta);
            if (notification.getTimeToLive() > 0) {
                renderer.setColor(notification.getBgColor());

                label.setText(notification.getText());
                label.setColor(notification.getFontcolor());

                renderer.begin(ShapeRenderer.ShapeType.Filled);
                renderer.rect(label.getX() - label.getPrefWidth() / 2f - 10f, label.getY() - label.getPrefHeight() / 2f,
                        label.getPrefWidth() + 20f, label.getPrefHeight());
                renderer.end();

                spriteBatch.begin();
                label.draw(spriteBatch, 1f);
                spriteBatch.end();
            } else {
                notifications.removeIndex(0);
            }
        }
    }

    @Override
    public void dispose() {
        renderer.dispose();
        spriteBatch.dispose();
    }

    private String checkForNewLines(String text) {
        StringBuilder sb = new StringBuilder(text);

        int i = 0;
        while ((i = sb.indexOf(" ", i + 28)) != -1) {
            sb.replace(i, i + 1, "\n");
        }

        return sb.toString();
    }
}
