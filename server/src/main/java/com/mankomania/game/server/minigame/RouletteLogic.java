package com.mankomania.game.server.minigame;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import com.mankomania.game.core.network.messages.clienttoserver.minigames.RouletteStakeMessage;
import com.mankomania.game.core.network.messages.servertoclient.minigames.RouletteResultMessage;
import com.mankomania.game.core.network.messages.servertoclient.minigames.StartRouletteServer;
import com.mankomania.game.server.data.ServerData;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class RouletteLogic {
   private ArrayList<Integer> players;
   private Map<Integer, Connection> userMap;
   private ServerData serverData;
   private Server server;
   private ArrayList <RouletteStakeMessage> bets;
   private String [] arrayColor = {"rot","schwarz","rot","schwarz","rot","schwarz","rot","schwarz","rot","schwarz","rot","schwarz","rot","schwarz","rot","schwarz","rot","schwarz","rot","schwarz","rot","schwarz","rot","schwarz","rot","schwarz","rot","schwarz","rot","schwarz","rot","schwarz","rot","schwarz","rot","schwarz"};
   private int [] arrayNumberWheel = {32,15,19,4,21,2,25,17,34,6,27,13,36,11,30,8,23,10,5,24,16,33,1,20,14,31,9,22,18,29,7,28,12,35,3,26};
   private String color;

   public RouletteLogic(ServerData serverData, Server server) {
      this.players = new ArrayList<>();
      this.serverData = serverData;
      this.server = server;
      this.bets = new ArrayList<>();
   }

   public void setUserMap (Map<Integer, Connection> userMap) {
      //jeden spieler individuell result zurück schicken
      this.userMap = userMap;
   }

   public void setBets (int playerId, RouletteStakeMessage rouletteStakeMessage) {
      bets.add(rouletteStakeMessage);
      //check, ob Size der Player gleich der Size der bets ist dann soll er berechnung machen und antwort schicken
      if(bets.size() == serverData.getPlayerList().size()) {
         generateResult();
      }
      //in Hashmap wird die PlayerId und der bet gespeichert
   }

   public void generateResult () {
      int number = (int)(Math.random()*36 +1);
      String numberInString = String.valueOf(number); //19
      String color =  arrayColor[findColor(number)]; //rot


      for (int i = 0; i < bets.size() ; i++) {
         RouletteResultMessage rouletteResultMessage = new RouletteResultMessage();
         boolean temp = false; //übergabe von win lost
         String eingabe = bets.get(i).getBet(); //input vom player bets
         int amount = bets.get(i).getAmountBet(); //amount 5000, 20000, 50000
         temp = resultRoulette(eingabe, number, amount); //win or lost
         rouletteResultMessage.setWinlost(temp); // message ob gewonnen oder verloren
         rouletteResultMessage.setPlayerID(bets.get(i).getPlayerId()); //message playerid
         rouletteResultMessage.setResult(numberInString); //result number

         //rouletteResultMessage.setWinAmount(); GEWINN

         userMap.get(bets.get(i).getPlayerId()).sendTCP(rouletteResultMessage); //player id

         //HASHMAP MESSAGE WER WIE VIEL GEWONNEN HAT, jeder Spieler
         //CONNECTION ID





      }

   }

   public boolean resultRoulette (String eingabe ,int random, int bet) {
      int zahl1_36 = Integer.parseInt(eingabe);

      boolean result = true;

      if (bet == 1) {
         if (random == zahl1_36) {
            result = true;
         } else {
            result = false;
         }
      } else if (bet == 2) {
         if (random >= 1 && random <=12) {
            result = true;
         } else {
            result = false;
         }
      }else if (bet == 3) {
         if (random >= 13 && random <=24) {
            result = true;
         } else {
            result = false;
         }
      }else if (bet == 4) {
         if (random >= 25 && random <= 36) {
            result = true;
         } else {
            result = false;
         }
      }else if (bet == 5) {
         String foundcolor = arrayColor[findColor(random)];
         if (foundcolor.equals(color)) {
            result = true;
         }else {
            result = false;
         }
      }
      else if (bet == 6) {
         String foundcolor = arrayColor[findColor(random)];
         if (foundcolor.equals(color)) {
            result = true;
         }else {
            result = false;
         }
      }
      return result;
   }

   public int findColor (int random) {
      int counter = 0;
      for (int i = 0; i < arrayNumberWheel.length -1; i++) {
         if (random == arrayNumberWheel[i]) {
            return counter;
         } else {
            counter++;
         }
      }
      return counter;
   }

   public void startRouletteGame () {
      //Server schickt jeden Client das das Minigame gestartet hat
      StartRouletteServer startRouletteServer = new StartRouletteServer();
      server.sendToAllTCP(startRouletteServer);
   }










}
