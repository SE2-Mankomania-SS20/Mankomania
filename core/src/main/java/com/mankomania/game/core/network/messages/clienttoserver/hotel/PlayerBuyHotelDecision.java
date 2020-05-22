package com.mankomania.game.core.network.messages.clienttoserver.hotel;

/**
 * This message is sent when a player decides wheter to buy a hotel or not.
 */
public class PlayerBuyHotelDecision {
    private int playerId;
    private int hotelFieldId;
    private int moneyPaid; // TODO: maybe store this value on the server so clients cant cheat the price

    public PlayerBuyHotelDecision() {
        // empty ctor needed for kryonet serialization
    }

    public PlayerBuyHotelDecision(int playerId, int hotelFieldId, int moneyPaid) {
        this.playerId = playerId;
        this.hotelFieldId = hotelFieldId;
        this.moneyPaid = moneyPaid;
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

    public int getMoneyPaid() {
        return moneyPaid;
    }

    public void setMoneyPaid(int moneyPaid) {
        this.moneyPaid = moneyPaid;
    }
}
