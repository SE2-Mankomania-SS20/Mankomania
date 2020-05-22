package com.mankomania.game.gamecore.client;

import com.mankomania.game.core.player.Player;
import com.mankomania.game.core.player.Stock;
import com.mankomania.game.gamecore.MankomaniaGame;
import com.mankomania.game.gamecore.StockExchange.AktienBörse;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class StockTest {
   private Player player;
   @BeforeEach
    public void init(){
       player=new Player(1,1);
       player.buyStock(Stock.BRUCHSTAHLAG,5);
   }
   @AfterEach
    public void reset(){
        player=null;
   }
   @Test
    public void onePlayer_normalMoneyAmount(){
       Assertions.assertEquals(player.getMoney(),1000000);
   }
   @Test
    public void onePlayer_StockLose(){
       MankomaniaGame.getMankomaniaGame().getClient().getMessageHandler().sendStockResultMessage(1);
       Assertions.assertEquals(950000,player.getMoney());
   }
}
