package ar.utn.frba.ddsi.cliente_liviano.repositories.agregador.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudResponse {
    private Long idSolicitud;
    private Long idHecho;
    private String tituloHecho;
    private String motivo;
    private String estado;
}
