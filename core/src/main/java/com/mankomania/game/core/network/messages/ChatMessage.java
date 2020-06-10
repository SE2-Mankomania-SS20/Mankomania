package com.mankomania.game.core.network.messages;


public class ChatMessage {

    private String text;

    public ChatMessage() {
        // empty for kryonet
    }

    public ChatMessage(String text) {
        this.setText(text);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
