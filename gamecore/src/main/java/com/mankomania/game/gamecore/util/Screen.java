package com.mankomania.game.gamecore.util;

import com.mankomania.game.gamecore.client.NetworkClient;
import com.mankomania.game.gamecore.screens.*;

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
            return new ChatScreen((NetworkClient) params[0],(Screen) params[1]);
        }
    },
   DICE {
        public AbstractScreen getScreen(Object... params) {
            return new DiceScreen();
        }
    },
    MAIN_GAME {
        public AbstractScreen getScreen(Object... params) {
            return new MainGameScreen();
        }
    };

    public abstract AbstractScreen getScreen(Object... params);


}
