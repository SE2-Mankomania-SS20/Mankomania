package com.mankomania.game.core.network.messages.servertoclient.hotel;

/**
 * Gets sent if a player lands on a hotel field and no other player has bought this hotel yet.
 * It holds the connection id of said player, the field id of the hotel field and the buying price
 */
public class PlayerCanBuyHotelMessage {
    private int playerIndex;
    private int hotelFieldId;

    public PlayerCanBuyHotelMessage() {
        // empty ctor needed for kryonet serialization
    }

    public PlayerCanBuyHotelMessage(int playerIndex, int hotelFieldId) {
        this.playerIndex = playerIndex;
        this.hotelFieldId = hotelFieldId;
    }

    public int getPlayerIndex() {
        return this.playerIndex;
    }

    public int getHotelFieldId() {
        return this.hotelFieldId;
    }

    public void setPlayerIndex(int playerIndex) {
        this.playerIndex = playerIndex;
    }
}
