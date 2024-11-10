package server.handlers;

import server.ServerApp;
import server.dao.DAOFactory;
import server.strategies.*;
import shared.enums.*;
import shared.entities.Ingrediente;
import shared.entities.Receita;
import shared.entities.Usuario;
import shared.serializable.RequestPacket;
import shared.serializable.ResponsePacket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            System.out.println("NEW::REQUEST::RECEIVED::" + acceptedClient.getInetAddress().getHostAddress());
            processCommand(requestPacket);

        } catch (ClassNotFoundException e) {

            System.out.println("INVALID::CLIENT::COMMAND");

        }

    }

    private void processCommand(RequestPacket requestPacket) throws IOException {

        if (requestPacket.getCommand() != Command.EXIT)
            System.out.println("CLIENT::"
                    + acceptedClient.getInetAddress().getHostAddress()
                    + "::"
                    + requestPacket.getCommand()
                    + "::"
                    + requestPacket.getEntity()
            );

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

            case CREATE -> {

                ResponsePacket responsePacket = null;

                if (requestPacket.getEntity() == null) {
                    System.out.println("CLIENT::UNKNOWN::COMMAND");

                    responsePacket = new ResponsePacket(
                            requestPacket.getRequestId(),
                            Status.ERROR,
                            null,
                            "Unkown Command!"
                    );

                } else {

                    switch (requestPacket.getEntity()) {

                        case USUARIO -> {

                            DAOFactory.createUsuarioDao().create((Usuario) requestPacket.getArgs().get(Attributes.USER.getDescription()));

                            responsePacket = new ResponsePacket(
                                    requestPacket.getRequestId(),
                                    Status.SUCCESS,
                                    null,
                                    null
                            );

                        }

                        case RECEITA -> {

                            DAOFactory.createReceitaDao().create((Receita) requestPacket.getArgs().get(Attributes.RECEITA.getDescription()));

                            responsePacket = new ResponsePacket(
                                    requestPacket.getRequestId(),
                                    Status.SUCCESS,
                                    null,
                                    null
                            );

                        }

                        case INGREDIENTE -> {

                            DAOFactory.createIngredienteDao().create((Ingrediente) requestPacket.getArgs().get(Attributes.INGREDIENTE.getDescription()));

                            responsePacket = new ResponsePacket(
                                    requestPacket.getRequestId(),
                                    Status.SUCCESS,
                                    null,
                                    null
                            );

                        }

                    }

                }

                output.writeObject(responsePacket);
                output.flush();

            }

            case READ -> {

                ResponsePacket responsePacket = null;

                if (requestPacket.getEntity() == null) {
                    System.out.println("CLIENT::UNKNOWN::COMMAND");

                    responsePacket = new ResponsePacket(
                            requestPacket.getRequestId(),
                            Status.ERROR,
                            null,
                            "Unkown Command!"
                    );

                } else {

                    switch (requestPacket.getEntity()) {

                        case USUARIO -> {

                            Usuario result = DAOFactory.createUsuarioDao().read((String) requestPacket.getArgs().get(Attributes.EMAIL.getDescription()));

                            Map<String, Object> data = new HashMap<>();

                            data.put(Attributes.RESULT.getDescription(), result);

                            responsePacket = new ResponsePacket(
                                    requestPacket.getRequestId(),
                                    Status.SUCCESS,
                                    data,
                                    null
                            );

                        }

                        case RECEITA -> {

                            Receita result = DAOFactory.createReceitaDao().read((Integer) requestPacket.getArgs().get(Attributes.RECIPE_ID.getDescription()));

                            Map<String, Object> data = new HashMap<>();

                            data.put(Attributes.RESULT.getDescription(), result);

                            responsePacket = new ResponsePacket(
                                    requestPacket.getRequestId(),
                                    Status.SUCCESS,
                                    data,
                                    null
                            );

                        }

                        case INGREDIENTE -> {

                            Ingrediente result = DAOFactory.createIngredienteDao().read((String) requestPacket.getArgs().get(Attributes.NAME.getDescription()));

                            Map<String, Object> data = new HashMap<>();

                            data.put(Attributes.RESULT.getDescription(), result);

                            responsePacket = new ResponsePacket(
                                    requestPacket.getRequestId(),
                                    Status.SUCCESS,
                                    data,
                                    null
                            );

                        }

                    }

                }

                output.writeObject(responsePacket);
                output.flush();

            }

            case UPDATE -> {

                ResponsePacket responsePacket = null;

                if (requestPacket.getEntity() == null) {
                    System.out.println("CLIENT::UNKNOWN::COMMAND");

                    responsePacket = new ResponsePacket(
                            requestPacket.getRequestId(),
                            Status.ERROR,
                            null,
                            "Unkown Command!"
                    );

                } else {

                    switch (requestPacket.getEntity()) {

                        case USUARIO -> {

                            Usuario usuario_up = (Usuario) requestPacket.getArgs().get(Attributes.USER.getDescription());

                            System.out.println(usuario_up.getDespensa().toString());

                            DAOFactory.createUsuarioDao().update(usuario_up);

                            responsePacket = new ResponsePacket(
                                    requestPacket.getRequestId(),
                                    Status.SUCCESS,
                                    null,
                                    null
                            );

                        }

                        case RECEITA -> {

                            DAOFactory.createReceitaDao().update((Receita) requestPacket.getArgs().get(Attributes.RECEITA.getDescription()));

                            responsePacket = new ResponsePacket(
                                    requestPacket.getRequestId(),
                                    Status.SUCCESS,
                                    null,
                                    null
                            );

                        }

                        case INGREDIENTE -> {

                            DAOFactory.createIngredienteDao().update((Ingrediente) requestPacket.getArgs().get(Attributes.INGREDIENTE.getDescription()));

                            responsePacket = new ResponsePacket(
                                    requestPacket.getRequestId(),
                                    Status.SUCCESS,
                                    null,
                                    null
                            );

                        }

                    }

                }

                output.writeObject(responsePacket);
                output.flush();

            }

            case DELETE -> {

                ResponsePacket responsePacket = null;

                if (requestPacket.getEntity() == null) {
                    System.out.println("CLIENT::UNKNOWN::COMMAND");

                    responsePacket = new ResponsePacket(
                            requestPacket.getRequestId(),
                            Status.ERROR,
                            null,
                            "Unkown Command!"
                    );

                } else {

                    switch (requestPacket.getEntity()) {

                        case USUARIO -> {

                            DAOFactory.createUsuarioDao().delete((String) requestPacket.getArgs().get(Attributes.EMAIL.getDescription()));

                            responsePacket = new ResponsePacket(
                                    requestPacket.getRequestId(),
                                    Status.SUCCESS,
                                    null,
                                    null
                            );

                        }

                        case RECEITA -> {

                            DAOFactory.createReceitaDao().delete((Integer) requestPacket.getArgs().get(Attributes.RECIPE_ID.getDescription()));

                            responsePacket = new ResponsePacket(
                                    requestPacket.getRequestId(),
                                    Status.SUCCESS,
                                    null,
                                    null
                            );

                        }

                        case INGREDIENTE -> {

                            DAOFactory.createIngredienteDao().delete((String) requestPacket.getArgs().get(Attributes.NAME.getDescription()));

                            responsePacket = new ResponsePacket(
                                    requestPacket.getRequestId(),
                                    Status.SUCCESS,
                                    null,
                                    null
                            );

                        }

                    }

                }

                output.writeObject(responsePacket);
                output.flush();

            }

            case READ_ALL -> {

                ResponsePacket responsePacket = null;

                if (requestPacket.getEntity() == null) {
                    System.out.println("CLIENT::UNKNOWN::COMMAND");

                    responsePacket = new ResponsePacket(
                            requestPacket.getRequestId(),
                            Status.ERROR,
                            null,
                            "Unkown Command!"
                    );

                } else {

                    switch (requestPacket.getEntity()) {

                        case USUARIO -> {

                            List<Usuario>  result = DAOFactory.createUsuarioDao().readAll();

                            Map<String, Object> data = new HashMap<>();

                            data.put(Attributes.RESULT.getDescription(), result);

                            responsePacket = new ResponsePacket(
                                    requestPacket.getRequestId(),
                                    Status.SUCCESS,
                                    data,
                                    null
                            );

                        }

                        case RECEITA -> {

                            List<Receita>  result = DAOFactory.createReceitaDao().readAll();

                            Map<String, Object> data = new HashMap<>();

                            data.put(Attributes.RESULT.getDescription(), result);

                            responsePacket = new ResponsePacket(
                                    requestPacket.getRequestId(),
                                    Status.SUCCESS,
                                    data,
                                    null
                            );

                        }

                        case INGREDIENTE -> {

                            List<Ingrediente>  result = DAOFactory.createIngredienteDao().readAll();

                            Map<String, Object> data = new HashMap<>();

                            data.put(Attributes.RESULT.getDescription(), result);

                            responsePacket = new ResponsePacket(
                                    requestPacket.getRequestId(),
                                    Status.SUCCESS,
                                    data,
                                    null
                            );

                        }

                    }

                }

                output.writeObject(responsePacket);
                output.flush();

            }

            case FILTER -> {

                ResponsePacket responsePacket;

                if (requestPacket.getEntity() != Entity.RECEITA) {
                    System.out.println("CLIENT::UNKNOWN::COMMAND");

                    responsePacket = new ResponsePacket(
                            requestPacket.getRequestId(),
                            Status.ERROR,
                            null,
                            "Unkown Command!"
                    );

                } else {

                    FilterStrategy filterStrategy = getStrategyFromRequestPacket(requestPacket);

                    Integer limit = (Integer) requestPacket.getArgs().get(Attributes.LIMIT.getDescription());
                    Integer offset = (Integer) requestPacket.getArgs().get(Attributes.OFFSET.getDescription());

                    List<Filterable> result = DAOFactory.createReceitaDao().filter(filterStrategy, limit, offset);

                    Map<String, Object> data = new HashMap<>();

                    data.put(Attributes.RESULT.getDescription(), result);

                    responsePacket = new ResponsePacket(
                            requestPacket.getRequestId(),
                            Status.SUCCESS,
                            data,
                            null
                    );

                }

                output.writeObject(responsePacket);
                output.flush();

            }

            case COUNT_ALL -> {

                ResponsePacket responsePacket;

                if (requestPacket.getEntity() != Entity.RECEITA) {
                    System.out.println("CLIENT::UNKNOWN::COMMAND");

                    responsePacket = new ResponsePacket(
                            requestPacket.getRequestId(),
                            Status.ERROR,
                            null,
                            "Unkown Command!"
                    );

                } else {

                    Integer result = DAOFactory.createReceitaDao().countAll();

                    Map<String, Object> data = new HashMap<>();

                    data.put(Attributes.RESULT.getDescription(), result);

                    responsePacket = new ResponsePacket(
                            requestPacket.getRequestId(),
                            Status.SUCCESS,
                            data,
                            null
                    );

                }

                output.writeObject(responsePacket);
                output.flush();

            }

            default -> {

                System.out.println("CLIENT::UNKNOWN::COMMAND");

                ResponsePacket responsePacket = new ResponsePacket(
                        requestPacket.getRequestId(),
                        Status.ERROR,
                        null,
                        "Unkown Command!"
                );

                output.writeObject(responsePacket);
                output.flush();

            }

        }

    }

    private FilterStrategy getStrategyFromRequestPacket(RequestPacket requestPacket) {

        Strategy strategy_type = (Strategy) requestPacket.getArgs().get(Attributes.STRATEGY.getDescription());

        FilterStrategy filterStrategy = null;

        switch (strategy_type) {

            case NOME -> {

                String nome_receita = (String) requestPacket.getArgs().get(Attributes.NAME.getDescription());
                filterStrategy = new FilterReceitasByNome(nome_receita);

            }

            case PAGE -> filterStrategy = new FilterReceitasByPage();

            case AUTOR -> {

                String autor = (String) requestPacket.getArgs().get(Attributes.AUTOR.getDescription());
                filterStrategy = new FilterReceitasByAutor(autor);

            }

            case INGREDIENTES -> {

                Usuario usuario = (Usuario) requestPacket.getArgs().get(Attributes.USER.getDescription());
                filterStrategy = new FilterReceitasByIngredientes(usuario);

            }

            case DATA_VALIDADE_AND_INGREDIENTES -> {

                Usuario usuario = (Usuario) requestPacket.getArgs().get(Attributes.USER.getDescription());
                filterStrategy = new FilterReceitasByDataValidadeAndIngredientes(usuario);

            }

        }

        return filterStrategy;

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
