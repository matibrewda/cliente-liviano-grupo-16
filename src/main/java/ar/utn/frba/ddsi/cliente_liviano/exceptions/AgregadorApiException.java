package ar.utn.frba.ddsi.cliente_liviano.exceptions;

public class AgregadorApiException extends RuntimeException {
    private final int status;
    private final String body;

    public AgregadorApiException(int status, String body) {
        super("Agregador respondió " + status + ": " + body);
        this.status = status;
        this.body = body;
    }

    public int getStatus() { return status; }
    public String getBody() { return body; }
}