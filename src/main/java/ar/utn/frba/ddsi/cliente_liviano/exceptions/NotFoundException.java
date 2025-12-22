package ar.utn.frba.ddsi.cliente_liviano.exceptions;

public class NotFoundException extends RuntimeException {

    public NotFoundException(String entidad, Long id) {
        super("No se ha encontrado " + entidad + " de id " + id);
    }
}
