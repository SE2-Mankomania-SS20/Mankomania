package com.mankomania.game.server.cli.commands;

import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;
import com.mankomania.game.core.player.Player;
import com.mankomania.game.server.cli.Command;
import com.mankomania.game.server.data.ServerData;

public class GetMoneyCmd extends Command {
    public GetMoneyCmd(Server server, ServerData serverData) {
        super(server, serverData);
    }

    @Override
    public String getMainCommand() {
        return "getmoney";
    }

    @Override
    public int getNumberOfParams() {
        return 1;
    }

    @Override
    public void run(String... params) {
        // param 1 is player index
        int playerIndex = Integer.parseInt(params[0]);
        if (playerIndex >= this.getServerData().getGameData().getPlayers().size()) {
            Log.error("GetMoneyCommand", "Tried getting money amount from player " + playerIndex + " but this is out of range!");
            return;
        }

        Player player = this.getServerData().getGameData().getPlayers().get(playerIndex);
        Log.info("GetMoneyCommand", "Money amount of player " + playerIndex + ": " + player.getMoney());
    }
}
