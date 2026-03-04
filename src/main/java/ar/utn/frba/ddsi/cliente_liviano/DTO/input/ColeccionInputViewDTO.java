package ar.utn.frba.ddsi.cliente_liviano.DTO.input;

import lombok.Data;
import java.time.LocalDate;

@Data
public class ColeccionInputViewDTO {
    private String titulo;
    private String descripcion;
    private String fechaAcontecimientoDesde;
    private String fechaAcontecimientoHasta;
    private Long categoria;
    private String origen;
    private Double latitud;
    private Double longitud;
    private Double radioKm;
}
