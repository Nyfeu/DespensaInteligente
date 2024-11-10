package shared.serializable;

import shared.enums.Status;

import java.io.Serializable;
import java.util.Map;

public class ResponsePacket implements Serializable {

    private String requestId;               // Identificador da requisição original
    private Status status;                  // Enum com o status da operação (SUCCESS, ERROR, etc.)
    private Map<String, Object> data;       // Dados de resposta (resultados da operação)
    private String errorMessage;            // Mensagem de erro, se houver

    public ResponsePacket(String requestId, Status status, Map<String, Object> data, String errorMessage) {
        this.requestId = requestId;
        this.status = status;
        this.data = data;
        this.errorMessage = errorMessage;
    }

    // Getters

    public Map<String, Object> getData() {
        return data;
    }

    public String getErrorMessage() {
        if (status == Status.ERROR) return errorMessage;
        else return null;
    }

    public String getRequestId() {
        return requestId;
    }

    public Status getStatus() {
        return status;
    }

}
