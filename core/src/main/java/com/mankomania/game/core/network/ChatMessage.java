package com.mankomania.game.core.network;


public class ChatMessage  {
    private String text;

    public ChatMessage() {

    }

    public ChatMessage(String text) {
        this.text = text;
    }

    public void setText(String txt) {
        this.text = txt;
    }

    public String getText() {
        return text;
    }
}
