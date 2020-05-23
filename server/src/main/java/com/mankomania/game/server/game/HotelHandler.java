package com.mankomania.game.server.game;

import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;
import com.mankomania.game.core.data.GameData;
import com.mankomania.game.core.network.messages.servertoclient.hotel.PlayerCanBuyHotelMessage;
import com.mankomania.game.core.network.messages.servertoclient.hotel.PlayerPaysHotelRentMessage;
import com.mankomania.game.server.data.ServerData;

/**
 * This class is used for all serverside logic and message handling involving hotels.
 */
public class HotelHandler {

    // references needed for logic and message handling
    private Server server;
    private GameData gameData;
    private ServerData serverData;

    public HotelHandler(Server server, GameData gameData, ServerData serverData) {
        this.server = server;
        this.gameData = gameData;
        this.serverData = serverData;
    }

    /**
     * Gets sent when a player lands on a hotel field and nobody owns the hotel on it.
     *
     * @param playerId     the player that landed on the field
     * @param hotelFieldId the field id of the field the player landed on
     * @param amountToPay  the amount the player has to pay to buy this hotel
     */
    public void sendPlayerCanBuyHotelMessage(int playerId, int hotelFieldId, int amountToPay) {
        Log.info("sendPlayerCanBuyHotelMessage", "Player " + playerId + " can buy hotel on field (" +
                hotelFieldId + ") for " + amountToPay + "$");

        PlayerCanBuyHotelMessage playerCanBuyHotelMessage = new PlayerCanBuyHotelMessage(playerId, hotelFieldId, amountToPay);
        server.sendToAllTCP(playerCanBuyHotelMessage);
    }

    /**
     * Gets send when a player lands on a hotel field and somebody else is owning this hotel field. The player has to pay rent to the owner.
     * @param playerConnectionId the connection id of the player that landed on the hotel field
     * @param otherPlayerConnectionId the connection if of the player that owns the hotel
     * @param rentAmount the amount to pay the rent
     */
    public void sendPlayerPaysHotelRentMessage(int playerConnectionId, int otherPlayerConnectionId, int rentAmount) {
        Log.info("sendPlayerPaysHotelRentMessage", "Sending PlayerPaysHotelRentMessage message, player " + playerConnectionId + " has to pay " +
                rentAmount + "$ to player " + otherPlayerConnectionId);

        PlayerPaysHotelRentMessage playerPaysHotelRentMessage = new PlayerPaysHotelRentMessage(playerConnectionId, otherPlayerConnectionId, rentAmount);
        server.sendToAllTCP(playerPaysHotelRentMessage);
    }


}
