package shared.serializable;

import shared.enums.Command;
import shared.enums.Entity;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

public class RequestPacket implements Serializable {

    private Entity entity;
    private Command command;        // Enum para indicar o comando (CREATE, READ, UPDATE, DELETE, etc.)
    private Map<String, Object> args; // Argumentos necessários para o comando
    private String requestId;       // Identificador único da requisição

    public RequestPacket(Entity entity, Command command, Map<String, Object> args) {
        this.entity = entity;
        this.command = command;
        this.args = args;
        this.requestId = UUID.randomUUID().toString(); // Gera um identificador único
    }

    // Getters

    public String getRequestId() {
        return requestId;
    }

    public Command getCommand() {
        return command;
    }

    public Map<String, Object> getArgs() {
        return args;
    }

    public Entity getEntity() {
        return entity;
    }
}
