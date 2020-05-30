package com.mankomania.game.gamecore.screens.stockexchange;

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
import com.mankomania.game.gamecore.MankomaniaGame;
import com.mankomania.game.gamecore.screens.AbstractScreen;
import com.mankomania.game.gamecore.util.AssetPaths;
import com.mankomania.game.gamecore.util.Screen;
import com.mankomania.game.gamecore.util.ScreenManager;

public class AktienBoerse extends AbstractScreen {
    private Stage stage;
    private Table table;
    private Image walzeimage;
    private Texture walze;
    private Label resultat;
    private Skin skin;
    private Label rolltext;
    private int inputCount = 0;
    String defaultStyle="default";

    public AktienBoerse() {
        String input="Input";

        skin = MankomaniaGame.getMankomaniaGame().getManager().get(AssetPaths.SKIN);
        stage = new Stage();
        table = new Table();
        table.setFillParent(true);
        table.setBackground(new TiledDrawable(skin.getTiledDrawable("tile-a")));
        table.setSize(stage.getWidth(), stage.getHeight());
        table.align(Align.center | Align.top);

        Gdx.input.setInputProcessor(stage);
        skin.getFont("font").getData().setScale(4, 4);

        walze = (MankomaniaGame.getMankomaniaGame().getManager().get(AssetPaths.GEWONNEN));
        walzeimage = new Image(walze);
        rolltext = new Label("Roll it!", skin, defaultStyle);
        resultat = new Label("Outcome:", skin, defaultStyle);
        table.add(rolltext).padTop(50);
        table.row();
        table.add(walzeimage).padTop(50).width(Gdx.graphics.getWidth() / 2f);
        table.row();
        table.add(resultat).padTop(50);
        walzeimage.setPosition(0, 0);
        stage.addActor(table);

        Gdx.input.setInputProcessor(new SwipeDetector(new SwipeDetector.DirectionListener() {

            @Override
            public void onUp() {
                if (inputCount == 0) {
                    result();
                    inputCount++;
                }
                Gdx.app.log(input, "Up");
            }


            @Override
            public void onRight() {
                Gdx.app.log(input, "Right (Methode soll nicht gecalled werden)");
            }

            @Override
            public void onLeft() {
                Gdx.app.log(input, "Left (Methode soll nicht gecalled werden)");
            }

            @Override
            public void onDown() {
                if (inputCount == 0) {
                    result();
                    inputCount++;
                }
                Gdx.app.log(input, "Down");
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
        Label text = new Label("", skin);
        Label text2 = new Label("", skin);
        Label text3 = new Label("", skin);
        Label text4 = new Label("", skin);
        table.clear();

        Texture walzeZwei = MankomaniaGame.getMankomaniaGame().getManager().get(AssetPaths.GEWONNENB);
        walzeimage = new Image(walzeZwei);
        rolltext = new Label("Roll it!", skin, defaultStyle);
        resultat = new Label("Outcome:", skin, defaultStyle);

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
            text4 = new Label("pro Aktie von der Bank", skin,  defaultStyle);
        }
        if (random == 4 || random == 5 || random == 6) {
            text2.setText("verliert");
            text3 = new Label("-10.000", skin, "red");
            text4.setText("pro Aktie");
        }

        table.add(rolltext).padTop(50);
        table.row();
        table.add(walzeimage).padTop(50).width(Gdx.graphics.getWidth() / 2f);
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
