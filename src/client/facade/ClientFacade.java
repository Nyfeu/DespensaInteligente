package client.facade;

import client.exceptions.ClientException;
import shared.Command;
import shared.Entity;
import shared.serializable.RequestPacket;
import shared.serializable.ResponsePacket;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;
import java.util.Map;
import java.util.Properties;
import java.util.function.Consumer;

public class ClientFacade {

    // Configurações do servidor (endereço e porta do serviço):
    public static String ADDRESS;
    public static int PORT;

    // Socket que estabelece a conexão:
    private static Socket serverSocket;

    // Gerenciador de comunicações:
    private CommunicationHandler communicationHandler;

    public ClientFacade() {

        // Carrega a PORTA e ENDEREÇO do servidor:
        loadProperties();

        // Inicializando o cliente - cria socket:
        connectToServer();

        // Inicializando CommunicationHandler:
        this.communicationHandler = new CommunicationHandler(serverSocket);

    }

    // Metodo para enviar uma solicitacao ao servidor:
    public void sendRequest(Entity entity, Command command, Map<String, Object> args, Consumer<ResponsePacket> callback) {
        RequestPacket requestPacket = new RequestPacket(entity, command, args);
        communicationHandler.enqueueRequest(requestPacket, callback);
    }

    private static void loadProperties() throws ClientException {

        Properties props = new Properties();

        // Carrega as propriedades: ADDRESS e PORT a partir dos dados compartilhados
        // com o servidor: shared/serverSocket.properties.

        try (FileInputStream fis = new FileInputStream("src/shared/server.properties")) {

            // try-with-resources garante que 'fis' seja fechado automaticamente - sem necessidade do finally

            props.load(fis);
            PORT = Integer.parseInt(props.getProperty("port"));
            ADDRESS = props.getProperty("address");

        } catch (FileNotFoundException e) {

            throw new ClientException("ERROR::CLIENT::IOEXCEPTION::COULDNT::FIND::PROPERTIES");

        } catch (IOException e) {

            throw new ClientException("ERROR::CLIENT::IOEXCEPTION::COULDNT::LOAD::PROPERTIES");

        } catch (NumberFormatException e) {

            throw new ClientException("ERROR::CLIENT::PROPERTY::PORT::INVALID::FORMAT");

        }

    }

    public static void connectToServer() throws ClientException {

        try {

            // Inicializa o socket para o servidor por meio dos dados carregados:
            serverSocket = new Socket(ADDRESS, PORT);
            System.out.println("SERVER::IP::" + ADDRESS + "::CONNECTED::PORT::" + PORT);

        } catch (IOException e) {

            throw new ClientException("CLIENT::FAILED::CONNECT::TO::SERVER");

        }

    }

    public void closeConnection() {

        sendRequest(null, Command.EXIT, null, e -> close());

    }

    public void close() {

        try {

            communicationHandler.close();
            serverSocket.close();
            System.out.println("SERVER::CONNECTION::SHUTDOWN");

        } catch (IOException e) {

            System.out.println(e.getMessage());

        }

    }

}
