package com.mankomania.game.gamecore.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

public class RouletteMinigameScreen extends AbstractScreen {
    private Stage stage;
    private Skin skin, skin1, skin2;
    private Texture texturePointer,textureWheel;
    private Image imagePointer, imageWheel;
    private Label einsatz1, einsatz2,einsatz3;
    private TextField textfieldEnteredNumber, textfieldInputPlayer, textfieldResultWheel, textfieldViewLostWin;
    private Button buttonCheckedInput;
    private TextButton button1, button2, button3, button4, button5, spinButton;
    private Table tableMain, table1, table2;
    private boolean tb1, tb2, tb3, tb4 ,tb5 = false;
    private int bet;
    private String [] arrayColor = {"rot","schwarz","rot","schwarz","rot","schwarz","rot","schwarz","rot","schwarz","rot","schwarz","rot","schwarz","rot","schwarz","rot","schwarz","rot","schwarz","rot","schwarz","rot","schwarz","rot","schwarz","rot","schwarz","rot","schwarz","rot","schwarz","rot","schwarz","rot","schwarz"};
    private int [] arrayNumberWheel = {32,15,19,4,21,2,25,17,34,6,27,13,36,11,30,8,23,10,5,24,16,33,1,20,14,31,9,22,18,29,7,28,12,35,3,26};
    private String color;


