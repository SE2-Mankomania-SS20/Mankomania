package com.mankomania.game.gamecore.screens;

import com.mankomania.game.gamecore.MankomaniaGame;
import com.mankomania.game.gamecore.client.NetworkClient;

public enum ScreenEnum {

    /**
     * Holds all types of Screens with getScreen method
     */
    LAUNCH {
        public AbstractScreen getScreen(Object... params) {
            return new LaunchScreen();
        }
    },
    LOBBY {
        public AbstractScreen getScreen(Object... params) {
            return new LobbyScreen();
        }
    },
    CHAT {
        public AbstractScreen getScreen(Object... params) {
            return new ChatScreen((NetworkClient) params[0]);
        }
    },
    MAIN_GAME {
        public AbstractScreen getScreen(Object... params) {
            return new MainGameScreen();
        }
    };

    public abstract AbstractScreen getScreen(Object... params);


}
