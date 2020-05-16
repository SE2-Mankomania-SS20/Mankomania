package com.mankomania.game.gamecore.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.Align;

public class RouletteMinigameScreen extends AbstractScreen {
    private Stage stage;
    private Skin skin, skin1, skin2;
    private Texture texturePointer,textureWheel;
    private Image imagePointer, imageWheel;
    private Label einsatz1, einsatz2,einsatz3;
    private TextField textfieldEnteredNumber, textfieldInputPlayer, textfieldResultWheel, textfieldViewLostWin;
    private Button buttonCheckedInput;
    private TextButton button1, button2, button3, button4, button5;

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





    }

    @Override
    public void render(float delta) {
        super.render(delta);
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
