package com.mankomania.game.core.player;

import com.mankomania.game.core.fields.Position3;

import java.util.HashMap;

public class Player {
    private Position3[] position;
    private int money;
    private HashMap<Stock, Integer> stock = new HashMap<>();
    private HashMap<Hotel, Boolean> hotel = new HashMap<>();


    public Player() {
        money = 1000000;
        stock.put(Stock.BRUCHSTAHLAG, 0);
        stock.put(Stock.KURZSCHLUSSAG, 0);
        stock.put(Stock.TROCKENOEL, 0);
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

    public void buyStock(Stock stock, int amount) {
        int curr = this.stock.get(stock);
        this.stock.put(stock, curr + amount);
    }

    public void sellAllStock(Stock stock) {
        this.stock.put(stock, 0);
    }

    public void sellSomeStock(Stock stock, int amount) {
        int curr = this.stock.get(stock);
        if (amount > curr) {
            this.stock.put(stock, 0);
        } else {
            this.stock.put(stock, curr - amount);
        }
    }

    public boolean buyHotel(Hotel hotel) {
        if (this.hotel.size() > 0) {
            System.out.println("Already hotel in possession");
            return false;
        } else {
            this.hotel.put(hotel, true);
            return true;
        }
    }

    public int getAmountOfStock(Stock stock) {
        return this.stock.get(stock);
    }

    public boolean ownsHotel(Hotel hotel) {
        return this.hotel.containsKey(hotel);
    }

}
