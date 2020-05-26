package com.mankomania.game.gamecore.util;

import com.mankomania.game.gamecore.StockExchange.AktienBoerse;
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
    AKTIEN_BOERSE{
            public AbstractScreen getScreen(Object... params) {
                return new AktienBoerse();
            }
    };

    public abstract AbstractScreen getScreen(Object... params);


}
