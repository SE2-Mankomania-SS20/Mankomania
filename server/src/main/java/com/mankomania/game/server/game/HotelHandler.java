package com.mankomania.game.server.game;

import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;
import com.mankomania.game.core.data.GameData;
import com.mankomania.game.core.fields.types.HotelField;
import com.mankomania.game.core.network.messages.clienttoserver.hotel.PlayerBuyHotelDecision;
import com.mankomania.game.core.network.messages.servertoclient.hotel.PlayerBoughtHotelMessage;
import com.mankomania.game.core.network.messages.servertoclient.hotel.PlayerCanBuyHotelMessage;
import com.mankomania.game.core.network.messages.servertoclient.hotel.PlayerPaysHotelRentMessage;
import com.mankomania.game.core.player.Player;
import com.mankomania.game.server.data.GameState;
import com.mankomania.game.server.data.ServerData;

/**
 * This class is used for all serverside logic and message handling involving hotels.
 */
public class HotelHandler {
    public static final String HOTEL_CATEGORY = "Hotels";
    // references needed for logic and message handling
    private final Server server;
    private final GameData gameData;
    private final ServerData serverData;

    public HotelHandler(Server server, ServerData serverData) {
        this.server = server;
        this.serverData = serverData;

        this.gameData = this.serverData.getGameData();
    }

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
     * @param playerIndex the current player's id
     * @param fieldId the id of the field the player landed on
     * @return true if it was actually a hotel field and we take responsibility for the further gameflow (i.e. ending turn)
     */
    public boolean handleHotelFieldAction(int playerIndex, int fieldId) {
        // check whether the field we landed on is a hotel field, if not, returning false
        if (!(this.gameData.getFieldByIndex(fieldId) instanceof HotelField)) {
            Log.error(HOTEL_CATEGORY, "got field (" + fieldId + ") to handle hotel action. given field is not of type hotel tho. abort.");
            return false;
        }

        Player playerThatOwnsTheHotel = this.gameData.getOwnerOfHotel(fieldId);
        // if nobody owns the hotel yet, the player can buy it
        if (playerThatOwnsTheHotel == null) {
            // check if the current player has already a hotel (if yes, abort, a player can only have one hotel)
            if (this.gameData.getHotelOwnedByPlayer(playerIndex) != null) {
                Log.info(HOTEL_CATEGORY, "Player with index  " + playerIndex + " landed on unowned hotel field (" + fieldId +
                        "). he can't buy it though, since he already owns a hotel field!");

                this.serverData.endTurn();
                return false;
            }

            // so the hotel is unowned and the player does not own an other hotel, therefore he can chose to buy it
            int hotelBuyPrice = ((HotelField) this.gameData.getFieldByIndex(fieldId)).getBuy();
            Log.info(HOTEL_CATEGORY, "Player " + playerIndex + " landed on unowned hotel field (" + fieldId + "). " +
                    "he can choose to buy it for " + hotelBuyPrice);
            this.sendPlayerCanBuyHotelMessage(playerIndex, fieldId);

            // do not end the turn here, since we expect a answer whether the player wants to buy the hotel
            // state handling is done in sendPlayerCanBuyHotelMessage()
            return true;
        }

        // check if the player himself owns player owns the hotel
        if (playerThatOwnsTheHotel.getPlayerIndex() == playerIndex) {
            Log.info(HOTEL_CATEGORY, "Player with index " + playerIndex + " landed on hotel field (" + fieldId + "). he owns this hotel, so do nothing.");

            this.serverData.endTurn();
            return true;
        }

        // if execution reaches down here, the hotel is owned by an other player -> current player has to pay rent
        this.sendPlayerPaysHotelRentMessage(playerIndex, playerThatOwnsTheHotel.getPlayerIndex(), fieldId);

        this.serverData.endTurn();
        return true;
    }

