package com.mankomania.game.core.network.messages.servertoclient.hotel;

/**
 * Gets sent if a player lands on a hotel field and no other player has bought this hotel yet.
 * It holds the connection id of said player, the field id of the hotel field and the buying price
 */
public class PlayerCanBuyHotelMessage {
    private int playerId;
    private int hotelFieldId;
    private int hotelCost;

    public PlayerCanBuyHotelMessage() {
        // empty ctor needed for kryonet serialization
    }

    public PlayerCanBuyHotelMessage(int playerId, int hotelFieldId, int hotelCost) {
        this.playerId = playerId;
        this.hotelFieldId = hotelFieldId;
        this.hotelCost = hotelCost;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public int getHotelFieldId() {
        return hotelFieldId;
    }

    public void setHotelFieldId(int hotelFieldId) {
        this.hotelFieldId = hotelFieldId;
    }

    public int getHotelCost() {
        return hotelCost;
    }

    public void setHotelCost(int hotelCost) {
        this.hotelCost = hotelCost;
    }
}
