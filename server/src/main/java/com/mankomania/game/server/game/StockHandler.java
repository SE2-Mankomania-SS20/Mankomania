package com.mankomania.game.server.game;

import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;
import com.mankomania.game.core.data.GameData;
import com.mankomania.game.core.network.messages.clienttoserver.stock.StockResultMessage;
import com.mankomania.game.core.network.messages.servertoclient.stock.EndStockMessage;
import com.mankomania.game.core.network.messages.servertoclient.stock.StartStockMessage;
import com.mankomania.game.core.player.Player;
import com.mankomania.game.core.player.Stock;
import com.mankomania.game.server.data.GameState;
import com.mankomania.game.server.data.ServerData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class StockHandler {
    private final Server refServer;
    private final ServerData refServerData;

    public StockHandler(Server refServer, ServerData refServerData) {
        this.refServer = refServer;
        this.refServerData = refServerData;
    }

    public void startGame() {
        int playerIndex = refServerData.getGameData().getCurrentPlayerTurnIndex();
        refServer.sendToAllTCP(new StartStockMessage(playerIndex));
        Log.info("MiniGame Stock Market", "Player " + (playerIndex + 1) + " started Stock Market miniGame");
        refServerData.setCurrentState(GameState.WAIT_STOCK_ROLL);
    }

    public void sendProfit(StockResultMessage stockResultMessage, GameData gameData) {
        int stockResultNR = stockResultMessage.getStockResult();
        int bruchstahlAG;
        int kurzschlussAG;
        int trockenoel;

        HashMap<Integer, Integer> profit = new HashMap<>();
        EndStockMessage esm = new EndStockMessage();
        List<Player> players = gameData.getPlayers();
        for (Player player : players) {
            if (stockResultNR == 1) {
                //BruchstahlAG++
                bruchstahlAG = player.getAmountOfStock(Stock.BRUCHSTAHLAG);
                esm.setStock(Stock.BRUCHSTAHLAG);
                esm.setRising(true);
                if (bruchstahlAG > 0) {
                    player.addMoney(bruchstahlAG * 10000);
                    profit.put(player.getPlayerIndex(), bruchstahlAG * 10000);
                }
            } else if (stockResultNR == 2) {
                //KurzschlussAG++
                kurzschlussAG = player.getAmountOfStock(Stock.KURZSCHLUSSAG);
                esm.setStock(Stock.KURZSCHLUSSAG);
                esm.setRising(true);
                if (kurzschlussAG > 0) {
                    player.addMoney(kurzschlussAG * 10000);
                    profit.put(player.getPlayerIndex(), kurzschlussAG * 10000);
                }
            } else if (stockResultNR == 3) {
                //Trockenoel++
                trockenoel = player.getAmountOfStock(Stock.TROCKENOEL);
                esm.setStock(Stock.TROCKENOEL);
                esm.setRising(true);
                if (trockenoel > 0) {
                    player.addMoney(trockenoel * 10000);
                    profit.put(player.getPlayerIndex(), trockenoel * 10000);
                }
            } else if (stockResultNR == 4) {
                //BruchstahlAG--
                bruchstahlAG = player.getAmountOfStock(Stock.BRUCHSTAHLAG);
                esm.setStock(Stock.BRUCHSTAHLAG);
                esm.setRising(false);
                if (bruchstahlAG > 0) {
                    player.loseMoney(bruchstahlAG * 10000);
                    profit.put(player.getPlayerIndex(), bruchstahlAG * 10000 * -1);
                }
            } else if (stockResultNR == 5) {
                //KurzschlussAG++
                kurzschlussAG = player.getAmountOfStock(Stock.KURZSCHLUSSAG);
                esm.setStock(Stock.KURZSCHLUSSAG);
                esm.setRising(false);
                if (kurzschlussAG > 0) {
                    player.loseMoney(kurzschlussAG * 10000);
                    profit.put(player.getPlayerIndex(), kurzschlussAG * 10000 * -1);
                }
            } else {
                //Trockenoel--
                trockenoel = player.getAmountOfStock(Stock.TROCKENOEL);
                esm.setStock(Stock.TROCKENOEL);
                esm.setRising(false);
                if (trockenoel > 0) {
                    player.loseMoney(trockenoel * 10000);
                    profit.put(player.getPlayerIndex(), trockenoel * 10000 * -1);
                }
            }
        }

        refServer.sendToAllTCP(esm);
        Log.info("[SendEndStockMessage] Result:");
        for (Map.Entry<Integer, Integer> me : profit.entrySet()) {
            Log.info("Player: " + me.getKey() + " Got: " + me.getValue());
        }
        Log.info("[SendEndStockMessage] Stock Market minigame was played!");
    }


    public void gotStockResult(StockResultMessage stockResultMessage) {
        sendProfit(stockResultMessage, refServerData.getGameData());
        refServerData.movePlayer(false, false);
    }
}
