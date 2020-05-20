package com.mankomania.game.gamecore.util;

import com.mankomania.game.gamecore.StockExchange.AktienBörse;
import com.mankomania.game.gamecore.client.NetworkClient;

import com.mankomania.game.gamecore.screens.*;

public enum Screen {

    /**
     * Holds all types of Screens with getScreen method
     */
    LAUNCH {
        public AbstractScreen getScreen(Object... params) {
            return new AktienBörse();
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
    MAIN_GAME {
        public AbstractScreen getScreen(Object... params) {
            return new MainGameScreen();
        }
         },
    AKTIEN_BOERSE{
            public AbstractScreen getScreen(Object... params) {
                return new AktienBörse();
            }
    };

    public abstract AbstractScreen getScreen(Object... params);


}
