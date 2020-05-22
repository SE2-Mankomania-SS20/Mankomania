package com.mankomania.game.core.player;

import com.badlogic.gdx.math.Vector3;

import java.util.HashMap;

public class Player {

    private final int ownConnectionId;
    private int money;
    private Vector3[] position;
    private final HashMap<Stock, Integer> stocks;

    private int currentField; // field id of the field the player is currently on
    private int fieldID;

    public Player(int startingField, int connectionId) {
        money = 1000000;
        stocks = new HashMap<>();
        stocks.put(Stock.BRUCHSTAHLAG, 0);
        stocks.put(Stock.KURZSCHLUSSAG, 0);
        stocks.put(Stock.TROCKENOEL, 0);

        this.currentField = startingField;
        this.ownConnectionId = connectionId;
    }

    public Vector3[] getPosition() {
        return position;
    }

    public void setPositions(Vector3[] pos) {
        this.position = pos;
    }

    public void buyStock(Stock stock, int amount) {
        int curr = stocks.get(stock);
        stocks.put(stock, curr + amount);
    }

    public void sellAllStock(Stock stock) {
        stocks.put(stock, 0);
    }

    public void sellSomeStock(Stock stock, int amount) {
        int curr = stocks.get(stock);
        if (amount > curr) {
            stocks.put(stock, 0);
        } else {
            stocks.put(stock, curr - amount);
        }
    }
    public int getAmountOfStock(Stock stock) {
        return stocks.get(stock);
    }

    public int getMoney() {
        return money;
    }

    public void addMoney(int amount) {
        money += amount;
    }

    public void loseMoney(int amount) {
        money -= amount;
    }

    public int getCurrentField() {
        return currentField;
    }

    public void setCurrentField(int currentField) {
        this.currentField = currentField;
    }

    public int getOwnConnectionId() {
        return ownConnectionId;
    }

    public void setFieldID(int fieldID) {
        this.fieldID = fieldID;
    }

    public int getFieldID() {
        return this.fieldID;
    }
}
