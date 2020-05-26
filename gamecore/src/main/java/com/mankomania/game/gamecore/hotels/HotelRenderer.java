package com.mankomania.game.gamecore.hotels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.UBJsonReader;
import com.mankomania.game.core.data.GameData;
import com.mankomania.game.core.fields.types.HotelField;
import com.mankomania.game.gamecore.MankomaniaGame;

import java.util.ArrayList;

/**
 * This class is responsible for loading, positioning and drawing the 3d model for the hotels.
 * It is "extracted" out of MainGameScreen, to avoid bloating MainGameScreen and
 * to help keeping the code there clean and readable.
 */
public class HotelRenderer {
    private GameData gameData; // reference to gameData, to get information about the game (which player owns which hotel)
    private ArrayList<ModelInstance> hotelModelInstances = new ArrayList<>(); // all four hotels get added to this list so they can be rendered
    private ArrayList<ModelInstance> flagpoleInstances = new ArrayList<>(); // possibly all four flagpoles (if all player have been bought)

    // need to store the model references to dispose it correctly (can be removed if asset manager does safe disposing)
    private Model hotelModel;
    private Model flagModelBlue;
    private Model flagModelGreen;
    private Model flagModelRed;
    private Model flagModelYellow;

    // configs
    private static final float HOTEL_MODEL_SCALE = 0.5f;
    private static final float HOTEL_FLAGPOLE_SCALE = 0.5f;
    private static final Vector3 HOTEL_MODEL_SCALING_VECTOR = new Vector3(HOTEL_MODEL_SCALE, HOTEL_MODEL_SCALE, HOTEL_MODEL_SCALE);
    private static final Vector3 HOTEL_FLAGPOLE_SCALING_VECTOR = new Vector3(HOTEL_FLAGPOLE_SCALE, HOTEL_FLAGPOLE_SCALE, HOTEL_FLAGPOLE_SCALE);
    private static final float HOTEL_MODEL_BOTTOM_PADDING = 5f;

    public HotelRenderer() {
        this.gameData = MankomaniaGame.getMankomaniaGame().getGameData();
    }

    /**
     * Loads all models and textures from disk.
     */
    public void create() {
        // TODO: load models through asset manager, that way G3dModelLoader is not necessary.
        ModelLoader modelLoader = new G3dModelLoader(new UBJsonReader());
        this.hotelModel = modelLoader.loadModel(Gdx.files.internal("hotels/tp_stack.g3db"));

        for (HotelField hotelField : this.gameData.getHotelFields()) {
            ModelInstance hotelModelInstance = new ModelInstance(this.hotelModel);
            // get the position vector from field loaded through json file setting the y coordinate to the bottom padding wanted (since the board has some thickness itself)
            Vector3 hotelPosition = hotelField.getHotelPosition();
            hotelPosition.y = HOTEL_MODEL_BOTTOM_PADDING;
            // scale and position the model instances
            hotelModelInstance.transform.setToTranslationAndScaling(hotelPosition, HOTEL_MODEL_SCALING_VECTOR);

            // add the model instance to the ArrayList that gets rendered
            this.hotelModelInstances.add(hotelModelInstance);
        }

        // now loading the flag models
        this.flagModelBlue = modelLoader.loadModel(Gdx.files.internal("hotels/tp_flagpole_blue.g3db"));
        this.flagModelGreen = modelLoader.loadModel(Gdx.files.internal("hotels/tp_flagpole_green.g3db"));
        this.flagModelRed = modelLoader.loadModel(Gdx.files.internal("hotels/tp_flagpole_red.g3db"));
        this.flagModelYellow = modelLoader.loadModel(Gdx.files.internal("hotels/tp_flagpole_yellow.g3db"));

        // add null elements so we dont get a index out of bounde exception
        for (int i = 0; i < 4; i++) {
            this.flagpoleInstances.add(null);
        }
    }

    /**
     * Renders all the hotels. On the modelBatch must have been called the ModelBatch.begin() method before calling this method.
     *
     * @param modelBatch the {@link ModelBatch} to render the hotels with
     */
    public void render(ModelBatch modelBatch) {
        // renders all the hotel instances (maybe add a environment for special lighting?)
        modelBatch.render(this.hotelModelInstances);

        // only render flag if a player owns a hotel
        // if the flag instance for a given player is not set yet, check if he owns a hotel now, if yes, set the modelinstance to that field and reder it from now on
        for (int i = 0; i < this.gameData.getPlayers().size(); i++) {
            if (this.flagpoleInstances.get(i) != null) {
                modelBatch.render(this.flagpoleInstances.get(i));
            } else {
                HotelField ownedField = this.gameData.getHotelOwnedByPlayer(i);
                if (ownedField != null) {
                    // add a new model instance that will get rendered further on
                    ModelInstance newFlagPoleInstance = new ModelInstance(this.getModelByPlayerIndex(i));
                    // get hotel position and add bottom padding
                    Vector3 hotelPosition = ownedField.getHotelPosition();
                    hotelPosition.y = HOTEL_MODEL_BOTTOM_PADDING;
                    newFlagPoleInstance.transform.setToTranslationAndScaling(hotelPosition, HOTEL_FLAGPOLE_SCALING_VECTOR);

                    this.flagpoleInstances.set(i, newFlagPoleInstance);
                }
            }
        }

    }

    public void dispose() {
        this.hotelModel.dispose();
        this.flagModelBlue.dispose();
        this.flagModelGreen.dispose();
        this.flagModelRed.dispose();
        this.flagModelYellow.dispose();
    }

    private Model getModelByPlayerIndex(int playerIndex) {
        switch (playerIndex) {
            case 0: return this.flagModelBlue;
            case 1: return this.flagModelGreen;
            case 2: return this.flagModelRed;
            case 3:
            default:
                return this.flagModelYellow;

        }
    }
}
