package server.handlers;

import server.ServerApp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ConnectionAccepter extends Thread {

    private final ServerSocket server;

    public ConnectionAccepter(ServerSocket server) {
        this.server = server;
    }

    @Override
    public void run() {

        System.out.println("CONNECTION::ACCEPTER::INITIALIZED");

        while (!isInterrupted()) {

            try {

                Socket acceptedClient = server.accept();

                ClientHandler clientHandler = new ClientHandler(acceptedClient);
                clientHandler.start();
                ServerApp.addClientHandler(clientHandler);

            } catch (IOException e) {

                if (!isInterrupted()) System.out.println("SERVER::ERROR::CLIENT::ACCEPTION");

            }

        }

    }

}
