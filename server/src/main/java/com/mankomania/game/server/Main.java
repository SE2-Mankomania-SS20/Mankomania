package com.mankomania.game.server;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.mankomania.game.core.network.ChatMessage;

import java.io.IOException;
import java.util.Scanner;

public class Main {

    static Server server = null;
    static Kryo kryo = null;

    public static void main(String[] args) throws IOException {
        System.out.println("Server is ready");
        Scanner sc = new Scanner(System.in);
        while(true) {
            String input = sc.nextLine();
            switch (input) {
                case "start": {
                    startGameServer();
                    break;
                }
                case "stop": {
                    stopGameServer();
                    break;
                }
                case "exit": {
                    stopGameServer();
                    System.out.println("Server is shutting down...");
                    System.exit(0);
                }
                default:{
                    System.out.println("Command \"" + input + "\" not recognized.");
                    break;
                }
            }
        }
    }

    private static void startGameServer() throws IOException {
        if(server == null) {
            server = new Server();
            server.start();
            server.bind(53211);

            kryo = server.getKryo();
            kryo.register(ChatMessage.class);

            server.addListener(new Listener() {
                public void received (Connection connection, Object object) {
                    if (object instanceof ChatMessage) {
                        ChatMessage request = (ChatMessage)object;
                        System.out.println(request.text);

                        ChatMessage response = new ChatMessage("mes from server");
                        connection.sendTCP(response);
                    }
                }
            });

            System.out.println("Server is starting...");
        } else {
            System.out.println("Server is already running...");
        }
    }

    static public void stopGameServer() {
        if(server != null) {
            server.stop();
            server = null;
            kryo = null;
            System.out.println("Server stopping...");
        }
    }
}
