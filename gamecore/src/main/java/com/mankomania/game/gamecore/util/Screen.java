package com.mankomania.game.gamecore.util;

import com.mankomania.game.gamecore.client.NetworkClient;

import com.mankomania.game.gamecore.screens.*;
import com.mankomania.game.gamecore.screens.trickyone.TrickyOneScreen;

public enum Screen {

    /**
     * Holds all types of Screens with getScreen method
     */
    LOADING {
        public AbstractScreen getScreen(Object... params) {
            return new LoadingScreen();
        }
    },
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
            return new ChatScreen((Screen) params[0]);
        }
    },
    MAIN_GAME {
        public AbstractScreen getScreen(Object... params) {
            return new MainGameScreen();
        }
    },
    TRICKY_ONE {
        public AbstractScreen getScreen(Object... params) {
            return new TrickyOneScreen();
        }
    };

    public abstract AbstractScreen getScreen(Object... params);


}
