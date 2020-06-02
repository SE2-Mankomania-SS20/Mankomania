package com.mankomania.game.gamecore.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;
import com.mankomania.game.core.network.messages.servertoclient.roulette.RouletteResultMessage;
import com.mankomania.game.gamecore.MankomaniaGame;
import com.mankomania.game.gamecore.client.RouletteClient;
import com.mankomania.game.gamecore.minigames.RouletteResultOfPlayers;
import com.mankomania.game.gamecore.util.Screen;
import com.mankomania.game.gamecore.util.ScreenManager;

import java.util.ArrayList;

public class RouletteMiniGameScreen extends AbstractScreen {

    private RouletteClient rouletteClient = RouletteClient.getInstance();
    private Stage stage;
    private Skin skin;
    private Skin skin1;
    private Texture texturePointer;
    private Texture textureWheel;
    private Image imagePointer;
    private Image imageWheel;
    private Label bet1;
    private Label bet2;
    private Label bet3;
    private TextField textFieldEnteredNumber;
    private TextField textFieldInputPlayer;
    private TextField textFieldResultWheel;
    private TextButton textButton1;
    private TextButton textButton2;
    private TextButton textButton3;
    private TextButton textButton4;
    private TextButton textButton5;
    private TextButton textButtonReady;
    private TextButton textButtonCheck;
    private Table tableMain;
    private Table table1;
    private Table table2;
    private boolean tb1;
    private boolean tb2;
    private boolean tb3;
    private boolean tb4;
    private boolean tb5;
    private boolean tb6;
    private int bettingOpportunity;
    private RouletteResultOfPlayers rouletteResultOfPlayers = new RouletteResultOfPlayers();
    private Dialog dialogWaitingResult;
    private final String blackString = "black";
    private final String defaultString = "default";
    private final String enterNumberString = "Enter 1-36";

