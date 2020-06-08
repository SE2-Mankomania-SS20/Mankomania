package com.mankomania.game.gamecore.util;

import com.mankomania.game.gamecore.screens.slots.SlotsScreen;
import com.mankomania.game.gamecore.screens.stockexchange.AktienBoerse;
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
    AKTIEN_BOERSE {
        public AbstractScreen getScreen(Object... params) {
            return new AktienBoerse();
        }
    },
    TRICKY_ONE {
        public AbstractScreen getScreen(Object... params) {
            return new TrickyOneScreen();
        }
    },
    MINIGAME_ROULETTE {
        public AbstractScreen getScreen(Object... params) {
            return RouletteMiniGameScreen.getInstance();
        }
    },
    SLOTS {
        public AbstractScreen getScreen(Object... params) {
            return new SlotsScreen();
        }
    };

    public abstract AbstractScreen getScreen(Object... params);


}
