package com.mankomania.game.gamecore.client;

public class RouletteClient {

    // update callback for UI
    public interface UpdateCallback{
        void update();
    }

    private static RouletteClient instance;
    private UpdateCallback callback;

    private RouletteClient() {}
    public static RouletteClient getInstance() {
        if (instance == null) {
            instance = new RouletteClient();
        }
        return instance;
    }

    public void setCallback(UpdateCallback callback) {
        this.callback = callback;
    }

    public void receivedResults(){
        this.callback.update();
    }

}
