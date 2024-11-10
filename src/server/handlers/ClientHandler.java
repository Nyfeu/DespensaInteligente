package server.handlers;

import server.ServerApp;
import shared.Status;
import shared.serializable.RequestPacket;
import shared.serializable.ResponsePacket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler extends Thread {

    private final Socket acceptedClient;
    private ObjectInputStream input;
    private ObjectOutputStream output;

    public ClientHandler(Socket acceptedClient) {
        this.acceptedClient = acceptedClient;
    }

    @Override
    public void run() {

        System.out.println("CLIENT::HANDLER::INITIALIZED");
        System.out.println("NEW::CLIENT::IP::" + acceptedClient.getInetAddress().getHostAddress() + "::CONNECTED");

        // Inicializando streams para comunicação com o cliente:
        setupStreams();

        // Iniciando loop para tratamento de comunicação:
        while (!isInterrupted()) {

            try {

                handleClient();

            } catch (IOException e) {

                close();
                ServerApp.removeClientHandler(this);
                break;

            }

        }


    }

    private void setupStreams() {

        try {

            output = new ObjectOutputStream(acceptedClient.getOutputStream());
            input = new ObjectInputStream(acceptedClient.getInputStream());

        } catch (IOException e) {

            System.out.println("FAILED::NEW::CLIENT::STREAM");

        }

    }

    private void handleClient() throws IOException {

        try {

            RequestPacket requestPacket = (RequestPacket) input.readObject();
            processCommand(requestPacket);

        } catch (ClassNotFoundException e) {

            System.out.println("INVALID::CLIENT::COMMAND");

        }

    }

    private void processCommand(RequestPacket requestPacket) throws IOException {

        switch (requestPacket.getCommand()) {

            case EXIT -> {

                System.out.println("CLIENT::" + acceptedClient.getInetAddress().getHostAddress() + "::DISCONNECTED");

                ResponsePacket responsePacket = new ResponsePacket(
                        requestPacket.getRequestId(),
                        Status.SUCCESS,
                        null,
                        null
                );

                output.writeObject(responsePacket);
                output.flush();

            }

            default -> System.out.println("CLIENT::UNKNOWN::COMMAND");

        }

    }

    public void close() {

        try {

            if (input != null) input.close();
            if (output != null) output.close();
            if (acceptedClient != null && !acceptedClient.isClosed()) acceptedClient.close();

        } catch (IOException e) {

            System.out.println("ERROR::CLOSING_RESOURCES::" + e.getMessage());

        }

    }

}
