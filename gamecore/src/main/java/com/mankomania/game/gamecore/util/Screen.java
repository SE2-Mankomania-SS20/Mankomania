package com.mankomania.game.gamecore.util;

import com.mankomania.game.gamecore.MankomaniaGame;
import com.mankomania.game.gamecore.client.NetworkClient;
import com.mankomania.game.gamecore.screens.*;

public enum Screen {

    /**
     * Holds all types of Screens with getScreen method
     */
    LAUNCH {
        public AbstractScreen getScreen(Object... params) {
            return new LaunchScreen((String) params[0]);
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
