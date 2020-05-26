package com.mankomania.game.server.game;

import com.esotericsoftware.kryonet.Server;
import com.mankomania.game.core.data.GameData;
import com.mankomania.game.core.network.messages.clienttoserver.baseturn.StockResultMessage;
import com.mankomania.game.core.player.Player;
import com.mankomania.game.core.player.Stock;
import com.mankomania.game.server.data.ServerData;

import java.util.HashMap;
import java.util.List;

public class StockHanlder {
    public StockHanlder(Server refServer, ServerData refServerData) {

    }
    public HashMap<Integer,Integer>  sendProfit(StockResultMessage stockResultMessage,GameData gameData){
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

}
