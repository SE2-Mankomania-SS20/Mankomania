package com.mankomania.game.core.network.messages.servertoclient.hotel;

/**
 * This message gets send if a player lands on a hotel field while an other player has bought
 * the hotel of this field.
 */
public class PlayerPaysHotelRentMessage {
    private int playerIndex; // player that landed on the field
    private int hotelOwnerPlayerId; // the player that owns the hotel
    private int hotelRentAmount; // the amount of rent the player has to pay the hotel owner

    public PlayerPaysHotelRentMessage() {
        // empty ctor needed for kryonet serialization
    }

    public PlayerPaysHotelRentMessage(int playerIndex, int hotelOwnerPlayerId, int hotelRentAmount) {
        this.playerIndex = playerIndex;
        this.hotelOwnerPlayerId = hotelOwnerPlayerId;
        this.hotelRentAmount = hotelRentAmount;
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

    public int getHotelRentAmount() {
        return hotelRentAmount;
    }

    public void setHotelRentAmount(int hotelRentAmount) {
        this.hotelRentAmount = hotelRentAmount;
    }
}
