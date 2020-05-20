package com.mankomania.game.server;

import com.esotericsoftware.minlog.Log;
import com.mankomania.game.server.game.NetworkServer;

import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        NetworkServer server = null;
        try {
            server = new NetworkServer();
        } catch (IOException e) {
            Log.trace("Server starting error: ",e);
            System.exit(1);
        }
        Scanner sc = new Scanner(System.in);
        while(true) {
            String input = sc.nextLine();
            server.processCommand(input);
        }
    }
}
