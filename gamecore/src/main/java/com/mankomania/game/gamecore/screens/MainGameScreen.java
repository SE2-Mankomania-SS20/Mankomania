package com.mankomania.game.gamecore.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.mankomania.game.gamecore.MankomaniaGame;
import com.mankomania.game.gamecore.fieldoverlay.FieldOverlay;
import com.mankomania.game.gamecore.hud.HUD;
import com.mankomania.game.gamecore.util.Vector3Helper;
import com.mankomania.game.gamecore.util.ScreenManager;

import java.util.ArrayList;
import java.util.HashMap;


public class MainGameScreen extends AbstractScreen {
    public PerspectiveCamera cam;
    public ModelBatch modelBatch;
    public Environment environment;
    public CameraInputController camController;
    public AssetManager assets;
    public boolean loading;
    public ArrayList<ModelInstance> boardInstance = new ArrayList<>();
    public HashMap<Integer, ModelInstance> playerModelInstances = new HashMap<>();
    public Model model;
    public MankomaniaGame game;
    public Batch batch;
    private SpriteBatch spriteBatch;
    private FieldOverlay fieldOverlay;

    private HUD hud;
    private Texture texture;
    private Image image;
    private Table table;
    private Stage stage;
    private TextButton btn1;
    private Skin skin;

    public MainGameScreen() {
        create();
    }


    public void create() {


        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1.0f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1.0f, -0 - 8f, -0.2f));

        modelBatch = new ModelBatch();

        cam = new PerspectiveCamera(60, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(0.0f, 160.0f, 20.0f);
        cam.lookAt(0, 0, 0);
        //cam.rotate(-90, 90,0, 0);
        cam.near = 20.0f;
        cam.far = 300.0f;
        cam.update();

        camController = new CameraInputController(cam);
        assets = new AssetManager();
        assets.load("board/board.g3db", Model.class);
        assets.load("player/player_blue.g3db", Model.class);
        assets.load("player/player_green.g3db", Model.class);
        assets.load("player/player_red.g3db", Model.class);
        assets.load("player/player_yellow.g3db", Model.class);

        loading = true;

        this.spriteBatch = new SpriteBatch();

        this.fieldOverlay = new FieldOverlay();
        this.fieldOverlay.create();


        hud = new HUD();
        stage = new Stage();
        stage = hud.create(fieldOverlay);


        // use a InputMultiplexer to delegate a list of InputProcessors.
        // "Delegation for an event stops if a processor returns true, which indicates that the event was handled."
        // add other needed InputPreprocessors here

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(this.fieldOverlay);
        multiplexer.addProcessor(camController);

        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    public void render(float delta) {
        if (loading && assets.update()) {
            doneLoading();
        }
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        modelBatch.begin(cam);
        modelBatch.render(playerModelInstances.values());
        modelBatch.render(boardInstance, environment);
        modelBatch.end();
        camController.update();

        // enabling blending, so transparency can be used (batch.setAlpha(x))
        this.spriteBatch.enableBlending();

        // start SpriteBatch and render overlay after model batch, so the overlay gets rendered "above" the 3d models
        this.spriteBatch.begin();
        this.fieldOverlay.render(spriteBatch);
        this.spriteBatch.end();


        stage.act(delta);
        stage.draw();

    }

    private void doneLoading() {
        Model board = assets.get("board/board.g3db", Model.class);
        Model player1 = assets.get("player/player_blue.g3db", Model.class);
        Model player2 = assets.get("player/player_green.g3db", Model.class);
        Model player3 = assets.get("player/player_red.g3db", Model.class);
        Model player4 = assets.get("player/player_yellow.g3db", Model.class);

        ModelInstance boardInstance = new ModelInstance(board);

        ArrayList<ModelInstance> list = new ArrayList<>();
        list.add(new ModelInstance(player1));
        list.add(new ModelInstance(player2));
        list.add(new ModelInstance(player3));
        list.add(new ModelInstance(player4));


        this.boardInstance.add(boardInstance);

        initPlayerModels(list);

        this.loading = false;
    }

    @Override
    public void dispose() {
        this.model.dispose();
        this.modelBatch.dispose();
    }

    public void movePlayerModels(Integer playerID, Vector3 endPosition) {

    }

    private void initPlayerModels(ArrayList<ModelInstance> list) {

        Vector3Helper helper = new Vector3Helper();

        //only add amount of players that are currently connected
        Object[] ids = ScreenManager.getInstance().getGame().getGameData().getPlayers().keySet().toArray();
        int playerAmount = ScreenManager.getInstance().getGame().getGameData().getPlayers().size();
        for (int i = 0; i < playerAmount; i++) {
            this.playerModelInstances.put((Integer) ids[i], list.get(i));
            this.playerModelInstances.get((Integer) ids[i]).transform.setTranslation(helper.getVector3(ScreenManager.getInstance().getGame().getGameData().getStartPosition(i)));

        }

    }
}