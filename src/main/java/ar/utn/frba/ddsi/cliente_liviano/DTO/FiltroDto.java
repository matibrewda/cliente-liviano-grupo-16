package ar.utn.frba.ddsi.cliente_liviano.DTO;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class FiltroDto {
    private String tipoFiltro;
    private Long codigoCategoria;
    private LocalDateTime fechaReporteDesde;
    private LocalDateTime fechaReporteHasta;
    private LocalDateTime fechaAcontecimientoDesde;
    private LocalDateTime fechaAcontecimientoHasta;
    private ZonaDTO zona;

    public FiltroDto(){
        codigoCategoria = 0L;
    }
}