package com.mankomania.game.gamecore.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
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
import com.mankomania.game.core.data.GameData;
import com.mankomania.game.gamecore.MankomaniaGame;
import com.mankomania.game.gamecore.fieldoverlay.FieldOverlay;
import com.mankomania.game.gamecore.hud.HUD;

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

    /**
     * @key: Player Array ID
     * @value: Player Model
     */
    public HashMap<Integer, ModelInstance> playerModelInstances = new HashMap<>();

    /**
     * @key: Player Array ID
     * @value: Field ID
     */
    public HashMap<Integer, Integer> currentPlayerFieldIDs = new HashMap<>();
    public Model model;
    public MankomaniaGame game;
    public Batch batch;
    private SpriteBatch spriteBatch;
    private FieldOverlay fieldOverlay;

    private HUD hud;
    private Stage stage;
    private float updateTime;

    public MainGameScreen() {
        create();
    }

    public void create() {

        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1.0f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1.0f, -0 - 8f, -0.2f));

        modelBatch = new ModelBatch();
        updateTime = 0;

        cam = new PerspectiveCamera(60, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(0.0f, 160.0f, 20.0f);
        cam.lookAt(0, 0, 0);
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
        super.render(delta);
        if (loading && assets.update()) {
            doneLoading();
        } else {
            // set the model positions
            checkForPlayerModelMove(delta);
            //setPlayerModelPositionToGameData();


            Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

            modelBatch.begin(cam);
            modelBatch.render(boardInstance, environment);

            //render playerModels after environment and board have been rendered
            checkForPlayerModelMove(delta);
            modelBatch.render(playerModelInstances.values());

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
            super.renderNotifications(delta);

            // TODO: remove this, just for debugging purposes
            if (Gdx.input.isKeyJustPressed(Input.Keys.I)) {
                MankomaniaGame.getMankomaniaGame().getClient().getMessageHandler().sendIntersectionSelectionMessage(MankomaniaGame.getMankomaniaGame().getGameData().getIntersectionSelectionOption1());
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.O)) {
                MankomaniaGame.getMankomaniaGame().getClient().getMessageHandler().sendIntersectionSelectionMessage(MankomaniaGame.getMankomaniaGame().getGameData().getIntersectionSelectionOption2());
                MankomaniaGame.getMankomaniaGame().getGameData().setSelectedOptional(true);
            }

        }
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
        if (this.model != null) {
            this.model.dispose();
        }
        this.modelBatch.dispose();
    }

    /**
     * Sets the player model instances to the positions given in GameData.
     * Player movements need to be fixed still.
     */
    private void setPlayerModelPositionToGameData() {
        for (int i = 0; i < playerModelInstances.size(); i++) {
            int realPlayerField = MankomaniaGame.getMankomaniaGame().getGameData().getPlayers().get(i).getCurrentField();
            Vector3 position = MankomaniaGame.getMankomaniaGame().getGameData().getFieldByIndex(realPlayerField).getPositions()[0];
            playerModelInstances.get(i).transform.setToTranslation(position);
        }
    }

    /**
     * checks if PlayerModels should be moved via the {@link MankomaniaGame} and if so one instance will be moved
     * for one tile forward per method invocation
     *
     * @param delta delta time from rendering thread
     */
    private void checkForPlayerModelMove(float delta) {
        // TODO@fabse: fix the "animated" player movement
        updateTime += delta;
        if (updateTime > 1) {
            for (int i = 0; i < playerModelInstances.size(); i++) {
                int currentFieldOfCurrentPlayer = currentPlayerFieldIDs.get(i);
                int realPlayerField = MankomaniaGame.getMankomaniaGame().getGameData().getPlayers().get(i).getCurrentField();

                if (currentFieldOfCurrentPlayer != realPlayerField) {
                    int nextField;
                    if (MankomaniaGame.getMankomaniaGame().getGameData().isSelectedOptional()) {
                        nextField = MankomaniaGame.getMankomaniaGame().getGameData().getFieldByIndex(currentFieldOfCurrentPlayer).getOptionalNextField();
                    } else {
                        nextField = MankomaniaGame.getMankomaniaGame().getGameData().getFieldByIndex(currentFieldOfCurrentPlayer).getNextField();
                    }
                    MankomaniaGame.getMankomaniaGame().getGameData().setSelectedOptional(false);
                    Vector3 vector3 = MankomaniaGame.getMankomaniaGame().getGameData().getFieldByIndex(nextField).getPositions()[i];
                    playerModelInstances.get(i).transform.setToTranslation(vector3);
                    currentPlayerFieldIDs.put(i, nextField);

                    //update cam
                    updateCam(i);
                }

            }

            updateTime = 0;
        }
    }

    /**
     * should be called once when screen is created transforms custom Vector3 object to Vector3
     *
     * @param list arrayList of ModelInstances that are created given a certain playerAmount from {@link GameData}
     */
    private void initPlayerModels(ArrayList<ModelInstance> list) {
        //only add amount of players that are currently connected
        int playerAmount = MankomaniaGame.getMankomaniaGame().getGameData().getPlayers().size();
        for (int i = 0; i < playerAmount; i++) {
            playerModelInstances.put(i, list.get(i));
            playerModelInstances.get(i).transform.setToTranslation(MankomaniaGame.getMankomaniaGame().getGameData().getVector3FromField(i));
            currentPlayerFieldIDs.put(i, MankomaniaGame.getMankomaniaGame().getGameData().getPlayers().get(i).getFieldID());
        }
    }

    /**
     * invoke when one player modelInstance has been moved to new position, updates camera position and attributes to
     * look at player
     *
     * @param playerID int id of player for hashMap to get players model instance
     */
    public void updateCam(int playerID) {
        final float xOff = -25f;
        final float yOff = 20f;
        final float zOff = -25f;
        final float initialXPos = -87.48767f;
        final float initialZPos = -82.28824f;

        Vector3 pos = playerModelInstances.get(playerID).transform.getTranslation(new Vector3());

        /**
         * get the correct position of the camera after modelinstance was moved
         * by calculating the normalized offset in relation to the start positions and multiplying it
         * with the offset values {@link xOff} {@link yOff} {@link zOff}
         */
        float camPosX = pos.x + (pos.x / initialXPos) * xOff;
        float camPosZ = pos.z + (pos.z / initialZPos) * zOff;

        if (Math.abs(pos.x) > Math.abs(pos.z)) {
            if (pos.x > 0)
                camPosX = pos.x - xOff;
            else {
                camPosX = pos.x + xOff;
            }
        } else {
            if (pos.z > 0){
                camPosZ = pos.z - zOff;
            }else {
                camPosZ = pos.z + zOff;
            }
        }
        cam.position.set(camPosX, yOff, camPosZ);
        cam.up.set(0, 1, 0);
        cam.lookAt(pos);
        cam.near = 1f;
        cam.update();

        camController.target.set(pos);
        camController.update();

    }
}