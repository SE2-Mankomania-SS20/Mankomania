package com.mankomania.game.gamecore.client;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.minlog.Log;
import com.mankomania.game.core.data.GameData;
import com.mankomania.game.core.network.messages.clienttoserver.baseturn.DiceResultMessage;
import com.mankomania.game.core.network.messages.clienttoserver.baseturn.IntersectionSelectedMessage;
import com.mankomania.game.core.network.messages.clienttoserver.minigames.RouletteStakeMessage;
import com.mankomania.game.core.network.messages.clienttoserver.minigames.StartRouletteClient;
import com.mankomania.game.core.network.messages.servertoclient.Notification;
import com.mankomania.game.core.network.messages.servertoclient.baseturn.MovePlayerToFieldAfterIntersectionMessage;
import com.mankomania.game.core.network.messages.servertoclient.baseturn.MovePlayerToFieldMessage;
import com.mankomania.game.core.network.messages.servertoclient.baseturn.MovePlayerToIntersectionMessage;
import com.mankomania.game.core.network.messages.servertoclient.baseturn.PlayerCanRollDiceMessage;
import com.mankomania.game.core.network.messages.servertoclient.minigames.StartRouletteServer;
import com.mankomania.game.core.player.Player;
import com.mankomania.game.gamecore.MankomaniaGame;
import com.mankomania.game.gamecore.util.Screen;
import com.mankomania.game.gamecore.util.ScreenManager;

/**
 * Class that handles incoming messages and trigger respective measures.
 */
public class MessageHandler {
    private GameData gameData;
    private Client client; // maybe use an intermediate handler for communication with the client instead of just a property

    public MessageHandler(Client client) {
        this.client = client;
        this.gameData = MankomaniaGame.getMankomaniaGame().getGameData();
    }

    /**
     * Handles PlayerCanRollDiceMessage messages.
     * @param message the incoming PlayerCanRollDiceMessage message
     */
    public void gotPlayerCanRollDiceMessage(PlayerCanRollDiceMessage message) {
        if (message.getPlayerId() == this.gameData.getLocalPlayer().getOwnConnectionId()) {
            Log.info("[gotPlayerCanRollDiceMessage] canRollTheDice message had the same player id as the local player -> roll the dice here.");

            MankomaniaGame.getMankomaniaGame().getNotifier().add(new Notification(4,"You can roll the dice"));
        } else {
            Log.info("[gotPlayerCanRollDiceMessage] canRollTheDice message had other player id as the local player -> DO NOT roll the dice here.");
        }
    }

    /**
     * Handles MovePlayerToFieldMessage messages.
     * @param message the incoming MovePlayerToFieldMessage message
     */
    public void gotMoveToFieldMessage(MovePlayerToFieldMessage message) {
        // TODO: write to HUD notification, center camera on player that is moving, move player on field, etc
        Log.info("[gotMoveToFieldMessage] moving player " + message.getPlayerId() + " now from field " +
                this.gameData.getPlayerByConnectionId(message.getPlayerId()).getCurrentField() + " to field " + message.getFieldToMoveTo());

        Player playerMoving = this.gameData.getPlayerByConnectionId(message.getPlayerId());
        playerMoving.setCurrentField(message.getFieldToMoveTo());

        this.gameData.setPlayerToNewField(message.getPlayerId(), message.getFieldToMoveTo());
    }

    /**
     * Helper for sending the dice result to the server.
     * @param diceResult the rolled dice value
     */
    public void sendDiceResultMessage(int diceResult) {
        Log.info("[sendDiceResultMessage] Got dice roll value from DiceScreen (" + diceResult + ").");
        Log.info("[sendDiceResultMessage] Sending to server that local player (id: " + this.gameData.getLocalPlayer().getOwnConnectionId() + ") rolled a " + diceResult + ".");

        DiceResultMessage diceResultMessage = DiceResultMessage.createDiceResultMessage(this.gameData.getLocalPlayer().getOwnConnectionId(), diceResult);
        this.client.sendTCP(diceResultMessage);
    }

    public void gotMoveToIntersectionMessage(MovePlayerToIntersectionMessage message) {
        Log.info("[MovePlayerToIntersectionMessage] moving player to (" + message.getFieldToMoveTo() + ")");
        Player playerMoving = this.gameData.getPlayerByConnectionId(message.getPlayerId());
        playerMoving.setCurrentField(message.getFieldToMoveTo());

        this.gameData.setPlayerToNewField(message.getPlayerId(), message.getFieldToMoveTo());

        Log.info("[MovePlayerToIntersectionMessage] need to send a path decision between (" + message.getSelectionOption1() + ") and (" + message.getSelectionOption2() + ")");
        this.gameData.setIntersectionSelectionOption1(message.getSelectionOption1());
        this.gameData.setIntersectionSelectionOption2(message.getSelectionOption2());
        // TODO: display UI to select intersection path
        if(message.getPlayerId() == this.gameData.getLocalPlayer().getOwnConnectionId()){
            MankomaniaGame.getMankomaniaGame().getNotifier().add(new Notification("Choose direction: PRESS I / O"));
        }
    }

    public void sendIntersectionSelectionMessage(int selectedField) {
        Log.info("[sendIntersectionSelectionMessage] sending that player selected field (" + selectedField + ") after intersection.");

        IntersectionSelectedMessage intersectionSelectedMessage = new IntersectionSelectedMessage();
        intersectionSelectedMessage.setPlayerId(this.gameData.getLocalPlayer().getOwnConnectionId());
        intersectionSelectedMessage.setFieldChosen(selectedField);
        this.client.sendTCP(intersectionSelectedMessage);
    }

    public void gotMoveAfterIntersectionMessage(MovePlayerToFieldAfterIntersectionMessage message) {
        Log.info("[gotMoveAfterIntersectionMessage] setting player " + message.getPlayerId() + " to field (" + message.getFieldToMoveTo() + ")");
        this.gameData.setPlayerToNewField(message.getPlayerId(), message.getFieldToMoveTo());
    }

    //Roulette Minigame
    public void startRouletteMessage () {
        //aufruf wenn der Spieler auf das Feld kommt -> UI von MankoField
        int playerID = this.gameData.getLocalPlayer().getOwnConnectionId();
        StartRouletteClient startRouletteClient = new StartRouletteClient(playerID);
    }
    //Handle StartRouletteServer message on client, wenn Nachricht kommt was soll dann passieren
    public void gotStartRouletteServer (StartRouletteServer startRouletteServer) {
        //jeder Spieler öffnet MINISPIEL
        Gdx.app.postRunnable(() -> ScreenManager.getInstance().switchScreen(Screen.ROULETTE_MINIGAME));
    }
    public void sendRouletteStackMessage (String bet, int amountbet) {
        //Auswahl der Einsätze
        int playerID = this.gameData.getLocalPlayer().getOwnConnectionId();
        RouletteStakeMessage rouletteStakeMessage = new RouletteStakeMessage(playerID, amountbet, bet);
        //die Message von Client und schickst an Server
        Log.info("send Roulettestackmessage");
        this.client.sendTCP(rouletteStakeMessage);
    }
}
