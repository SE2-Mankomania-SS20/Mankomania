package com.mankomania.game.core.player;

import com.mankomania.game.core.fields.Position3;

import java.util.HashMap;

public class Player {
    private Position3[] position;
    private int money;
    private HashMap<StockEnum, Integer> stock = new HashMap<>();
    private HashMap<HotelEnum, Integer> hotel = new HashMap<>();


    public Player() {
        money = 1000000;
        stock.put(StockEnum.BRUCHSTAHLAG, 0);
        stock.put(StockEnum.KURZSCHLUSSAG, 0);
        stock.put(StockEnum.TROCKENÖL, 0);

    }

    public int getMoney() {
        return money;
    }

    public void addMoney(int amount) {
        this.money += amount;
    }

    public void loseMoney(int amount) {
        this.money -= amount;
    }

    public void setPositions(Position3[] pos) {
        this.position = pos;
    }

    public Position3[] getPosition() {
        return position;
    }

    public void buyStock(StockEnum stockEnum, int amount) {
        int curr = stock.get(stockEnum);
        stock.put(stockEnum, curr + amount);
    }

    public void sellAllStock(StockEnum stockEnum) {
        stock.put(stockEnum, 0);
    }

    public void sellSomeStock(StockEnum stockEnum, int amount) {
        int curr = stock.get(stockEnum);
        if (amount > curr) {
            stock.put(stockEnum, 0);
        } else {
            stock.put(stockEnum, curr - amount);
        }
    }

    public void buyHotel(HotelEnum hotelEnum) {
        if (hotel.size() > 0) {
            System.out.println("Already hotel in possession");
        } else {
            hotel.put(hotelEnum, 1);
        }
    }

    public int getAmountOfStock(StockEnum stockEnum) {
        return stock.get(stockEnum);
    }

    public boolean ownsHotel(HotelEnum hotelEnum) {
        return hotel.containsKey(hotelEnum);
    }

}
