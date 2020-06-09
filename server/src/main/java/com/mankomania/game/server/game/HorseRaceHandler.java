package com.mankomania.game.server.game;

import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;
import com.mankomania.game.core.data.horserace.HorseRaceData;
import com.mankomania.game.core.data.horserace.HorseRacePlayerInfo;
import com.mankomania.game.core.network.messages.clienttoserver.horserace.HorseRaceSelection;
import com.mankomania.game.core.network.messages.servertoclient.Notification;
import com.mankomania.game.core.network.messages.servertoclient.horserace.HorseRaceStart;
import com.mankomania.game.core.network.messages.servertoclient.horserace.HorseRaceUpdate;
import com.mankomania.game.core.network.messages.servertoclient.horserace.HorseRaceWinner;
import com.mankomania.game.core.player.Player;
import com.mankomania.game.server.data.ServerData;

public class HorseRaceHandler {
    //necessary references
    private final Server refServer;
    private final ServerData refServerData;
    private final HorseRaceData refHorseRaceData;

    public HorseRaceHandler(Server refServer, ServerData refServerData) {
        this.refServer = refServer;
        this.refServerData = refServerData;
        refHorseRaceData = refServerData.getGameData().getHorseRaceData();
    }

    public void start() {
        refHorseRaceData.reset();
        Log.info("HorseRaceHandler", "start");
        refHorseRaceData.setCurrentPlayerIndex(refServerData.getGameData().getCurrentPlayerTurnIndex());
        refServer.sendToAllTCP(new HorseRaceStart(refServerData.getGameData().getCurrentPlayer().getPlayerIndex()));
    }

    public void processUpdate(HorseRaceSelection hrs) {
        refHorseRaceData.update(hrs);
        Player player = refServerData.getGameData().getPlayers().get(hrs.getHorseRacePlayerInfo().getPlayerIndex());
        player.loseMoney(hrs.getHorseRacePlayerInfo().getBetAmount());
        int next = (refHorseRaceData.getCurrentPlayerIndex() + 1) % refServerData.getGameData().getPlayers().size();

        if (refServerData.getGameData().getCurrentPlayerTurnIndex() == next) {
            end();
        } else {
            refHorseRaceData.setCurrentPlayerIndex(next);
            refServer.sendToAllTCP(new HorseRaceUpdate(refHorseRaceData));
        }
    }

    private int getWinner() {
        int winner = 0;
        double rand = Math.random();
        // 45% for 0
        if (rand > 0.45d && rand < 0.7) {
            winner = 1;
        } else if (rand > 0.7d && rand < 0.9) {
            winner = 2;
        } else if (rand > 0.9d) {
            winner = 3;
        }
        return winner;
    }

    public void end() {

        refHorseRaceData.setCurrentPlayerIndex(-1);
        refServer.sendToAllTCP(new HorseRaceUpdate(refHorseRaceData));

        int winner = getWinner();

        refServer.sendToAllTCP(new HorseRaceWinner(winner));

        for (HorseRacePlayerInfo horseRacePlayerInfo : refHorseRaceData.getHorseRacePlayerInfo()) {
            if (horseRacePlayerInfo.getHorseIndex() == winner) {
                int winMoney = (horseRacePlayerInfo.getHorseIndex() + 2) * horseRacePlayerInfo.getBetAmount();
                Player player = refServerData.getGameData().getPlayers().get(horseRacePlayerInfo.getPlayerIndex());
                player.addMoney(winMoney);
                refServer.sendToAllExceptTCP(player.getConnectionId(), new Notification("Player " + (player.getPlayerIndex() + 1) + " won at horse race: " + winMoney + "$"));
                refServer.sendToTCP(player.getConnectionId(), new Notification("You won the horse race: " + winMoney + "$"));
                break;
            }
        }

        refServerData.sendGameData();

        refServerData.movePlayer(false, false);
    }
}
