package com.mankomania.game.gamecore.screens.trickyone;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.mankomania.game.gamecore.screens.AbstractScreen;
import javafx.scene.control.Tab;

/*********************************
 Created by Fabian Oraze on 22.05.20
 *********************************/

public class TrickyOneScreen extends AbstractScreen {

    private Stage stage;
    private Skin skin;
    private TextButton rollButton;
    private TextButton stopButton;
    private Label resultLabel;
    private Table back;

    public TrickyOneScreen() {

        stage = new Stage();
        skin = new Skin(Gdx.files.internal("skin/terra-mother-ui.json"));
        skin.getFont("font").getData().setScale(3, 3);

        back = new Table();
        back.setFillParent(true);
        back.setBackground(new TiledDrawable(skin.getTiledDrawable("tile-a")));

        rollButton = new TextButton("Würfeln", skin);
        rollButton.setPosition(Gdx.graphics.getWidth() / 3, Gdx.graphics.getHeight() / 6);
        rollButton.setSize(220, 140);

        stopButton = new TextButton("Stop", skin);
        stopButton.setPosition(Gdx.graphics.getWidth() / (1 / 3), Gdx.graphics.getHeight() / 6);
        stopButton.setSize(220, 140);


        resultLabel = new Label("", skin);


        stage.addActor(back);
        stage.addActor(rollButton);
        stage.addActor(stopButton);
        stage.addActor(resultLabel);

    }

    @Override
    public void render(float delta) {

        super.render(delta);
        stage.act(delta);
        stage.draw();
        super.renderNotifications(delta);

    }

    @Override
    public void dispose() {

        stage.dispose();
        skin.dispose();


    }

}
