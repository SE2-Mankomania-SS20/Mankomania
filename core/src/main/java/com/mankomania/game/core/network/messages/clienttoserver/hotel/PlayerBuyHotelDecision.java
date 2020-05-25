package com.mankomania.game.core.network.messages.clienttoserver.hotel;

/**
 * This message is sent when a player decides wheter to buy a hotel or not.
 */
public class PlayerBuyHotelDecision {
    private int playerIndex;
    private int hotelFieldId;
    private int moneyPaid; // TODO: maybe store this value on the server so clients cant cheat the price
    private boolean hotelBought;

    public PlayerBuyHotelDecision() {
        // empty ctor needed for kryonet serialization
    }

    public PlayerBuyHotelDecision(int playerIndex, int hotelFieldId, int moneyPaid, boolean hotelBought) {
        this.playerIndex = playerIndex;
        this.hotelFieldId = hotelFieldId;
        this.moneyPaid = moneyPaid;
        this.hotelBought = hotelBought;
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

    public int getMoneyPaid() {
        return moneyPaid;
    }

    public void setMoneyPaid(int moneyPaid) {
        this.moneyPaid = moneyPaid;
    }

    public boolean isHotelBought() {
        return hotelBought;
    }

    public void setHotelBought(boolean hotelBought) {
        this.hotelBought = hotelBought;
    }
}
