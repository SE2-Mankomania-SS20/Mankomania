package com.mankomania.game.server.minigames;

import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;
import com.mankomania.game.core.network.messages.clienttoserver.roulette.RouletteStakeMessage;
import com.mankomania.game.core.network.messages.servertoclient.roulette.RouletteResultAllPlayer;
import com.mankomania.game.core.network.messages.servertoclient.roulette.RouletteResultMessage;
import com.mankomania.game.core.network.messages.servertoclient.roulette.StartRouletteServer;
import com.mankomania.game.server.data.GameState;
import com.mankomania.game.server.data.ServerData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RouletteHandler {
    private ServerData serverData;
    private Server server;
    private ArrayList<RouletteStakeMessage> inputPlayerBets;
    private int[] arrayNumberWheel = {32, 15, 19, 4, 21, 2, 25, 17, 34, 6, 27, 13, 36, 11, 30, 8, 23, 10, 5, 24, 16, 33, 1, 20, 14, 31, 9, 22, 18, 29, 7, 28, 12, 35, 3, 26};
    private HashMap<Integer, Integer> money;
    private List<RouletteResultMessage> resultsList;


    public RouletteHandler(ServerData serverData, Server server) {
        this.serverData = serverData;
        this.server = server;
        this.inputPlayerBets = new ArrayList<>();
        this.money = new HashMap<>();
    }

    public void startGame() {
        server.sendToAllTCP(new StartRouletteServer());
        Log.info("start Minigame Roulette");
        serverData.setCurrentState(GameState.WAIT_FOR_ALL_ROULETTE_BET);
    }

    public void setInputPlayerBet(int playerId, RouletteStakeMessage rouletteStakeMessage) {
        if (serverData.getCurrentState() != GameState.WAIT_FOR_ALL_ROULETTE_BET) {
            Log.error("MiniGame Roulette", "Ignoring Player " + playerId + " try to bet roulette");
            return;
        }

        inputPlayerBets.add(rouletteStakeMessage);
        //check, if size is equals the size of the bets of the players
        if (inputPlayerBets.size() == serverData.getGameData().getPlayers().size()) {
            generateResult();
        }
    }

    public void generateResult() {
        int generateNumber = (int) (Math.random() * 36 + 1);
        String numberInString = String.valueOf(generateNumber); //generate Number
        String color = findColor(generateNumber); //the color of the number

        resultsList = new ArrayList<>();

        for (int i = 0; i < inputPlayerBets.size(); i++) {
            int inputPlayerBet = inputPlayerBets.get(i).getRsmSelectedBet(); //1-36, 37, 38, 39, 40, 41
            boolean winOrLost = resultRoulette(inputPlayerBet, generateNumber);
            int wonMoney = generateAmountWin(winOrLost, inputPlayerBets.get(i).getRsmAmountBet()); //return won money

            int playerIndex = inputPlayerBets.get(i).getRsmPlayerIndex();
            serverData.getGameData().getPlayers().get(playerIndex).addMoney(wonMoney);

            money.put(inputPlayerBets.get(i).getRsmPlayerIndex(), wonMoney);

            //generateRouletteMessage
            String resultOfRouletteWheel = numberInString + ", " + color;
            resultsList.add(generateRouletteMessage(inputPlayerBets.get(i).getRsmPlayerIndex(), inputPlayerBet, resultOfRouletteWheel, winOrLost, wonMoney)); //win or lost of all players

        }

        RouletteResultAllPlayer rouletteResultAllPlayer = new RouletteResultAllPlayer(resultsList); //list of results of all player
        this.server.sendToAllTCP(rouletteResultAllPlayer);

        serverData.movePlayer(false, false);
        clearInputs();
    }

    public RouletteResultMessage generateRouletteMessage(int playerId, int bet, String resultOfRouletteWheel, boolean winOrLost, int amountWin) {
        RouletteResultMessage rouletteResultMessage = new RouletteResultMessage();
        rouletteResultMessage.setPlayerIndex(playerId);
        rouletteResultMessage.setBet(bet);
        rouletteResultMessage.setResultOfRouletteWheel(resultOfRouletteWheel);
        rouletteResultMessage.setWinOrLost(winOrLost);
        rouletteResultMessage.setAmountWin(amountWin);
        return rouletteResultMessage;
    }

    public int generateAmountWin(boolean win, int amount) {
        if (win) {
            switch (amount) {
                case 5000:
                    return 150000;
                case 20000:
                    return 100000;
                case 50000:
                    return 80000;
                default:
                    return 0;
            }
        } else {
            switch (amount) {
                case 5000:
                    return -5000;
                case 20000:
                    return -20000;
                case 50000:
                    return -50000;
                default:
                    return 0;
            }
        }
    }

    public boolean resultRoulette(int chooseBetFromPlayer, int generateNumberServer) {
        if (chooseBetFromPlayer >= 36) {
            switch (chooseBetFromPlayer) {
                case 37: {
                    return (generateNumberServer >= 1 && generateNumberServer <= 12);
                }
                case 38: {
                    return (generateNumberServer >= 13 && generateNumberServer <= 24);
                }
                case 39: {
                    return (generateNumberServer >= 25 && generateNumberServer <= 36);
                }
                case 40: {
                    String foundColor = findColor(generateNumberServer);
                    return (foundColor.equals("red"));
                }
                case 41: {
                    String foundColor = findColor(generateNumberServer);
                    return (foundColor.equals("black"));
                }
                default:
                    return false;
            }
        } else {
            return (chooseBetFromPlayer == generateNumberServer);
        }
    }

    public String findColor(int random) {
        String color = "";
        for (int i = 0; i < arrayNumberWheel.length; i++) {
            if (arrayNumberWheel[i] == random) {
                if (i % 2 == 0) {
                    color = "red";
                } else {
                    color = "black";
                }
            }
        }
        return color;
    }

    public void startRouletteGame() {
        StartRouletteServer startRouletteServer = new StartRouletteServer();
        server.sendToAllTCP(startRouletteServer);
    }

    public void clearInputs() {
        this.inputPlayerBets.clear();
    }

    public List<RouletteStakeMessage> getInputPlayerBets() {
        return inputPlayerBets;
    }

    public void setInputPlayerBetsList(ArrayList<RouletteStakeMessage> inputPlayerBets) {
        this.inputPlayerBets = inputPlayerBets;
    }

}
