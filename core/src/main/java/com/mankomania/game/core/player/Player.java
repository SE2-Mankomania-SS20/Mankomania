package com.mankomania.game.core.player;

import com.badlogic.gdx.math.Vector3;

import java.util.EnumMap;
import java.util.HashMap;

public class Player {
    private Vector3[] position;
    private int currentField; // field id of the field the player is currently on
    private int ownConnectionId;
    private int money;
    private HashMap<Stock, Integer> stock = new HashMap<>();
    private EnumMap<Hotel, Boolean> hotel = new EnumMap<>(Hotel.class); // remove on of the hotel storage locations (either here or in gamedata, preferably not both)

    private int fieldID;


    public Player(int startingField, int connectionId) {
        money = 1000000;
        stock.put(Stock.BRUCHSTAHLAG, 0);
        stock.put(Stock.KURZSCHLUSSAG, 0);
        stock.put(Stock.TROCKENOEL, 0);

        this.currentField = startingField;
        this.ownConnectionId = connectionId;
    }


    public Vector3[] getPosition() {
        return position;
    }

    public void setPositions(Vector3[] pos) {
        this.position = pos;
    }

    public void movePlayer(int newField) {
        this.currentField = newField;
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


    /* === GETTER === */
    public int getMoney() {
        return money;
    }

    public void addMoney(int amount) {
        this.money += amount;
    }

    public void loseMoney(int amount) {
        this.money -= amount;
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
