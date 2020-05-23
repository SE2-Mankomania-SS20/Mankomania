package com.mankomania.game.server.game;

import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;
import com.mankomania.game.core.data.GameData;
import com.mankomania.game.core.fields.types.HotelField;
import com.mankomania.game.core.network.messages.servertoclient.hotel.PlayerCanBuyHotelMessage;
import com.mankomania.game.core.network.messages.servertoclient.hotel.PlayerPaysHotelRentMessage;
import com.mankomania.game.core.player.Player;
import com.mankomania.game.server.data.ServerData;

/**
 * This class is used for all serverside logic and message handling involving hotels.
 */
public class HotelHandler {

    // references needed for logic and message handling
    private Server server;
    private GameData gameData;
    private ServerData serverData;

    /**
     * This function is called when a player landed on a field.
     * It handles checking whether the field is a hotel field and if it is a hotel field it initiates
     * all necessary actions and communications.
     *
     * Introduce a variable that returns only true if we actually landed on a field and
     * are therefore responsible for keeping the state flow going?
     * If we return true than, we have to be careful to end the turn when this action is finished. (!)
     * On the other side, every turn should end on a action being called eventually, therefore this will
     * maybe not needed in the end.
     *
     * @param playerId the current player's id
     * @param fieldId the id of the field the player landed on
     * @return true if it was actually a hotel field and we take responsibility for the further gameflow (i.e. ending turn)
     */
    public boolean handleHotelFieldAction(int playerId, int fieldId) {
        // check whether the field we landed on is a hotel field, if not, returning false
        if (!(this.gameData.getFieldById(fieldId) instanceof HotelField)) {
            return false;
        }

        Player playerThatOwnsTheHotel = this.gameData.getOwnerOfHotel(fieldId);
        // if nobody owns the hotel yet, the player can buy it
        if (playerThatOwnsTheHotel == null) {
            // check if the current player has already a hotel (if yes, abort, a player can only have one hotel)
            if (this.gameData.getHotelOwnedByPlayer(playerId) >= 0) {
                Log.info("Hotels", "Player " + playerId + " landed on unowned hotel field (" + fieldId +
                        "). he can't buy it though, since he already owns a hotel field!");

                // TODO:  end turn now, return true or simple return false to let the other handler call endturn lol
                return false;
            }

            // so the hotel is unowned and the player does not own an other hotel, therefore he can chose to buy it
            int hotelBuyPrice = ((HotelField) this.gameData.getFieldById(fieldId)).getBuy();
            Log.info("Hotels", "Player " + playerId + " landed on unowned hotel field (" + fieldId + "). " +
                    "he can choose to buy it for " + hotelBuyPrice);
            this.sendPlayerCanBuyHotelMessage(playerId, fieldId, hotelBuyPrice);

            // TODO: end turn here and return true
            return false;
        }

        // check if the player himself owns player owns the hotel
        if (playerThatOwnsTheHotel.getOwnConnectionId() == playerId) {
            Log.info("Hotels", "Player " + playerId + " landet on hotel field (" + fieldId + "). he owns this hotel, so do nothing.");

            // TODO: end turn and return true
            return false;
        }

        // if execution reaches down here, the hotel is owned by an other player -> current player has to pay rent
        int hotelRent = ((HotelField) this.gameData.getFieldById(fieldId)).getRent();
        this.sendPlayerPaysHotelRentMessage(playerId, playerThatOwnsTheHotel.getOwnConnectionId(), hotelRent);
        // TODO: end turn and return true
        return false;
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
