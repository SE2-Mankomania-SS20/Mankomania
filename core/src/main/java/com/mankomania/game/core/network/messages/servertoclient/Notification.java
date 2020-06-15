package com.mankomania.game.core.network.messages.servertoclient;

import com.badlogic.gdx.graphics.Color;

import java.util.Objects;

/**
 * A Notification that will be displayed on any current screen
 */
public class Notification {
    private float timeToLive;
    private String text;
    private final Color bgColor;
    private final Color fontColor;

    public float getTimeToLive() {
        return timeToLive;
    }

    public String getText() {
        return text;
    }

    public Color getBgColor() {
        return bgColor;
    }

    public Color getFontcolor() {
        return fontColor;
    }

    public void updateTime(float delta) {
        this.timeToLive -= delta;
    }

    public void setText(String text) {
        this.text = text;
    }

    /**
     * @param timeToLive time in seconds how long the notification will be showed
     * @param text       notification text
     * @param fontColor  notification font color
     * @param bgColor    notification background color
     */
    public Notification(float timeToLive, String text, Color fontColor, Color bgColor) {
        this.timeToLive = timeToLive;
        this.text = text;
        this.bgColor = bgColor;
        this.fontColor = fontColor;
    }

    /**
     * Default font is black and background is white
     *
     * @param timeToLive time in seconds how long the notification will be showed
     * @param text       notification text
     */
    public Notification(float timeToLive, String text) {
        this.timeToLive = timeToLive;
        this.text = text;
        this.fontColor = new Color(0, 0, 0, 1);
        this.bgColor = new Color(1, 1, 1, 1);
    }

    /**
     * Default font is black and background is white and timeToLive is 3s
     *
     * @param text time in seconds how long the notification will be showed
     */
    public Notification(String text) {
        this.timeToLive = 3f;
        this.text = text;
        this.fontColor = new Color(0, 0, 0, 1);
        this.bgColor = new Color(1, 1, 1, 1);
    }

    /**
     * Empty for Kryonet
     */
    public Notification() {
        this.timeToLive = 3f;
        this.text = null;
        this.fontColor = null;
        this.bgColor = null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Notification that = (Notification) o;
        return Float.compare(that.timeToLive, timeToLive) == 0 &&
                Objects.equals(text, that.text) &&
                Objects.equals(bgColor, that.bgColor) &&
                Objects.equals(fontColor, that.fontColor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timeToLive, text, bgColor, fontColor);
    }
}
