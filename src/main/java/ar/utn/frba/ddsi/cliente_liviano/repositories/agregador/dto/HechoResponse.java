package ar.utn.frba.ddsi.cliente_liviano.repositories.agregador.dto;


import ar.utn.frba.ddsi.cliente_liviano.DTO.CategoriaDTO;
import ar.utn.frba.ddsi.cliente_liviano.DTO.HechoDTO;
import ar.utn.frba.ddsi.cliente_liviano.DTO.UbicacionDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class HechoResponse {

    @JsonProperty("id")
    private Long id;
    @JsonProperty("externalId")
    private Long externalId;
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
    @JsonProperty("consenso")
    private Boolean consenso;
    @JsonProperty("multimediaUrl")
    private String multimediaUrl;

    public HechoDTO toHechoDTO() {
        HechoDTO hechoDTO = new HechoDTO();
        hechoDTO.setId(id);
        hechoDTO.setExternalId(externalId);
        hechoDTO.setTitulo(titulo);
        hechoDTO.setDescripcion(descripcion);
        hechoDTO.setOrigen(origen);
        hechoDTO.setFechaAcontecimiento(fechaAcontecimiento != null ? fechaAcontecimiento.toLocalDate() : null);
        hechoDTO.setFechaCarga(fechaCarga);
        hechoDTO.setUbicacionDTO(ubicacion != null ? new UbicacionDTO(ubicacion.getLatitud(), ubicacion.getLongitud()) : null);
        hechoDTO.setCategoriaDTO(categoria != null ? new CategoriaDTO(categoria.getId(), categoria.getNombre()) : null);
        hechoDTO.setConsenso(consenso);
        hechoDTO.setMultimediaUrl(multimediaUrl);
        return hechoDTO;
    }
}