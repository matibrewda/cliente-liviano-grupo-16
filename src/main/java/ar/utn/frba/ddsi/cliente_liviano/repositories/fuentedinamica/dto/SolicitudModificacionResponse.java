package ar.utn.frba.ddsi.cliente_liviano.repositories.fuentedinamica.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudModificacionResponse {
    private Long idSolicitud;
    private Long idHecho;
    private String titulo;
    private String tituloHecho;
    private String motivo;
    private String estado;
}
