package com.mankomania.game.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.mankomania.game.core.network.ChatMessage;

import java.io.IOException;

import com.mankomania.game.core.network.NetworkConstants;

public class NetworkServer extends Server {
    private Server server;
    private ServerData data;

    public NetworkServer() throws IOException {
        server = new Server();
        server.start();
        server.bind(NetworkConstants.TCP_PORT);


        data = new ServerData();


        server.getKryo().register(ChatMessage.class);

        server.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                if (object instanceof ChatMessage) {
                    ChatMessage request = (ChatMessage) object;
                    request.setText("Player " + connection.getID() + ": " + request.getText());
                    System.out.println(request.getText());

                    server.sendToAllTCP(request);

                }
            }

            @Override
            public void connected(Connection connection) {
                if (data.connectPlayer(connection)) {
                    System.out.println("Player has connected");
                } else {
                    System.err.println("Player could not connect!");
                    connection.close();
                }
            }

            @Override
            public void disconnected(Connection connection) {
                data.disconnectPlayer(connection);
                super.disconnected(connection);
            }
        });
        System.out.println("Server is ready...");
    }

    public void processCommand(String command) {
        switch (command) {
            case "exit": {
                server.stop();
                System.out.println("Server is shutting down...");
                System.exit(0);
            }
            default: {
                System.out.println("Command \"" + command + "\" not recognized.");
                break;
            }
        }
    }
}
