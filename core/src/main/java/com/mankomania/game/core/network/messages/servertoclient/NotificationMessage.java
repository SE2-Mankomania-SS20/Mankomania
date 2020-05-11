package com.mankomania.game.core.network.messages.servertoclient;

/**
 * Generic notification message, that just informs each client what just happened.
 */
public class NotificationMessage {
    private String notificationType; // TODO: maybe create enum for notification types
    private String notificationText;

    public NotificationMessage() { }

    public String getNotificationType() {
        return notificationType;
    }

    public String getNotificationText() {
        return notificationText;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    public void setNotificationText(String notificationText) {
        this.notificationText = notificationText;
    }
}
