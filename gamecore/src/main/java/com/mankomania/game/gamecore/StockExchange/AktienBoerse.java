package com.mankomania.game.gamecore.StockExchange;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;
import com.esotericsoftware.minlog.Log;
import com.mankomania.game.gamecore.MankomaniaGame;
import com.mankomania.game.gamecore.screens.AbstractScreen;
import com.mankomania.game.gamecore.util.Screen;
import com.mankomania.game.gamecore.util.ScreenManager;

public class AktienBoerse extends AbstractScreen {
    private Stage stage;
    private Table table;
    private Image walze_image;
    private Texture walze;
    private Label resultat;
    private Label text;
    private Skin skin;
    private Label roll_text;
    private Label text2;
    private Label text3;
    private Label text4;
    private int inputCount = 0;

    public AktienBoerse() {
        skin = new Skin(Gdx.files.internal("skin/terra-mother-ui.json"));
        stage = new Stage();
        table = new Table();
        table.setFillParent(true);
        table.setBackground(new TiledDrawable(skin.getTiledDrawable("tile-a")));
        table.setSize(stage.getWidth(), stage.getHeight());
        table.align(Align.center | Align.top);

        Gdx.input.setInputProcessor(stage);
        skin.getFont("font").getData().setScale(4, 4);

        walze = new Texture(Gdx.files.internal("aktien/geld.png"));
        walze_image = new Image(walze);
        roll_text = new Label("Roll it!", skin, "default");
        resultat = new Label("Outcome:", skin, "default");
        table.add(roll_text).padTop(50);
        table.row();
        table.add(walze_image).padTop(50).width(Gdx.graphics.getWidth() / 2f);
        table.row();
        table.add(resultat).padTop(50);
        walze_image.setPosition(0, 0);
        stage.addActor(table);

        Gdx.input.setInputProcessor(new SwipeDetector(new SwipeDetector.DirectionListener() {

            @Override
            public void onUp() {
                if (inputCount == 0) {
                    result();
                    inputCount++;
                }
                Gdx.app.log("Input", "Up");
            }


            @Override
            public void onRight() {
                Gdx.app.log("Input", "Right (Methode soll nicht gecalled werden)");
            }

            @Override
            public void onLeft() {
                Gdx.app.log("Input", "Left (Methode soll nicht gecalled werden)");
            }

            @Override
            public void onDown() {
                if (inputCount == 0) {
                    result();
                    inputCount++;
                }
                Gdx.app.log("Input", "Down");
            }
        }));
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        stage.act(delta);
        stage.draw();
        super.renderNotifications(delta);
    }

    private void result() {

        int max = 6;
        int min = 1;
        int range = max - min + 1;
        int random = (int) (Math.random() * range) + min;
        text = new Label("", skin);
        text2 = new Label("", skin);
        text3 = new Label("", skin);
        text4 = new Label("", skin);
        table.clear();

        Texture walze = new Texture(Gdx.files.internal("aktien/geld_gewonnen_b.png"));
        walze_image = new Image(walze);
        roll_text = new Label("Roll it!", skin, "default");
        resultat = new Label("Outcome:", skin, "default");

        MankomaniaGame.getMankomaniaGame().getClient().getMessageHandler().sendStockResultMessage(random);
        if (random == 1 || random == 4) {
            text.setText("Jeder mit Bruchstahl Aktien");
        }
        if (random == 2 || random == 5) {
            text.setText("Jeder mit Kurzschluss Aktien");
        }
        if (random == 3 || random == 6) {
            text.setText("Jeder mit Trockenoel Aktien");
        }
        if (random == 1 || random == 2 || random == 3) {
            text2.setText("bekommt");
            text3.setText("+10.000");
            text4 = new Label("pro Aktie von der Bank", skin, "default");
        }
        if (random == 4 || random == 5 || random == 6) {
            text2.setText("verliert");
            text3 = new Label("-10.000", skin, "red");
            text4.setText("pro Aktie");
        }

        table.add(roll_text).padTop(50);
        table.row();
        table.add(walze_image).padTop(50).width(Gdx.graphics.getWidth() / 2f);
        table.row();
        table.add(resultat).padTop(50);
        table.row();
        table.add(text).padTop(25);
        table.row();
        table.add(text2);
        table.row();
        table.add(text3);
        table.row();
        table.add(text4);

        backToMainGame();
    }

    private void backToMainGame() {
        float delayInSeconds = 3f;
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                ScreenManager.getInstance().switchScreen(Screen.MAIN_GAME);
            }
        }, delayInSeconds);
    }
}
