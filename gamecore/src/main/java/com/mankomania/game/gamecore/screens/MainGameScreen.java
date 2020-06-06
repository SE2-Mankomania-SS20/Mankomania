package com.mankomania.game.gamecore.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
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
import com.esotericsoftware.minlog.Log;
import com.badlogic.gdx.utils.Timer;
import com.mankomania.game.core.data.GameData;
import com.mankomania.game.core.player.Player;
import com.mankomania.game.gamecore.MankomaniaGame;
import com.mankomania.game.gamecore.fieldoverlay.FieldOverlay;
import com.mankomania.game.gamecore.hotels.BuyHotelOverlay;
import com.mankomania.game.gamecore.hotels.HotelRenderer;
import com.mankomania.game.gamecore.hud.HUD;
import com.mankomania.game.gamecore.hud.IntersectionOverlay;
import com.mankomania.game.gamecore.util.AssetPaths;

import java.util.ArrayList;
import java.util.List;

public class MainGameScreen extends AbstractScreen {
    private final PerspectiveCamera cam;
    private final ModelBatch modelBatch;
    private final Environment environment;
    private final CameraInputController camController;
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
    private final BuyHotelOverlay buyHotelOverlay;
    private final IntersectionOverlay intersectionOverlay;

    private HUD hud;
    private Stage stage;
    private float updateTime;
    private final GameData refGameData;
    private final MankomaniaGame mankomaniaGame;

    public MainGameScreen() {
        mankomaniaGame = MankomaniaGame.getMankomaniaGame();
        refGameData = mankomaniaGame.getGameData();
        boardInstance = new ArrayList<>();
        playerModelInstances = new ArrayList<>();
        modelBatch = new ModelBatch();
        updateTime = 0;
        spriteBatch = new SpriteBatch();
        fieldOverlay = new FieldOverlay();
        hotelRenderer = new HotelRenderer();
        buyHotelOverlay = new BuyHotelOverlay();
        intersectionOverlay = new IntersectionOverlay();
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

        loading = true;

        fieldOverlay.create();
        stage = hud.create(fieldOverlay);
        hotelRenderer.create();
        buyHotelOverlay.create();
        intersectionOverlay.create();

        // use a InputMultiplexer to delegate a list of InputProcessors.
        // "Delegation for an event stops if a processor returns true, which indicates that the event was handled."
        // add other needed InputPreprocessors here

        buyHotelOverlay.addStageToMultiplexer(multiplexer);
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(fieldOverlay);
        multiplexer.addProcessor(camController);

        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        if (loading) {
            doneLoading();
        } else {
            hud.render(delta);
            Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

            modelBatch.begin(cam);
            modelBatch.render(boardInstance, environment);

            //render playerModels after environment and board have been rendered
            checkForPlayerModelMove(delta);
            modelBatch.render(playerModelInstances);
            if (mankomaniaGame.isCamNeedsUpdate()) {
                updateCam(refGameData.getCurrentPlayerTurnIndex());
                mankomaniaGame.setCamNeedsUpdate(false);
            }

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

            buyHotelOverlay.render(delta);
            intersectionOverlay.render(delta);

            // TODO: remove this, just for debugging purposes
            if (Gdx.input.isKeyJustPressed(Input.Keys.I)) {
                mankomaniaGame.getNetworkClient().getMessageHandler().sendIntersectionSelectionMessage(refGameData.getCurrentPlayerTurnField().getNextField());
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.O)) {
                mankomaniaGame.getNetworkClient().getMessageHandler().sendIntersectionSelectionMessage(refGameData.getCurrentPlayerTurnField().getOptionalNextField());
            }
            // debugging help for chosing wheter to buy a hotel or not
            if (Gdx.input.isKeyJustPressed(Input.Keys.B)) {
                MankomaniaGame.getMankomaniaGame().getNetworkClient().getMessageHandler().sendPlayerBuyHotelDecisionMessage(true);
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.N)) {
                MankomaniaGame.getMankomaniaGame().getNetworkClient().getMessageHandler().sendPlayerBuyHotelDecisionMessage(false);
            }
        }
    }

    private void doneLoading() {
        Model board = MankomaniaGame.getMankomaniaGame().getManager().get(AssetPaths.BOARD);
        Model player1 = MankomaniaGame.getMankomaniaGame().getManager().get(AssetPaths.PLAYER_BLUE);
        Model player2 = MankomaniaGame.getMankomaniaGame().getManager().get(AssetPaths.PLAYER_GREEN);
        Model player3 = MankomaniaGame.getMankomaniaGame().getManager().get(AssetPaths.PLAYER_RED);
        Model player4 = MankomaniaGame.getMankomaniaGame().getManager().get(AssetPaths.PLAYER_YELLOW);

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
    }

    /**
     * checks if PlayerModels should be moved via the {@link MankomaniaGame} and if so one instance will be moved
     * for one tile forward per method invocation
     *
     * @param delta delta time from rendering thread
     */
    private void checkForPlayerModelMove(float delta) {
        updateTime += delta;
        if (updateTime > 0.5) {
            for (Player player : refGameData.getPlayers()) {
                if (!player.isMovePathEmpty()) {
                    int playerIndex = player.getPlayerIndex();
                    playerModelInstances.get(playerIndex).transform.setToTranslation(refGameData.movePlayer(playerIndex));
                    updateCam(playerIndex);
                    if (refGameData.isCurrentPlayerMovePathEmpty() && mankomaniaGame.isLocalPlayerTurn() && !mankomaniaGame.isTurnFinishSend()) {
                        Timer.schedule(new Timer.Task() {
                            @Override
                            public void run() {
                                mankomaniaGame.getNetworkClient().getMessageHandler().sendTurnFinished();
                            }
                        }, 1f);
                        mankomaniaGame.setTurnFinishSend(true);
                    }
                }
            }
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
        List<Player> players = mankomaniaGame.getGameData().getPlayers();
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