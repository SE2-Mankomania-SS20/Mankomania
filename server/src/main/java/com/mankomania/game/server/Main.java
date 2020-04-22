package com.mankomania.game.server;

import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        NetworkServer server = null;
        try {
            server = new NetworkServer();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        Scanner sc = new Scanner(System.in);
        while(true) {
            String input = sc.nextLine();
            server.processCommand(input);
        }
    }
}
