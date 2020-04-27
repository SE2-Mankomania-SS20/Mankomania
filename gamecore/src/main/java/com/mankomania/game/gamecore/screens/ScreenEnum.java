package com.mankomania.game.gamecore.screens;

import com.mankomania.game.gamecore.MankomaniaGame;
import com.mankomania.game.gamecore.client.NetworkClient;

public enum ScreenEnum {

    /**
     * Holds all types of Screens with getScreen method
     */
    LAUNCH {
        public AbstractScreen getScreen(Object... params) {
            return new LaunchScreen((MankomaniaGame) params[0]);
        }
    },
    LOBBY {
        public AbstractScreen getScreen(Object... params) {
            return new LobbyScreen((MankomaniaGame) params[0]);
        }
    },
    CHAT {
        public AbstractScreen getScreen(Object... params) {
            return new ChatScreen((MankomaniaGame) params[0], (NetworkClient) params[1]);
        }
    },
    MAIN_GAME {
        public AbstractScreen getScreen(Object... params) {
            return new MainGameScreen((MankomaniaGame) params[0]);
        }
    };

    public abstract AbstractScreen getScreen(Object... params);


}
