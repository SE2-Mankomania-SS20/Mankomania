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

public class AktienBörse extends AbstractScreen {
    private Stage stage;
    private Table table;
    private Image walze_image;
    private Texture walze;
    private Label resultat;
    private Label text;
    private Skin skin;
    private Label roll_text;
    public AktienBörse(){create();}
    public int inputCount=0;

    public void create(){
        skin = new Skin(Gdx.files.internal("skin/terra-mother-ui.json"));
        stage = new Stage();
        table = new Table();
        table.setFillParent(true);
        table.setBackground(new TiledDrawable(skin.getTiledDrawable("tile-a")));
        table.setSize(stage.getWidth(),stage.getHeight());
        table.align(Align.center | Align.top);

        Gdx.input.setInputProcessor(stage);
        skin.getFont("font").getData().setScale(4, 4);

        walze= new Texture(Gdx.files.internal("aktien/geld.png"));
        walze_image = new Image(walze);
        roll_text = new Label("Roll it!", skin, "default");
        resultat = new Label("Outcome:", skin, "default");
        table.add(roll_text).padTop(50);
        table.row();
        table.add(walze_image).padTop(50).width(Gdx.graphics.getWidth()/2);
        table.row();
        table.add(resultat).padTop(50);
        walze_image.setPosition(0,0);
        stage.addActor(table);

        Gdx.input.setInputProcessor(new SwipeDetector(new SwipeDetector.DirectionListener() {

            @Override
            public void onUp() {
                if (inputCount==0) {
                    result();
                    inputCount++;
                }
                Gdx.app.log("Input","Up");
            }


            @Override
            public void onRight() {
                Gdx.app.log("Input","Right (Methode soll nicht gecalled werden)");
            }

            @Override
            public void onLeft() {
                Gdx.app.log("Input","Left (Methode soll nicht gecalled werden)");
            }

            @Override
            public void onDown() {
                if (inputCount==0) {
                    result();
                    inputCount++;
                }
                Gdx.app.log("Input","Down");
            }
        }));
    }

    @Override
    public void render(float delta){
        super.render(delta);
        stage.act(delta);
        stage.draw();
        super.renderNotifications(delta);
    }

    public void result(){

        int max = 6;
        int min = 1;
        int range = max - min + 1;
        int random = (int) (Math.random() * range) + min;

        switch (random){
            case 1: {
                MankomaniaGame.getMankomaniaGame().getClient().getMessageHandler().sendStockResultMessage(1);
                table.clear();

                Texture walze= new Texture(Gdx.files.internal("aktien/geld_gewonnen_b.png"));
                walze_image = new Image(walze);
                roll_text = new Label("Roll it!", skin, "default");
                resultat = new Label("Outcome:", skin, "default");
                text=new Label("Jeder mit Bruchstahl Aktien ",skin,"default");
                Label text2=new Label("bekommt ",skin,"default");
                Label text3=new Label("+10.000",skin,"green");
                Label text4=new Label("pro Aktie von der Bank",skin,"default");
                table.add(roll_text).padTop(50);
                table.row();
                table.add(walze_image).padTop(50).width(Gdx.graphics.getWidth()/2);
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
                break;
            }
            case 2: {
                MankomaniaGame.getMankomaniaGame().getClient().getMessageHandler().sendStockResultMessage(2);
                table.clear();

                Texture walze= new Texture(Gdx.files.internal("aktien/geld_gewonnen_k.png"));
                walze_image = new Image(walze);
                roll_text = new Label("Roll it!", skin, "default");
                resultat = new Label("Outcome:", skin, "default");
                text=new Label("Jeder mit Kurzschluss Aktien", skin,"default");
                Label text2=new Label("bekommt ",skin,"default");
                Label text3=new Label("+10.000",skin,"green");
                Label text4=new Label("pro Aktie von der Bank",skin,"default");
                table.add(roll_text).padTop(50);
                table.row();
                table.add(walze_image).padTop(50).width(Gdx.graphics.getWidth()/2);
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
                break;
            }
            case 3: {
                MankomaniaGame.getMankomaniaGame().getClient().getMessageHandler().sendStockResultMessage(3);
                table.clear();

                Texture walze= new Texture(Gdx.files.internal("aktien/geld_gewonnen_t.png"));
                walze_image = new Image(walze);
                roll_text = new Label("Roll it!", skin, "default");
                resultat = new Label("Outcome:", skin, "default");
                text=new Label("Jeder mit Trockenoel Aktien", skin,"default");
                Label text2=new Label("bekommt ",skin,"default");
                Label text3=new Label("+10.000",skin,"green");
                Label text4=new Label("pro Aktie von der Bank",skin,"default");
                table.add(roll_text).padTop(50);
                table.row();
                table.add(walze_image).padTop(50).width(Gdx.graphics.getWidth()/2);
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
                break;
            }
            case 4: {
                MankomaniaGame.getMankomaniaGame().getClient().getMessageHandler().sendStockResultMessage(4);
                table.clear();

                Texture walze= new Texture(Gdx.files.internal("aktien/geld_verlieren_b.png"));
                walze_image = new Image(walze);
                roll_text = new Label("Roll it!", skin, "default");
                resultat = new Label("Outcome:", skin, "default");
                text=new Label("Jeder mit der Bruchstahl Aktien ", skin,"default");
                Label text2=new Label("verliert ",skin,"default");
                Label text3=new Label("-10.000",skin,"red");
                Label text4=new Label("pro Aktie",skin,"default");
                table.add(roll_text).padTop(50);
                table.row();
                table.add(walze_image).padTop(50).width(Gdx.graphics.getWidth()/2);
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
                break;
            }
            case 5: {
                MankomaniaGame.getMankomaniaGame().getClient().getMessageHandler().sendStockResultMessage(5);
                table.clear();

                Texture walze= new Texture(Gdx.files.internal("aktien/geld_verlieren_k.png"));
                walze_image = new Image(walze);
                roll_text = new Label("Roll it!", skin, "default");
                resultat = new Label("Outcome:", skin, "default");
                text=new Label("Jeder mit Kurzschluss Aktien", skin,"default");
                Label text2=new Label("verliert ",skin,"default");
                Label text3=new Label("-10.000",skin,"red");
                Label text4=new Label("pro Aktie",skin,"default");
                table.add(roll_text).padTop(50);
                table.row();
                table.add(walze_image).padTop(50).width(Gdx.graphics.getWidth()/2);
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
                break;
            }
            case 6: {
                MankomaniaGame.getMankomaniaGame().getClient().getMessageHandler().sendStockResultMessage(6);
                table.clear();

                Texture walze= new Texture(Gdx.files.internal("aktien/geld_verlieren_t.png"));
                walze_image = new Image(walze);
                roll_text = new Label("Roll it!", skin, "default");
                resultat = new Label("Outcome:", skin, "default");
                text=new Label("Jeder mit Trockenoel Aktien", skin,"default");
                Label text2=new Label("verliert ",skin,"default");
                Label text3=new Label("-10.000",skin,"red");
                Label text4=new Label("pro Aktie",skin,"default");
                table.add(roll_text).padTop(50);
                table.row();
                table.add(walze_image).padTop(50).width(Gdx.graphics.getWidth()/2);
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
                break;
            }
        }
    }
    public void backToMainGame(){
        float delayInSeconds = 3f;
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                ScreenManager.getInstance().switchScreen(Screen.MAIN_GAME);
            }
        }, delayInSeconds);
    }
}
