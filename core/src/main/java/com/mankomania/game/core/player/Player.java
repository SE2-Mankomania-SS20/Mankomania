package com.mankomania.game.core.player;

import com.mankomania.game.core.fields.Position3;

import java.util.HashMap;

public class Player {
    private Position3[] positions;
    private int money;
    private HashMap <String, Integer> stock = new HashMap<>();
    private HashMap <String, Integer> hotel = new HashMap<>();


    public Player() {
        money = 1000000;
        stock.put("Bruchstahl-AG",0);
        stock.put("Kurzschluss-Versorungs-AG",0);
        stock.put("Trockenöl-AG",0);

        hotel.put("Schloss Dietrich",0);
        hotel.put("Hotel Garnie",0);
        hotel.put("Hotel Sehblick",0);
        hotel.put("Hotel Ruhe Sanft",0);
        hotel.put("Hotel Willa Nicht",0);
        hotel.put("Hotel Santa Fu",0);


    }










}
