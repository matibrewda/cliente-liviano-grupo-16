package ar.utn.frba.ddsi.cliente_liviano.DTO.input;

import lombok.Data;

@Data
public class ColeccionInputViewDTO {
    private String titulo;
    private String descripcion;
    private String tipoConsenso;
    private Boolean fuenteProxy;
    private Boolean fuenteEstatica;
    private Boolean fuenteDinamica;
    private String fechaAcontecimientoDesde;
    private String fechaAcontecimientoHasta;
    private Long categoria;
    private Double latitud;
    private Double longitud;
    private Double radioKm;
}
