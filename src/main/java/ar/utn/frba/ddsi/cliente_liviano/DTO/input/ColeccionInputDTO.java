package ar.utn.frba.ddsi.cliente_liviano.DTO.input;

import ar.utn.frba.ddsi.cliente_liviano.DTO.FiltroDto;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
public class ColeccionInputDTO {
    private String titulo;
    private String descripcion;
    private List<FiltroDto> filtros;
}