    public RouletteMinigameScreen () {
        //set skin
        skin = new Skin(Gdx.files.internal("skin/terra-mother-ui.json"));
        skin1 = new Skin(Gdx.files.internal("skin/uiskin.json"));
        skin2 = new Skin(Gdx.files.internal("skin/terra-mother-ui.json"));

        //set font size of skin
        skin.getFont("font").getData().setScale(5, 5);
        skin1.getFont("default-font").getData().setScale(3, 3);
        skin2.getFont("font").getData().setScale(3, 3);

        //image for pointer and wheel
        texturePointer = new Texture(Gdx.files.internal("Minigame_Roulette/PointerRoulette.png"));
        imagePointer = new Image(texturePointer);
        textureWheel = new Texture(Gdx.files.internal("Minigame_Roulette/WheelRoulette.png"));
        imageWheel = new Image(textureWheel);

        //1.Einsatz 5000€ - Gewinn 150000€ + Textfield
        einsatz1 = new Label("1.Einsatz 5.000 €- Gewinn 150.000 €", skin, "black");
        textfieldEnteredNumber = new TextField("0", skin2, "black");
        textfieldEnteredNumber.setColor(Color.BLACK);
        textfieldEnteredNumber.setAlignment(Align.left); //Text in center
        buttonCheckedInput = new Button(skin1);

        //2.Einsatz 20000€ - Gewinn 100000€ + Checkbox
        einsatz2 = new Label("2.Einsatz 20.000 €- Gewinn 100.000 €", skin, "black");
        button1 = new TextButton("1-12", skin1,"default");
        button2 = new TextButton("13-24", skin1,"default");
        button3 = new TextButton("25-36", skin1,"default");

        //3.Einsatz 50000€ - Gewinn 80000€ + Checkbox
        einsatz3 = new Label("3.Einsatz 50.000 €- Gewinn 80.000 €", skin, "black");
        button4 = new TextButton("Rot", skin1,"default");
        button5 = new TextButton("Schwarz", skin1,"default");

        //input from player viewed in a textfield
        textfieldInputPlayer = new TextField("", skin2, "black");
        textfieldInputPlayer.setColor(Color.BLACK);
        textfieldInputPlayer.setAlignment(Align.center);

        //result viewed in a textfield
        textfieldResultWheel = new TextField("", skin2, "black");
        textfieldResultWheel.setColor(Color.BLACK);
        textfieldResultWheel.setAlignment(Align.center);

        //win/lost viewed in a textfield
        textfieldViewLostWin = new TextField("", skin2, "black");
        textfieldViewLostWin.setColor(Color.BLACK);
        textfieldViewLostWin.setAlignment(Align.center);

        //textbutton for "spin"
        spinButton = new TextButton("SPIN", skin2, "default");

        //table1 setWidth, setHeight
        stage = new Stage();
        table1 = new Table();
        table1.setFillParent(false);
        table1.setWidth(Gdx.graphics.getWidth());
        table1.setHeight(Gdx.graphics.getHeight());

        //table2 setWidth, setHeight
        table2 = new Table();
        table2.setFillParent(false);
        table2.setWidth(Gdx.graphics.getWidth());
        table2.setHeight(Gdx.graphics.getHeight());

        //add elements to the table1
        table1.add(imagePointer).width(150).height(150).padTop(50);
        table1.row();
        table1.add(imageWheel).width(Gdx.graphics.getWidth()/2f-200).height(Gdx.graphics.getWidth()/2f-200);

        //add elements to the table2
        table2.add(einsatz1).width(800).height(120).padTop(50);
        table2.row();
        table2.add(textfieldEnteredNumber).width(700).height(120);
        table2.add(buttonCheckedInput).padRight(380).width(100).height(100);
        table2.row();
        table2.add(einsatz2).width(800).height(120);
        table2.row();
        table2.add(button1).width(250).height(120);
        table2.row();
        table2.add(button2).width(250).height(120);
        table2.row();
        table2.add(button3).width(250).height(120);
        table2.row();
        table2.add(einsatz3).width(800).height(120);
        table2.row();
        table2.add(button4).width(400).height(120);
        table2.add(button5).width(400).height(120);
        table2.row();
        table2.add(textfieldInputPlayer).width(400).height(100);
        table2.add(textfieldResultWheel).width(400).height(100);
        table2.row();
        table2.add(spinButton).width(500).height(100);
        table2.add(textfieldViewLostWin).width(400).height(100);
        table2.row();

        //join table1 and table2 to tableMain
        tableMain = new Table();
        tableMain.setFillParent(true);
        tableMain.add(table1);
        tableMain.add(table2);

        Gdx.input.setInputProcessor(stage);

        textfieldEnteredNumber.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent e, float x, float y, int point, int button) {
                textfieldEnteredNumber.setText("");
                textfieldEnteredNumber.setColor(Color.RED);
                button1.setColor(Color.GRAY);
                button2.setColor(Color.GRAY);
                button3.setColor(Color.GRAY);
                button4.setColor(Color.GRAY);
                button5.setColor(Color.GRAY);
            }
        });

        buttonCheckedInput.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                bet = 1;
                String eingabe = textfieldEnteredNumber.getText();
                int zahl1_36 = Integer.parseInt(eingabe);
                try {
                    if (zahl1_36 >= 1 && zahl1_36 <= 36) {
                        textfieldInputPlayer.setText(textfieldEnteredNumber.getText());
                    } else {
                        textfieldEnteredNumber.setText("Number 1-36");
                    }
                } catch (Exception ex) {
                    textfieldEnteredNumber.setText("Not a Number");
                }
            }
        });

        spinButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //generate random value for spin
                int number = (int)(Math.random()*36 +1);
                String numberInString = String.valueOf(number);
                textfieldResultWheel.setText(numberInString + ", " + arrayColor[findColor(number)]);
                resultRoulette(number,bet);
            }
        });

        //if player choose button 1 the other are disabled
        button1.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                bet = 2;
                tb1 = true;
                tb2 = false;
                tb3 = false;
                tb4 = false;
                tb5 = false;
                button1.setColor(Color.RED);
                button2.setColor(Color.GRAY);
                button3.setColor(Color.GRAY);
                button4.setColor(Color.GRAY);
                button5.setColor(Color.GRAY);
                choosenField();
            }
        });

        button2.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                bet = 3;
                tb1 = false;
                tb2 = true;
                tb3 = false;
                tb4 = false;
                tb5 = false;
                button2.setColor(Color.RED);
                button1.setColor(Color.GRAY);
                button3.setColor(Color.GRAY);
                button4.setColor(Color.GRAY);
                button5.setColor(Color.GRAY);
                choosenField();
            }
        });

        button3.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                bet = 4;
                tb1 = false;
                tb2 = false;
                tb3 = true;
                tb4 = false;
                tb5 = false;
                button3.setColor(Color.RED);
                button1.setColor(Color.GRAY);
                button2.setColor(Color.GRAY);
                button4.setColor(Color.GRAY);
                button5.setColor(Color.GRAY);
                choosenField();
            }
        });

        button4.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                bet = 5;
                color = "rot";
                tb1 = false;
                tb2 = false;
                tb3 = false;
                tb4 = true;
                tb5 = false;
                button3.setColor(Color.GRAY);
                button1.setColor(Color.GRAY);
                button2.setColor(Color.GRAY);
                button4.setColor(Color.RED);
                button5.setColor(Color.GRAY);
                choosenField();
            }
        });

        button5.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                color = "schwarz";
                bet = 6;
                tb1 = false;
                tb2 = false;
                tb3 = false;
                tb4 = false;
                tb5 = true;
                button3.setColor(Color.GRAY);
                button1.setColor(Color.GRAY);
                button2.setColor(Color.GRAY);
                button4.setColor(Color.GRAY);
                button5.setColor(Color.RED);
                choosenField();
            }
        });
        stage.addActor(tableMain);

    }

    public void choosenField () {
        String rouletteValue = "";
        if (tb1 == true) {
            rouletteValue += "1-12";
        }
        else if (tb2 ==true) {
            rouletteValue += "13-24";
        }
        else if (tb3 ==true) {
            rouletteValue += "25-36";
        }
        else if (tb4 == true) {
            rouletteValue += "Rot";
        }
        else if(tb5 == true)  {
            rouletteValue += "Schwarz";
        }
        textfieldInputPlayer.setText(rouletteValue);
    }

    public void resultRoulette (int random, int bet) {
        String eingabe = textfieldEnteredNumber.getText();
        int zahl1_36 = Integer.parseInt(eingabe);

        if (bet == 1) {
            if (random == zahl1_36) {
                textfieldViewLostWin.setText("Gewonnen");
            } else {
                textfieldViewLostWin.setText("Verloren");
            }
        } else if (bet == 2) {
            if (random >= 1 && random <=12) {
                textfieldViewLostWin.setText("Gewonnen");
            } else {
                textfieldViewLostWin.setText("Verloren");
            }
        }else if (bet == 3) {
            if (random >= 13 && random <=24) {
                textfieldViewLostWin.setText("Gewonnen");
            } else {
                textfieldViewLostWin.setText("Verloren");
            }
        }else if (bet == 4) {
            if (random >= 25 && random <= 36) {
                textfieldViewLostWin.setText("Gewonnen");
            } else {
                textfieldViewLostWin.setText("Verloren");
            }
        }else if (bet == 5) {
            String foundcolor = arrayColor[findColor(random)];
            if (foundcolor.equals(color)) {
                textfieldViewLostWin.setText("Gewonnen");
            }else {
                textfieldViewLostWin.setText("Verloren");
            }
        }
        else if (bet == 6) {
            String foundcolor = arrayColor[findColor(random)];
            if (foundcolor.equals(color)) {
                textfieldViewLostWin.setText("Gewonnen");
            }else {
                textfieldViewLostWin.setText("Verloren");
            }
        }
    }

    public int findColor (int random) {
        int counter = 0;
        for (int i = 0; i < arrayNumberWheel.length -1; i++) {
            if (random == arrayNumberWheel[i]) {
                return counter;
            } else {
                counter++;
            }
        }
        return counter;
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        stage.act(delta);
        stage.draw();

    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
    }
}
