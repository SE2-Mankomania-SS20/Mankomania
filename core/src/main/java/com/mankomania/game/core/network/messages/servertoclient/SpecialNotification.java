package com.mankomania.game.core.network.messages.servertoclient;

/**
 * Used to command the client to display the SpecialNotifier with given text.
 */
public class SpecialNotification {
    private String textToShow;

    public SpecialNotification() {
        // empty ctor needed for kryonet deserialization
    }

    public SpecialNotification(String textToShow) {
        this.textToShow = textToShow;
    }

    public String getTextToShow() {
        return textToShow;
    }
}
