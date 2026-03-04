package ar.utn.frba.ddsi.cliente_liviano.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ZonaDTO {
    private Double latitud;
    private Double longitud;
    private Double radio;
}

