package ar.utn.frba.ddsi.cliente_liviano.repositories.fuentedinamica.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SolicitudModificacionRequest {

    private Long idHecho;
    private String titulo;
    private String descripcion;
    private String categoriaNombre;
    private UbicacionDto ubicacion;
    private String fechaAcontecimiento;
    private String fechaCarga;
    private byte[] datosMultimedia;
    private String multimediaRuta;
    private String multimediaUrl;
    private String motivo;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UbicacionDto {
        private Double latitud;
        private Double longitud;
    }
}
