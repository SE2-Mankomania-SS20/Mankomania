package com.mankomania.game.server.minigames;

import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;
import com.mankomania.game.core.network.messages.clienttoserver.roulette.RouletteStakeMessage;
import com.mankomania.game.core.network.messages.servertoclient.roulette.EndRouletteResultMessage;
import com.mankomania.game.core.network.messages.servertoclient.roulette.RouletteResultAllPlayer;
import com.mankomania.game.core.network.messages.servertoclient.roulette.RouletteResultMessage;
import com.mankomania.game.core.network.messages.servertoclient.roulette.StartRouletteServer;
import com.mankomania.game.core.player.Player;
import com.mankomania.game.server.data.GameState;
import com.mankomania.game.server.data.ServerData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RouletteHandler {
    private ServerData serverData;
    private Server server;
    private ArrayList<RouletteStakeMessage> inputPlayerBets;
    private String[] arrayColor = {"red", "black", "red", "black", "red", "black", "red", "black", "red", "black", "red", "black", "red", "black", "red", "black", "red", "black", "red", "black", "red", "black", "red", "black", "red", "black", "red", "black", "red", "black", "red", "black", "red", "black", "red", "black"};
    private int[] arrayNumberWheel = {32, 15, 19, 4, 21, 2, 25, 17, 34, 6, 27, 13, 36, 11, 30, 8, 23, 10, 5, 24, 16, 33, 1, 20, 14, 31, 9, 22, 18, 29, 7, 28, 12, 35, 3, 26};
    private HashMap<Integer, Integer> money;
    private List<Player> players;


    public RouletteHandler(ServerData serverData, Server server) {
        this.serverData = serverData;
        this.server = server;
        this.inputPlayerBets = new ArrayList<>();
        this.money = new HashMap<>();
        this.players = serverData.getGameData().getPlayers();
    }

    public void startGame() {
        server.sendToAllTCP(new StartRouletteServer());
        Log.info("Minigame Roulette startet");
        serverData.setCurrentState(GameState.WAIT_FOR_ALL_ROULETTE_BET);
    }

    public void setInputPlayerBet(int playerId, RouletteStakeMessage rouletteStakeMessage) {
        if (serverData.getCurrentState() != GameState.WAIT_FOR_ALL_ROULETTE_BET) {
            Log.error("MiniGame Roulette", "Ignoring Player try to bet roulette");
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
        String color = arrayColor[findColor(generateNumber)]; //the color of the number

        ArrayList<RouletteResultMessage> resultsList = new ArrayList<>();

        for (int i = 0; i < inputPlayerBets.size(); i++) {

            int inputPlayerBet = inputPlayerBets.get(i).getRsmSelectedBet(); //1-36, 37, 38, 39, 40, 41
            int amount = inputPlayerBets.get(i).getRsmAmountBet(); //5000, 20000, 50000
            boolean winOrLost = resultRoulette(inputPlayerBet, generateNumber, amount);
            int wonMoney = genereateAmountWin(winOrLost, inputPlayerBets.get(i).getRsmAmountBet()); //return won money

            if (players.get(i).getConnectionId() == inputPlayerBets.get(i).getRsmPlayerId()) {
                players.get(i).addMoney(wonMoney);
            }
            money.put(inputPlayerBets.get(i).getRsmPlayerId(), wonMoney);

            //generateRouletteMessage
            String resultOfRouletteWheel = numberInString + ", " + color;
            resultsList.add(generateRouletteMessage(inputPlayerBets.get(i).getRsmPlayerId(), inputPlayerBet, resultOfRouletteWheel, winOrLost, wonMoney)); //win or lost of all players
        }

        RouletteResultAllPlayer rouletteResultAllPlayer = new RouletteResultAllPlayer(resultsList); //list of results of all player
        this.server.sendToAllTCP(rouletteResultAllPlayer);

        serverData.movePlayer(false, false);
        clearInputs();
    }


    public RouletteResultMessage generateRouletteMessage(int playerId, int bet, String resultOfRouletteWheel, boolean winOrLost, int amountWin) {
        RouletteResultMessage rouletteResultMessage = new RouletteResultMessage();
        rouletteResultMessage.setPlayerID(playerId);
        rouletteResultMessage.setBet(bet);
        rouletteResultMessage.setResultOfRouletteWheel(resultOfRouletteWheel);
        rouletteResultMessage.setWinOrLost(winOrLost);
        rouletteResultMessage.setAmountWin(amountWin);
        return rouletteResultMessage;
    }

    public int genereateAmountWin(boolean win, int amount) {
        if (win) {
            switch (amount) {
                case 5000:
                    return 155000;
                case 20000:
                    return 120000;
                case 50000:
                    return 130000;
            }
        } else {
            switch (amount) {
                case 5000:
                    return -5000;
                case 20000:
                    return -20000;
                case 50000:
                    return -50000;
            }
        }
        return amount;
    }

    public boolean resultRoulette(int chooseBetFromPlayer, int generateNumberServer, int amountWinBet) {
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
                    String foundcolor = arrayColor[findColor(generateNumberServer)];
                    return (foundcolor.equals("red"));
                }
                case 41: {
                    String foundcolor = arrayColor[findColor(generateNumberServer)];
                    return (foundcolor.equals("black"));
                }
            }
        } else {
            return (chooseBetFromPlayer == generateNumberServer);
        }
        return false;
    }

    public int findColor(int random) {
        int counter = 0;
        for (int i = 0; i < arrayNumberWheel.length - 1; i++) {
            if (random == arrayNumberWheel[i]) {
                return counter;
            } else {
                counter++;
            }
        }
        return counter;
    }

    public void startRouletteGame() {
        //Server schickt jeden Client das das Minigame gestartet hat
        StartRouletteServer startRouletteServer = new StartRouletteServer();
        server.sendToAllTCP(startRouletteServer);
    }

    private void clearInputs() {
        this.inputPlayerBets.clear();
    }
}
