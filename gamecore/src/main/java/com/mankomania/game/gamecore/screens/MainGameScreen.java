package com.mankomania.game.gamecore.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
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
import com.mankomania.game.core.player.Player;
import com.mankomania.game.gamecore.MankomaniaGame;
import com.mankomania.game.gamecore.fieldoverlay.FieldOverlay;
import com.mankomania.game.gamecore.hotels.HotelRenderer;
import com.mankomania.game.gamecore.hud.HUD;

import java.util.ArrayList;
import java.util.List;

public class MainGameScreen extends AbstractScreen {
    private final PerspectiveCamera cam;
    private final ModelBatch modelBatch;
    private final Environment environment;
    private final CameraInputController camController;
    private final AssetManager assets;
    private boolean loading;
    private final ArrayList<ModelInstance> boardInstance;

    /**
     * Player Array ID
     * Player Model
     */
    private final List<ModelInstance> playerModelInstances;

    private final SpriteBatch spriteBatch;
    private final FieldOverlay fieldOverlay;

    private final HotelRenderer hotelRenderer;

    private HUD hud;
    private Stage stage;
    private float updateTime;
    private final GameData refGameData;

    public MainGameScreen() {
        refGameData = MankomaniaGame.getMankomaniaGame().getGameData();
        boardInstance = new ArrayList<>();
        playerModelInstances = new ArrayList<>();
        modelBatch = new ModelBatch();
        updateTime = 0;
        spriteBatch = new SpriteBatch();
        fieldOverlay = new FieldOverlay();
        hud = new HUD();
        stage = new Stage();
        InputMultiplexer multiplexer = new InputMultiplexer();

        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1.0f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1.0f, -0 - 8f, -0.2f));

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
        // needs to be set after assets.load
        loading = true;

        fieldOverlay.create();
        stage = hud.create(fieldOverlay);

        // use a InputMultiplexer to delegate a list of InputProcessors.
        // "Delegation for an event stops if a processor returns true, which indicates that the event was handled."
        // add other needed InputPreprocessors here

        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(fieldOverlay);
        multiplexer.addProcessor(camController);

        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        if (loading && assets.update()) {
            doneLoading();
        } else {

            Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

            modelBatch.begin(cam);
            modelBatch.render(boardInstance, environment);

            //render playerModels after environment and board have been rendered
            checkForPlayerModelMove(delta);
            modelBatch.render(playerModelInstances);

            // render the hotels
            hotelRenderer.render(modelBatch);
            modelBatch.end();

            camController.update();
            // enabling blending, so transparency can be used (batch.setAlpha(x))
            spriteBatch.enableBlending();

            // start SpriteBatch and render overlay after model batch, so the overlay gets rendered "above" the 3d models
            spriteBatch.begin();
            fieldOverlay.render(spriteBatch);
            spriteBatch.end();

            stage.act(delta);
            stage.draw();
            super.renderNotifications(delta);

            // TODO: remove this, just for debugging purposes
            if (Gdx.input.isKeyJustPressed(Input.Keys.I)) {
                MankomaniaGame.getMankomaniaGame().getClient().getMessageHandler().sendIntersectionSelectionMessage(MankomaniaGame.getMankomaniaGame().getGameData().getIntersectionSelectionOption1());
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.O)) {
                MankomaniaGame.getMankomaniaGame().getClient().getMessageHandler().sendIntersectionSelectionMessage(MankomaniaGame.getMankomaniaGame().getGameData().getIntersectionSelectionOption2());
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

        loading = false;
    }

    @Override
    public void dispose() {
        this.modelBatch.dispose();
        this.fieldOverlay.dispose();
        this.stage.dispose();
        this.assets.dispose();
    }

    /**
     * checks if PlayerModels should be moved via the {@link MankomaniaGame} and if so one instance will be moved
     * for one tile forward per method invocation
     *
     * @param delta delta time from rendering thread
     */
    private void checkForPlayerModelMove(float delta) {
        updateTime += delta;
        if (updateTime > 1 && !playerModelInstances.isEmpty()) {
            for (int i = 0; i < refGameData.getPlayers().size(); i++) {
                Player player = refGameData.getPlayers().get(i);
                int currentFieldIndex = player.getCurrentField();
                if(player.getTargetFieldIndex() != currentFieldIndex){
                    int nextFieldIndex = refGameData.getFields()[currentFieldIndex].getNextField();
                    player.updateField(refGameData.getFields()[nextFieldIndex]);
                    playerModelInstances.get(i).transform.setToTranslation(player.getPosition());
                }
            }
            updateCam(MankomaniaGame.getMankomaniaGame().getCurrentPlayerTurn());
            updateTime = 0;
        }
    }

    /**
     * should be called once when screen is created transforms custom Vector3 object to Vector3
     *
     * @param modelInstances arrayList of ModelInstances that are created given a certain playerAmount from {@link GameData}
     */
    private void initPlayerModels(ArrayList<ModelInstance> modelInstances) {
        //only add amount of players that are currently connected
        List<Player> players = MankomaniaGame.getMankomaniaGame().getGameData().getPlayers();
        for (int i = 0; i < players.size(); i++) {
            modelInstances.get(i).transform.setTranslation(players.get(i).getPosition());
            playerModelInstances.add(modelInstances.get(i));
        }
    }

    /**
     * invoke when one player modelInstance has been moved to new position, updates camera position and attributes to
     * look at player
     *
     * @param playerIndex int id of player for hashMap to get players model instance
     */
    public void updateCam(int playerIndex) {
        final float xOff = -25f;
        final float yOff = 20f;
        final float zOff = -25f;
        final float initialXPos = -87.48767f;
        final float initialZPos = -82.28824f;

        Vector3 pos = playerModelInstances.get(playerIndex).transform.getTranslation(new Vector3());

        /*
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
            if (pos.z > 0) {
                camPosZ = pos.z - zOff;
            } else {
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