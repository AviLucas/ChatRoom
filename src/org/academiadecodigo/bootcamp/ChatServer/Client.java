package org.academiadecodigo.bootcamp.ChatServer;

import java.io.*;
import java.net.Socket;
import java.util.regex.PatternSyntaxException;

public class Client implements Runnable {

    Socket clientSocket;
    Server server;
    int id;
    boolean command;
    String user = "user";
    DataOutputStream receive;
    BufferedReader send;
    String toSend;

    public Client(Server server, Socket clientSocket, int id){

        this.server = server;
        this.clientSocket = clientSocket;
        this.id = id;
    }

    public void init(){
        try {
            createSockets();
            System.out.println("connection established");
        } catch (IOException e) {
            System.out.println("Failed to create client sockets.");
        }

        try {
            receive.writeBytes("Welcome to the Server, for help use /help\n");
        } catch (IOException e) {
            System.out.println("whoops");
        }

    }

    private void createSockets() throws IOException {

        send = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        receive = new DataOutputStream(clientSocket.getOutputStream());
    }

    public void receiveMessage(String str){
        System.out.println("receiving");

        try {
            receive.writeBytes(str);
        } catch (IOException e) {
            System.out.println("Error getting message.");
        }
    }

    @Override
    public void run() {
        System.out.println("running");
        while (!clientSocket.isClosed()) {
            try {
                toSend = send.readLine();

                if (toSend.startsWith("/") && !toSend.startsWith("/exit")) {
                    command = specialCommand(toSend);
                }
                if(!command) {

                    System.out.println("sending");

                    if (user == "user"){
                        toSend = user + " (" + id + "): " + toSend;
                    }
                    else {
                        toSend = user + ": " + toSend;
                    }

                    server.massSend(clientSocket, toSend);
                }
                command = false;

            } catch (IOException e) {
                System.out.println("line read failed.");
            }

        }

        try {
            send.close();
        } catch (IOException e) {
            System.out.println("already closed");
        }

        try {
            receive.close();
        } catch (IOException e) {
            System.out.println("already closed as well");
        }
    }

    private boolean specialCommand(String command){
        if (command.startsWith("/user")){

            String[] splitted = command.split("/user ");

            try {
                user = command.split("/user ")[1];

            }catch (Exception e){

                user = "user";

                try {
                    receive.writeBytes("/user [username]\n");

                }
                catch (IOException ex) {
                    System.out.println("another whoops");

                }

            }
            return true;
        }

        if(command.startsWith("/help")){
            try {
                receive.writeBytes("/user - changes the username;\n" +
                        "/exit - terminates the connection to the server");
            } catch (IOException e) {
                System.out.println("what");
            }
        }

        return false;
    }
}
