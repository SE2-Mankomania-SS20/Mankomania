package com.mankomania.game.gamecore.client;

import com.esotericsoftware.kryonet.Client;
import com.mankomania.game.core.data.GameData;
import com.mankomania.game.core.network.messages.clienttoserver.baseturn.DiceResultMessage;
import com.mankomania.game.core.network.messages.servertoclient.baseturn.MovePlayerToFieldMessage;
import com.mankomania.game.core.network.messages.servertoclient.baseturn.PlayerCanRollDiceMessage;
import com.mankomania.game.core.player.Player;
import com.mankomania.game.gamecore.util.ScreenManager;

/**
 * Class that handles incoming messages and trigger respective measures.
 */
public class MessageHandler {
    private GameData gameData;
    private Client client; // maybe use an intermediate handler for communication with the client instead of just a property

    public MessageHandler(Client client) {
        this.client = client;
        this.gameData = ScreenManager.getInstance().getGame().getGameData();
    }

    public void gotPlayerCanRollDiceMessage(PlayerCanRollDiceMessage message) {
        // TODO: notify respective player that he can roll the dice
        if (message.getPlayerId() == this.gameData.getLocalPlayer().getOwnConnectionId()) {
            System.out.println("[gotPlayerCanRollDiceMessage] canRollTheDice message had the same player id as the local player -> roll the dice here.");

            int randomRoll = (int) Math.ceil(Math.random() * 12);
            System.out.println("[gotPlayerCanRollDiceMessage] simulate rolling the dice... rolled " + randomRoll + ". sending this as result to the server.");
            System.out.println("[gotPlayerCanRollDiceMessage] sending that player " + this.gameData.getLocalPlayer().getOwnConnectionId() + " rolled " + randomRoll);

            DiceResultMessage diceResultMessage = DiceResultMessage.createDiceResultMessage(this.gameData.getLocalPlayer().getOwnConnectionId(), randomRoll);
//            this.client.sendTCP(diceResultMessage);


        } else {
            System.out.println("[gotPlayerCanRollDiceMessage] canRollTheDice message had other player id as the local player -> DO NOT roll the dice here.");
        }
    }

    public void gotMoveToFieldMessage(MovePlayerToFieldMessage message) {
        // TODO: write to HUD notification, center camera on player that is moving, move player on field, etc
        System.out.println("[gotMoveToFieldMessage] moving player " + message.getPlayerId() + " now from field " +
                this.gameData.getPlayerByConnectionId(message.getPlayerId()).getCurrentField() + " to field " + message.getFieldToMoveTo());

        Player playerMoving = this.gameData.getPlayerByConnectionId(message.getPlayerId());
        playerMoving.setCurrentField(message.getFieldToMoveTo());
    }
}
