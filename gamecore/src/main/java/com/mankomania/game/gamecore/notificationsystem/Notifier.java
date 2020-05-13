package com.mankomania.game.gamecore.notificationsystem;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;

public class Notifier {
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
        label.setPosition(500, 200);

    }

    public void add(Notification notification) {
        notifications.add(notification);
    }

    public void render(float delta) {
        if (notifications.size > 0) {
            Notification notification = notifications.get(0);
            notification.updateTime(delta);
            if (notification.getTimeToLive() > 0) {
                renderer.setColor(notification.getBgColor());

                renderer.begin(ShapeRenderer.ShapeType.Filled);
                renderer.rect(500, 150, 800, 100);

                renderer.end();

                label.setText(notification.getText());
                label.setColor(notification.getFontcolor());

                spriteBatch.begin();
                label.draw(spriteBatch, 1f);
                spriteBatch.end();
            } else {
                notifications.removeIndex(0);
            }
        }
    }
}
