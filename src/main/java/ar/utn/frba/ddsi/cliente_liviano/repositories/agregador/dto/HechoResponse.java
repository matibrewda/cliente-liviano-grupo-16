package ar.utn.frba.ddsi.cliente_liviano.repositories.agregador.dto;

import ar.utn.frba.ddsi.cliente_liviano.DTO.CategoriaDTO;
import ar.utn.frba.ddsi.cliente_liviano.DTO.HechoDTO;
import ar.utn.frba.ddsi.cliente_liviano.DTO.UbicacionDTO;
import ar.utn.frba.ddsi.cliente_liviano.models.Hecho;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class HechoResponse {

    @JsonProperty("id")
    private Long id;
    @JsonProperty("titulo")
    private String titulo;
    @JsonProperty("descripcion")
    private String descripcion;
    @JsonProperty("ubicacion")
    private UbicacionResponse ubicacion;
    @JsonProperty("categoria")
    private CategoriaResponse categoria;
    @JsonProperty("fechaAcontecimiento")
    private LocalDateTime fechaAcontecimiento;
    @JsonProperty("fechaCarga")
    private LocalDateTime fechaCarga;
    @JsonProperty("origen")
    private String origen;
    @JsonProperty("visible")
    private Boolean visible;
    @JsonProperty("consenso")
    private Boolean consenso;

    public HechoDTO toHechoDTO() {
        HechoDTO hechoDTO = new HechoDTO();
        hechoDTO.setId(id);
        hechoDTO.setTitulo(titulo);
        hechoDTO.setDescripcion(descripcion);
        hechoDTO.setOrigen(origen);
        hechoDTO.setFechaAcontecimiento(fechaAcontecimiento.toLocalDate());
        hechoDTO.setFechaCarga(fechaCarga);
        hechoDTO.setUbicacionDTO(new UbicacionDTO(ubicacion.getLatitud(),  ubicacion.getLongitud()));
        hechoDTO.setCategoriaDTO(new CategoriaDTO(categoria.getId(),categoria.getNombre()));
        hechoDTO.setVisible(visible);
        hechoDTO.setConsenso(consenso);
    return hechoDTO;
    }
}