    /**
     * Gets sent when a player lands on a hotel field and nobody owns the hotel on it.
     *
     * @param playerId     the player that landed on the field
     * @param hotelFieldId the field id of the field the player landed on
     */
    public void sendPlayerCanBuyHotelMessage(int playerId, int hotelFieldId) {
        Log.info("sendPlayerCanBuyHotelMessage", "Sending PlayerCanBuyHotelMessage: Player with idx " + playerId + " can buy hotel on field (" +
                hotelFieldId + ") for " + "xxx" + "$");

        PlayerCanBuyHotelMessage playerCanBuyHotelMessage = new PlayerCanBuyHotelMessage(playerId, hotelFieldId);
        server.sendToAllTCP(playerCanBuyHotelMessage);

        // go into waiting state
        this.serverData.setCurrentState(GameState.WAIT_HOTELBUY_DECISION);
    }

    /**
     * Gets send when a player lands on a hotel field and somebody else is owning this hotel field. The player has to pay rent to the owner.
     * @param playerIndex the connection id of the player that landed on the hotel field
     * @param otherPlayerIndex the connection if of the player that owns the hotel
     * @param hotelFieldId the id of the hotel field that the player landet on
     */
    public void sendPlayerPaysHotelRentMessage(int playerIndex, int otherPlayerIndex, int hotelFieldId) {
        Log.info("sendPlayerPaysHotelRentMessage", "Sending PlayerPaysHotelRentMessage message, player " + playerIndex + " has to pay " +
                "xxx" + "$ to player " + otherPlayerIndex + " from landing on field (" + hotelFieldId + ")");

        PlayerPaysHotelRentMessage playerPaysHotelRentMessage = new PlayerPaysHotelRentMessage(playerIndex, otherPlayerIndex, hotelFieldId);
        server.sendToAllTCP(playerPaysHotelRentMessage);
    }

    public void sendPlayerBoughtHotelMessage(int playerIndex, int hotelFieldId) {
        PlayerBoughtHotelMessage boughtHotelMessage = new PlayerBoughtHotelMessage(playerIndex, hotelFieldId);
        Log.info("sendPlayerBoughtHotelMessage", "Sending PlayerBoughtHotelMessage message, player " + playerIndex +
                " has bought hotel on field (" + hotelFieldId + ")");

        server.sendToAllTCP(boughtHotelMessage);
    }

    public void gotPlayerBuyHotelDecision(PlayerBuyHotelDecision playerBuyHotelDecision, int playerConnectionId) {
        // check if the player thats currently on turn sent this message, otherwise ignore it
        if (this.serverData.getCurrentPlayerTurnConnectionId() != playerConnectionId) {
            Log.error("gotPlayerBuyHotelDecision", "Got PlayerBuyHotelDecision from a player thats not on turn, ignore it.");
            return;
        }
        if (this.serverData.getCurrentState() != GameState.WAIT_HOTELBUY_DECISION) {
            Log.error("gotPlayerBuyHotelDecision", "Got PlayerBuyHotelDecision, but not in state WAIT_HOTELBUY_DECISION. Current state: " + this.serverData.getCurrentState());
            return;
        }


        // ignore if the player did not buy the hotel
        if (!playerBuyHotelDecision.isHotelBought()) {
            Log.info("gotPlayerBuyHotelDecision", "Player with index  " + playerBuyHotelDecision.getPlayerIndex() + " did not want to buy hotel field (" +
                    playerBuyHotelDecision.getHotelFieldId() + "), so going to simply end turn now.");
        } else {
            // TODO: check if its actually a HotelField
            HotelField hotelField = (HotelField)this.gameData.getFieldByIndex(playerBuyHotelDecision.getHotelFieldId());

            Log.info("gotPlayerBuyHotelDecision", "Player with index " + playerBuyHotelDecision.getPlayerIndex() + " did buy hotel hotelField (" +
                    playerBuyHotelDecision.getHotelFieldId() + ") for " + hotelField.getBuy() + "$!");

            Player player = this.gameData.getPlayers().get(playerBuyHotelDecision.getPlayerIndex());
            // reduce the money from the player
            Log.info("gotPlayerBuyHotelDecision", "Reducing players money from " + player.getMoney() + " to " + (player.getMoney() - hotelField.getBuy()));
            player.loseMoney(hotelField.getBuy());

            // give the ownership of the hotel to the player
            hotelField.setOwnerPlayerIndex(player.getPlayerIndex());

            // update all player that a hotel has been bought
            this.sendPlayerBoughtHotelMessage(player.getPlayerIndex(), playerBuyHotelDecision.getHotelFieldId());
        }

        this.serverData.endTurn();
    }

}
