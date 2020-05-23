package com.mankomania.game.core.player;

import com.badlogic.gdx.math.Vector3;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/*
 Created by Fabian Oraze on 02.05.20
 */

public class TestPlayer {
    private Player player1;
    private Player player2;
    private Player player3;
    private Player player4;
    private int startMoney;
    private int addMoney;
    private int loseMoney;
    private Stock bruchstahl;
    private Stock trockenoel;
    private Stock kurzschluss;

    @BeforeEach
    public void init() {
        player1 = new Player(78,11,new Vector3(),0);
        player2 = new Player(79,22,new Vector3(),1);
        player3 = new Player(80,33,new Vector3(),2);
        player4 = new Player(80,44,new Vector3(),3);
        startMoney = 1000000;
        addMoney = 10000;
        loseMoney = 15000;
        bruchstahl = Stock.BRUCHSTAHLAG;
        trockenoel = Stock.TROCKENOEL;
        kurzschluss = Stock.KURZSCHLUSSAG;
    }

    @AfterEach
    public void reset() {
        player1 = null;
        player2 = null;
        player3 = null;
        player4 = null;
        startMoney = 0;
        addMoney = 0;
        loseMoney = 0;
        bruchstahl = null;
        trockenoel = null;
        kurzschluss = null;
    }

    @Test
    public void checkStartBalanceOfPlayer() {

        assertEquals(startMoney, player1.getMoney());
    }

    @Test
    public void addMoneyToBalance() {

        player1.addMoney(addMoney);
        assertEquals(startMoney + addMoney, player1.getMoney());
    }

    @Test
    public void loseMoneyInBalance() {
        player2.loseMoney(loseMoney);
        assertEquals(startMoney - loseMoney, player2.getMoney());
    }

    @Test
    public void firstAddThenloseMoney() {

        player3.addMoney(addMoney);
        player3.loseMoney(loseMoney);
        assertEquals(startMoney + addMoney - loseMoney, player3.getMoney());
    }

    @Test
    public void checkStartStockOfPlayer() {

        int amount = 0;
        player4.getAmountOfStock(bruchstahl);
        assertEquals(amount, player1.getAmountOfStock(bruchstahl));
    }

    @Test
    public void buyStock() {

        int buyAmount = 1;
        player1.buyStock(bruchstahl, buyAmount);
        player1.getAmountOfStock(bruchstahl);
        assertEquals(buyAmount, player1.getAmountOfStock(bruchstahl));
    }

    @Test
    public void buyTwoStockSellOne() {

        int buyAmount = 2;
        int sellAmount = 1;
        player1.buyStock(bruchstahl, buyAmount);
        player1.getAmountOfStock(bruchstahl);
        player1.sellSomeStock(bruchstahl, sellAmount);
        assertEquals(1, player1.getAmountOfStock(bruchstahl));
    }

    @Test
    public void buyThreeStockSellAll() {

        int buyAmount = 3;
        player1.buyStock(trockenoel, buyAmount);
        player1.getAmountOfStock(trockenoel);
        player1.sellAllStock(trockenoel);
        assertEquals(0, player1.getAmountOfStock(trockenoel));
    }

    @Test
    public void sellSomeStockBeforeBuyingStock() {

        int sellAmount = 0;
        player4.sellSomeStock(bruchstahl, sellAmount);
        player4.getAmountOfStock(bruchstahl);
        assertEquals(0, player1.getAmountOfStock(bruchstahl));
    }

    @Test
    public void sellAllStockBeforeBuyingStock() {

        player4.sellAllStock(trockenoel);
        player4.getAmountOfStock(trockenoel);
        assertEquals(0, player1.getAmountOfStock(trockenoel));
    }

    @Test
    public void sellAllStock() {

        player2.buyStock(kurzschluss, 3);
        player2.getAmountOfStock(kurzschluss);
        assertEquals(3, player2.getAmountOfStock(kurzschluss));
        player2.sellAllStock(kurzschluss);
        assertEquals(0, player2.getAmountOfStock(kurzschluss));
    }

    @Test
    public void sellSomeStock() {

        player2.buyStock(trockenoel, 3);
        player2.getAmountOfStock(trockenoel);
        player2.sellSomeStock(trockenoel, 2);
        assertEquals(1, player2.getAmountOfStock(trockenoel));
    }

    @Test
    public void sellMoreStockThanInPossession() {

        player1.buyStock(kurzschluss, 3);
        player1.sellSomeStock(kurzschluss, 5);
        assertEquals(0, player1.getAmountOfStock(kurzschluss));
    }
}
