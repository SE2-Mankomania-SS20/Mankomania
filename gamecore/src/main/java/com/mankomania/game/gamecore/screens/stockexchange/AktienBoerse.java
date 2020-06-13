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
import com.mankomania.game.core.data.GameData;
import com.mankomania.game.core.player.Stock;
import com.mankomania.game.gamecore.MankomaniaGame;
import com.mankomania.game.gamecore.screens.AbstractScreen;
import com.mankomania.game.gamecore.util.AssetPaths;

public class AktienBoerse extends AbstractScreen {
    private Stage stage;
    private Table table;
    private Image walzeimage;
    private Texture walze;
    private Label resultat;
    private Skin skin;
    private Label rolltext;
    private int inputCount = 0;
    String defaultStyle = "black";

    public AktienBoerse() {
        String input = "Input";

        skin = MankomaniaGame.getMankomaniaGame().getManager().get(AssetPaths.SKIN);
        stage = new Stage();
        table = new Table();
        table.setFillParent(true);
        table.setBackground(new TiledDrawable(skin.getTiledDrawable("tile-a")));
        table.setSize(stage.getWidth(), stage.getHeight());
        table.align(Align.center | Align.top);

        Gdx.input.setInputProcessor(stage);
        skin.getFont("font").getData().setScale(4, 4);

        walze = (MankomaniaGame.getMankomaniaGame().getManager().get(AssetPaths.GELD_PNG));
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
        if (MankomaniaGame.getMankomaniaGame().isLocalPlayerTurn()) {
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
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        stage.act(delta);
        stage.draw();
        update();
        super.renderNotifications(delta);
    }

    private void result() {
        int max = 6;
        int min = 1;
        int range = max - min + 1;
        int random = (int) (Math.random() * range) + min;
        MankomaniaGame.getMankomaniaGame().getNetworkClient().getMessageHandler().sendStockResultMessage(random);
    }

    public void update() {
        GameData refGameData = MankomaniaGame.getMankomaniaGame().getGameData();
        if (refGameData.getAktienBoerseData().isNeedUpdate()) {
            refGameData.getAktienBoerseData().setNeedUpdate(false);
            Stock stock = refGameData.getAktienBoerseData().getStock();
            boolean isRising = refGameData.getAktienBoerseData().isRising();
            Label text = new Label("", skin, defaultStyle);
            Label text2 = new Label("", skin, defaultStyle);
            Label text3 = new Label("", skin, defaultStyle);
            Label text4 = new Label("", skin, defaultStyle);
            table.clear();

            Texture walzeZwei = MankomaniaGame.getMankomaniaGame().getManager().get(AssetPaths.GELD_PNG);
            walzeimage = new Image(walzeZwei);
            rolltext = new Label("Roll it!", skin, defaultStyle);
            resultat = new Label("Outcome:", skin, defaultStyle);

            switch (stock) {
                case BRUCHSTAHLAG: {
                    text.setText("Jeder mit Bruchstahl Aktien");
                    if(isRising){
                        walzeZwei = MankomaniaGame.getMankomaniaGame().getManager().get(AssetPaths.GEWONNENB);
                    }else {
                        walzeZwei = MankomaniaGame.getMankomaniaGame().getManager().get(AssetPaths.VERLORENB);
                    }
                    break;
                }
                case KURZSCHLUSSAG: {
                    text.setText("Jeder mit Kurzschluss Aktien");
                    if(isRising){
                        walzeZwei = MankomaniaGame.getMankomaniaGame().getManager().get(AssetPaths.GEWONNENK);
                    }else {
                        walzeZwei = MankomaniaGame.getMankomaniaGame().getManager().get(AssetPaths.VERLORENK);
                    }
                    break;
                }
                case TROCKENOEL: {
                    text.setText("Jeder mit Trockenoel Aktien");
                    if(isRising){
                        walzeZwei = MankomaniaGame.getMankomaniaGame().getManager().get(AssetPaths.GEWONNENT);
                    }else {
                        walzeZwei = MankomaniaGame.getMankomaniaGame().getManager().get(AssetPaths.VERLORENT);
                    }
                    break;
                }
            }

            if (isRising) {
                text2.setText("bekommt");
                text3.setText("+10.000");
                text4 = new Label("pro Aktie von der Bank", skin, defaultStyle);
            } else {
                text2.setText("verliert");
                text3 = new Label("-10.000", skin, "red");
                text4.setText("pro Aktie");
            }

            walzeimage = new Image(walzeZwei);

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
        }
    }
}
