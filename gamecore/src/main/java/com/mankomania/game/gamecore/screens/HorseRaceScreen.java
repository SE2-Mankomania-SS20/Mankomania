package com.mankomania.game.gamecore.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.mankomania.game.core.network.messages.servertoclient.Notification;
import com.mankomania.game.gamecore.MankomaniaGame;
import com.mankomania.game.gamecore.util.AssetPaths;

public class HorseRaceScreen extends AbstractScreen {
    private final Stage stage;

    private final Label winner;

    private final Label horse1Pl;
    private final Label horse1Bet;
    private final Label horse1Win;

    private final Label horse2Pl;
    private final Label horse2Bet;
    private final Label horse2Win;

    private final Label horse3Pl;
    private final Label horse3Bet;
    private final Label horse3Win;

    private final Label horse4Pl;
    private final Label horse4Bet;
    private final Label horse4Win;

    public HorseRaceScreen() {
        final String black = "black";
        final String dollar = "$";
        final String startMoney = "5000";
        final String player = "________";

        final float spaceBottom = 20f;
        final float btnSpace = 40f;
        final float dollarSpace = 60f;
        final float moneySpace = 20f;
        final float multSpace = 40f;
        final float winSpace = 40f;
        final float moneyWidth = 200f;
        final float playerNameWidth = 300f;

        stage = new Stage();

        Skin skin = MankomaniaGame.getMankomaniaGame().getManager().get(AssetPaths.SKIN);
        skin.getFont("font").getData().setScale(3, 3);
        skin.getFont("info").getData().setScale(2.5f, 2.5f);

        Skin skin2 = MankomaniaGame.getMankomaniaGame().getManager().get(AssetPaths.SKIN_2);

        Table background = new Table();
        background.setSkin(skin);
        background.setFillParent(true);
        background.setBackground(new TiledDrawable(skin.getTiledDrawable("tile-a")));

        background.columnDefaults(0).padRight(btnSpace).fill().spaceBottom(spaceBottom);
        background.columnDefaults(1).width(playerNameWidth).padRight(dollarSpace);
        background.columnDefaults(2).padRight(multSpace);
        background.columnDefaults(3).width(moneyWidth);
        background.columnDefaults(4).padRight(winSpace);
        background.columnDefaults(5).padRight(moneySpace);
        background.columnDefaults(6).width(moneyWidth);

        TextButton horse1Btn = new TextButton("Blitz", skin);
        horse1Btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                MankomaniaGame.getMankomaniaGame().getNotifier().add(new Notification("Blitz"));
            }
        });
        horse1Btn.pad(0f, 20f, 0f, 20f);

        TextButton horse2Btn = new TextButton("Bahnfrei", skin);
        horse2Btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                MankomaniaGame.getMankomaniaGame().getNotifier().add(new Notification("Bahnfrei"));
            }
        });
        horse2Btn.pad(0f, 20f, 0f, 20f);

        TextButton horse3Btn = new TextButton("Silberpfeil", skin);
        horse3Btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                MankomaniaGame.getMankomaniaGame().getNotifier().add(new Notification("Silberpfeil"));
            }
        });
        horse3Btn.pad(0f, 20f, 0f, 20f);

        TextButton horse4Btn = new TextButton("Donner", skin);
        horse4Btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                MankomaniaGame.getMankomaniaGame().getNotifier().add(new Notification("Donner"));
            }
        });
        horse4Btn.pad(0f, 20f, 0f, 20f);

        background.add("Horse:", black).padLeft(20f);

        background.add("Player", black).padLeft(20f);
        background.add();

        background.add("Bet:" , black).padLeft(20f);
        background.add();
        background.add();

        background.add("Win:", black).padLeft(20f);
        background.add();
        background.row();

        // first row
        background.add(horse1Btn);

        horse1Pl = new Label(player, skin, black);
        background.add(horse1Pl);

        background.add(dollar, black);

        horse1Bet = new Label(startMoney, skin, black);
        background.add(horse1Bet);

        background.add("2x", black);

        background.add(dollar, black);

        horse1Win = new Label("10000", skin, black);
        background.add(horse1Win);


        // second row
        background.row().spaceBottom(spaceBottom);

        background.add(horse2Btn);

        horse2Pl = new Label(player, skin, black);
        background.add(horse2Pl);

        background.add(dollar, black);

        horse2Bet = new Label(startMoney, skin, black);
        background.add(horse2Bet);

        background.add("3x", black);

        background.add(dollar, black);

        horse2Win = new Label("15000", skin, black);
        background.add(horse2Win);

        // third row
        background.row().spaceBottom(spaceBottom);

        background.add(horse3Btn);

        horse3Pl = new Label(player, skin, black);
        background.add(horse3Pl);

        background.add(dollar, black);

        horse3Bet = new Label(startMoney, skin, black);
        background.add(horse3Bet);

        background.add("4x", black);

        background.add(dollar, black);

        horse3Win = new Label("20000", skin, black);
        background.add(horse3Win);

        // fourth row
        background.row().spaceBottom(spaceBottom);

        background.add(horse4Btn);

        horse4Pl = new Label(player, skin, black);
        background.add(horse4Pl);

        background.add(dollar, black);

        horse4Bet = new Label(startMoney, skin, black);
        background.add(horse4Bet);

        background.add("5x", black);

        background.add(dollar, black);

        horse4Win = new Label("25000", skin, black);
        background.add(horse4Win);

        background.row().padTop(50f);


        background.add("Place your bet:",black).colspan(2);
        background.add();
        background.add();
        background.add();

        background.add("Winner:", black).colspan(3);
        background.add();


        background.row();

        Slider slider = new Slider(5000f, 50000f, 1, false, skin2);
        slider.getStyle().knob.setMinHeight(50f);
        slider.getStyle().knob.setMinWidth(30f);
        slider.getStyle().background.setMinHeight(20f);
        background.add(slider).colspan(2);

        Label money = new Label(startMoney, skin, black);

        slider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                money.setText(String.valueOf((int) slider.getValue()));
            }
        });

        background.add(dollar, black);

        background.add(money);
        background.add();

        winner = new Label("asdasdasdasdasdasdasdasd", skin, black);
        background.add(winner).colspan(3);

        // place table from top left
        background.padTop(30f);
        background.padLeft(30f);
        background.top().left();

        background.debugAll();

        stage.addActor(background);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        stage.draw();

        super.renderNotifications(delta);
    }

    public void setPlayerData(int playerIndex, int betAmount) {
        switch (playerIndex) {
            case 0: {
                horse1Pl.setText("Player 1");
                horse1Bet.setText(String.valueOf(betAmount));
                horse1Win.setText(String.valueOf(betAmount * 2));
                break;
            }
            case 1: {
                horse2Pl.setText("Player 2");
                horse2Bet.setText(String.valueOf(betAmount));
                horse2Win.setText(String.valueOf(betAmount * 3));
                break;
            }
            case 2: {
                horse3Pl.setText("Player 3");
                horse3Bet.setText(String.valueOf(betAmount));
                horse3Win.setText(String.valueOf(betAmount * 4));
                break;
            }
            case 3: {
                horse4Pl.setText("Player 4");
                horse4Bet.setText(String.valueOf(betAmount));
                horse4Win.setText(String.valueOf(betAmount * 5));
                break;
            }
            default:
                throw new IllegalStateException("Unexpected value: " + playerIndex);
        }
    }
}
