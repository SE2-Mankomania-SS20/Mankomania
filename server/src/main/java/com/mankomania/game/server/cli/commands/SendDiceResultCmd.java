package com.mankomania.game.server.cli.commands;

import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;
import com.mankomania.game.core.network.messages.clienttoserver.baseturn.DiceResultMessage;
import com.mankomania.game.core.player.Player;
import com.mankomania.game.server.cli.Command;
import com.mankomania.game.server.data.ServerData;

public class SendDiceResultCmd extends Command {
    public SendDiceResultCmd(Server server, ServerData serverData) {
        super(server, serverData);
    }

    @Override
    public String getMainCommand() {
        return "move";
    }

    @Override
    public int getNumberOfParams() {
        return 2;
    }

    @Override
    public void run(String... params) {
        int playerIndex = Integer.parseInt(params[0]);
        int moveCount = Integer.parseInt(params[1]);

        if (playerIndex < 0 || playerIndex >= this.getServerData().getGameData().getPlayers().size()) {
            Log.error("SendDiceResultCmd", "Tried moving player " + playerIndex + " " + moveCount + " steps, but playerIndex is out of range!");
            return;
        }

        Player player = this.getServerData().getGameData().getPlayers().get(playerIndex);

        // simulate a DiceResultMessage so the player moves
        DiceResultMessage message = new DiceResultMessage(playerIndex, moveCount);
        this.getServerData().gotDiceRollResult(message, player.getConnectionId());
    }

    @Override
    public String getHelpText() {
        return "simulate a dice roll result. params: <playerIndex, diceResult>";
    }
}
