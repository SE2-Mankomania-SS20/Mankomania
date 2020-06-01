package com.mankomania.game.server.game;

import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;
import com.mankomania.game.core.data.GameData;
import com.mankomania.game.core.network.messages.clienttoserver.stock.StockResultMessage;
import com.mankomania.game.core.network.messages.servertoclient.stock.EndStockMessage;
import com.mankomania.game.core.player.Player;
import com.mankomania.game.core.player.Stock;
import com.mankomania.game.server.data.ServerData;

import java.util.HashMap;
import java.util.List;


public class StockHandler {
    private final Server refServer;
    private final ServerData refServerData;

    public StockHandler(Server refServer, ServerData refServerData) {
        this.refServer = refServer;
        this.refServerData = refServerData;
    }

    public HashMap<Integer, Integer> sendProfit(StockResultMessage stockResultMessage, GameData gameData) {
        int stockResultNR = stockResultMessage.getStockResult();
        int bruchstahlAG;
        int kurzschlussAG;
        int trockenoel;

        HashMap<Integer, Integer> profit = new HashMap<>();

        List<Player> players = gameData.getPlayers();
        if (stockResultNR == 1) {
            //BruchstahlAG++
            for (Player player : players) {
                bruchstahlAG = player.getAmountOfStock(Stock.BRUCHSTAHLAG);
                if (bruchstahlAG > 0) {
                    player.addMoney(bruchstahlAG * 10000);
                    profit.put(player.getConnectionId(), bruchstahlAG * 10000);
                }
            }
        } else if (stockResultNR == 2) {
            //KurzschlussAG++
            for (Player player : players) {
                kurzschlussAG = player.getAmountOfStock(Stock.KURZSCHLUSSAG);
                if (kurzschlussAG > 0) {
                    player.addMoney(kurzschlussAG * 10000);
                    profit.put(player.getConnectionId(), kurzschlussAG * 10000);
                }
            }
        } else if (stockResultNR == 3) {
            //Trockenoel++
            for (Player player : players) {
                trockenoel = player.getAmountOfStock(Stock.TROCKENOEL);
                if (trockenoel > 0) {
                    player.addMoney(trockenoel * 10000);
                    profit.put(player.getConnectionId(), trockenoel * 10000);
                }
            }
        } else if (stockResultNR == 4) {
            //BruchstahlAG--
            for (Player player : players) {
                bruchstahlAG = player.getAmountOfStock(Stock.BRUCHSTAHLAG);
                if (bruchstahlAG > 0) {
                    player.loseMoney(bruchstahlAG * 10000);
                    profit.put(player.getConnectionId(), bruchstahlAG * 10000 * -1);
                }
            }
        } else if (stockResultNR == 5) {
            //KurzschlussAG++
            for (Player player : players) {
                kurzschlussAG = player.getAmountOfStock(Stock.KURZSCHLUSSAG);
                if (kurzschlussAG > 0) {
                    player.loseMoney(kurzschlussAG * 10000);
                    profit.put(player.getConnectionId(), kurzschlussAG * 10000 * -1);
                }
            }
        } else {
            //Trockenoel--
            for (Player player : players) {
                trockenoel = player.getAmountOfStock(Stock.TROCKENOEL);
                if (trockenoel > 0) {
                    player.loseMoney(trockenoel * 10000);
                    profit.put(player.getConnectionId(), trockenoel * 10000 * -1);
                }
            }
        }

        return profit;
    }

    public void sendEndStockMessage(HashMap<Integer, Integer> profit) {
        EndStockMessage e = new EndStockMessage();
        e.setPlayerProfit(profit);
        refServer.sendToAllTCP(e);
        Log.info("[SendEndStockMessage]");
    }

    public void gotStockResult(StockResultMessage stockResultMessage) {
        //TODO: STATE AM ENDE
        HashMap<Integer, Integer> profit = sendProfit(stockResultMessage, refServerData.getGameData());
        sendEndStockMessage(profit);
    }
}
