package com.mankomania.game.gamecore.client;

import com.badlogic.gdx.graphics.Color;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.minlog.Log;
import com.mankomania.game.core.data.GameData;

import com.mankomania.game.core.fields.types.Field;
import com.mankomania.game.core.fields.types.HotelField;
import com.mankomania.game.core.network.messages.clienttoserver.baseturn.DiceResultMessage;
import com.mankomania.game.core.network.messages.clienttoserver.baseturn.IntersectionSelection;
import com.mankomania.game.core.network.messages.clienttoserver.baseturn.TurnFinished;
import com.mankomania.game.core.network.messages.servertoclient.GameUpdate;
import com.mankomania.game.core.network.messages.servertoclient.Notification;
import com.mankomania.game.core.network.messages.servertoclient.baseturn.PlayerCanRollDiceMessage;
import com.mankomania.game.core.network.messages.servertoclient.baseturn.PlayerMoves;
import com.mankomania.game.core.network.messages.servertoclient.stock.EndStockMessage;
import com.mankomania.game.core.network.messages.clienttoserver.stock.StockResultMessage;
import com.mankomania.game.core.network.messages.clienttoserver.trickyone.RollDiceTrickyOne;
import com.mankomania.game.core.network.messages.clienttoserver.trickyone.StopRollingDice;
import com.mankomania.game.core.network.messages.servertoclient.trickyone.CanRollDiceTrickyOne;
import com.mankomania.game.core.network.messages.servertoclient.trickyone.EndTrickyOne;
import com.mankomania.game.core.network.messages.servertoclient.hotel.PlayerBoughtHotelMessage;
import com.mankomania.game.core.network.messages.servertoclient.hotel.PlayerCanBuyHotelMessage;
import com.mankomania.game.core.network.messages.servertoclient.hotel.PlayerPaysHotelRentMessage;
import com.mankomania.game.core.network.messages.clienttoserver.hotel.PlayerBuyHotelDecision;
import com.mankomania.game.core.player.Player;
import com.mankomania.game.core.player.Stock;
import com.mankomania.game.gamecore.MankomaniaGame;
import java.util.Map;

/**
 * Class that handles incoming messages and trigger respective measures.
 */
public class MessageHandler {
    private final GameData gameData;
    private final Client client; // maybe use an intermediate handler for communication with the client instead of just a property

    public MessageHandler(Client client) {
        this.client = client;
        gameData = MankomaniaGame.getMankomaniaGame().getGameData();
    }

    /**
     * Handles PlayerCanRollDiceMessage messages.
     *
     * @param message the incoming PlayerCanRollDiceMessage message
     */
    public void gotPlayerCanRollDiceMessage(PlayerCanRollDiceMessage message) {
        MankomaniaGame.getMankomaniaGame().getGameData().setCurrentPlayerTurn(message.getPlayerIndex());
        if (message.getPlayerIndex() == MankomaniaGame.getMankomaniaGame().getLocalClientPlayer().getPlayerIndex()) {
//            Log.info("gotPlayerCanRollDiceMessage", "canRollTheDice message had the same player id as the local player -> roll the dice here.");

            MankomaniaGame.getMankomaniaGame().getNotifier().add(new Notification(4, "You can roll the dice"));
        } else {
//            Log.info("gotPlayerCanRollDiceMessage", "canRollTheDice message had other player id as the local player -> DO NOT roll the dice here.");

            MankomaniaGame.getMankomaniaGame().getNotifier().add(new Notification(4, "Player " + (message.getPlayerIndex() + 1) + " on turn", gameData.getColorOfPlayer(message.getPlayerIndex()), Color.WHITE));
        }
    }

    /**
     * Helper for sending the dice result to the server.
     *
     * @param diceResult the rolled dice value
     */
    public void sendDiceResultMessage(int diceResult) {
        Log.info("sendDiceResultMessage", "Got dice roll value from DiceScreen (" + diceResult + ").");
        Log.info("sendDiceResultMessage", "Sending to server that local player (id: " + MankomaniaGame.getMankomaniaGame().getLocalClientPlayer().getConnectionId() + ") rolled a " + diceResult + ".");

        DiceResultMessage diceResultMessage = new DiceResultMessage(MankomaniaGame.getMankomaniaGame().getLocalClientPlayer().getPlayerIndex(), diceResult);
        client.sendTCP(diceResultMessage);
    }

    public void sendIntersectionSelectionMessage(int selectedField) {
        Log.info("sendIntersectionSelectionMessage", "sending that player selected field (" + selectedField + ") after intersection.");

        IntersectionSelection ism = new IntersectionSelection();
        ism.setPlayerIndex(MankomaniaGame.getMankomaniaGame().getLocalClientPlayer().getPlayerIndex());
        ism.setFieldIndex(selectedField);
        client.sendTCP(ism);
    }

