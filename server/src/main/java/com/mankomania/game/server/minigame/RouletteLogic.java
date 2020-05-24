package com.mankomania.game.server.minigame;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import com.mankomania.game.core.network.messages.clienttoserver.minigames.RouletteStakeMessage;
import com.mankomania.game.core.network.messages.servertoclient.minigames.RouletteResultMessage;
import com.mankomania.game.core.network.messages.servertoclient.minigames.StartRouletteServer;
import com.mankomania.game.server.data.ServerData;

import java.util.ArrayList;
import java.util.Map;

public class RouletteLogic {
    private ArrayList<Integer> players;
    private Map<Integer, Connection> userMap;
    private ServerData serverData;
    private Server server;
    private ArrayList<RouletteStakeMessage> inputPlayerBets;
    private String[] arrayColor = {"rot", "schwarz", "rot", "schwarz", "rot", "schwarz", "rot", "schwarz", "rot", "schwarz", "rot", "schwarz", "rot", "schwarz", "rot", "schwarz", "rot", "schwarz", "rot", "schwarz", "rot", "schwarz", "rot", "schwarz", "rot", "schwarz", "rot", "schwarz", "rot", "schwarz", "rot", "schwarz", "rot", "schwarz", "rot", "schwarz"};
    private int[] arrayNumberWheel = {32, 15, 19, 4, 21, 2, 25, 17, 34, 6, 27, 13, 36, 11, 30, 8, 23, 10, 5, 24, 16, 33, 1, 20, 14, 31, 9, 22, 18, 29, 7, 28, 12, 35, 3, 26};
    private String color;

    public RouletteLogic(ServerData serverData, Server server) {
        this.players = new ArrayList<>();
        this.serverData = serverData;
        this.server = server;
        this.inputPlayerBets = new ArrayList<>();
    }

    public void setUserMap(Map<Integer, Connection> userMap) {
        //jeden spieler individuell result zurück schicken
        this.userMap = userMap;
    }

    public void setInputPlayerBet(int playerId, RouletteStakeMessage rouletteStakeMessage) {
        inputPlayerBets.add(rouletteStakeMessage);
        //check, ob Size der Player gleich der Size der bets ist dann soll er berechnung machen und antwort schicken
        if (inputPlayerBets.size() == serverData.getGameData().getPlayers().size()) {
            generateResult();
        }
        //in Hashmap wird die PlayerId und der bet gespeichert
    }

    public void generateResult() {
        int number = (int) (Math.random() * 36 + 1);
        String numberInString = String.valueOf(number); //19
        String color = arrayColor[findColor(number)]; //rot


        for (int i = 0; i < inputPlayerBets.size(); i++) {
            RouletteResultMessage rouletteResultMessage = new RouletteResultMessage();
            //temp -> win or lost the minigame
            boolean temp;
            int inputPlayerBet = inputPlayerBets.get(i).getSelectedBet(); //input vom player bets
            int amount = inputPlayerBets.get(i).getAmountBet(); //amount 5000, 20000, 50000
            temp = resultRoulette(inputPlayerBet, number, amount); //win or lost
            rouletteResultMessage.setWinOrLost(temp); // message ob gewonnen oder verloren
            rouletteResultMessage.setPlayerID(inputPlayerBets.get(i).getPlayerId()); //message playerid
            rouletteResultMessage.setResultOfRouletteWheel(numberInString + "x"); //result number
            //rouletteResultMessage.setWinAmount(); GEWINN

            //userMap.get(inputPlayerBets.get(i).getPlayerId()).sendTCP(rouletteResultMessage); //player id

            //HASHMAP MESSAGE WER WIE VIEL GEWONNEN HAT, jeder Spieler
            //CONNECTION ID

        }

    }

    public boolean resultRoulette(int choosenBetFromPlayer, int generateNumberServer, int amountWinBet) {
        if (choosenBetFromPlayer >= 36) {
            switch (choosenBetFromPlayer) {
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
                    return (foundcolor.equals("rot"));
                }
                case 41: {
                    String foundcolor = arrayColor[findColor(generateNumberServer)];
                    return (foundcolor.equals("schwarz"));
                }
            }
        } else {
            return (choosenBetFromPlayer == generateNumberServer);
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


}
