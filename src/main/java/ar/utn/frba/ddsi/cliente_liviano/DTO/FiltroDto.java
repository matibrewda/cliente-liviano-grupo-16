package ar.utn.frba.ddsi.cliente_liviano.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FiltroDto {
    private String tipoFiltro;
    private Long codigoCategoria;
    private LocalDateTime fechaReporteDesde;
    private LocalDateTime fechaReporteHasta;
    private LocalDateTime fechaAcontecimientoDesde;
    private LocalDateTime fechaAcontecimientoHasta;
    private ZonaDTO zona;
}