    public void sendTurnFinished() {
        client.sendTCP(new TurnFinished());
    }

    public void gameUpdate(GameUpdate gameUpdate) {
        gameData.updateGameData(gameUpdate);
    }

    public void playerMoves(PlayerMoves playerMoves) {
        gameData.getCurrentPlayer().addToMovePath(playerMoves.getMoves());
    }


    /* ====== HOTEL ====== */
    public void gotPlayerCanBuyHotelMessage(PlayerCanBuyHotelMessage canBuyHotelMessage) {
        Field field = gameData.getFieldByIndex(canBuyHotelMessage.getHotelFieldId());
        // check if given field is a hotel field, if not, ignore this message
        if (!(field instanceof HotelField)) {
            Log.error("gotPlayerCanBuyHotelMessage", "Got PlayerCanBuyHotelMessage, but given field id was not a hotel field! Ignore it therefore.");
            return;
        }

        int hotelPrice = ((HotelField) field).getBuy();
        Log.info("gotPlayerCanBuyHotelMessage", "Got a PlayerCanBuyHotelMessage, player " + canBuyHotelMessage.getPlayerIndex() +
                " can buy hotel on field (" + canBuyHotelMessage.getHotelFieldId() + " for " + hotelPrice + "$");

        // display notifications
        if (canBuyHotelMessage.getPlayerIndex() == MankomaniaGame.getMankomaniaGame().getLocalClientPlayer().getPlayerIndex()) {
            MankomaniaGame.getMankomaniaGame().getNotifier().add(new Notification(4, "Chose to buy hotel " +
                    canBuyHotelMessage.getHotelFieldId() + " for " + hotelPrice + "$? Press B/N."));

            // store in GameData which hotelfield can be bought, but only if the local player is the one that can actually buy the hotel
            gameData.setBuyableHotelFieldId(canBuyHotelMessage.getHotelFieldId());

        } else {
            // display UI for other players
            MankomaniaGame.getMankomaniaGame().getNotifier().add(new Notification(4, "Player " + canBuyHotelMessage.getPlayerIndex() +
                    " can chose to buy hotel " + canBuyHotelMessage.getHotelFieldId() + " for " + hotelPrice + "$."));
        }
    }

    public void gotPlayerPayHotelRentMessage(PlayerPaysHotelRentMessage paysHotelRentMessage) {
        Field hotelField = gameData.getFieldByIndex(paysHotelRentMessage.getHotelFieldId());
        // check if given field is a hotel field, if not, ignore this message
        if (!(hotelField instanceof HotelField)) {
            Log.error("gotPlayerPayHotelRentMessage", "Got PlayerPayHotelRentMessage, but given field id was not a hotel field! Ignore it therefore.");
            return;
        }

        int hotelRent = ((HotelField) hotelField).getRent();
        Log.info("gotPlayerPayHotelRentMessage", "Got PlayerPayHotelRentMessage. Player " + paysHotelRentMessage.getPlayerIndex() +
                " has to pay " + hotelRent + "$ to player " + paysHotelRentMessage.getHotelOwnerPlayerId());

        MankomaniaGame.getMankomaniaGame().getNotifier().add(new Notification(4, "Player " + paysHotelRentMessage.getPlayerIndex() + " has to pay " +
                hotelRent + "$ to player " + paysHotelRentMessage.getHotelOwnerPlayerId() + "!"));
    }

    public void gotPlayerBoughtHotelMessage(PlayerBoughtHotelMessage boughtHotelMessage) {
        Log.info("gotPlayerBoughtHotelMessage", "Got PlayerBoughtHotelMessage. Player " + boughtHotelMessage.getPlayerIndex() +
                " bought hotel on field (" + boughtHotelMessage.getHotelFieldId() + ")");

        Field boughtHotelField = gameData.getFieldByIndex(boughtHotelMessage.getHotelFieldId());
        if (!(boughtHotelField instanceof HotelField)) {
            Log.error("gotPlayerBoughtHotelMessage", "Got a PlayerBoughtHotelMessage but the given field id is NOT a hotel, ignore it.");
            return;
        }

        // player paying the hotel price and managing ownership now gets done automatically with GameUpdate messages
        // just log the infos here now
        HotelField boughtHotelFieldCasted = (HotelField) boughtHotelField;
        Player player = gameData.getPlayers().get(boughtHotelMessage.getPlayerIndex());
        Log.info("gotPlayerBoughtHotelMessage", "Reducing the money of player " + boughtHotelMessage.getPlayerIndex() + " by " + boughtHotelFieldCasted.getBuy() +
                "$ to " + (player.getMoney() - boughtHotelFieldCasted.getBuy()) + " due to buying a hotel.");

        MankomaniaGame.getMankomaniaGame().getNotifier().add(new Notification(4, "Player " + boughtHotelMessage.getPlayerIndex() + " bought hotel " +
                boughtHotelField.getFieldIndex() + " for " + boughtHotelFieldCasted.getBuy() + "$!"));
    }

