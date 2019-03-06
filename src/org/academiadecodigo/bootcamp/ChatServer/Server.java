package org.academiadecodigo.bootcamp.ChatServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    LinkedList<Client> clientList;
    int clientId = 0;

    public void init(int portNumber, int threadNumber) throws IOException {

        ServerSocket serverSocket = new ServerSocket(portNumber);

        ExecutorService fixedPool = Executors.newFixedThreadPool(threadNumber);
        clientList = new LinkedList<>();

        while (true){
            Socket clientSocket = serverSocket.accept();
            System.out.println("connected to " + clientSocket.getLocalAddress());

            Client c = new Client(this, clientSocket, clientId);

            clientList.add(c);
            clientId++;

            c.init();

            fixedPool.submit(c);



        }
    }

    public void massSend(Socket clientSocket, String msg){

        if(msg.split(":\\s")[1].equalsIgnoreCase("/exit")){
            try {
                clientSocket.close();
                return;
            } catch (IOException e) {
                System.out.println("socket closing failed. you're here forever.");
            }
        }

        System.out.println("sending");

        msg = msg + "\n";

        for(Client c: clientList){
            c.receiveMessage(msg);
        }

    }
}
