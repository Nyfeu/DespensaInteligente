package client.facade;

import shared.entities.Usuario;
import shared.enums.Attributes;
import shared.enums.Command;
import shared.enums.Entity;
import shared.enums.Status;
import shared.serializable.RequestPacket;
import shared.serializable.ResponsePacket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.*;
import java.util.function.Consumer;

public class CommunicationHandler {

    // Streams de comunicação com o servidor:
    private ObjectOutputStream outStream;
    private ObjectInputStream inStream;

    // Mapeando as callback functions:
    private final Map<String, Consumer<ResponsePacket>> callbackMap;

    // Mapa de requestId para callbacks
    private final BlockingQueue<RequestPacket> requestQueue;
    private final ExecutorService executorService;

    public CommunicationHandler(Socket socket) {

        try {

            this.outStream = new ObjectOutputStream(socket.getOutputStream());
            this.inStream = new ObjectInputStream(socket.getInputStream());

        } catch (IOException ex) {

            System.out.println("FAILED::ESTABLISHING::OBJECT::STREAMS");

        }

        this.callbackMap = new ConcurrentHashMap<>();
        this.requestQueue = new LinkedBlockingQueue<>();
        this.executorService = Executors.newSingleThreadExecutor();

        // A thread que vai consumir a fila e enviar as requisições
        startRequestProcessing();

    }

    // Inicia o processamento das requisições na fila
    private void startRequestProcessing() {

        executorService.submit(() -> {

            while (true) {

                try {

                    // Aguarda por uma requisição
                    RequestPacket request = requestQueue.take();

                    // Envia a requisição ao servidor
                    sendRequest(request);

                } catch (InterruptedException e) {

                    System.out.println(e.getMessage());
                    break;

                }

            }

        });

    }

    public void sendRequest(RequestPacket request) {

        try {

            Command command = request.getCommand();
            Entity entity = request.getEntity();

            if (command == Command.UPDATE && entity == Entity.USUARIO) {

                System.out.println("Enviando USER:");
                Usuario user = (Usuario) request.getArgs().get(Attributes.USER.getDescription());
                System.out.println(user.getDespensa().toString());

            }

            // Envia a requisição ao servidor
            outStream.writeUnshared(request);
            outStream.reset();
            outStream.flush();

            // Aguarda a resposta do servidor
            ResponsePacket response = (ResponsePacket) inStream.readUnshared();

            // Chama o callback associado à requisição usando o requestId
            Consumer<ResponsePacket> callback = callbackMap.remove(response.getRequestId());
            if (callback != null) callback.accept(response);

        } catch (IOException | ClassNotFoundException e) {

            System.out.println(e.getMessage());

            // Em caso de erro, invoca o callback com uma resposta de erro
            Consumer<ResponsePacket> callback = callbackMap.remove(request.getRequestId());
            if (callback != null) callback.accept(new ResponsePacket(request.getRequestId(), Status.ERROR, null, "Erro na comunicação com o servidor."));

        }

    }

    public void enqueueRequest(RequestPacket request, Consumer<ResponsePacket> callback) {

        callbackMap.put(request.getRequestId(), callback);
        try {
            requestQueue.put(request);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }

    }

    public void close() {

        try {

            inStream.close();
            outStream.close();

        } catch (IOException e) {

            System.out.println(e.getMessage());

        }

    }

}
