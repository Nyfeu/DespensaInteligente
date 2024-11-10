package server;

import server.exceptions.ServerException;
import server.handlers.ClientHandler;
import server.handlers.ConnectionAccepter;
import server.prompts.ServerPrompt;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ServerApp {

    private static ServerSocket serverSocket;
    private static ConnectionAccepter connectionAccepter;
    private static List<ClientHandler> clientHandlerList;

    public static void main(String[] args) {

        try {

            // Inicializar o PROMPT
            initPrompt();

            // Carrega a PORTA para o serviço:
            int PORT = loadProperties();

            // Inicialização do serviço:
            initServer(PORT);

            // Inicializar o ClientAccepter
            initAccepter();

        } catch (ServerException exception) {

            System.out.println(exception.getMessage());

            if (serverSocket != null) {

                try {

                    serverSocket.close();

                } catch (IOException e) {

                    System.out.println("ERROR::SERVER::COULDNT::CLOSE::SERVER_SOCKET");

                }

            }

            System.exit(1);

        }

    }

    private static int loadProperties() throws ServerException {

        Properties props = new Properties();
        int PORT;

        // Carregar propriedades a partir do arquivo especificado:

        try (FileInputStream fis = new FileInputStream("src/shared/server.properties")) {

            // try-with-resources garante que 'fis' seja fechado automaticamente - sem necessidade do finally

            props.load(fis);
            PORT = Integer.parseInt(props.getProperty("port"));

        } catch (FileNotFoundException e) {

            throw new ServerException("ERROR::SERVER::IOEXCEPTION::COULDNT::FIND::PROPERTIES");

        } catch (IOException e) {

            throw new ServerException("ERROR::SERVER::IOEXCEPTION::COULDNT::LOAD::PROPERTIES");

        } catch (NumberFormatException e) {

            throw new ServerException("ERROR::SERVER::PROPERTY::PORT::INVALID::FORMAT");

        }

        return PORT;

    }

    private static void initServer(int PORT) throws ServerException {

        try {

            serverSocket = new ServerSocket(PORT);
            System.out.println("SERVICE::INITIALIZED::PORT::" + PORT);

        } catch (IOException e) {

            throw new ServerException("FAILED::INIT::SERVER_SOCKET");

        }

    }

    private static void initAccepter() {

        clientHandlerList = new ArrayList<>();
        connectionAccepter = new ConnectionAccepter(serverSocket);
        connectionAccepter.start();

    }

    private static void initPrompt() {

        new ServerPrompt().start();

    }

    public static void addClientHandler(ClientHandler clientHandler) {

        clientHandlerList.add(clientHandler);

    }

    public static void removeClientHandler(ClientHandler clientHandler) {

        clientHandlerList.remove(clientHandler);

    }

    public static int getUsersNumber() {

        return clientHandlerList.size();

    }

    public static void shutdownServer() throws ServerException {

        try {

            connectionAccepter.interrupt();
            System.out.println("SERVER::CONNECTION::ACCEPTER::FINISHED");

            for (ClientHandler client : clientHandlerList) {
                client.interrupt();
                client.close();
            }
            System.out.println("SERVER::CLIENT::HANDLERS::DISCONNECTED");

            serverSocket.close();
            System.out.println("SERVER::SOCKET::CLOSED");

        } catch (IOException e) {

            throw new ServerException("ERROR::SHUTDOWN::SERVER");

        }

        System.out.println("SERVER::SHUTTING::DOWN\n");

    }

}
