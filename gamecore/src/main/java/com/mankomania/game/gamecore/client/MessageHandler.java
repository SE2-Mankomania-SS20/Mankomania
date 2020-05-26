package com.mankomania.game.gamecore.client;

import com.badlogic.gdx.graphics.Color;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.minlog.Log;
import com.mankomania.game.core.data.GameData;
import com.mankomania.game.core.network.messages.clienttoserver.baseturn.DiceResultMessage;
import com.mankomania.game.core.network.messages.clienttoserver.baseturn.IntersectionSelectedMessage;
import com.mankomania.game.core.network.messages.clienttoserver.baseturn.StockResultMessage;
import com.mankomania.game.core.network.messages.servertoclient.Notification;
import com.mankomania.game.core.network.messages.servertoclient.baseturn.MovePlayerToFieldAfterIntersectionMessage;
import com.mankomania.game.core.network.messages.servertoclient.baseturn.MovePlayerToFieldMessage;
import com.mankomania.game.core.network.messages.servertoclient.baseturn.MovePlayerToIntersectionMessage;
import com.mankomania.game.core.network.messages.servertoclient.baseturn.PlayerCanRollDiceMessage;
import com.mankomania.game.core.network.messages.servertoclient.minigames.EndStockMessage;
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
        if (message.getPlayerIndex() == MankomaniaGame.getMankomaniaGame().getLocalClientPlayer().getPlayerIndex()) {
            Log.info("gotPlayerCanRollDiceMessage", "canRollTheDice message had the same player id as the local player -> roll the dice here.");

            MankomaniaGame.getMankomaniaGame().getNotifier().add(new Notification(4, "You can roll the dice"));
        } else {
            Log.info("gotPlayerCanRollDiceMessage", "canRollTheDice message had other player id as the local player -> DO NOT roll the dice here.");

            MankomaniaGame.getMankomaniaGame().getNotifier().add(new Notification(4, "Player " + (message.getPlayerIndex() + 1) + " on turn", gameData.getColorOfPlayer(message.getPlayerIndex()), Color.WHITE));
        }
    }

    /**
     * Handles MovePlayerToFieldMessage messages.
     *
     * @param message the incoming MovePlayerToFieldMessage message
     */
    public void gotMoveToFieldMessage(MovePlayerToFieldMessage message) {
        // TODO: write to HUD notification, center camera on player that is moving, move player on field, etc
        Log.info("gotMoveToFieldMessage", "moving player " + message.getPlayerIndex() + " now from field " +
                gameData.getPlayers().get(message.getPlayerIndex()).getCurrentField() + " to field " + message.getFieldToMoveTo());

        gameData.setPlayerToField(message.getPlayerIndex(), message.getFieldToMoveTo());
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
    public void gotMoveToIntersectionMessage(MovePlayerToIntersectionMessage message) {
        Log.info("gotMovePlayerToIntersectionMessage", "moving player to (" + message.getFieldIndex() + ")");

        gameData.setPlayerToField(message.getPlayerIndex(), message.getFieldIndex());

        Log.info("gotMovePlayerToIntersectionMessage", "need to send a path decision between (" + message.getSelectionOption1() + ") and (" + message.getSelectionOption2() + ")");
        gameData.setIntersectionSelectionOption1(message.getSelectionOption1());
        gameData.setIntersectionSelectionOption2(message.getSelectionOption2());

        if (message.getPlayerIndex() == MankomaniaGame.getMankomaniaGame().getLocalClientPlayer().getPlayerIndex()) {
            MankomaniaGame.getMankomaniaGame().getNotifier().add(new Notification("Choose direction: PRESS I / O"));
        }
    }

    public void sendIntersectionSelectionMessage(int selectedField) {
        Log.info("sendIntersectionSelectionMessage", "sending that player selected field (" + selectedField + ") after intersection.");

        IntersectionSelectedMessage ism = new IntersectionSelectedMessage();
        ism.setPlayerIndex(MankomaniaGame.getMankomaniaGame().getLocalClientPlayer().getPlayerIndex());
        ism.setFieldChosen(selectedField);
        client.sendTCP(ism);
    }

    public void gotMoveAfterIntersectionMessage(MovePlayerToFieldAfterIntersectionMessage message) {
        Log.info("gotMoveAfterIntersectionMessage", "setting player " + message.getPlayerIndex() + " to field (" + message.getFieldIndex() + ")");

        int fieldToMoveTo = message.getFieldIndex();
        gameData.setPlayerToField(message.getPlayerIndex(), fieldToMoveTo);

        // fields that are reached through taking the optionalPath: 15, 24, 55, 64
        // if we get one of this fields, set selectedOptional to true, so the player renderer knows which path to go
        if (fieldToMoveTo == 15 || fieldToMoveTo == 24 || fieldToMoveTo == 55 || fieldToMoveTo == 64) {
            gameData.setSelectedOptional(true);
        }
    }
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
}