    public RouletteMiniGameScreen() {
        //set skin
        skin = new Skin(Gdx.files.internal("skin/terra-mother-ui.json"));
        skin1 = new Skin(Gdx.files.internal("skin/uiskin.json"));

        //set font size of skin
        skin.getFont("font").getData().setScale(3, 3);
        skin1.getFont("default-font").getData().setScale(3, 3);

        //image for pointer and wheel
        texturePointer = new Texture(Gdx.files.internal("Minigame_Roulette/PointerRoulette.png"));
        imagePointer = new Image(texturePointer);
        textureWheel = new Texture(Gdx.files.internal("Minigame_Roulette/WheelRoulette.png"));
        imageWheel = new Image(textureWheel);

        //1.Einsatz 5000€ - Gewinn 150000€ + Textfield
        bet1 = new Label("1. BET 5.000 \u20AC - WIN 150.000 \u20AC", skin, blackString);
        textFieldEnteredNumber = new TextField(enterNumberString, skin, blackString);
        textFieldEnteredNumber.setColor(Color.BLACK);
        textFieldEnteredNumber.setAlignment(Align.left); //Text in center
        textButtonCheck = new TextButton("Check", skin1, defaultString);

        //2.Einsatz 20000€ - Gewinn 100000€ + Checkbox
        bet2 = new Label("2. BET 20.000 \u20AC - WIN 100.000 \u20AC", skin, blackString);
        textButton1 = new TextButton("1-12", skin1, defaultString);
        textButton2 = new TextButton("13-24", skin1, defaultString);
        textButton3 = new TextButton("25-36", skin1, defaultString);

        //3.Einsatz 50000€ - Gewinn 80000€ + Checkbox
        bet3 = new Label("3. BET 50.000 \u20AC - WIN 80.000 \u20AC", skin, blackString);
        textButton4 = new TextButton("RED", skin1, defaultString);
        textButton5 = new TextButton("BLACK", skin1, defaultString);

        //input from player viewed in a textfield
        textFieldInputPlayer = new TextField("", skin, blackString);
        textFieldInputPlayer.setColor(Color.BLACK);
        textFieldInputPlayer.setAlignment(Align.center);

        //result viewed in a textfield
        textFieldResultWheel = new TextField("", skin, blackString);
        textFieldResultWheel.setColor(Color.BLACK);
        textFieldResultWheel.setAlignment(Align.center);

        textButtonReady = new TextButton("READY", skin, defaultString);

        stage = new Stage();
        table1 = new Table();
        table1.setFillParent(false);
        table1.setWidth(Gdx.graphics.getWidth());
        table1.setHeight(Gdx.graphics.getHeight());

        table2 = new Table();
        table2.setFillParent(false);
        table2.setWidth(Gdx.graphics.getWidth());
        table2.setHeight(Gdx.graphics.getHeight());

        table1.add(imagePointer).width(50).height(50).padTop(50).padLeft(200);
        table1.row();
        table1.add(imageWheel).width(Gdx.graphics.getWidth() / 2f - 400).height(Gdx.graphics.getWidth() / 2f - 400).padLeft(200);
        table1.row();

        table2.add(bet1).width(800).height(100).padTop(50);
        table2.row();
        table2.add(textFieldEnteredNumber).width(700).height(100);
        table2.add(textButtonCheck).padRight(380).width(200).height(100);
        table2.row();
        table2.add(bet2).width(800).height(100);
        table2.row();
        table2.add(textButton1).width(250).height(100);
        table2.row();
        table2.add(textButton2).width(250).height(100);
        table2.row();
        table2.add(textButton3).width(250).height(100);
        table2.row();
        table2.add(bet3).width(800).height(100);
        table2.row();
        table2.add(textButton4).width(250).height(100);
        table2.add(textButton5).width(250).height(100).padRight(380);
        table2.row();
        table2.add(textFieldInputPlayer).width(250).height(100);
        table2.add(textFieldResultWheel).width(250).height(100).padRight(380);
        table2.row();
        table2.add(textButtonReady).width(500).height(100);
        table2.row();


        //join table1 and table2 to tableMain
        tableMain = new Table();
        tableMain.setFillParent(true);
        tableMain.add(table1);
        tableMain.add(table2);
        tableMain.setBackground(new TiledDrawable(skin.getTiledDrawable("tile-a")));
        tableMain.setWidth(stage.getWidth());

        tb1 = false;
        tb2 = false;
        tb3 = false;
        tb4 = false;
        tb5 = false;
        tb6 = false;

        Gdx.input.setInputProcessor(stage);

        textFieldEnteredNumber.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent e, float x, float y, int point, int button) {
                textFieldEnteredNumber.setText("");
                textButton1.setColor(Color.GRAY);
                textButton2.setColor(Color.GRAY);
                textButton3.setColor(Color.GRAY);
                textButton4.setColor(Color.GRAY);
                textButton5.setColor(Color.GRAY);
            }
        });

        // update ui callback
        rouletteClient.setCallback(() -> {
            updateUI();
        });

        textButtonReady.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                int amountBet;
                switch (bettingOpportunity) {
                    case 1: amountBet = 5000; break;
                    case 2: amountBet = 20000; break;
                    case 3: amountBet = 20000; break;
                    case 4: amountBet = 20000; break;
                    case 5: amountBet = 50000; break;
                    case 6: amountBet = 50000; break;
                    default: amountBet = 0; break;
                }

                if (textFieldInputPlayer.getText().equals("")) {
                    textFieldInputPlayer.setText("no bet");

                } else {
                    float delayInSeconds = 2;
                    Timer.schedule(new Timer.Task() {
                        @Override
                        public void run() {
                            Skin uiSkin = new Skin(Gdx.files.internal("skin/uiskin.json"));
                            uiSkin.getFont("default-font").getData().setScale(6, 6);
                            dialogWaitingResult = new Dialog("waiting for other players..", uiSkin, "dialog") {
                                public void result(Object obj) {
                                    System.out.println("result " + obj);
                                }
                            };
                            dialogWaitingResult.show(stage);
                        }
                    }, delayInSeconds);

                }

                textButtonReady.setVisible(false);

                int betOptions = chooseBetButton();
                //sendRouletteStackMessage -> client input send to server
                MankomaniaGame.getMankomaniaGame().getNetworkClient().getMessageHandler().sendRouletteStackMessage(betOptions, amountBet);

                /*
                Spieler der Einsätze gesetzt hat, soll einen Dialog am Bildschirm erhalten. Bis alle Ergebnisse am Client sind.
                 */


            }
        });

        textButtonCheck.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                String enteredRouletteNumber = textFieldEnteredNumber.getText();

                    if (isNumeric(enteredRouletteNumber)) {
                        int numberInString = Integer.parseInt(enteredRouletteNumber);
                        if (numberInString >= 1 && numberInString <= 36) {
                            textFieldInputPlayer.setText(textFieldEnteredNumber.getText());
                            bettingOpportunity = 1;
                            tb1 = true;
                        } else {
                            textFieldEnteredNumber.setText(enterNumberString);
                        }
                    }else {
                        textFieldEnteredNumber.setText(enterNumberString);
                    }

            }
        });

        //if player choose button 1 the other are disabled
        textButton1.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                bettingOpportunity = 2;
                tb1 = false;
                tb2 = true;
                tb3 = false;
                tb4 = false;
                tb5 = false;
                tb6 = false;
                textButton1.setColor(Color.RED);
                textButton2.setColor(Color.GRAY);
                textButton3.setColor(Color.GRAY);
                textButton4.setColor(Color.GRAY);
                textButton5.setColor(Color.GRAY);
                textFieldInputPlayer.setText("1-12");

            }
        });

        textButton2.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                bettingOpportunity = 3;
                tb1 = false;
                tb2 = false;
                tb3 = true;
                tb4 = false;
                tb5 = false;
                tb6 = false;
                textButton1.setColor(Color.GRAY);
                textButton2.setColor(Color.RED);
                textButton3.setColor(Color.GRAY);
                textButton4.setColor(Color.GRAY);
                textButton5.setColor(Color.GRAY);
                textFieldInputPlayer.setText("13-24");
            }
        });

        textButton3.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                bettingOpportunity = 4;
                tb1 = false;
                tb2 = false;
                tb3 = false;
                tb4 = true;
                tb5 = false;
                tb6 = false;
                textButton1.setColor(Color.GRAY);
                textButton2.setColor(Color.GRAY);
                textButton3.setColor(Color.RED);
                textButton4.setColor(Color.GRAY);
                textButton5.setColor(Color.GRAY);
                textFieldInputPlayer.setText("25-36");
            }
        });

        textButton4.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                bettingOpportunity = 5;
                tb1 = false;
                tb2 = false;
                tb3 = false;
                tb4 = false;
                tb5 = true;
                tb6 = false;
                textButton1.setColor(Color.GRAY);
                textButton2.setColor(Color.GRAY);
                textButton3.setColor(Color.GRAY);
                textButton4.setColor(Color.RED);
                textButton5.setColor(Color.GRAY);
                textFieldInputPlayer.setText("red");
            }
        });

        textButton5.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                bettingOpportunity = 6;
                tb1 = false;
                tb2 = false;
                tb3 = false;
                tb4 = false;
                tb5 = false;
                tb6 = true;
                textButton1.setColor(Color.GRAY);
                textButton2.setColor(Color.GRAY);
                textButton3.setColor(Color.GRAY);
                textButton4.setColor(Color.GRAY);
                textButton5.setColor(Color.RED);
                textFieldInputPlayer.setText("black");
            }
        });
        stage.addActor(tableMain);
    }

    public boolean isNumeric(String strNum) {

        if (strNum == null) {
            return false;
        }else if((strNum.matches("[0-9]+") && strNum.length() <= 2)){
            return true;
        }else{
            return false;
        }
    }

    public int chooseBetButton() {
        int rouletteValue = 0;
        if (tb1) {
            String enteredNumberInTextField = textFieldEnteredNumber.getText();
            int numberInInt = Integer.parseInt(enteredNumberInTextField);
            rouletteValue = numberInInt;
        } else if (tb2) {
            rouletteValue = 37;
        } else if (tb3) {
            rouletteValue = 38;
        } else if (tb4) {
            rouletteValue = 39;
        } else if (tb5) {
            rouletteValue = 40;
        } else if (tb6) {
            rouletteValue = 41;
        }
        return rouletteValue;
    }

    public void updateUI() {

        // set result field
        textFieldResultWheel.setText(MankomaniaGame.getMankomaniaGame().getGameData().getArrayPlayerInformation().get(0).getResultOfRouletteWheel());

        ArrayList<RouletteResultMessage> results = MankomaniaGame.getMankomaniaGame().getGameData().getArrayPlayerInformation();

        float delayInSeconds = 10;
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                dialogWaitingResult.hide();
                tableMain.clear();
                Table tabelResult = rouletteResultOfPlayers.createRouletteResultOfPlayers(results);
                tableMain.add(tabelResult);

            }
        }, delayInSeconds);

        float delayInSecondsTable = 13;
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                ScreenManager.getInstance().switchScreen(Screen.MAIN_GAME);
            }
        }, delayInSecondsTable);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        stage.act(delta);
        stage.draw();

    }

}
