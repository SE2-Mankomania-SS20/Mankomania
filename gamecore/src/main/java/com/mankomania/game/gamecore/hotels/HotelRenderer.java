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
import com.mankomania.game.core.data.IDConverter;
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

    // need to store the model references to dispose it correctly (can be removed if asset manager does safe disposing)
    private Model hotelModel;

    // configs
    private static final float HOTEL_MODEL_SCALE = 0.5f;
    private static final Vector3 HOTEL_MODEL_SCALING_VECTOR = new Vector3(HOTEL_MODEL_SCALE, HOTEL_MODEL_SCALE, HOTEL_MODEL_SCALE);
    private static final Vector3 HOTEL_POSITION_1 = new Vector3(-92f, 5f, 41f);
    private static final Vector3 HOTEL_POSITION_2 = new Vector3(-92f, 5f, -41f);
    private static final Vector3 HOTEL_POSITION_3 = new Vector3(92f, 5f, -41f);
    private static final Vector3 HOTEL_POSITION_4 = new Vector3(86f, 5f, 34f);

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

        // create a model instance for each of the four hotels using the same base model
        ModelInstance hotelModelInstance1 = new ModelInstance(this.hotelModel);
        ModelInstance hotelModelInstance2 = new ModelInstance(this.hotelModel);
        ModelInstance hotelModelInstance3 = new ModelInstance(this.hotelModel);
        ModelInstance hotelModelInstance4 = new ModelInstance(this.hotelModel);

        // scale and position all the model instances
        hotelModelInstance1.transform.setToTranslationAndScaling(HOTEL_POSITION_1, HOTEL_MODEL_SCALING_VECTOR);
        hotelModelInstance2.transform.setToTranslationAndScaling(HOTEL_POSITION_2, HOTEL_MODEL_SCALING_VECTOR);
        hotelModelInstance3.transform.setToTranslationAndScaling(HOTEL_POSITION_3, HOTEL_MODEL_SCALING_VECTOR);
        hotelModelInstance4.transform.setToTranslationAndScaling(HOTEL_POSITION_4, HOTEL_MODEL_SCALING_VECTOR);


        this.hotelModelInstances.add(hotelModelInstance1);
        this.hotelModelInstances.add(hotelModelInstance2);
        this.hotelModelInstances.add(hotelModelInstance3);
        this.hotelModelInstances.add(hotelModelInstance4);
    }

    /**
     * Renders all the hotels. On the modelBatch must have been called the ModelBatch.begin() method before calling this method.
     *
     * @param modelBatch the {@link ModelBatch} to render the hotels with
     */
    public void render(ModelBatch modelBatch) {
        // renders all the hotel instances (maybe add a environment for special lighting?)
        modelBatch.render(this.hotelModelInstances);
    }

    public void dispose() {
        this.hotelModel.dispose();
    }
}
