package com.mankomania.game.core.network.messages.servertoclient.hotel;

/**
 * This message gets sent to all clients after a player bought a hotel to notify them about this.
 */
public class PlayerBoughtHotelMessage {
    private int playerIndex;
    private int hotelFieldId;

    public PlayerBoughtHotelMessage() {
        // empty ctor for kryonet serialisation
    }

    public PlayerBoughtHotelMessage(int playerIndex, int hotelFieldId) {
        this.playerIndex = playerIndex;
        this.hotelFieldId = hotelFieldId;
    }

    public int getPlayerIndex() {
        return playerIndex;
    }

    public void setPlayerIndex(int playerIndex) {
        this.playerIndex = playerIndex;
    }

    public int getHotelFieldId() {
        return hotelFieldId;
    }

    public void setHotelFieldId(int hotelFieldId) {
        this.hotelFieldId = hotelFieldId;
    }
}
