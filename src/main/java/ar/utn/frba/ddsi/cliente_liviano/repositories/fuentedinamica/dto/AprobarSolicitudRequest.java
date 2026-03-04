package ar.utn.frba.ddsi.cliente_liviano.repositories.fuentedinamica.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AprobarSolicitudRequest {

    private String comentario;
    private Long idHecho;
    private String titulo;
    private String descripcion;
    private String categoriaNombre;
    private SolicitudModificacionRequest.UbicacionDto ubicacion;
    private String fechaAcontecimiento;
    private String motivo;
}
