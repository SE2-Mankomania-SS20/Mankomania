package com.mankomania.game.gamecore.client;

import com.mankomania.game.core.player.Player;
import com.mankomania.game.core.player.Stock;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public class StockTest {
   private Player player;
   @BeforeEach
    public void init(){
       player=new Player();
       player.buyStock(Stock.BRUCHSTAHLAG,5);

   }
   @AfterEach
    public void reset(){
        player=null;
   }

  /* @Test
    public void onePlayer_StockLose(){
       MankomaniaGame.getMankomaniaGame().getClient().getMessageHandler().sendStockResultMessage(1);
       Assertions.assertEquals(950000,player.getMoney());
   } */
}
