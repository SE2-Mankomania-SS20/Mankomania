package com.mankomania.game.core.network.messages.servertoclient.hotel;

/**
 * This message gets send if a player lands on a hotel field while an other player has bought
 * the hotel of this field.
 */
public class PlayerPaysHotelRentMessage {
    private int playerIndex; // player that landed on the field
    private int hotelOwnerPlayerId; // the player that owns the hotel
    private int hotelFieldId; // the hotel field id to get the rent amount

    public PlayerPaysHotelRentMessage() {
        // empty ctor needed for kryonet serialization
    }

    public PlayerPaysHotelRentMessage(int playerIndex, int hotelOwnerPlayerId, int hotelFieldId) {
        this.playerIndex = playerIndex;
        this.hotelOwnerPlayerId = hotelOwnerPlayerId;
        this.hotelFieldId = hotelFieldId;
    }

    public int getPlayerIndex() {
        return playerIndex;
    }

    public void setPlayerIndex(int playerIndex) {
        this.playerIndex = playerIndex;
    }

    public int getHotelOwnerPlayerId() {
        return hotelOwnerPlayerId;
    }

    public void setHotelOwnerPlayerId(int hotelOwnerPlayerId) {
        this.hotelOwnerPlayerId = hotelOwnerPlayerId;
    }

    public int getHotelFieldId() {
        return hotelFieldId;
    }

    public void setHotelFieldId(int hotelFieldId) {
        this.hotelFieldId = hotelFieldId;
    }
}
