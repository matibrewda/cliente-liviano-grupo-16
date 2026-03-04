package ar.utn.frba.ddsi.cliente_liviano.DTO;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ActividadDTO
{
    private String modulo;
    private String tipo;
    private String entidad;
    private String entidadId;
    private String referencia;
    private String mensaje;
    private LocalDateTime fecha;

}
