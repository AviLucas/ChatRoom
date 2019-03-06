package org.academiadecodigo.bootcamp.ChatServer;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {

        Server server = new Server();
        try {
            server.init(8080, 10);
        } catch (IOException e) {
            System.out.println("Failed to initiate Server.");
        }
    }
}
