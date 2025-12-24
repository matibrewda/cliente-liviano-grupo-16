package ar.utn.frba.ddsi.cliente_liviano.repositories.agregador.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class SolicitudEliminacionRequest {
    public Long idHecho;
    public String motivo;
}