    public void sendPlayerBuyHotelDecisionMessage(boolean hotelBought) {
        int localPlayerIndex = MankomaniaGame.getMankomaniaGame().getLocalClientPlayer().getPlayerIndex();
        int hotelFieldIdToBeBought = this.gameData.getBuyableHotelFieldId();
        Log.info("sendPlayerBuyHotelDecisionMessage", "Send that this local player (" + localPlayerIndex + ") "
                + (hotelBought ? "bought" : "did not buy") + " the hotel on field (" + hotelFieldIdToBeBought + ") for  xxx $");

        PlayerBuyHotelDecision buyHotelDecision = new PlayerBuyHotelDecision(localPlayerIndex, hotelFieldIdToBeBought, hotelBought);
        client.sendTCP(buyHotelDecision);

        // reset the buyable field id just to be safe and avoid hard to find bugs
        this.gameData.setBuyableHotelFieldId(-1);
    }

    /* ====== STOCKS ====== */
    public void sendStockResultMessage(int stockResult) {
        Log.info("[sendStockResultMessage] Got Stock roll value from AktienBörse (" + stockResult + ").");
        Log.info("[sendStockResultMessage] Sending to server that local player (id: " + MankomaniaGame.getMankomaniaGame().getLocalClientPlayer().getConnectionId() + ") rolled a " + stockResult + ".");

        StockResultMessage stcokResultMessage = StockResultMessage.createStockResultMessage(MankomaniaGame.getMankomaniaGame().getLocalClientPlayer().getConnectionId(), stockResult);
        this.client.sendTCP(stcokResultMessage);
    }
    public void gotEndStockMessage(EndStockMessage endStockMessage) {
        Log.info("[gotEndStockMessage] Stock(BruchstahlAG): "+ MankomaniaGame.getMankomaniaGame().getLocalClientPlayer().getAmountOfStock(Stock.BRUCHSTAHLAG));
        Log.info("[gotEndStockMessage] Stock(KurzschlussAG): "+MankomaniaGame.getMankomaniaGame().getLocalClientPlayer().getAmountOfStock(Stock.BRUCHSTAHLAG));
        Log.info("[gotEndStockMessage] Stock(Trockenoel): "+MankomaniaGame.getMankomaniaGame().getLocalClientPlayer().getAmountOfStock(Stock.BRUCHSTAHLAG));
        String player="Player:";
        Map<Integer, Integer> profit = endStockMessage.getPlayerProfit();

        for (Map.Entry<Integer, Integer> profit_entry : profit.entrySet()) {
            int currentPlayerConnectionID = profit_entry.getKey();
            int amountOne = profit_entry.getValue();
            if (amountOne > 0) {
                this.gameData.getPlayerByConnectionId(currentPlayerConnectionID).addMoney(amountOne);
                Log.info(player+currentPlayerConnectionID+" got: "+amountOne+"$"+" new amount is:"+this.gameData.getPlayerByConnectionId(currentPlayerConnectionID).getMoney()+"$");
            } else if(amountOne < 0){
                this.gameData.getPlayerByConnectionId(currentPlayerConnectionID).loseMoney(amountOne);
                Log.info(player+currentPlayerConnectionID+" lost: "+amountOne+"$"+"new amount is:"+this.gameData.getPlayerByConnectionId(currentPlayerConnectionID).getMoney()+"$");
            } else { Log.info(player+currentPlayerConnectionID+" amount stated the same: "+amountOne+"$");}
        }
    }

    public void gotTrickyOneCanRollDiceMessage(CanRollDiceTrickyOne message) {
        //update only container for trickyOne date because at this point server still does not know
        //how the miniGame will end
        gameData.getTrickyOneData().setFirstDice(message.getFirstDice());
        gameData.getTrickyOneData().setSecondDice(message.getSecondDice());
        gameData.getTrickyOneData().setPot(message.getPot());
        gameData.getTrickyOneData().setRolledAmount(message.getRolledAmount());

    }

    public void gotEndTrickyOneMessage(EndTrickyOne message) {
        gameData.getPlayers().get(message.getPlayerIndex()).addMoney(message.getAmountWinLose());
        gameData.getTrickyOneData().setInputEnabled(false);
    }

    public void sendRollTrickyOneMessage() {
        client.sendTCP(new RollDiceTrickyOne(MankomaniaGame.getMankomaniaGame().getLocalClientPlayer().getPlayerIndex()));
    }

    public void sendStopTrickyOneMessage() {
        client.sendTCP(new StopRollingDice(MankomaniaGame.getMankomaniaGame().getLocalClientPlayer().getPlayerIndex()));
    }

    public void gotStartOfTrickyOne() {
        gameData.getTrickyOneData().setInputEnabled(true);
    }
}
