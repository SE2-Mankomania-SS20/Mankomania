package com.mankomania.game.core.player;

import com.mankomania.game.core.fields.Position3;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/*********************************
 Created by Fabian Oraze on 02.05.20
 *********************************/

public class TestPlayer {
    private Player player1 = new Player();
    private Player player2 = new Player();
    private Player player3 = new Player();
    private Player player4 = new Player();
    private int startMoney;
    private int addMoney;
    private int loseMoney;
    private Stock bruchstahl;
    private Stock trockenoel;
    private Stock kurzschluss;
    private Hotel hotel1;
    private Hotel hotel2;


    @Before
    public void init() {

        startMoney = 1000000;
        addMoney = 10000;
        loseMoney = 15000;
        bruchstahl = Stock.BRUCHSTAHLAG;
        trockenoel = Stock.TROCKENOEL;
        kurzschluss = Stock.KURZSCHLUSSAG;
        hotel1 = Hotel.SCHLOSSDIETRICH;
        hotel2 = Hotel.HOTELSEHBLICK;
    }

    @After
    public void reset() {

        startMoney = 1000000;
        addMoney = 0;
        loseMoney = 0;
    }

    @Test
    public void checkStartBalanceOfPlayer () {

        assertEquals(startMoney, player1.getMoney());
    }

    @Test
    public void addMoneyToBalance () {

        player1.addMoney(addMoney);
        assertEquals(startMoney+addMoney, player1.getMoney());
    }

    @Test
    public void loseMoneyInBalance () {
        player2.loseMoney(loseMoney);
        assertEquals(startMoney-loseMoney, player2.getMoney());
    }

    @Test
    public void firstAddThenloseMoney () {

        player3.addMoney(addMoney);
        player3.loseMoney(loseMoney);
        assertEquals(startMoney+addMoney-loseMoney, player3.getMoney());
    }

    @Test
    public void checkStartStockOfPlayer () {

        int amount = 0;
        player4.getAmountOfStock(bruchstahl);
        assertEquals(amount, player1.getAmountOfStock(bruchstahl));
    }

    @Test
    public void buyStock() {

        int buyAmount = 1;
        player1.buyStock(bruchstahl,buyAmount);
        player1.getAmountOfStock(bruchstahl);
        assertEquals(buyAmount, player1.getAmountOfStock(bruchstahl));
    }

    @Test
    public void buyTwoStockSellOne() {

        int buyAmount = 2;
        int sellAmount = 1;
        player1.buyStock(bruchstahl,buyAmount);
        player1.getAmountOfStock(bruchstahl);
        player1.sellSomeStock(bruchstahl, sellAmount);
        assertEquals(1, player1.getAmountOfStock(bruchstahl));
    }

    @Test
    public void buyThreeStockSellAll() {

        int buyAmount = 3;
        player1.buyStock(trockenoel,buyAmount);
        player1.getAmountOfStock(trockenoel);
        player1.sellAllStock(trockenoel);
        assertEquals(0, player1.getAmountOfStock(trockenoel));
    }

    @Test
    public void sellSomeStockBeforeBuyingStock() {

        int sellAmount = 0;
        player4.sellSomeStock(bruchstahl,sellAmount);
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

        player2.buyStock(kurzschluss,3);
        player2.getAmountOfStock(kurzschluss);
        assertEquals(3, player2.getAmountOfStock(kurzschluss));
        player2.sellAllStock(kurzschluss);
        assertEquals(0, player2.getAmountOfStock(kurzschluss));
    }

    @Test
    public void sellSomeStock() {

        player2.buyStock(trockenoel,3);
        player2.getAmountOfStock(trockenoel);
        player2.sellSomeStock(trockenoel,2);
        assertEquals(1, player2.getAmountOfStock(trockenoel));
    }

    @Test
    public void buyHotel() {

        player3.buyHotel(hotel1);
        assertEquals(true,player3.ownsHotel(hotel1));
    }

    @Test
    public void buyHotelAlreadyPoessession() {

        player4.buyHotel(hotel1);
        assertEquals(false,player4.buyHotel(hotel1));
    }

    @Test
    public void checkPlayerOwnsHotel () {

        player1.ownsHotel(hotel2);
        assertEquals(false,player1.ownsHotel(hotel2));
    }












}
